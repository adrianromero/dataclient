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

import java.awt.Component;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import org.apache.hadoop.hbase.HTableDescriptor;

/**
 *
 * @author adrian
 */
public class TableRenderer extends DefaultListCellRenderer {

    private static final Logger logger = Logger.getLogger(TableRenderer.class.getName());
    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/dataclient/ui/i18n/messages"); // NOI18N

    private static Icon rooticon = new ComposedIcon(
            new ImageIcon(TableRenderer.class.getResource("/com/adr/dataclient/ui/images/32x32/view_text.png")),
            new ImageIcon(TableRenderer.class.getResource("/com/adr/dataclient/ui/images/32x32/root.png")));
    private static Icon metaicon = new ComposedIcon(
            new ImageIcon(TableRenderer.class.getResource("/com/adr/dataclient/ui/images/32x32/view_text.png")),
            new ImageIcon(TableRenderer.class.getResource("/com/adr/dataclient/ui/images/32x32/meta.png")));
    private static Icon enabledicon = new ComposedIcon(
            new ImageIcon(TableRenderer.class.getResource("/com/adr/dataclient/ui/images/32x32/view_text.png")),
            new ImageIcon(TableRenderer.class.getResource("/com/adr/dataclient/ui/images/32x32/greenled.png")));
    private static Icon disabledicon = new ComposedIcon(
            new ImageIcon(TableRenderer.class.getResource("/com/adr/dataclient/ui/images/32x32/view_text.png")),
            new ImageIcon(TableRenderer.class.getResource("/com/adr/dataclient/ui/images/32x32/redled.png")));

    private String template;

    public TableRenderer() {
        template = bundle.getString("message.tabledescriptor");
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);

        ElementTable t = (ElementTable) value;
        HTableDescriptor tdescriptor = t.getTableDescriptor();

        setText(MessageFormat.format(template,
                tdescriptor.getNameAsString(),
                bundle.getString(tdescriptor.isReadOnly() ? "label.readonly" : "label.readwrite"),
                Formats.MEMORY.format(tdescriptor.getMaxFileSize()),
                Formats.MEMORY.format(tdescriptor.getMemStoreFlushSize()),
                bundle.getString(tdescriptor.isDeferredLogFlush() ? "label.true" : "label.false")));

        if (tdescriptor.isRootRegion()) {
            setIcon(rooticon);
        } else if (tdescriptor.isMetaRegion()) {
            setIcon(metaicon);
        } else {
            setIcon(t.isEnabled() ? enabledicon : disabledicon);
        }

        return this;
    }
}
