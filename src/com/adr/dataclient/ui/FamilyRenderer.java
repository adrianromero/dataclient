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
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JList;
import org.apache.hadoop.hbase.HColumnDescriptor;

/**
 *
 * @author adrian
 */
public class FamilyRenderer extends DefaultListCellRenderer {

    private static Icon icon = new ImageIcon(TableRenderer.class.getResource("/com/adr/dataclient/ui/images/32x32/5days.png"));
    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/dataclient/ui/i18n/messages"); // NOI18N

    private String template;

    public FamilyRenderer() {
        template = bundle.getString("message.familydescriptor");
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

        super.getListCellRendererComponent(list, null, index, isSelected, cellHasFocus);

        HColumnDescriptor c = (HColumnDescriptor) value;

        setText(MessageFormat.format(template,
                c.getNameAsString(),
                c.getBloomFilterType().toString(),
                c.getScope(),
                c.getCompression().toString(),
                c.getMaxVersions(),
                c.getTimeToLive(),
                c.getBlocksize(),
                bundle.getString(c.isInMemory() ? "label.true" : "label.false"),
                bundle.getString(c.isBlockCacheEnabled() ? "label.true" : "label.false")));
        setIcon(icon);

        return this;
    }
}
