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

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author adrian
 */
public class ResultsModel extends AbstractTableModel {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/dataclient/ui/i18n/messages"); // NOI18N

    private String[] columnNames = new String[]{
        bundle.getString("header.row"),
        bundle.getString("header.familycolumn"),
        bundle.getString("header.value"),
    };
    private List<String[]> data = new ArrayList<String[]>();

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data.get(row)[col];
    }

    public String[] getRowAt(int row) {
        return data.get(row);
    }

    public void addRow(String row, String familycolumn, String value) {
        data.add(new String[]{
                row,
                familycolumn,
                value
            });
        fireTableRowsInserted(data.size() - 1, data.size() -1);
    }

    public void reset() {
        data = new ArrayList<String[]>();
        fireTableDataChanged();
    }
}