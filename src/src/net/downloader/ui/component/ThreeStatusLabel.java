/*
 * Copyright (c) 2013, Bob, . All rights reserved.
 * Licensed under the GNU General Public License version 2.0 (GPLv2)
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://www.gnu.org/licenses/old-licenses/gpl-2.0.en.html
 */

package net.downloader.ui.component;

import net.downloader.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.font.TextAttribute;

public class ThreeStatusLabel extends javax.swing.JPanel {

    private ImageIcon          icon;
    private ImageIcon          iconHover;
    private ImageIcon          iconSelected;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    public  javax.swing.JLabel jLabel;
    Font original;

    /**
     * Creates new form SearchLabel
     */
    public ThreeStatusLabel() {
        initComponents();
        setNormal();
    }

    public ThreeStatusLabel setIconPath(String path) {
        icon = new ImageIcon(getClass().getResource(path));
        setNormal();
        return this;
    }

    public ThreeStatusLabel setIconHoverPath(String path) {
        iconHover = new ImageIcon(getClass().getResource(path));
        return this;
    }

    public void setIconSelectedPath(String path) {
        iconSelected = new ImageIcon(getClass().getResource(path));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(jLabel.getWidth(), jLabel.getHeight());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel = new javax.swing.JLabel();

        setBorder(javax.swing.BorderFactory.createCompoundBorder());
        setPreferredSize(new java.awt.Dimension(115, 33));

        jLabel.setText("jLabel2");
        jLabel.setIconTextGap(2);
        jLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabelMousePressed(evt);
            }

            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabelMouseEntered(evt);
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabelMouseExited(evt);
            }

            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabelMouseReleased(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 115, Short.MAX_VALUE)
                                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 33, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelMouseEntered
        setHover();
    }//GEN-LAST:event_jLabelMouseEntered

    private void jLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelMouseExited
        setNormal();
    }//GEN-LAST:event_jLabelMouseExited

    private void jLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelMousePressed
        setSelected();
    }//GEN-LAST:event_jLabelMousePressed

    private void jLabelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabelMouseReleased
        setNormal();
    }//GEN-LAST:event_jLabelMouseReleased
    // End of variables declaration//GEN-END:variables

    private void setHover() {
        if (iconHover != null) {
            jLabel.setIcon(iconHover);
        }
        jLabel.setForeground(new Color(45, 155, 49));
        SwingUtils.setFontAttribute(jLabel, TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        SwingUtils.removeFontAttr(jLabel, TextAttribute.UNDERLINE);
    }

    private void setNormal() {
        if (icon != null) {
            jLabel.setIcon(icon);
        }
        jLabel.setForeground(new Color(117, 117, 117));
        SwingUtils.setFontAttribute(jLabel, TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        jLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        jLabel.setFont(new java.awt.Font("Tahoma", Font.BOLD, 12));
    }

    private void setSelected() {
        if (iconSelected != null) {
            jLabel.setIcon(iconSelected);
        }
        jLabel.setForeground(new Color(239, 239, 239));
    }
}
