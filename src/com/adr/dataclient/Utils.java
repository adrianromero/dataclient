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

package com.adr.dataclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adrian
 */
public class Utils {

    private static final Logger logger = Logger.getLogger(Utils.class.getName());

    public static Reader getResourceReader(String file) {
        try {
            return new InputStreamReader(Utils.class.getResourceAsStream(file), "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            logger.log(Level.SEVERE, ex.getMessage(), ex);
            return null;
        }
    }

    public static String loadResourceText(String file) throws IOException {
        return loadReader(getResourceReader(file));
    }

    private static String loadReader(Reader stream) throws IOException {
        BufferedReader r = null;
        StringBuilder text = new StringBuilder();
        try {
            r = new BufferedReader(stream);

            String line = null;
            while ((line = r.readLine()) != null) {
                text.append(line);
                text.append(System.getProperty("line.separator"));
            }
        } finally {
            if (r != null) {
                r.close();
            }
        }
        return text.toString();
    }
}
