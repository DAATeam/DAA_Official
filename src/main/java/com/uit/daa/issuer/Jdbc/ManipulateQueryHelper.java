/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Jdbc;

import java.util.ArrayList;

/**
 *
 * @author nguyenduyy
 */
public class ManipulateQueryHelper {
    public ArrayList<String> tableNames;
    public ArrayList<String> columnNames;
    public ArrayList<String> whereclauses;
    
    public ManipulateQueryHelper(){
        tableNames = new ArrayList<>();
        columnNames = new ArrayList<>();
        whereclauses = new ArrayList<>();
    }
    public ManipulateQueryHelper addTableName(String ... names){
        int l = names.length;
        for(int i =0; i < l; i++){
            tableNames.add(names[i]);
        }
        return this;
    }
    public ManipulateQueryHelper addColumnName(String ... names){
        int  l = names.length;
        for(int i =0; i < l; i++){
            columnNames.add(names[i]);
        }
        return this;
    }
    public ManipulateQueryHelper addWhereClause(String logic ,String column, String compare){
        if(whereclauses.isEmpty()){
            whereclauses.add(column + " " + compare);
        }
        else{
            whereclauses.add(logic+" " + column + " " + compare);
        }
        
        return this;
    }
    public String getInsertSQL() {
        if(tableNames.size() != 1){
            return null;
            
        }
        else{
            String sql = "insert into "+tableNames.get(0) + "(";
            sql += String.join(",",columnNames);
            sql+= ")";
            sql += " values (";
            for(int i=0; i< columnNames.size()-1; i++){
                sql += "? ,";
            }
            
            sql += "? )";
            return sql;
        }
    }
    
    public String getUpdateSQL(){
        if(tableNames.size() != 1){
            return null;
        }
        else{
            String sql ="update "+ tableNames.get(0) + " set ";
            for(int i =0; i < columnNames.size() -1 ; i++){
                sql+= columnNames.get(i) + " = ?";
                sql += ",";
            }
              sql+= columnNames.get(columnNames.size()-1) + " = ?";
              sql+= " where ";
            if(whereclauses.size() > 0){
                for(int i =0; i< whereclauses.size(); i++){
                    sql += whereclauses.get(i) + " ? ";
                    
                }
            }
            return sql;
        }
    }
    
}
