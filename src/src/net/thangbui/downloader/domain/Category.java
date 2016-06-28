/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.thangbui.downloader.domain;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Category {

    public String     name;
    public List<Link> links;

    public static List<Category> load() {
        List<Category> categorys = new ArrayList<Category>();

        try {
            InputStream inputStream = Category.class.getResourceAsStream("/catdata");
//            Document d = Jsoup.parse(Category.class.getResource("/catdata").getFile(), null);
            Document d = Jsoup.parse(inputStream, null, "http://kat.cr");
            d.setBaseUri("http://kat.cr");
            Elements titleEles = d.select("body div.mainpart div.margauto h2");

            //TODO: hard code to remove Anime
            titleEles.remove(6);

            Elements linkEles = d.select("body div.mainpart div.margauto div.botmarg10px");

            for (int i = 0; i < titleEles.size(); i++) {
                if (linkEles.size() <= i) {
                    break;
                }
                Category category = new Category();
                category.name = titleEles.get(i).select("a").get(0).html();

                List<Link> links = new ArrayList<Link>();

                Elements childLinkEles = linkEles.get(i).select("a");

                for (int j = 0; j < childLinkEles.size(); j++) {
                    Element e    = childLinkEles.get(j);
                    Link    link = new Link();
                    link.text = e.html();
                    link.url = e.absUrl("href");
                    links.add(link);
                }

                category.links = links;
                categorys.add(category);

            }
        } catch (Exception ex) {
            Logger.getLogger(Category.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categorys;
    }
}
