/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.thangbui.downloader.ui.search;

import net.thangbui.downloader.domain.Item;
import net.thangbui.downloader.ui.component.CellEditorRender;
import net.thangbui.downloader.ui.bgtask.DownloadTask;
import net.thangbui.downloader.utils.FileUtils;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Nguyen Thang
 */
class DownloadButtonCellRenderer extends CellEditorRender implements TableCellRenderer {

    Item item;
    private DownloadButtonCell searchResultActionCellPanel;

    public DownloadButtonCellRenderer(JCheckBox checkBox) {
        super(checkBox);
        searchResultActionCellPanel = new DownloadButtonCell();
        searchResultActionCellPanel.jButtonDownload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                System.out.println("searchResultActionCellPanel.jButtonDownload.addActionListener fireEditingStopped");
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable, final Object o, boolean bln, boolean bln1, final int i, int i1) {
//        System.out.println("SearchResultActionCellRenderer getTableCellRendererComponent row " + i);

        return searchResultActionCellPanel;
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, final Object o, boolean bln, final int i, int i1) {
//        System.out.println("SearchResultActionCellRenderer getTableCellEditorComponent row " + i);

        item = (Item) o;
        isPushed = true;
        return searchResultActionCellPanel;
    }

    @Override
    public Object getCellEditorValue() {
//        System.out.println("SearchResultActionCellRenderer getCellEditorValue" + item.title + " isPushed " + isPushed);

        if (isPushed) {
//            System.out.println("download for item ");
            File file;
            FileFilter fileFilter = new FileFilter() {
                @Override
                public boolean accept(File file) {
                    return true;
                }

                @Override
                public String getDescription() {
                    return "Choose directory to save your download";
                }
            };
            file = FileUtils.showSaveFileDialog(fileFilter, JFileChooser.DIRECTORIES_ONLY);
            if (file == null || !file.exists()) {
                return item;
            }
            DownloadTask downloadTask = new DownloadTask(item, file);

            downloadTask.execute();
        }
        isPushed = false;
        return item;
    }
}
