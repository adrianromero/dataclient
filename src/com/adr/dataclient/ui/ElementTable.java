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

import org.apache.hadoop.hbase.HTableDescriptor;

/**
 *
 * @author adrian
 */
public class ElementTable {

    private HTableDescriptor tabledescriptor;
    private boolean enabled;

    public ElementTable(HTableDescriptor tabledescriptor, boolean enabled) {
        this.tabledescriptor = tabledescriptor;
        this.enabled = enabled;
    }

    public HTableDescriptor getTableDescriptor() {
        return tabledescriptor;
    }

    public void setTableDescriptor(HTableDescriptor tabledescriptor) {
        this.tabledescriptor = tabledescriptor;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        enabled = value;
    }
}
