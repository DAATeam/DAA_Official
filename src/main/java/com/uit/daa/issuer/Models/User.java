/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Models;

import com.uit.daa.issuer.Jdbc.C;
import com.uit.daa.issuer.Jdbc.ManipulateQueryHelper;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class User {
    public String name;
    public String job;
    public Member member;
    public Integer id;
    public ResultSet resultSet;
    public User(Member m, String n, String j){
        this.member = m;
        this.name = n;
        this.job = j;
    }
    public User(){
        
    }
    public void save(JdbcTemplate j) throws SQLException{
        ManipulateQueryHelper qh = new ManipulateQueryHelper();
        qh.addTableName(C.TB_USER);
        qh.addColumnName(C.CL_NAME,C.CL_JOB, C.CL_M_ID);
        String sql = qh.getInsertSQL();
        if(sql != null){
        PreparedStatement pp = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pp.setString(1, name);
        pp.setString(2, job);
        pp.setInt(3, member.id);
        pp.executeUpdate();
        ResultSet gk = pp.getGeneratedKeys();
            if(gk.next()){
                this.id = gk.getInt(1);
            }
        pp.close();
        }
    }
    public static User getFromID(JdbcTemplate j, Integer id) throws SQLException{
         String sql = "select * from "+C.TB_USER + " inner join " + C.TB_MEMBER 
                    + " on " +C.TB_MEMBER+"."+C.CL_ID + " = " +C.TB_USER+"."+ C.CL_M_ID 
                + " where "+C.TB_USER+"."+ C.CL_ID + " = " + id;
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            User a = new User();
            a.member = new Member();
            a.member.id = rs.getInt(C.CL_M_ID);
            a.member.curve = BNCurve.createBNCurveFromName("TPM_ECC_BN_P256");
            a.member.epk = rs.getBytes(C.CL_EPK);
            a.member.esk = rs.getBytes(C.CL_ESK);
            a.member.M = rs.getString(C.CL_M);
            a.id =id;
            a.name = rs.getString(C.CL_NAME);
            a.job = rs.getString(C.CL_JOB);
            a.resultSet = rs;
            return a;
            
        }
        else{
            return null;
        }
    }      
    
    public static User getFromMemberID(JdbcTemplate j, Integer id) throws SQLException{
         String sql = "select * from "+C.TB_USER + " inner join " + C.TB_MEMBER 
                    + " on " +C.TB_MEMBER+"."+C.CL_ID + " = " +C.TB_USER+"."+ C.CL_M_ID 
                + " where "+C.TB_USER+"."+ C.CL_M_ID + " = " + id;
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            User a = new User();
            a.member = new Member();
            a.member.id = rs.getInt(C.CL_M_ID);
            a.member.curve = BNCurve.createBNCurveFromName("TPM_ECC_BN_P256");
            a.member.epk = rs.getBytes(C.CL_EPK);
            a.member.esk = rs.getBytes(C.CL_ESK);
            a.member.M = rs.getString(C.CL_M);
            a.id =id;
            a.name = rs.getString(C.CL_NAME);
            a.job = rs.getString(C.CL_JOB);
            
            return a;
            
        }
        else{
            return null;
        }
    }
    
    public boolean contain(String key, String value){
        if(key.equals(C.CL_NAME)){
            return value.equals(name);
        }        
        else if(key.equals(C.CL_JOB)){
            return value.equals(job);
        }
        else return false;
    }
    
          
}
