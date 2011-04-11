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

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

/**
 *
 * @author adrian
 */
public class ClientConfig {

    private static final Logger logger = Logger.getLogger(ClientConfig.class.getName());

    private String name;
    private String configproperties;

    public ClientConfig(String name, String configproperties) {
        this.name = name;
        this.configproperties = configproperties;
    }

    public String getName() {
        return name;
    }

    public Configuration getConfiguration() {
        Configuration config = HBaseConfiguration.create();
        if (configproperties != null && !configproperties.equals("")) {
            try {
                config.addResource(new ByteArrayInputStream(configproperties.getBytes("UTF-8")));
                config.reloadConfiguration();
            } catch (UnsupportedEncodingException ex) {
                logger.log(Level.SEVERE, null, ex);
            }
        }
        return config;
    }

    public String getConfigurationProperties() {
        return configproperties;
    }
}
