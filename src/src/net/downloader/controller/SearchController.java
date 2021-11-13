/*
 * Copyright (c) 2013, Bob, . All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.downloader.controller;

import net.downloader.constants.Constants;
import net.downloader.domain.Item;
import net.downloader.ui.MainUI;
import net.downloader.utils.FileUtils;
import net.downloader.utils.RetryUtils;
import net.downloader.utils.Utils;
import org.apache.log4j.Logger;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchController {

    private static final Logger LOG = Logger.getLogger(SearchController.class);

    public SearchController() {
    }

    public void search(String searchKey, boolean quickSearch) {
        System.out.println("search begin with key: " + searchKey);
        final String URLString;
        final String URLString2;
        final String URLString3;
        final String URLString4;
        if (quickSearch) {
            URLString = searchKey;
            URLString2 = searchKey + "2/";
            URLString3 = searchKey + "3/";
            URLString4 = searchKey + "4/";
        } else {
            URLString = Constants.URL_SEARCH + searchKey + "/";
            URLString2 = Constants.URL_SEARCH + searchKey + "/2/";
            URLString3 = Constants.URL_SEARCH + searchKey + "/3/";
            URLString4 = Constants.URL_SEARCH + searchKey + "/4/";
        }

        // progressively search and update result to enhanced search speed
        //try catch only first round, after have some search result, any problem will be hide from user
        try {
            Document d = (Document) RetryUtils.retry(
                    new DownloadSearchResultTask(URLString), 4, 1000);

            //404 error occured
            if (d == null) {
                MainUI.getInstance().refreshResultFromANewSearch(new ArrayList<Item>());
                return;
            }

            Elements elements = d.select(Constants.CSS_QUERY);

            //no search result return
            if (elements.isEmpty()) {
                elements = d.select(Constants.CSS_QUERY2);
                if (elements.isEmpty()) {
                    MainUI.getInstance().refreshResultFromANewSearch(new ArrayList<Item>());
                    return;
                }
            }

            List<Item> resultItem1 = getResultItem(elements);

            MainUI.getInstance().refreshResultFromANewSearch(resultItem1);
            if (resultItem1 == null || resultItem1.isEmpty()) {
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            MainUI.getInstance().refreshResultFromANewSearch(null);
            return;
        }
        searchNextPage(URLString2);
        searchNextPage(URLString3);
        searchNextPage(URLString4);
    }

    private void searchNextPage(final String URLString) {
        Document d = (Document) RetryUtils.retry(
                new DownloadSearchResultTask(URLString), 4, 1000);
        if (d == null) {
            return;
        }
        Elements   links      = d.select(Constants.CSS_QUERY);
        List<Item> resultItem = getResultItem(links);
        MainUI.getInstance().searchResultPanel.addNewSearchResult(resultItem);
    }

    private List<Item> getResultItem(Elements links) {
        List<Item> items = new ArrayList<Item>();
        for (Element element : links) {
            Item     item  = new Item();
            Elements cells = element.select("td");

            Element firstCell = cells.get(0);
            Element titleEl   = firstCell.select(":root > div.torrentname > div > a").first();
            item.title = Utils.wrapHTML(titleEl.html());
            item.title_clean = titleEl.text();
            item.URL = titleEl.absUrl("href");

            Element torrent_url = firstCell.select("div.iaconbox.center.floatright > a:last-child").first();
            item.torrent_URL = torrent_url.attr("href");
            Element category = firstCell.select(":root > div.torrentname > div > span > span > strong > a:nth-child(1)").first();
            item.setCategoryFromCategoryName(category.html());
//            item.setCategoryFromCategoryName(category.text());

            Element secondCell = cells.get(1);
            item.size = Utils.wrapHTML(secondCell.html());
            item.size_clean = secondCell.text();
            item.size_kb = FileUtils
                    .calculateSize(item.size_clean);

            items.add(item);
        }

        return items;
    }

    private final class DownloadSearchResultTask implements RetryUtils.Task {

        private final String uRLString;
        Document document;

        private DownloadSearchResultTask(String uRLString) {
            this.uRLString = uRLString.replaceAll(" ", "%20");
        }

        @Override
        public void performAction() throws IOException {
            try {
                document = Jsoup.connect(uRLString).get();
            } catch (HttpStatusException e) {
                e.printStackTrace();
                if (e.getStatusCode() == 404) {
                    document = null;
                } else {
                    throw e;
                }
            }
        }

        @Override
        public Object getReturnValue() {
            return document;
        }
    }
}
