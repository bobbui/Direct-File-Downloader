/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the Under GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.thangbui.downloader.utils;

import com.sun.net.ssl.internal.ssl.X509ExtendedTrustManager;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import javax.net.ssl.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HttpUtils {

    public static File saveToFile(String address, String localFileName) throws IOException {
        File       file       = new File(localFileName);
        Connection connection = Jsoup.connect(address).ignoreContentType(true).ignoreHttpErrors(true);
        connection.userAgent("Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
        connection.timeout(10000);
        Connection.Response resultImageResponse = connection.execute();
        FileOutputStream    out                 = (new FileOutputStream(file));
        out.write(resultImageResponse.bodyAsBytes());           // resultImageResponse.body() is where the image's contents are.
        out.close();
        return file;
    }

    public static void bypassHTTPSvalidation() {
        X509ExtendedTrustManager x509ExtendedTrustManager = new X509ExtendedTrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s, String s1, String s2) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s, String s1, String s2) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null; /* Not relevant.*/
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        };
        TrustManager[] trustAllCertificates = new TrustManager[]{
                x509ExtendedTrustManager
        };

        HostnameVerifier trustAllHostnames = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true; // Just allow them all.
            }
        };

        try {
            System.setProperty("jsse.enableSNIExtension", "false");
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCertificates, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(trustAllHostnames);
        } catch (GeneralSecurityException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
}
