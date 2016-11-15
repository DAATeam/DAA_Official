/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Jdbc;

import java.util.ArrayList;
import com.uit.daa.issuer.Jdbc.Column;
/**
 *
 * @author nguyenduyy
 */
public class Table{
    
    public ArrayList<Column> columns;
    public String table_name;
    
    public Table(String t){
        columns = new ArrayList<Column>();
        table_name = t;
    }
    public void addColumn(Column c){
        columns.add(c);
    }
    public String getCreateTableSql(){
        String sql = null;
        sql = "create table "+table_name + "(";
        ArrayList<String> fields = new ArrayList<>();
        ArrayList<String> primaryKeys = new ArrayList<>();
        ArrayList<String> foriegnKeys = new ArrayList<>();
        for(Column c :  columns){
            fields.add(c.getSqlString());
            if(c.isPrimary){
                primaryKeys.add(c.field);
            }
            if(c.foreign_key != null){
                foriegnKeys.add("foreign key ("+c.field+")"
                        + "references +"+ c.foreign_key);
            }
        }
        sql += String.join(",", fields);
        if(primaryKeys.size() > 0){
            sql +=",";
            sql+= "primary key ( " + String.join(",", primaryKeys) + ")";
        }
        if(foriegnKeys.size() > 0){
            sql += ",";
            sql += String.join(",", foriegnKeys);
        }
        
        
        sql += ")";
        return sql;
    }

    
    
}


