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

import java.io.IOException;
import java.util.Date;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;

/**
 *
 * @author adrian
 */
public class TableScripting {

    private HTable table;

    public TableScripting(HTable table) {
        this.table = table;
    }

    public void put(String key, String familycolumn, String value) throws IOException {
        
        Pair<String, String> fc = getFamilyColumn(familycolumn);    
        Put p = new Put(Bytes.toBytes(key));
        p.add(Bytes.toBytes(fc.getFirst()), Bytes.toBytes(fc.getSecond()), Bytes.toBytes(value));
        table.put(p);
    }
    public void put(String key, String familycolumn, String value, Date ts) throws IOException {

        Pair<String, String> fc = getFamilyColumn(familycolumn);
        Put p = new Put(Bytes.toBytes(key));
        p.add(Bytes.toBytes(fc.getFirst()), Bytes.toBytes(fc.getSecond()), ts.getTime(), Bytes.toBytes(value));
        table.put(p);
    }

    public void remove(String key, String... familycolumns) throws IOException {

        Delete d = new Delete(Bytes.toBytes(key));
        for (String familycolumn : familycolumns) {
            Pair<String, String> fc = getFamilyColumn(familycolumn);
            if (fc.getSecond() == null) {
                d.deleteFamily(Bytes.toBytes(fc.getFirst()));
            } else {
                d.deleteColumn(Bytes.toBytes(fc.getFirst()), Bytes.toBytes(fc.getSecond()));
            }
        }

        table.delete(d);
    }

    public void remove(String key, String familycolumn) throws IOException {
        remove(key, new String[]{familycolumn});
    }

    public void remove(String key) throws IOException {
        remove(key, new String[0]);
    }

    public void remove(String key, String familycolumn, Date ts) throws IOException {

        Pair<String, String> fc = getFamilyColumn(familycolumn);
        Delete d = new Delete(Bytes.toBytes(key));
        d.deleteColumn(Bytes.toBytes(fc.getFirst()), Bytes.toBytes(fc.getSecond()), ts.getTime());
        table.delete(d);
    }

    public Result get(String key, String... familiescolumns) throws IOException {

        Get g = new Get(Bytes.toBytes(key));

        for (String familycolumn : familiescolumns) {
            Pair<String, String> fc = getFamilyColumn(familycolumn);
            if (fc.getSecond() == null) {
                g.addFamily(Bytes.toBytes(fc.getFirst()));
            } else {
                g.addColumn(Bytes.toBytes(fc.getFirst()), Bytes.toBytes(fc.getSecond()));
            }
        }

        return table.get(g);
    }
    
    public Result get(String key) throws IOException {
        return get(key, new String[0]);
    }

    public Result get(String key, String familycolumn) throws IOException {
        return get(key, new String[]{ familycolumn });
    }

    public ResultScanner scan(String[] familiescolumns) throws IOException {

        Scan s = new Scan();
        
        for (String familycolumn : familiescolumns) {
            Pair<String, String> fc = getFamilyColumn(familycolumn);
            if (fc.getSecond() == null) {
                s.addFamily(Bytes.toBytes(fc.getFirst()));
            } else {
                s.addColumn(Bytes.toBytes(fc.getFirst()), Bytes.toBytes(fc.getSecond()));
            }
        }

        return table.getScanner(s);
    }

    public ResultScanner scan() throws IOException {

        return scan(new String[0]);
    }

    public ResultScanner scan(String familycolumn) throws IOException {
        
        return scan(new String[]{familycolumn});
    }

    private static Pair<String,String> getFamilyColumn(String s) {
        String [] a = s.split(":");
        if (a.length == 1) {
            return new Pair<String, String>(a[0], null); // family only
        } else if (a.length == 2) {
            return new Pair<String, String>(a[0], a[1]); // family and column
        } else {
            throw new IllegalArgumentException("Column name is not valid");
        }
    }
}
