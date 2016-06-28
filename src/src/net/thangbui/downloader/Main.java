/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */
package net.thangbui.downloader;

import net.thangbui.downloader.domain.Category;
import net.thangbui.downloader.ui.MainUI;
import net.thangbui.downloader.utils.SwingUtils;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.List;

/**
 * @author Nguyen Thang
 */
public class Main {
    public static final List<Category> CATEGORYS = Category.load();
    private static      Logger         LOG       = Logger.getRootLogger();

    public static void main(String args[]) throws ClassNotFoundException,
            UnsupportedLookAndFeelException, InstantiationException,
            IllegalAccessException {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    SwingUtils.setLAFNimbus();
                    SwingUtils.setTheme();
                    MainUI.getInstance().setVisible(true);
                } catch (InstantiationException ex) {
                    LOG.error("", ex);
                } catch (ClassNotFoundException ex) {
                    LOG.error("", ex);
                } catch (IllegalAccessException ex) {
                    LOG.error("", ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    LOG.error("", ex);
                }
            }
        });

        addSystemTrayIcon();

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                System.out.println("save data after exit");
                org.gudy.azureus2.ui.common.Main.shutdown();
            }
        });
    }

    private static void addSystemTrayIcon() {
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon = new TrayIcon(createImage(
                "/images/tray_icon.png", "tray icon"));
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a pop-up menu components
        MenuItem exitItem    = new MenuItem("Exit");
        MenuItem displayItem = new MenuItem("Display");

        popup.add(displayItem);
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        trayIcon.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainUI.getInstance().setVisible(true);
            }
        });
        displayItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                MainUI.getInstance().setVisible(true);
            }
        });

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }
    }

    protected static Image createImage(String path, String description) {
        URL imageURL = Main.class.getResource(path);

        if (imageURL == null) {
            System.err.println("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }
}
