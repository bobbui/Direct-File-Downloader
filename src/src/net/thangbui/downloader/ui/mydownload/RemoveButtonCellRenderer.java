/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.thangbui.downloader.ui.mydownload;

import net.thangbui.downloader.controller.DownloadController;
import net.thangbui.downloader.domain.Download;
import net.thangbui.downloader.ui.MainUI;
import net.thangbui.downloader.ui.component.CellEditorRender;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Nguyen Thang
 */
class RemoveButtonCellRenderer extends CellEditorRender implements
        TableCellRenderer {

    private static final Logger LOG = Logger.getLogger(RemoveButtonCellRenderer.class);
    private       Download         download;
    private final RemoveButtonCell deltl;

    public RemoveButtonCellRenderer(JCheckBox box) {
        super(box);
        deltl = new RemoveButtonCell();
        deltl.jButtonDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable,
                                                   final Object o, boolean bln, boolean bln1, final int i, int i1) {
        return deltl;
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, final Object o,
                                                 boolean bln, final int i, int i1) {
        download = (Download) o;
        isPushed = true;
        return deltl;
    }

    @Override
    public Object getCellEditorValue() {
//        System.out.println("getCellEditorValue" + download.downloadingPausedCompleteFlag
//                + " : " + download.title + " isPushed : " + isPushed);
        if (isPushed) {
            int reply = JOptionPane.showConfirmDialog(MainUI.getInstance(),
                    "Are you sure?", "Are you sure to remove this download ?",
                    JOptionPane.YES_NO_OPTION);
            if (reply == JOptionPane.YES_OPTION) {
                try {
                    DownloadController.remove(download.dm);
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("Can not remove download :" + download.title, e);
                }
                MainUI.getInstance().myDownloadPanel
                        .refreshCompletely();
            }
        }
        isPushed = false;
        return download;
    }
}