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

import com.adr.dataclient.StopWatch;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.NavigableMap;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.util.Bytes;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

/**
 *
 * @author adrian
 */
public class ClientTable extends ClientPanel {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/dataclient/ui/i18n/messages"); // NOI18N
    private static final NumberFormat fmtseconds = new DecimalFormat("#,##0.000");
    private static final Logger logger = Logger.getLogger(ClientTable.class.getName());

    private ClientConfig config;
    private HTable table;
    private String nameasstring;

    private RSyntaxTextArea textArea;
    private RTextScrollPane sp;

    private ResultsModel resmodel;

    /** Creates new form HClientTable */
    public ClientTable(ClientConfig config, byte[] name) throws IOException {
        initComponents();

        resmodel = new ResultsModel();

        jTableResults.setModel(resmodel);

        // SwingConstants: LEFT, CENTER, RIGHT, LEADING, or TRAILING
        jTableResults.getTableHeader().setReorderingAllowed(false);
        jTableResults.setDefaultRenderer(Object.class, new TableCellRenderer(new int[]{
            SwingConstants.LEADING,
            SwingConstants.LEADING,
            SwingConstants.LEADING
        }));
        jTableResults.getColumnModel().getColumn(0).setPreferredWidth(150);
        jTableResults.getColumnModel().getColumn(1).setPreferredWidth(150);
        jTableResults.getColumnModel().getColumn(2).setPreferredWidth(100);

        textArea = new RSyntaxTextArea();

        // desktophints = (Map) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");
        // desktophints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
        // textArea.setTextAntiAliasHint("VALUE_TEXT_ANTIALIAS_ON");
        textArea.setTextAntiAliasHint("VALUE_TEXT_ANTIALIAS_LCD_HRGB");
        // textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
        textArea.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F5) {
                    if (e.isShiftDown()) {
                        runCodeLine();
                    } else {
                        runCode();
                    }
                }
            }
        });
        textArea.setFont(new Font(Font.MONOSPACED, textAreaHidden.getFont().getStyle(), textAreaHidden.getFont().getSize()));

        textArea.setSyntaxEditingStyle("text/javascript");
        textArea.setText("");
        textArea.setCaretPosition(0);

        sp = new RTextScrollPane(textArea);
        sp.getGutter().setLineNumberFont(new Font(Font.MONOSPACED, textAreaHidden.getFont().getStyle(), textAreaHidden.getFont().getSize()));

        panelText.add(sp, BorderLayout.CENTER);

        textOutput.setFont(new Font(Font.MONOSPACED, textOutput.getFont().getStyle(), textOutput.getFont().getSize()));
        PopUp.addPopup(textOutput, false);

        // construction
        this.config = config;
        nameasstring = Bytes.toString(name);
        table = new HTable(config.getConfiguration(), name);
    }

    @Override
    public String getTabTitle() {
        return config.getName() + "/" + nameasstring;
    }

    @Override
    public boolean askClose() {
        return true;
    }

    private void clearOutputText() {
        try {
            StyledDocument doc = textOutput.getStyledDocument();
            doc.remove(0, doc.getLength());
        } catch (BadLocationException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private void addResult(Result result) {

        NavigableMap<byte[],NavigableMap<byte[],byte[]>> map = result.getNoVersionMap();

        if (map == null) {
            resmodel.addRow(Bytes.toString(result.getRow()), null, null);
        } else {
            for (byte[] family: map.navigableKeySet()) {
                NavigableMap<byte[],byte[]> mapfamily = map.get(family);
                for (byte[] qualifier : mapfamily.navigableKeySet()) {
                    resmodel.addRow(Bytes.toString(result.getRow()), Bytes.toString(family) + ":" + Bytes.toString(qualifier), Bytes.toString(mapfamily.get(qualifier)));
                }
            }
        }
    }

    private void runCodeLine() {
        try {
            int line = textArea.getCaretLineNumber();
            int offset = textArea.getLineStartOffset(line);
            int len = textArea.getLineEndOffset(line) - offset;
            runCode(textArea.getText(offset, len));
        } catch (BadLocationException ex) {
            Logger.getLogger(ClientTable.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void runCode() {

        runCode(textArea.getSelectedText() == null
                ? textArea.getText()
                : textArea.getSelectedText());
    }

    private void runCode(String code) {

        try {
            ScriptEngine engine = null;
            ScriptEngineManager factory = new ScriptEngineManager();
            // create a JavaScript engine
            engine = factory.getEngineByName("javascript");
            engine.put("table", new TableScripting(table));
            engine.eval(
                      "var scan = function() {\n"
                    + "    if (arguments.length == 0) { return table.scan();}\n"
                    + "    else if (arguments.length == 1) { return table.scan(arguments[0]);}\n"
                    + "    else { throw \"Invalid arguments number.\"; } };\n"
                    + "var get = function() {\n"
                    + "    if (arguments.length == 1) { return table.get(arguments[0]);}\n"
                    + "    else if (arguments.length == 2) { return table.get(arguments[0], arguments[1]);}\n"
                    + "    else { throw \"Invalid arguments number.\"; } };\n"
                    + "var put = function() {\n"
                    + "    if (arguments.length == 3) { return table.put(arguments[0], arguments[1], arguments[2]);}\n"
                    + "    else if (arguments.length == 4) { return table.put(arguments[0], arguments[1], arguments[2], arguments[3]);}\n"
                    + "    else { throw \"Invalid arguments number.\"; } };\n"
                    + "var remove = function() {\n"
                    + "    if (arguments.length == 1) { return table.remove(arguments[0]);}\n"
                    + "    else if (arguments.length == 2) { return table.remove(arguments[0], arguments[1]);}\n"
                    + "    else if (arguments.length == 3) { return table.remove(arguments[0], arguments[1], arguments[2]);}\n"
                    + "    else { throw \"Invalid arguments number.\"; } };\n");

            clearOutputText();
            resmodel.reset();
            StopWatch timeelapsed = new StopWatch();
            Object result = engine.eval(code);
            long seconds = timeelapsed.getMillisElapsed();
            int rows = 0;

            // printing result
            if (result instanceof ResultScanner) {
                ResultScanner scanner = (ResultScanner) result;
                for (Result rr = scanner.next(); rr != null; rr = scanner.next()) {
                    addResult(rr);
                    rows ++;
                }
                jTabbedPane1.setSelectedComponent(jResults);
            } else if(result instanceof Result) {
                addResult((Result) result);
                rows ++;
                jTabbedPane1.setSelectedComponent(jResults);
            } else if (result != null) {
                addOutputText(result.toString() + "\n", Color.BLACK);
                jTabbedPane1.setSelectedComponent(jOutput);
            } else {
                jTabbedPane1.setSelectedComponent(jOutput);
            }
            addOutputText(MessageFormat.format(bundle.getString("message.resultexecution"), rows, fmtseconds.format(seconds / 1000.0)), Color.BLACK);

//        } catch (IOException ex) {
//        } catch (ScriptException ex) {
        } catch (Exception ex) {
            StringWriter trace = new StringWriter();
            ex.printStackTrace(new PrintWriter(trace));
            addOutputText(trace.toString(), Color.RED.darker());
            jTabbedPane1.setSelectedComponent(jOutput);

            Logger.getLogger(ClientTable.class.getName()).log(Level.SEVERE, null, ex);
        }
        jScrollPane2.scrollRectToVisible(new Rectangle(0, 0, 1, 1)); // go to the first position.
    }

   
    private void addOutputText(final String text, final Color color) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    StyledDocument doc = textOutput.getStyledDocument();
                    MutableAttributeSet attr = new SimpleAttributeSet();

                    StyleConstants.setForeground(attr, color);
        //            StyleConstants.setBackground(attr, Color.yellow);
        //            StyleConstants.setBold(attr,true);
                    int offset = doc.getLength();
                    doc.insertString(offset, text, attr);
                } catch (BadLocationException ex) {
                    logger.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        });
    }

    private void insertTemplateText(String text, int offset) {
        insertTemplateText(text, offset, 0);
    }

    private void insertTemplateText(String text, int offset, int selected) {

        int i = textArea.getCaretPosition();
        if (textArea.getCaretOffsetFromLineStart() > 0 ) {
            textArea.insert("\n" + text, i);
            // textArea.setCaretPosition(i + 1 + offset);
            textArea.setSelectionStart(i + 1 + offset);
            textArea.setSelectionEnd(i + 1 + offset + selected);
        } else {
            textArea.insert(text, i);
            // textArea.setCaretPosition(i + offset);
            textArea.setSelectionStart(i + offset);
            textArea.setSelectionEnd(i + offset + selected);
        }
        requestTextAreaFocus();
    }

    private void requestTextAreaFocus() {

        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                textArea.requestFocusInWindow();
            }
        });
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        textAreaHidden = new javax.swing.JTextArea();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JToolBar.Separator();
        jButton2 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JToolBar.Separator();
        jButton3 = new javax.swing.JButton();
        jButtonDelete = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jResults = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableResults = new javax.swing.JTable();
        jOutput = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        textOutput = new javax.swing.JTextPane();
        panelText = new javax.swing.JPanel();

        textAreaHidden.setColumns(20);
        textAreaHidden.setRows(5);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });
        setLayout(new java.awt.BorderLayout());

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/player_play.png"))); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/dataclient/ui/i18n/messages"); // NOI18N
        jButton1.setText(bundle.getString("button.run")); // NOI18N
        jButton1.setFocusable(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/player_play_line.png"))); // NOI18N
        jButton4.setText(bundle.getString("button.runline")); // NOI18N
        jButton4.setFocusable(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);
        jToolBar1.add(jSeparator1);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/enumList.png"))); // NOI18N
        jButton2.setText("Scan");
        jButton2.setFocusable(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);
        jToolBar1.add(jSeparator2);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/edit_add.png"))); // NOI18N
        jButton3.setText("Put");
        jButton3.setFocusable(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButtonDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/editdelete.png"))); // NOI18N
        jButtonDelete.setText("Remove");
        jButtonDelete.setFocusable(false);
        jButtonDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonDeleteActionPerformed(evt);
            }
        });
        jToolBar1.add(jButtonDelete);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/adr/dataclient/ui/images/22x22/kfind.png"))); // NOI18N
        jButton5.setText("Get");
        jButton5.setFocusable(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        add(jToolBar1, java.awt.BorderLayout.PAGE_START);

        jSplitPane1.setDividerLocation(200);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setOneTouchExpandable(true);

        jResults.setLayout(new java.awt.BorderLayout());

        jTableResults.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane1.setViewportView(jTableResults);

        jResults.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(bundle.getString("label.results"), jResults); // NOI18N

        jOutput.setLayout(new java.awt.BorderLayout());

        textOutput.setEditable(false);
        jScrollPane2.setViewportView(textOutput);

        jOutput.add(jScrollPane2, java.awt.BorderLayout.CENTER);

        jTabbedPane1.addTab(bundle.getString("label.output"), jOutput); // NOI18N

        jSplitPane1.setRightComponent(jTabbedPane1);

        panelText.setLayout(new java.awt.BorderLayout());
        jSplitPane1.setLeftComponent(panelText);

        add(jSplitPane1, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        runCode();

    }//GEN-LAST:event_jButton1ActionPerformed

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown

        requestTextAreaFocus();

    }//GEN-LAST:event_formComponentShown

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

       insertTemplateText("scan();", 5);

    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed

        String key;
        String familycolumn;
        String value;
        int row = jTableResults.getSelectedRow();
        if (row < 0) {
            key = "<key>";
            familycolumn = "<family:qualifier>";
            value = "<value>";
        } else {
            key = resmodel.getRowAt(row)[0];
            familycolumn = resmodel.getRowAt(row)[1];
            value = resmodel.getRowAt(row)[2];
        }
        insertTemplateText("put(\"" + key + "\", \"" + familycolumn + "\", \"" + value + "\");", 13 + key.length() + familycolumn.length(), value.length());

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed

        String key;
        String familycolumn;;
        int row = jTableResults.getSelectedRow();
        if (row < 0) {
            key = "<key>";
            familycolumn = "<family:qualifier>";
        } else {
            key = resmodel.getRowAt(row)[0];
            familycolumn = resmodel.getRowAt(row)[1];
        }
        insertTemplateText("get(\"" + key + "\", [\"" + familycolumn + "\"]);", 5, key.length());

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButtonDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonDeleteActionPerformed

        String key;
        String familycolumn;
        int row = jTableResults.getSelectedRow();
        if (row < 0) {
            key = "<key>";
            familycolumn = "<family:qualifier>";
        } else {
            key = resmodel.getRowAt(row)[0];
            familycolumn = resmodel.getRowAt(row)[1];
        }
        insertTemplateText("remove(\"" + key + "\", [\"" + familycolumn + "\"]);", 8, key.length());

    }//GEN-LAST:event_jButtonDeleteActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        
        runCodeLine();

    }//GEN-LAST:event_jButton4ActionPerformed



    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButtonDelete;
    private javax.swing.JPanel jOutput;
    private javax.swing.JPanel jResults;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JToolBar.Separator jSeparator1;
    private javax.swing.JToolBar.Separator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable jTableResults;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JPanel panelText;
    private javax.swing.JTextArea textAreaHidden;
    private javax.swing.JTextPane textOutput;
    // End of variables declaration//GEN-END:variables


}
