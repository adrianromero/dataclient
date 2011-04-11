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

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;

// DONE: Set focus of HClient windows to the right component, in HClientAdmin to the tables list, in HClientTable to the editor.
// DONE: In HTableScripting get y scan add a better way to differenciate family from column. just fc
// DONE: scan() and scan([])
// TODO: The reader of family:column must differenciate between "fc:" and "fc"
// TODO: translate to spanish

// TODO: In scripting get(row) todos los datos...
// TODO: In scripting get con timestamp.
// TODO: In scripting add a better way to table.scan();...
// TODO: In scripting delete family *
// TODO: In scriptiong delete(row) a saco..

// TODO: wait dialog while waiting for an action to complete...



// TODO: In results table añadir el timestamp.



/**
 *
 * @author adrian
 */
public class Start {
    private static final Logger logger = Logger.getLogger("com.adr.dataclient.ui.Start");

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                // Set the look and feel.
                try {
                     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//                     UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
//                     UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//                     UIManager.setLookAndFeel("org.pushingpixels.substance.api.skin.SubstanceModerateLookAndFeel");
                } catch (Exception e) {
                    logger.log(Level.WARNING, "Cannot set look and feel", e); // NOI18N
                }
                
                UIConfiguration.getInstance().init();
                new ClientFrame().start();
            }
        });
    }
}
