/*
 * Copyright (c) 2013, Bob, . All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.downloader.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class SwingUtils {

    public static void hideComponents(Container container) {
        for (Component component : container.getComponents()) {
            if (component instanceof Container) {
                component.setVisible(false);
            }
        }
    }

    public static void setLAFNimbus() throws InstantiationException, ClassNotFoundException, IllegalAccessException, UnsupportedLookAndFeelException {
        setLAF("Nimbus");
    }

    public static void setTheme() {
        final Color color             = new Color(85, 142, 119);
        final Color colorBackground   = new Color(247, 247, 247);
        Image       error_dialog_icon = new ImageIcon("/images/error_dialog.png").getImage();

        UIManager.getLookAndFeelDefaults().put("nimbusOrange", color);
        UIManager.getLookAndFeelDefaults().put("control", colorBackground);

        UIManager.getLookAndFeelDefaults().put("OptionPane.errorIcon", error_dialog_icon);
        UIManager.getLookAndFeelDefaults().put("OptionPane.background", colorBackground);

        UIManager.getLookAndFeelDefaults().put("Panel.background", new Color(245, 245,
                245));
        UIManager.put("Table.background", new Color(250, 250,
                250));
//        UIManager.put("Table.alternateRowColor", new Color(159,203,64));
    }

    private static void setLAF(String LAFName) throws ClassNotFoundException,
            IllegalAccessException, UnsupportedLookAndFeelException,
            InstantiationException {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager
                .getInstalledLookAndFeels()) {
            if (LAFName.equals(info.getName())) {
                javax.swing.UIManager.setLookAndFeel(info.getClassName());
                break;
            }
        }
    }

    private static void openWebpage(URI uri) throws IOException {
        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop()
                : null;
        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
            desktop.browse(uri);
        }
    }

    public static void openWebpage(String url) throws IOException,
            URISyntaxException {
        openWebpage(new URI(url));
    }

    private static void setFontStyle(Component c, int fontStyle) {
        Font newLabelFont = new Font(c.getFont().getName(), fontStyle, c
                .getFont().getSize());
        c.setFont(newLabelFont);
    }

    public static void setFontStyle(ComponentEvent event, int fontStyle) {
        Component c = (Component) event.getSource();
        setFontStyle(c, fontStyle);
    }

    public static void setFontAttribute(Component component, Object key, Object value) {
        Font original   = component.getFont();
        Map  attributes = original.getAttributes();
        attributes.put(key, value);
        component.setFont(original.deriveFont(attributes));
    }

    public static void removeFontAttr(Component component, Object key) {
        Font original   = component.getFont();
        Map  attributes = original.getAttributes();
        attributes.remove(key);
        component.setFont(original.deriveFont(attributes));
    }
}
