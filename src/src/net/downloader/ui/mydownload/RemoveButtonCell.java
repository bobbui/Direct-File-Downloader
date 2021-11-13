/*
 * Copyright (c) 2013, Bob, . All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.downloader.ui.mydownload;

class RemoveButtonCell extends javax.swing.JPanel {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public  javax.swing.JButton   jButtonDelete;
    private javax.swing.JMenuItem jMenuItem1;

    /**
     * Creates new form MyDownloadActionColumnPanel
     */
    public RemoveButtonCell() {
        initComponents();

        jButtonDelete.setToolTipText("Remove download");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jButtonDelete = new javax.swing.JButton();

        jMenuItem1.setText("jMenuItem1");

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/remove.png"))); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButtonDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jButtonDelete)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    // End of variables declaration//GEN-END:variables
}
