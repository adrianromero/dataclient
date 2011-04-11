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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 *
 * @author adrian
 */
public class UIConfiguration {

    private static final String APP_CONFIG_NODE = "dataclient";
    private static final Logger logger = Logger.getLogger(UIConfiguration.class.getName());

    private static UIConfiguration instance = null;

    private Preferences pref;

    public static UIConfiguration getInstance() {
        if (instance == null) {
            instance = new UIConfiguration();
        }
        return instance;
    }

    private UIConfiguration() {
    }

    public void init() {
        pref = Preferences.userRoot().node(APP_CONFIG_NODE);
    }

    public ClientConfig[] getConnections() {

        ArrayList<ClientConfig> connlist = new ArrayList<ClientConfig>();
        connlist.add(readConnection("default"));

        Preferences connections = pref.node("connections");
        try {
            for (String c : connections.keys()) {
                if (!"default".equals(c)) {
                    connlist.add(readConnection(c));
                }
            }
        } catch (BackingStoreException ex) {
            logger.log(Level.SEVERE, null, ex);
        }

        return connlist.toArray(new ClientConfig[connlist.size()]);
    }

    public ClientConfig readConnection(String name) {

        Preferences connections = pref.node("connections");
        return new ClientConfig(name, connections.get(name, null));
    }

    public void deleteConnection(String name) {
        
        Preferences connections = pref.node("connections");
        connections.remove(name);
    }

    public void saveConnection(ClientConfig conn) {
        Preferences connections = pref.node("connections");
        connections.put(conn.getName(), conn.getConfigurationProperties());
        flushPreferences();
    }


    public String getPreference(String key, String defaultvalue) {
        return pref.get(key, defaultvalue);
    }

    public void setPreference(String key, String value) {
        pref.put(key, value);
    }

    public void flushPreferences() {
        try {
            pref.flush();
        } catch (BackingStoreException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }
}
