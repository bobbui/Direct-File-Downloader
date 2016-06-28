/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */
package net.thangbui.downloader.controller;

import net.thangbui.downloader.domain.Download;
import net.thangbui.downloader.domain.Item;
import net.thangbui.downloader.utils.HttpUtils;
import net.thangbui.downloader.utils.RetryUtils;
import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.download.DownloadManagerState;
import org.gudy.azureus2.core3.util.SystemProperties;
import org.gudy.azureus2.ui.common.Main;
import org.gudy.azureus2.ui.common.UIConst;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DownloadController {

    public static final String DOWNLOAD_LINK_CSS_PATH = "html body div.mainpart table#mainDetailsTable.doublecelltable tbody tr td div.buttonsline a.siteButton";

    public static void clearCompletedDownload() {
        List<DownloadManager> dms = UIConst.getAzureusCore()
                .getGlobalManager().getDownloadManagers();

        for (Iterator<DownloadManager> it = dms.iterator(); it.hasNext(); ) {
            DownloadManager dm = it.next();
            if (dm.getStats().getCompleted() == 1000) {
                it.remove();
            }
        }
    }

    public static List<Download> getStatusFromCore() {
        System.out.println();
        List<DownloadManager> downloadManagers = UIConst.getAzureusCore()
                .getGlobalManager().getDownloadManagers();

        List<Download> list = new ArrayList<Download>();

        for (DownloadManager dm : downloadManagers) {
            Download download = new Download();

            download.dm = dm;
            download.refreshStatus();

            list.add(download);
        }
        return list;
    }

    public static void add(File torrentFile, File outputFolder)
            throws Exception {
        DownloadManager manager = Main.core.getGlobalManager()
                .addDownloadManager(torrentFile.getCanonicalPath(),
                        outputFolder.getCanonicalPath());
        manager.getDownloadState().setAttribute(DownloadManagerState.AT_USER,
                "currentUser");
    }

    public static void remove(DownloadManager dm) throws Exception {
        Main.core.getGlobalManager().removeDownloadManager(dm);
    }

    public static void stop(DownloadManager dm) throws Exception {
        dm.stopIt(DownloadManager.STATE_STOPPED, false, false);
    }

    public static void start(DownloadManager dm) throws Exception {
        int state = dm.getState();

        if (state != DownloadManager.STATE_STOPPED) {
            System.out.println("Torrent isn't stopped");
        }
        dm.stopIt(DownloadManager.STATE_QUEUED, false, false);
    }

    public File getTorrentFile(final Item item) {

        File torrentFile;
        try {
            torrentFile = (File) RetryUtils.retry(new RetryUtils.Task() {
                File file;

                @Override
                public void performAction() throws Exception {
                    String URL = item.torrent_URL.indexOf("http") < 0 ? item.torrent_URL.replace("//", "http://") : item.torrent_URL;
                    String filePath = new File(SystemProperties.getUserPath()).getAbsolutePath() + "/"
                            + item.title_clean;
                    file = HttpUtils.saveToFile(URL, filePath);
                }

                @Override
                public Object getReturnValue() {
                    return file;
                }
            }, 4, 0);
        } catch (RuntimeException e) {
            throw e;
        }
        return torrentFile;
    }
}
