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
public class User extends MemberInfo{
    //info fields, except id and member id
    public final static String[] fieldSet = { C.CL_NAME, C.CL_JOB, C.CL_USER_ACCOUNT, C.CL_USER_DRIVE};
    public ResultSet resultSet;
    
    
    public User(Member m){
        this.member = m;
        initData();
    }
    public User(){
        initData();
    }
    private void initData(){
        for(int i=0; i< fieldSet.length; i++){
            infoMap.put(fieldSet[i], "");
        }
    }
    
    public void save(JdbcTemplate j) throws SQLException{
        ManipulateQueryHelper qh = new ManipulateQueryHelper();
        qh.addTableName(C.TB_USER);
        for(int i=0; i<fieldSet.length; i++){
            qh.addColumnName(fieldSet[i]);
        }
        qh.addColumnName(C.CL_M_ID);
        String sql = qh.getInsertSQL();
        if(sql != null){
        PreparedStatement pp = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int i = 0;
        for(i=0; i< fieldSet.length; i++){
            pp.setString(i+1,infoMap.get(fieldSet[i]) );
        }
        
        pp.setInt(i+1, this.member.id);
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
            Level l = new Level();
            l.getFromLevelID(j,rs.getInt(C.CL_USER_LEVEL));
            a.level = l;
            for(int i=0; i< User.fieldSet.length; i++){
               a.setInfo(User.fieldSet[i], rs.getString(User.fieldSet[i]));
            }
            
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
            String[] fs = User.fieldSet;
            for(int i=0; i< fs.length; i++){
                String f = fs[i];
                a.setInfo(f, rs.getString(f));
            }
            
            a.resultSet = rs;
            
            return a;
            
        }
        else{
            return null;
        }
    }
    
    
          
}
