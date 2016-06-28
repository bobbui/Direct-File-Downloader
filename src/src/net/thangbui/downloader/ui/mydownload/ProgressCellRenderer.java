/*
 * Copyright (c) 2013, Bui Nguyen Thang, thang.buinguyen@gmail.com, thangbui.net. All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.thangbui.downloader.ui.mydownload;

import net.thangbui.downloader.domain.Download;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

/**
 * @author Nguyen Thang
 */
public class ProgressCellRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

    private ProgressCell progressCell;

    public ProgressCellRenderer() {
        progressCell = new ProgressCell();
    }

    @Override
    public Component getTableCellRendererComponent(JTable jtable, final Object o, boolean bln, boolean bln1, final int i, int i1) {
        init(o);
        return progressCell;
    }


    @Override
    public Component getTableCellEditorComponent(JTable jtable, Object o, boolean bln, final int i, int i1) {
        init(o);
        return progressCell;
    }

    private void init(final Object o) {
        Download download = (Download) o;

        progressCell.jProgressBar.setString(download.percent);
        progressCell.jProgressBar.setValue(download.percentInt);
    }

    @Override
    public Object getCellEditorValue() {
        return null;
    }
}
