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

import com.adr.dataclient.FamilyExistsException;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.apache.hadoop.hbase.ClusterStatus;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.util.Bytes;

/**
 *
 * @author adrian
 */
public class ClientAdmin extends ClientPanel {

    private static final Logger logger = Logger.getLogger(ClientAdmin.class.getName());
    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/dataclient/ui/i18n/messages"); // NOI18N

    private ClientFrame parent;
    private ClientConfig config;
    private HBaseAdmin admin;

    private DefaultListModel tablesmodel;
    private DefaultListModel familiesmodel;

    /** Creates new form HClientAdmin */
    public ClientAdmin(ClientFrame parent, ClientConfig config) throws Exception {
        initComponents();

        this.parent = parent;
        this.config = config;
        this.admin = new HBaseAdmin(config.getConfiguration());

        tablesmodel = new DefaultListModel();
        jTables.setModel(tablesmodel);
        jTables.setCellRenderer(new TableRenderer());

        familiesmodel = new DefaultListModel();
        jFamilies.setModel(familiesmodel);
        jFamilies.setCellRenderer(new FamilyRenderer());

        refreshSchema();
    }

    private void refreshClusterStatus() throws IOException {

        ClusterStatus status = admin.getClusterStatus();

        jLabel1.setText(MessageFormat.format(bundle.getString("message.clusterstatus"),
                admin.getMaster().toString(),
                status.getHBaseVersion(),
                status.getServers(),
                status.getDeadServers(),
                status.getRegionsCount(),
                status.getAverageLoad()
                ));
    }

    private void refreshSchema() throws IOException {

        tablesmodel.clear();
        tablesmodel.addElement(new ElementTable(HTableDescriptor.ROOT_TABLEDESC, true));
        tablesmodel.addElement(new ElementTable(HTableDescriptor.META_TABLEDESC, true));

        HTableDescriptor[] tables = admin.listTables();

        for (HTableDescriptor t : tables) {
            boolean enabled;
            try {
                enabled = admin.isTableEnabled(t.getName());
            } catch (IOException ex) {
                enabled = false;
            }
            tablesmodel.addElement(new ElementTable(t, enabled));
        }

        jTables.setSelectedIndex(0);
        jTables.ensureIndexIsVisible(0);

        refreshClusterStatus();
    }

    private void refreshFamilies(ElementTable t) {

        familiesmodel.clear();

        if (t != null) {
            HColumnDescriptor[] families = t.getTableDescriptor().getColumnFamilies();
            for (HColumnDescriptor c : families) {
                familiesmodel.addElement(c);
            }

            if (familiesmodel.size() > 0) {
                jFamilies.setSelectedIndex(0);
                jFamilies.ensureIndexIsVisible(0);
            }
        }
    }

    private void refreshEditEnabled(ElementTable t) {

        if (t == null) {
            jGotoTable.setEnabled(false);
        } else {
            jGotoTable.setEnabled(t.isEnabled());
        }
    }

    private void removeSelectedElement(JList list, DefaultListModel model) {

        int index = list.getSelectedIndex();
        model.remove(index);
        if (index < model.size()) {
            list.setSelectedIndex(index);
            list.ensureIndexIsVisible(index);
        } else if (index > 0) {
            list.setSelectedIndex(index - 1);
            list.ensureIndexIsVisible(index - 1);
        }
    }

    private void openSelectedTable() {
        ElementTable t = (ElementTable) jTables.getSelectedValue();

        try {
            parent.addPanelToTab(new ClientTable(config, t.getTableDescriptor().getName()));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, new MessageComponent(MessageFormat.format(bundle.getString("message.connectfail"), config.getName()), ex), bundle.getString("title.hbaseconnect"), JOptionPane.WARNING_MESSAGE);
            logger.log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public String getTabTitle() {
        return config.getName();
    }

    @Override
    public boolean askClose() {
        return true;
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
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jTableEnabled = new javax.swing.JToggleButton();
        jSeparator3 = new javax.swing.JToolBar.Separator();
        jAddTable = new javax.swing.JButton();
        jRemoveTable = new javax.swing.JButton();
        jEditTable = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jAddFamily = new javax.swing.JButton();
        jRemoveFamily = new javax.swing.JButton();
        jEditFamily = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jGotoTable = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTables = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        jFamilies = new javax.swing.JList();

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
        );

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/quick_restart.png"))); // NOI18N
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);
        jToolBar1.add(jSeparator1);

        jTableEnabled.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/redled.png"))); // NOI18N
        jTableEnabled.setSelected(true);
        jTableEnabled.setFocusable(false);
        jTableEnabled.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jTableEnabled.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/redled.png"))); // NOI18N
        jTableEnabled.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/greenled.png"))); // NOI18N
        jTableEnabled.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/greenled.png"))); // NOI18N
        jTableEnabled.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTableEnabled.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTableEnabledActionPerformed(evt);
            }
        });
        jToolBar1.add(jTableEnabled);
        jToolBar1.add(jSeparator3);

        jAddTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/table_add.png"))); // NOI18N
        jAddTable.setFocusable(false);
        jAddTable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jAddTable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jAddTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddTableActionPerformed(evt);
            }
        });
        jToolBar1.add(jAddTable);

        jRemoveTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/table_remove.png"))); // NOI18N
        jRemoveTable.setFocusable(false);
        jRemoveTable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jRemoveTable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jRemoveTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRemoveTableActionPerformed(evt);
            }
        });
        jToolBar1.add(jRemoveTable);

        jEditTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/table_edit.png"))); // NOI18N
        jEditTable.setFocusable(false);
        jEditTable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jEditTable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jEditTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEditTableActionPerformed(evt);
            }
        });
        jToolBar1.add(jEditTable);
        jToolBar1.add(jSeparator2);

        jAddFamily.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/family_add.png"))); // NOI18N
        jAddFamily.setFocusable(false);
        jAddFamily.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jAddFamily.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jAddFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddFamilyActionPerformed(evt);
            }
        });
        jToolBar1.add(jAddFamily);

        jRemoveFamily.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/family_remove.png"))); // NOI18N
        jRemoveFamily.setFocusable(false);
        jRemoveFamily.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jRemoveFamily.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jRemoveFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRemoveFamilyActionPerformed(evt);
            }
        });
        jToolBar1.add(jRemoveFamily);

        jEditFamily.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/family_edit.png"))); // NOI18N
        jEditFamily.setFocusable(false);
        jEditFamily.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jEditFamily.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jEditFamily.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEditFamilyActionPerformed(evt);
            }
        });
        jToolBar1.add(jEditFamily);
        jToolBar1.add(jSeparator4);

        jGotoTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/neonpen.png"))); // NOI18N
        jGotoTable.setFocusable(false);
        jGotoTable.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jGotoTable.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jGotoTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jGotoTableActionPerformed(evt);
            }
        });
        jToolBar1.add(jGotoTable);

        jSplitPane1.setDividerLocation(150);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setOneTouchExpandable(true);

        jTables.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jTables.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTablesMouseClicked(evt);
            }
        });
        jTables.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jTablesValueChanged(evt);
            }
        });
        jTables.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTablesKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTables);

        jSplitPane1.setTopComponent(jScrollPane1);

        jFamilies.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jFamilies.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jFamiliesValueChanged(evt);
            }
        });
        jScrollPane2.setViewportView(jFamilies);

        jSplitPane1.setRightComponent(jScrollPane2);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 367, Short.MAX_VALUE))
        );

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/dataclient/ui/i18n/messages"); // NOI18N
        jTabbedPane1.addTab(bundle.getString("label.tables"), jPanel2); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12))
            .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 480, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        try {
            refreshClusterStatus();
            refreshSchema();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, new MessageComponent(MessageFormat.format(bundle.getString("message.refreshfail"), config.getName()), ex), bundle.getString("title.dataclient"), JOptionPane.WARNING_MESSAGE);
            logger.log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTablesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jTablesValueChanged

        if (!evt.getValueIsAdjusting()) {
            ElementTable t = (ElementTable) jTables.getSelectedValue();
            if (t == null || t.getTableDescriptor().isMetaRegion() || t.getTableDescriptor().isMetaTable()) {
                jTableEnabled.setSelected(true);
                jTableEnabled.setEnabled(false);
                jAddTable.setEnabled(true);
                jEditTable.setEnabled(false);
                jRemoveTable.setEnabled(false);
                jAddFamily.setEnabled(false);
                jEditFamily.setEnabled(false);
                jRemoveFamily.setEnabled(false);
            } else {
                jTableEnabled.setSelected(t.isEnabled());
                jTableEnabled.setEnabled(true);
                jAddTable.setEnabled(true);
                jEditTable.setEnabled(true);
                jRemoveTable.setEnabled(true);
                jAddFamily.setEnabled(true);
                jEditFamily.setEnabled(false);
                jRemoveFamily.setEnabled(false);
            }

            refreshEditEnabled(t);

            refreshFamilies(t);
        }

    }//GEN-LAST:event_jTablesValueChanged

    private void jAddTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAddTableActionPerformed

        CreateTableDialog createtable = new CreateTableDialog(SwingUtilities.getWindowAncestor(this));
        createtable.setVisible(true);

        if (createtable.getOK()) {

            try {
                HTableDescriptor tdescriptor = createtable.getTableDescriptor();

                admin.createTable(tdescriptor); // TODO: add split keys...

                ElementTable t = new ElementTable(tdescriptor, true);
                tablesmodel.addElement(t);
                jTables.setSelectedValue(t, true);

                refreshClusterStatus();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, new MessageComponent(bundle.getString("message.createtablefail"), ex), bundle.getString("title.dataclient"), JOptionPane.WARNING_MESSAGE);
                logger.log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jAddTableActionPerformed

    private void jRemoveTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRemoveTableActionPerformed

        ElementTable t = (ElementTable) jTables.getSelectedValue();

        if (JOptionPane.showConfirmDialog(this,
                MessageFormat.format(bundle.getString("message.askdeletetable"), t.getTableDescriptor().getNameAsString()),
                bundle.getString("title.dataclient"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {

                admin.disableTable(t.getTableDescriptor().getName());
                admin.deleteTable(t.getTableDescriptor().getName());

                removeSelectedElement(jTables, tablesmodel);

                refreshClusterStatus();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, new MessageComponent(MessageFormat.format(bundle.getString("message.deletetablefail"), t.getTableDescriptor().getNameAsString()), ex), bundle.getString("title.dataclient"), JOptionPane.WARNING_MESSAGE);
                logger.log(Level.SEVERE, null, ex);

                t.setEnabled(false);
                jTables.repaint(jTables.getCellBounds(jTables.getSelectedIndex(), jTables.getSelectedIndex()));
            }
        }

    }//GEN-LAST:event_jRemoveTableActionPerformed

    private void jTableEnabledActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTableEnabledActionPerformed

        ElementTable t = (ElementTable) jTables.getSelectedValue();

        try {
            if (jTableEnabled.isSelected()) {
                admin.enableTable(t.getTableDescriptor().getName());
            } else {
                admin.disableTable(t.getTableDescriptor().getName());
            }
            t.setEnabled(jTableEnabled.isSelected());

            refreshClusterStatus();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, new MessageComponent(MessageFormat.format(bundle.getString("message.enabletablefail"), t.getTableDescriptor().getNameAsString()), ex), bundle.getString("title.dataclient"), JOptionPane.WARNING_MESSAGE);
            logger.log(Level.SEVERE, null, ex);

            t.setEnabled(false);
        }

        jTables.repaint(jTables.getCellBounds(jTables.getSelectedIndex(), jTables.getSelectedIndex()));
        refreshEditEnabled(t);

    }//GEN-LAST:event_jTableEnabledActionPerformed

    private void jFamiliesValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jFamiliesValueChanged

        if (!evt.getValueIsAdjusting()) {
            ElementTable t = (ElementTable) jTables.getSelectedValue();
            if (t != null && !t.getTableDescriptor().isMetaRegion() && !t.getTableDescriptor().isMetaTable()) {
                HColumnDescriptor c = (HColumnDescriptor) jFamilies.getSelectedValue();
                jEditFamily.setEnabled(c != null);
                jRemoveFamily.setEnabled(c != null);
            }
        }

    }//GEN-LAST:event_jFamiliesValueChanged

    private void jAddFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAddFamilyActionPerformed

        CreateFamilyDialog createfamily = new CreateFamilyDialog(SwingUtilities.getWindowAncestor(this));
        createfamily.setVisible(true);

        if (createfamily.getOK()) {

            ElementTable t = (ElementTable) jTables.getSelectedValue();

            try {
                HColumnDescriptor c = createfamily.getColumnDescriptor();

                if (t.getTableDescriptor().hasFamily(c.getName())) {
                    throw new FamilyExistsException(c.getNameAsString());
                }

                admin.disableTable(t.getTableDescriptor().getName());
                admin.addColumn(t.getTableDescriptor().getName(), c);
                if (t.isEnabled()) {
                    admin.enableTable(t.getTableDescriptor().getName());
                }

                t.getTableDescriptor().addFamily(c);
                familiesmodel.addElement(c);
                jFamilies.setSelectedValue(c, true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, new MessageComponent(bundle.getString("message.createfamilyfail"), ex), bundle.getString("title.dataclient"), JOptionPane.WARNING_MESSAGE);
                logger.log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jAddFamilyActionPerformed

    private void jRemoveFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRemoveFamilyActionPerformed

        HColumnDescriptor c = (HColumnDescriptor) jFamilies.getSelectedValue();

        if (JOptionPane.showConfirmDialog(this,
                MessageFormat.format(bundle.getString("message.askdeletefamily"), c.getNameAsString()),
                bundle.getString("title.dataclient"), JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                ElementTable t = (ElementTable) jTables.getSelectedValue();

                admin.disableTable(t.getTableDescriptor().getName());
                admin.deleteColumn(t.getTableDescriptor().getName(), c.getName());
                if (t.isEnabled()) {
                    admin.enableTable(t.getTableDescriptor().getName());
                }

                t.getTableDescriptor().removeFamily(c.getName());
                removeSelectedElement(jFamilies, familiesmodel);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, new MessageComponent(MessageFormat.format(bundle.getString("message.deletecolumnfail"), c.getNameAsString()), ex), bundle.getString("title.dataclient"), JOptionPane.WARNING_MESSAGE);
                logger.log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jRemoveFamilyActionPerformed

    private void jEditTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditTableActionPerformed

        ElementTable t = (ElementTable) jTables.getSelectedValue();
        byte[] name = t.getTableDescriptor().getName();

        CreateTableDialog createtable = new CreateTableDialog(SwingUtilities.getWindowAncestor(this), t.getTableDescriptor());
        createtable.setVisible(true);

        if (createtable.getOK()) {

            try {
                HTableDescriptor tdescriptor = createtable.getTableDescriptor();

                admin.disableTable(name);
                admin.modifyTable(name, tdescriptor); // TODO: add split keys...
                if (t.isEnabled()) {
                    admin.enableTable(t.getTableDescriptor().getName());
                }

                t.setTableDescriptor(tdescriptor);
                tablesmodel.set(jTables.getSelectedIndex(), t);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, new MessageComponent(MessageFormat.format(bundle.getString("message.edittablefail"), name), ex), bundle.getString("title.dataclient"), JOptionPane.WARNING_MESSAGE);
                logger.log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jEditTableActionPerformed

    private void jEditFamilyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditFamilyActionPerformed

        HColumnDescriptor c = (HColumnDescriptor) jFamilies.getSelectedValue();
        byte[] name = c.getName();

        CreateFamilyDialog createfamily = new CreateFamilyDialog(SwingUtilities.getWindowAncestor(this), c);
        createfamily.setVisible(true);

        if (createfamily.getOK()) {

            try {
                ElementTable t = (ElementTable) jTables.getSelectedValue();
                HColumnDescriptor cnew = createfamily.getColumnDescriptor();

                admin.disableTable(t.getTableDescriptor().getName());
                admin.modifyColumn(t.getTableDescriptor().getName(), cnew);
                if (t.isEnabled()) {
                    admin.enableTable(t.getTableDescriptor().getName());
                }

                t.getTableDescriptor().removeFamily(c.getName());
                t.getTableDescriptor().addFamily(cnew);
                familiesmodel.set(jFamilies.getSelectedIndex(), cnew);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, new MessageComponent(MessageFormat.format(bundle.getString("message.editfamilyfail"), Bytes.toString(name)), ex), bundle.getString("title.dataclient"), JOptionPane.WARNING_MESSAGE);
                logger.log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jEditFamilyActionPerformed

    private void jGotoTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jGotoTableActionPerformed

        openSelectedTable();

    }//GEN-LAST:event_jGotoTableActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                jTables.requestFocusInWindow();
            }
        });
    }//GEN-LAST:event_formComponentShown

    private void jTablesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTablesMouseClicked

        if (evt.getClickCount() == 2) {
            openSelectedTable();
        }

    }//GEN-LAST:event_jTablesMouseClicked

    private void jTablesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTablesKeyReleased

        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            openSelectedTable();
        }
        
    }//GEN-LAST:event_jTablesKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jAddFamily;
    private javax.swing.JButton jAddTable;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jEditFamily;
    private javax.swing.JButton jEditTable;
    private javax.swing.JList jFamilies;
    private javax.swing.JButton jGotoTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jRemoveFamily;
    private javax.swing.JButton jRemoveTable;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JToolBar.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JToggleButton jTableEnabled;
    private javax.swing.JList jTables;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables


}
