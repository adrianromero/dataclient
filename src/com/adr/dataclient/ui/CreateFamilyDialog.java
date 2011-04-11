//    Data Client is a simple script tasks executor.
//    Copyright (C) 2011 Adrián Romero Corchado.
//
//    This file is part of Task Executor
//
//    Data Client is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    Data Client is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with Data Client. If not, see <http://www.gnu.org/licenses/>.

package com.adr.dataclient.ui;

import java.awt.Rectangle;
import java.text.ParseException;
import java.util.ResourceBundle;
import javax.swing.DefaultComboBoxModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.io.hfile.Compression;
import org.apache.hadoop.hbase.io.hfile.Compression.Algorithm;
import org.apache.hadoop.hbase.regionserver.StoreFile.BloomType;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author adrian
 */
public class CreateFamilyDialog extends javax.swing.JDialog {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/dataclient/ui/i18n/messages"); // NOI18N

    private boolean ok;

    /** Creates new form ServerPropertiesDialog */
    public CreateFamilyDialog(java.awt.Window parent) {
        super(parent, ModalityType.APPLICATION_MODAL);

        init(
                Bytes.toBytes("new_family"),
                true,
                BloomType.valueOf(HColumnDescriptor.DEFAULT_BLOOMFILTER.toUpperCase()),
                HColumnDescriptor.DEFAULT_REPLICATION_SCOPE,
                Compression.Algorithm.valueOf(HColumnDescriptor.DEFAULT_COMPRESSION.toUpperCase()),
                HColumnDescriptor.DEFAULT_VERSIONS,
                HColumnDescriptor.DEFAULT_TTL,
                HColumnDescriptor.DEFAULT_BLOCKSIZE,
                HColumnDescriptor.DEFAULT_IN_MEMORY,
                HColumnDescriptor.DEFAULT_BLOCKCACHE);
    }

    /** Creates new form ServerPropertiesDialog */
    public CreateFamilyDialog(java.awt.Window parent, HColumnDescriptor family) {
        super(parent, ModalityType.APPLICATION_MODAL);
        
        init(
                family.getName(),
                false,
                family.getBloomFilterType(),
                family.getScope(),
                family.getCompressionType(),
                family.getMaxVersions(),
                family.getTimeToLive(),
                family.getBlocksize(),
                family.isInMemory(),
                family.isBlockCacheEnabled());
    }

    private void init(byte[] name, boolean nameenabled, BloomType bloom, int scope, Algorithm compression, int versions, int ttl, int blocksize, boolean inmemory, boolean blockcache) {

        initComponents();

        jName.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                jOK.setEnabled(jName.getText().length() > 0);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                jOK.setEnabled(jName.getText().length() > 0);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                jOK.setEnabled(jName.getText().length() > 0);
            }
        });

        jBloomfilter.setModel(new DefaultComboBoxModel(BloomType.values()));
        jCompression.setModel(new DefaultComboBoxModel(Algorithm.values()));

        jName.setText(Bytes.toString(name));
        jName.setEnabled(nameenabled);
        jBloomfilter.setSelectedItem(bloom);
        jScope.setText(Formats.INTEGER.format(scope));
        jCompression.setSelectedItem(compression);
        jMaxversions.setText(Formats.INTEGER.format(versions));
        jTimetolive.setText(Formats.INTEGER.format(ttl));
        jBlocksize.setText(Formats.INTEGER.format(blocksize));
        jInmemory.setSelected(inmemory);
        jCacheenabled.setSelected(blockcache);

        pack();
        Rectangle screenSize = this.getParent().getBounds();
        setLocation(screenSize.x + (screenSize.width - getWidth()) / 2, screenSize.y + (screenSize.height - getHeight())/2);

        getRootPane().setDefaultButton(jOK);

        ok = false;
    }

    public boolean getOK() {
        return ok;
    }

    public HColumnDescriptor getColumnDescriptor() throws ParseException {

        HColumnDescriptor c = new HColumnDescriptor(HColumnDescriptor.isLegalFamilyName(Bytes.toBytes(jName.getText())));
        c.setBloomFilterType((BloomType) jBloomfilter.getSelectedItem());
        c.setScope(Formats.INTEGER.parse(jScope.getText()));
        c.setCompressionType((Algorithm) jCompression.getSelectedItem());
        c.setMaxVersions(Formats.INTEGER.parse(jMaxversions.getText()));
        c.setTimeToLive(Formats.INTEGER.parse(jTimetolive.getText()));
        c.setBlocksize(Formats.INTEGER.parse(jBlocksize.getText()));
        c.setInMemory(jInmemory.isSelected());
        c.setBlockCacheEnabled(jCacheenabled.isSelected());
        return c;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jOK = new javax.swing.JButton();
        jCancel = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jName = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jBloomfilter = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jScope = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jCompression = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        jMaxversions = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jTimetolive = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jBlocksize = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jInmemory = new javax.swing.JCheckBox();
        jLabel9 = new javax.swing.JLabel();
        jCacheenabled = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/dataclient/ui/i18n/messages"); // NOI18N
        setTitle(bundle.getString("title.dataclient")); // NOI18N
        setResizable(false);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jOK.setText(bundle.getString("label.ok")); // NOI18N
        jOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jOKActionPerformed(evt);
            }
        });
        jPanel1.add(jOK);

        jCancel.setText(bundle.getString("button.cancel")); // NOI18N
        jCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCancelActionPerformed(evt);
            }
        });
        jPanel1.add(jCancel);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        jLabel1.setText(bundle.getString("label.name")); // NOI18N

        jLabel2.setText(bundle.getString("label.bloomfilter")); // NOI18N

        jLabel3.setText(bundle.getString("label.scope")); // NOI18N

        jLabel4.setText(bundle.getString("label.compression")); // NOI18N

        jLabel5.setText(bundle.getString("label.maxversions")); // NOI18N

        jLabel6.setText(bundle.getString("label.ttl")); // NOI18N

        jLabel7.setText(bundle.getString("label.blocksize")); // NOI18N

        jLabel8.setText(bundle.getString("label.inmemory")); // NOI18N

        jLabel9.setText(bundle.getString("label.cacheenabled")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 127, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jBlocksize)
                        .addComponent(jTimetolive)
                        .addComponent(jMaxversions)
                        .addComponent(jCompression, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScope)
                        .addComponent(jBloomfilter, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jName, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))
                    .addComponent(jCacheenabled)
                    .addComponent(jInmemory))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jBloomfilter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jScope, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jCompression, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jMaxversions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jTimetolive, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jBlocksize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jInmemory))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(jCacheenabled))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jOKActionPerformed

        ok = true;
        dispose();
        
    }//GEN-LAST:event_jOKActionPerformed

    private void jCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCancelActionPerformed

        dispose();

    }//GEN-LAST:event_jCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField jBlocksize;
    private javax.swing.JComboBox jBloomfilter;
    private javax.swing.JCheckBox jCacheenabled;
    private javax.swing.JButton jCancel;
    private javax.swing.JComboBox jCompression;
    private javax.swing.JCheckBox jInmemory;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField jMaxversions;
    private javax.swing.JTextField jName;
    private javax.swing.JButton jOK;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField jScope;
    private javax.swing.JTextField jTimetolive;
    // End of variables declaration//GEN-END:variables

}
