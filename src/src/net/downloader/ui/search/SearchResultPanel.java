/*
 * Copyright (c) 2013, Bob, . All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.downloader.ui.search;

import net.downloader.domain.Item;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Bob
 */
public class SearchResultPanel extends javax.swing.JPanel {

    private List<Item> datas = new ArrayList<Item>();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private       javax.swing.JLabel                             jLabelConnectionError;
    private       javax.swing.JLabel                             jLabelNoResultMessage;
    private final TableCellRenderer                              iconCell;
    private       javax.swing.JScrollPane                        jScrollPaneResult;
    private       javax.swing.JTable                             jTable1;
    private       net.downloader.ui.search.SearchFilter searchFilterPanel;
    /**
     * Creates new form SearchResultPanel
     */
    public SearchResultPanel() {
        iconCell = new net.downloader.ui.search.CategoryIconCellRenderer();
        initComponents();
        jLabelConnectionError.setVisible(false);
        jLabelNoResultMessage.setVisible(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPaneResult = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        searchFilterPanel = new net.downloader.ui.search.SearchFilter();
        jLabelNoResultMessage = new javax.swing.JLabel();
        jLabelConnectionError = new javax.swing.JLabel();

        jScrollPaneResult.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jTable1.setAutoCreateRowSorter(true);
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{

                },
                new String[]{
                        "", "Title", "Size", "Description", ""
                }
        ) {
            final Class[] types = new Class[]{
                    java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            final boolean[] canEdit = new boolean[]{
                    false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
        jTable1.setIntercellSpacing(new java.awt.Dimension(0, 0));
        jTable1.setRowHeight(35);
        jTable1.setRowSelectionAllowed(false);
        jTable1.setSelectionBackground(new java.awt.Color(204, 204, 204));
        jTable1.setShowVerticalLines(false);
        jScrollPaneResult.setViewportView(jTable1);
        jTable1.getColumnModel().getColumn(0).setMinWidth(30);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTable1.getColumnModel().getColumn(0).setMaxWidth(30);
        jTable1.getColumnModel().getColumn(0).setCellRenderer(iconCell);
        jTable1.getColumnModel().getColumn(2).setMinWidth(80);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(85);
        jTable1.getColumnModel().getColumn(2).setMaxWidth(200);
        jTable1.getColumnModel().getColumn(3).setMinWidth(85);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(85);
        jTable1.getColumnModel().getColumn(3).setMaxWidth(85);
        jTable1.getColumnModel().getColumn(4).setMinWidth(40);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(40);
        jTable1.getColumnModel().getColumn(4).setMaxWidth(40);
        jTable1.getColumnModel().getColumn(4).setCellEditor(new DownloadButtonCellRenderer(new JCheckBox()));
        jTable1.getColumnModel().getColumn(4).setCellRenderer(new DownloadButtonCellRenderer(new JCheckBox()));

        jLabelNoResultMessage.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14)); // NOI18N
        jLabelNoResultMessage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelNoResultMessage.setText("Search return no result, please refine your search keyword and try again.");

        jLabelConnectionError.setFont(new java.awt.Font("Tahoma", Font.BOLD, 14)); // NOI18N
        jLabelConnectionError.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabelConnectionError.setText("Can not connect to search server, please check your internet connection and try again");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabelNoResultMessage, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPaneResult)
                        .addComponent(jLabelConnectionError, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(searchFilterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(searchFilterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, 0)
                                .addComponent(jScrollPaneResult, javax.swing.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabelConnectionError, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                                .addGap(0, 0, 0)
                                .addComponent(jLabelNoResultMessage, javax.swing.GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
                                .addGap(0, 6, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // End of variables declaration//GEN-END:variables

    public void refreshCompletelyFromANewSearch(List<Item> searchResult) {
        setVisible(true);
        hideAll();
        jScrollPaneResult.setVisible(true);
        searchFilterPanel.setVisible(true);

        if (searchResult == null) {
            showConnectionErrorMessage();
            return;
        }
        if (searchResult.isEmpty()) {
            showNoResultMessage();
            return;
        }
        datas = new ArrayList<Item>();
        datas.addAll(searchResult);
        replaceData(searchResult);
        updateCategoryCount();
    }

    //when have new search or filter category
    private void replaceData(List<Item> searchResult) {
        DefaultTableModel defaultTableModel = (DefaultTableModel) jTable1.getModel();
        for (int i = defaultTableModel.getRowCount() - 1; i >= 0; i--) {
            defaultTableModel.removeRow(i);
        }

        // add record to table
        for (Item item : searchResult) {
            defaultTableModel.addRow(new Object[]{item.category, item.title, item.size,
                    item.description, item});
        }
    }

    public void addNewSearchResult(List<Item> searchResult) {
        datas.addAll(searchResult);
        DefaultTableModel defaultTableModel = (DefaultTableModel) jTable1.getModel();

        for (Item item : searchResult) {
            defaultTableModel.addRow(new Object[]{item.category, item.title, item.size,
                    item.description, item});
        }

        updateCategoryCount();
    }

    public void filterCategory(int category) {
        if (category == -1) {
            replaceData(datas);
            return;
        }
        List<Item> items = new ArrayList<Item>();
        for (Item item : datas) {
            if (item.category == category) {
                items.add(item);
            }
        }
        replaceData(items);
    }

    private void updateCategoryCount() {
        int size = datas.size();
        searchFilterPanel.jToggleButtonALL.setText("All (" + size + ")");
        searchFilterPanel.jToggleButtonALL.setEnabled(size > 0);

        int totalInCategory = getTotalInCategory(Item.CATEGORY_XXX);
        searchFilterPanel.jToggleButton18.setText("18+ (" + totalInCategory + ")");
        searchFilterPanel.jToggleButton18.setEnabled(totalInCategory > 0);


        int totalInCategory2 = getTotalInCategory(Item.CATEGORY_APPS);
        searchFilterPanel.jToggleButtonSoftware.setText("Software (" + totalInCategory2 + ")");
        searchFilterPanel.jToggleButtonSoftware.setEnabled(totalInCategory2 > 0);

        int totalInCategory3 = getTotalInCategory(Item.CATEGORY_BOOK);
        searchFilterPanel.jToggleButtonBook.setText("Book (" + totalInCategory3 + ")");
        searchFilterPanel.jToggleButtonBook.setEnabled(totalInCategory3 > 0);

        int totalInCategory4 = getTotalInCategory(Item.CATEGORY_GAMES);
        searchFilterPanel.jToggleButtonGame.setText("Games (" + totalInCategory4 + ")");
        searchFilterPanel.jToggleButtonGame.setEnabled(totalInCategory4 > 0);

        int totalInCategory5 = getTotalInCategory(Item.CATEGORY_MOVIES);
        searchFilterPanel.jToggleButtonMovies.setText("Movies (" + totalInCategory5 + ")");
        searchFilterPanel.jToggleButtonMovies.setEnabled(totalInCategory5 > 0);

        int totalInCategory6 = getTotalInCategory(Item.CATEGORY_MUSIC);
        searchFilterPanel.jToggleButtonMusic.setText("Music (" + totalInCategory6 + ")");
        searchFilterPanel.jToggleButtonMusic.setEnabled(totalInCategory6 > 0);

        int totalInCategory7 = getTotalInCategory(Item.CATEGORY_OTHER);
        searchFilterPanel.jToggleButtonOther.setText("Other (" + totalInCategory7 + ")");
        searchFilterPanel.jToggleButtonOther.setEnabled(totalInCategory7 > 0);

        int totalInCategory8 = getTotalInCategory(Item.CATEGORY_TV);
        searchFilterPanel.jToggleButtonTV.setText("TV Shows (" + totalInCategory8 + ")");
        searchFilterPanel.jToggleButtonTV.setEnabled(totalInCategory8 > 0);
    }

    private int getTotalInCategory(int i) {
        int no = 0;
        for (Item item : datas) {
            if (item.category == i) {
                no++;
            }
        }
        return no;
    }

    private void showConnectionErrorMessage() {
        hideAll();
        jLabelConnectionError.setVisible(true);
    }

    private void showNoResultMessage() {
        hideAll();
        jLabelNoResultMessage.setVisible(true);
    }

    private void hideAll() {
        jScrollPaneResult.setVisible(false);
        searchFilterPanel.setVisible(false);
        jLabelConnectionError.setVisible(false);
        jLabelNoResultMessage.setVisible(false);
    }

    public void showUp() {
        if (datas == null || datas.isEmpty()) {
            datas = new ArrayList<Item>();
        }
        updateCategoryCount();
        setVisible(true);
    }
}
