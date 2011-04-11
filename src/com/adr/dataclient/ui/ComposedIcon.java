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
import java.awt.Graphics;
import javax.swing.Icon;

/**
 *
 * @author adrian
 */
public class ComposedIcon implements Icon {

    private Icon icon1;
    private Icon icon2;

    public ComposedIcon(Icon icon1, Icon icon2) {
        this.icon1 = icon1;
        this.icon2 = icon2;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        icon1.paintIcon(c, g, x, y);
        icon2.paintIcon(c, g, x, y);
    }

    @Override
    public int getIconWidth() {
        return Math.max(icon1.getIconWidth(), icon2.getIconWidth());
    }

    @Override
    public int getIconHeight() {
        return Math.max(icon1.getIconHeight(), icon2.getIconHeight());
    }
}
