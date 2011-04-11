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

import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ResourceBundle;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.text.JTextComponent;

/**
 *
 * @author adrian
 */
public class PopUp {

    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("com/adr/dataclient/ui/i18n/messages"); // NOI18N

    public static void addPopup(final JTextComponent text, boolean enabled) {

        Action act;
        final JPopupMenu menu = new JPopupMenu();

        act = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.cut();
            }
        };
        act.putValue(Action.NAME, bundle.getString("label.cut"));
        act.setEnabled(enabled);
        menu.add(act);
        act = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.copy();
            }
        };
        act.putValue(Action.NAME, bundle.getString("label.copy"));
        menu.add(act);
        act = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.paste();
            }
        };
        act.putValue(Action.NAME, bundle.getString("label.paste"));
        act.setEnabled(enabled);
        menu.add(act);
        act = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.replaceSelection(null);
            }
        };
        act.putValue(Action.NAME, bundle.getString("label.delete"));
        act.setEnabled(enabled);
        menu.add(act);
        menu.add(new JSeparator());
        act = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.selectAll();
            }
        };
        act.putValue(Action.NAME, bundle.getString("label.selectall"));
        menu.add(act);
        text.add(menu);

        text.addMouseListener(new MouseAdapter() {
            private void showMenuIfPopupTrigger(MouseEvent e) {

                text.requestFocus();

                if (e.isPopupTrigger()) {
                   menu.show(text, e.getX(), e.getY());
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                showMenuIfPopupTrigger(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                showMenuIfPopupTrigger(e);
            }
        });
    }
}
