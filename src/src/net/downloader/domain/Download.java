/*
 * Copyright (c) 2013, Bob, . All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.downloader.domain;

import org.gudy.azureus2.core3.disk.DiskManager;
import org.gudy.azureus2.core3.download.DownloadManager;
import org.gudy.azureus2.core3.util.DisplayFormatters;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Download {

    public static final  int           FLAG_COMPLETED   = 1;
    public static final  int           FLAG_DOWNLOADING = 2;
    public static final  int           FLAG_PAUSED      = 3;
    private static final int           FLAG_ERROR       = 4;
    private static final DecimalFormat df               = new DecimalFormat("0.00%");
    public  String          ETA;
    public  boolean         paused;
    public  String          state;
    public  DownloadManager dm;
    public  String          percent;
    public  String          speed;
    public  String          size;
    public  String          title;
    public  int             percentInt;
    public  int             downloadingPausedCompleteFlag;
    private int             previousState;
    public  boolean         stateChanged;

    public Download() {
    }

    private static String formatPercentFromThousands(int thousands) {
        NumberFormat percentage_format = NumberFormat.getPercentInstance();
        percentage_format.setMinimumFractionDigits(1);
        percentage_format.setMaximumFractionDigits(1);
        return percentage_format.format(thousands / 1000.0);
    }

    private static String formatDownloadStatus(DownloadManager manager) {
        int state = manager.getState();

        String tmp = "";

        switch (state) {
            case DownloadManager.STATE_QUEUED:
                tmp = "Queued";
                break;

            case DownloadManager.STATE_DOWNLOADING:
                tmp = "Downloading";
                break;

            case DownloadManager.STATE_SEEDING: {
                DiskManager diskManager = manager.getDiskManager();
                if (diskManager != null) {
                    int mp = diskManager.getMoveProgress();

                    if (mp != -1) {
                        tmp = "Moving" + ": " + formatPercentFromThousands(mp);
                    } else {
                        int done = diskManager.getCompleteRecheckStatus();

                        if (done != -1) {
                            tmp = "Completed " + " + " + "Checking" + ": "
                                    + formatPercentFromThousands(done);
                        }
                    }
                }

                if (tmp.equals("")) {
                    if (manager.getPeerManager() != null
                            && manager.getPeerManager().isSuperSeedMode()) {
                        tmp = "Super Seeding ";
                    } else {
                        tmp = "Completed";
                    }
                }

                break;
            }
            case DownloadManager.STATE_STOPPED:
                tmp = manager.isPaused() ? "Paused" : "Stopped";
                break;

            case DownloadManager.STATE_ERROR:
                tmp = "Error" + ": " + manager.getErrorDetails();
                break;

            case DownloadManager.STATE_WAITING:
                tmp = "Waiting";
                break;

            case DownloadManager.STATE_INITIALIZING:
                tmp = "Initializing";
                break;

            case DownloadManager.STATE_INITIALIZED:
                tmp = "Initialized";
                break;

            case DownloadManager.STATE_ALLOCATING: {
                tmp = "Allocating";
                DiskManager diskManager = manager.getDiskManager();
                if (diskManager != null) {
                    tmp += ": "
                            + formatPercentFromThousands(diskManager
                            .getPercentDone());
                }
                break;
            }
            case DownloadManager.STATE_CHECKING:
                tmp = "Checking"
                        + ": "
                        + formatPercentFromThousands(manager.getStats()
                        .getCompleted());
                break;

            case DownloadManager.STATE_FINISHING:
                tmp = "Finishing";
                break;

            case DownloadManager.STATE_READY:
                tmp = "Ready";
                break;

            case DownloadManager.STATE_STOPPING:
                tmp = "Stopping";
                break;

            default:
                tmp = String.valueOf(state);
        }

        if (manager.isForceStart()
                && (state == DownloadManager.STATE_SEEDING || state == DownloadManager.STATE_DOWNLOADING)) {
            tmp = "Forced" + " " + tmp;
        }
        return (tmp);
    }

    public void refreshStatus() {
        title = dm.getDisplayName();
        state = formatDownloadStatus(dm);
        ETA = DisplayFormatters.formatETA(dm.getStats().getETA());
        percent = df.format(dm.getStats().getCompleted() / 1000.0);
        percentInt = (int) ((float) dm.getStats().getCompleted() / 10);
        speed = DisplayFormatters.formatByteCountToKiBEtcPerSec(dm.getStats()
                .getDataReceiveRate());
        size = DisplayFormatters.formatByteCountToKiBEtc(dm.getSize());

        downloadingPausedCompleteFlag = getFlag();

        stateChanged = previousState != dm.getState();
        previousState = dm.getState();
    }

    private int getFlag() {
        switch (dm.getState()) {
            case DownloadManager.STATE_DOWNLOADING:
            case DownloadManager.STATE_ALLOCATING:
            case DownloadManager.STATE_CHECKING:
            case DownloadManager.STATE_INITIALIZING:
            case DownloadManager.STATE_INITIALIZED:
                return FLAG_DOWNLOADING;
            case DownloadManager.STATE_STOPPED:
            case DownloadManager.STATE_STOPPING:
            case DownloadManager.STATE_QUEUED:
                return FLAG_PAUSED;
            case DownloadManager.STATE_FINISHING:
            case DownloadManager.STATE_SEEDING:
                return FLAG_COMPLETED;

            default:
                return FLAG_ERROR;
        }
    }
}
