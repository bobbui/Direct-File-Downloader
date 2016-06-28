/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the Under GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.thangbui.downloader.utils;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class HttpUtils {

    public static File saveToFile(String address, String localFileName) throws IOException {
        File       file       = new File(localFileName);
        Connection connection = Jsoup.connect(address).ignoreContentType(true).ignoreHttpErrors(true);
        connection.timeout(10000);
        Connection.Response resultImageResponse = connection.execute();
        FileOutputStream    out                 = (new FileOutputStream(file));
        out.write(resultImageResponse.bodyAsBytes());           // resultImageResponse.body() is where the image's contents are.
        out.close();
        return file;
    }
}
