/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.thangbui.downloader.ui.bgtask;

import net.thangbui.downloader.controller.DownloadController;
import net.thangbui.downloader.domain.Item;
import net.thangbui.downloader.ui.MainUI;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.File;

public class DownloadTask extends SwingWorker<Void, String> {

    private static final Logger LOG = Logger.getLogger(DownloadTask.class);
    private final Item item;
    private final File outputFolder;

    public DownloadTask(Item item, File file) {
        this.item = item;
        this.outputFolder = file;
    }

    @Override
    protected Void doInBackground() throws Exception {
        System.out.println(" Begin to download torrent file");

        DownloadController downloadController = new DownloadController();

        File torrentFile = null;
        try {
            torrentFile = downloadController.getTorrentFile(item);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(MainUI.getInstance(),
                    "Can find the download link for item " + item.title_clean
                            + " : " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            LOG.error("Can find the download link for item " + item.title_clean
                    + " : " + e.getMessage(), e);
        }

        // TODO: check if a duplicate download file exist

        try {
            // add new download
            downloadController.add(torrentFile, outputFolder);
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("Can find start download for item " + item.title_clean
                    + " : " + e.getMessage(), e);
            JOptionPane.showMessageDialog(MainUI.getInstance(),
                    "Can find start download for item " + item.title_clean
                            + " : " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    @Override
    protected void done() {
    }
}
