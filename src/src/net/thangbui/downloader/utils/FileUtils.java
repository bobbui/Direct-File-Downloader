/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.thangbui.downloader.utils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileUtils {

    public static File showSaveFileDialog(FileFilter FF, int FileSelectionMode) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setFileSelectionMode(FileSelectionMode);
        fileChooser.addChoosableFileFilter(FF);
        int returnValue = fileChooser.showSaveDialog(fileChooser);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else {
            return null;
        }
    }

    public static long calculateSize(String size_clean) {
        try {
            size_clean = size_clean.trim();
            String[] tokens = size_clean.split(" ");

            float num  = Float.parseFloat(tokens[0]);
            float unit = 1;
            if ("gb".equalsIgnoreCase(tokens[1].trim().toLowerCase())) {
                unit = org.apache.commons.io.FileUtils.ONE_GB / 1000;
            } else if ("mb".equalsIgnoreCase(tokens[1].trim().toLowerCase())) {
                unit = org.apache.commons.io.FileUtils.ONE_MB / 1000;
            } else if ("kb".equalsIgnoreCase(tokens[1].trim().toLowerCase())) {
                unit = org.apache.commons.io.FileUtils.ONE_KB / 1000;
            } else if ("bytes".equalsIgnoreCase(tokens[1].trim().toLowerCase())) {
                unit = 1;
            }
            return (long) (num * unit);
        } catch (Exception e) {
            throw new RuntimeException("format of input string is not correct");
        }
    }

    public static String fromBytesToSizeString(long bytes, boolean isIncludeFloatPoint) {
        double num  = 0;
        String unit = "";
        if (bytes < org.apache.commons.io.FileUtils.ONE_KB) {
            num = bytes;
            unit = "bytes";
        } else if (org.apache.commons.io.FileUtils.ONE_KB < bytes && bytes < org.apache.commons.io.FileUtils.ONE_MB) {
            num = bytes / (double) org.apache.commons.io.FileUtils.ONE_KB;
            unit = "KB";
        } else if (org.apache.commons.io.FileUtils.ONE_MB < bytes && bytes < org.apache.commons.io.FileUtils.ONE_GB) {
            num = bytes / (double) org.apache.commons.io.FileUtils.ONE_MB;
            unit = "MB";
        } else if (org.apache.commons.io.FileUtils.ONE_GB < bytes) {
            num = bytes / (double) org.apache.commons.io.FileUtils.ONE_GB;
            unit = "GB";
        }
        if (isIncludeFloatPoint) {
            return num + " " + unit;
        } else {
            return (int) num + " " + unit;
        }
    }
}
