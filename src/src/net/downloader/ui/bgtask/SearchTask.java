/*
 * Copyright (c) 2013, Bob, . All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.downloader.ui.bgtask;

import net.downloader.controller.SearchController;
import org.apache.log4j.Logger;

import javax.swing.*;

public class SearchTask extends SwingWorker<Void, String> {

    private static final Logger LOG = Logger.getLogger(SearchTask.class);
    private final String seachKey;
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