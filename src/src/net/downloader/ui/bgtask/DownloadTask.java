/*
 * Copyright (c) 2013, Bob, . All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.downloader.ui.bgtask;

import net.downloader.controller.DownloadController;
import net.downloader.domain.Item;
import net.downloader.ui.MainUI;
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
        System.out.println(" Begin to download torrent file " + item.torrent_URL);

        DownloadController downloadController = new DownloadController();

        File torrentFile = null;
        try {
            torrentFile = downloadController.getTorrentFile(item);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(MainUI.getInstance(), "Can find the download link for item " + item.title_clean + " : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            LOG.error("Can find the download link for item " + item.title_clean + " : " + e.getMessage(), e);
        }

        // TODO: check if a duplicate download file exist

        try {
            // add new download
            DownloadController.add(torrentFile, outputFolder);
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
