/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.thangbui.downloader.ui.bgtask;

import net.thangbui.downloader.controller.SearchController;
import org.apache.log4j.Logger;

import javax.swing.*;

public class SearchTask extends SwingWorker<Void, String> {

    private static Logger  LOG           = Logger.getLogger(SearchTask.class);
    String seachKey;
    private        boolean isQuickSearch = false;

    public SearchTask(String seachKey, boolean isQuickSearch) {
        this.seachKey = seachKey;
        this.isQuickSearch = isQuickSearch;
    }

    @Override
    protected Void doInBackground() throws Exception {
        System.out.println(" Begin to search in background");

        try {
            new SearchController().search(seachKey, isQuickSearch);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Can not perform search " + e.getMessage(), e);
        }
        return null;
    }

    @Override
    protected void done() {
    }
}