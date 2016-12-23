/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Models;

import com.uit.daa.issuer.Jdbc.C;
import com.uit.daa.issuer.Jdbc.SelectQueryHelper;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class Level {
    String level_name;
    String level_permission;
    Integer senderType;
    Integer recieverType;
    Integer id;

    public Level() {
        
    }
    

    public String getLevel_name() {
        return level_name;
    }

    public void setLevel_name(String level_name) {
        this.level_name = level_name;
    }

    public String getLevel_permission() {
        return level_permission;
    }

    public void setLevel_permission(String level_permission) {
        this.level_permission = level_permission;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void getFromLevelName(JdbcTemplate j, String levelName) throws SQLException{
        SelectQueryHelper sq = new SelectQueryHelper();
        sq.addTableName(C.TB_LEVEL);
        sq.addColumnName("*");
        sq.addWhereClause(null, C.CL_LEVEL_NAME, "=", levelName);
        String sql = sq.getSelectSQL();
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            this.id = rs.getInt(rs.getInt(C.CL_ID));
            this.level_name = rs.getString(C.CL_LEVEL_NAME);
            this.level_permission = rs.getString(C.CL_LEVEL_PERMISSION);
            this.setSenderType(rs.getInt(C.CL_LEVEL_SENDER));
            this.setRecieverType(rs.getInt(C.CL_LEVEL_RECIEVER));
            
        }
        
        
    }
    public void getFromLevelID(JdbcTemplate j, Integer id) throws SQLException{
        SelectQueryHelper sq = new SelectQueryHelper();
        sq.addTableName(C.TB_LEVEL);
        sq.addColumnName("*");
        sq.addWhereClause(null, C.CL_ID, "=", ""+id);
        String sql = sq.getSelectSQL();
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            this.id = rs.getInt(rs.getInt(C.CL_ID));
            this.level_name = rs.getString(rs.getInt(C.CL_LEVEL_NAME));
            this.level_permission = rs.getString(rs.getInt(C.CL_LEVEL_PERMISSION));
            this.setSenderType(rs.getInt(C.CL_LEVEL_SENDER));
            this.setRecieverType(rs.getInt(C.CL_LEVEL_RECIEVER));
        }
    }

    public Integer getSenderType() {
        return senderType;
    }

    public void setSenderType(Integer senderType) {
        this.senderType = senderType;
    }

    public Integer getRecieverType() {
        return recieverType;
    }

    public void setRecieverType(Integer recieverType) {
        this.recieverType = recieverType;
    }
    /**
     * 
     * @param j
     * @param i
     * @return Map between( MemberTypeId <-> set of fields of MemberType) that sender can send as requested info
     */
    public static Map<Integer,String> getSetOfPermissionForMemberType(JdbcTemplate j, Integer i) throws SQLException{
        Map<Integer,String> map =new HashMap<Integer,String>();
        SelectQueryHelper sq = new SelectQueryHelper();
        sq.addTableName(C.TB_LEVEL);
        sq.addColumnName("*");
        sq.addWhereClause(null, C.CL_LEVEL_SENDER, "=", ""+i);
        String sql = sq.getSelectSQL();
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            do{
                Integer mid = rs.getInt(C.CL_LEVEL_RECIEVER);
                String p = rs.getString(C.CL_LEVEL_NAME);
                map.put(mid, p);
            }while(rs.next());
            
            return map;
        }else return null;
        
    }
    public static Map<String,String> getSetBasenameWrtFields(JdbcTemplate j, Integer i) throws SQLException{
        Map<String,String> map =new HashMap<String,String>();
        SelectQueryHelper sq = new SelectQueryHelper();
        sq.addTableName(C.TB_LEVEL);
        sq.addColumnName("*");
        sq.addWhereClause(null, C.CL_LEVEL_RECIEVER, "=", ""+i);
        String sql = sq.getSelectSQL();
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            do{
                String basename  = rs.getString(C.CL_LEVEL_NAME);
                String p = rs.getString(C.CL_LEVEL_PERMISSION);
                map.put(basename, p);
            }while(rs.next());
            return map;
        }
        else return null;
            
    }
    
    
    
}
