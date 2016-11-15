/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Jdbc;

import com.sun.javafx.collections.MappingChange.Map;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 *
 * @author nguyenduyy
 */
public class SelectQueryHelper {
    ArrayList<String> tableNames;
    ArrayList<String> columnNames;
    ArrayList<String> whereClauses;
    
    
    public SelectQueryHelper(){
        tableNames = new ArrayList<>();
        whereClauses = new ArrayList<>();
        columnNames = new ArrayList<>();
    
    }
    public SelectQueryHelper addTableName(String name){
        tableNames.add(name);
        return this;
               
    }
    public SelectQueryHelper addColumnName(String name){
        columnNames.add(name);
        return this;
    }
    public SelectQueryHelper addColumnWrtTable(String tb, String cl){
        columnNames.add(tb+"."+cl);
        return this;
    }
    public SelectQueryHelper addWhereClause(String logic, String column, String compare, String value){
        if(whereClauses.size() == 0){
            whereClauses.add(column + " "+ compare+ " " + value);
        }
        else{
        whereClauses.add(logic + " " + column + " "+ compare+ " " + value);
        }
        return this;
    }
    public SelectQueryHelper addTableInnerJoin(String tb1, String cl1, String tb2, String cl2){
        String s = tb1+"."+cl1+ " = " + tb2+"."+cl2;
        String w = tb1 + " inner join " + tb2 + " on " + s;
        tableNames.add(w);
        return this;
    }
    public String getSelectSQL(){
        String sql = null;
        if(tableNames.size() == 0 || columnNames.size() == 0){
            return null;
        }
        else{
            sql = "select " + String.join(",", columnNames)+ " ";
            sql += "from " + String.join(",", tableNames)+ " ";
        }
        
        if(!whereClauses.isEmpty())    {
            sql += "where " + String.join(" ",whereClauses);
        }
        return sql;
        
    }
    
    
}
