/*
 * Copyright (c) 2013, Bob, . All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.downloader.utils;

import org.apache.log4j.Logger;

public class RetryUtils {
    private static final Logger LOG = Logger.getLogger(RetryUtils.class);

    public static Object retry(Task task, int retryNo, long waitBetweenRetry) {
        int retryTimes = 0;
        while (retryTimes < retryNo)
            try {
                task.performAction();
                return task.getReturnValue();
            } catch (Exception e) {
                e.printStackTrace();
                retryTimes++;
                try {
                    Thread.sleep(waitBetweenRetry);
                } catch (InterruptedException e1) {
                    LOG.error(e1.getMessage());
                    LOG.error(" Error when execute task , retry for " + retryTimes + " times", e);
                }
                LOG.error(" Error when execute task , retry for " + retryTimes + " times", e);
            }

        throw new RuntimeException("Can not execute task for " + retryNo + " , task aborted");
    }

    public interface Task {

        void performAction() throws Exception;

        Object getReturnValue();
    }
}
