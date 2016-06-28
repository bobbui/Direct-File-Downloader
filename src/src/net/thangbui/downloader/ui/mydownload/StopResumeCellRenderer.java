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
import net.thangbui.downloader.ui.component.CellEditorRender;
import org.apache.log4j.Logger;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

/**
 * @author Nguyen Thang
 */
class StopResumeCellRenderer extends CellEditorRender implements TableCellRenderer, TableCellEditor {

    private static final Logger LOG = Logger.getLogger(StopResumeCellRenderer.class);
    private       Download       download;
    private final StopResumeCell myDownloadActionCell;

    public StopResumeCellRenderer(JCheckBox checkBox) {
        super(checkBox);

        myDownloadActionCell = new StopResumeCell();
        myDownloadActionCell.jButtonOpen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
        myDownloadActionCell.jButtonPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
        myDownloadActionCell.jButtonResume.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable, final Object o, boolean bln, boolean bln1, final int i, int i1) {
        setButtonAction((Download) o);
        return myDownloadActionCell;
    }

    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, final int i, int i1) {
        setButtonAction((Download) o);

        isPushed = true;
        return myDownloadActionCell;
    }

    private void setButtonAction(Download o) {
        download = o;
        myDownloadActionCell.jButtonOpen.setVisible(false);
        myDownloadActionCell.jButtonPause.setVisible(false);
        myDownloadActionCell.jButtonResume.setVisible(false);
        if (download.downloadingPausedCompleteFlag == Download.FLAG_COMPLETED) {
            myDownloadActionCell.jButtonOpen.setVisible(true);
        } else if (download.downloadingPausedCompleteFlag == Download.FLAG_DOWNLOADING) {
            myDownloadActionCell.jButtonPause.setVisible(true);
        } else if (download.downloadingPausedCompleteFlag == Download.FLAG_PAUSED) {
            myDownloadActionCell.jButtonResume.setVisible(true);
        }
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            if (download.downloadingPausedCompleteFlag == Download.FLAG_COMPLETED) {
                try {
                    Desktop.getDesktop().open(download.dm.getAbsoluteSaveLocation());
                } catch (IOException e) {
                    LOG.error("Can not open download : " + download.title, e);
                    e.printStackTrace();
                }
            } else if (download.downloadingPausedCompleteFlag == Download.FLAG_DOWNLOADING) {

                try {
                    DownloadController.stop(download.dm);
//                            MainUI.getInstance().myDownloadPanel.refreshCompletely();
                    LOG.info("stop download" + DownloadController.getStatusFromCore().size());
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("Can not stop download : " + download.title, e);
                }
            } else if (download.downloadingPausedCompleteFlag == Download.FLAG_PAUSED) {
                try {
                    DownloadController.start(download.dm);
//                            MainUI.getInstance().myDownloadPanel.refreshCompletely();
                    LOG.info("start download" + DownloadController.getStatusFromCore().size());
                } catch (Exception e) {
                    e.printStackTrace();
                    LOG.error("Can not start download : " + download.title, e);
                }
            }
        }
        isPushed = false;
        return download;
    }
}
