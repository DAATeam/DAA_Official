/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Models;

import com.uit.daa.issuer.Controllers.Config;
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
public class Service {
    public String service_name;
    public String service_permission;
    public Member member;
    public Integer id;
    public ResultSet resultSet;
    
    public Service(){
        
    }
    public Service(Member m,String name,String permission ){
        service_name = name;
        service_permission = permission;
        member = m;
                
    }
    public void save(JdbcTemplate j) throws SQLException{
          ManipulateQueryHelper qh = new ManipulateQueryHelper();
        qh.addTableName(C.TB_SERVICE);
        qh.addColumnName(C.CL_SERNAME,C.CL_PERMISSION, C.CL_M_ID);
        String sql = qh.getInsertSQL();
        if(sql != null){
        PreparedStatement pp = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        pp.setString(1, service_name);
        pp.setString(2, service_permission);
        pp.setInt(3, member.id);
        pp.executeUpdate();
        ResultSet gk = pp.getGeneratedKeys();
            if(gk.next()){
                this.id = gk.getInt(1);
            }
        pp.close();
        }
    }
    public static Service getFromID(JdbcTemplate j, Integer id) throws SQLException{
         String sql = "select * from "+C.TB_SERVICE + " inner join " + C.TB_MEMBER 
                    + " on " +C.TB_MEMBER+"."+C.CL_ID + " = " +C.TB_SERVICE+"."+ C.CL_M_ID 
                + " where "+C.TB_SERVICE+"."+ C.CL_ID + " = " + id;
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            Service a = new Service();
            a.member = new Member();
            a.member.id = rs.getInt(C.CL_M_ID);
            a.member.curve = BNCurve.createBNCurveFromName(Config.curveName);
            a.member.epk = rs.getBytes(C.CL_EPK);
            a.member.esk = rs.getBytes(C.CL_ESK);
            a.member.M = rs.getString(C.CL_M);
            a.id =id;
            a.service_name = rs.getString(C.CL_SERNAME);
            a.service_permission = rs.getString(C.CL_PERMISSION);
            a.resultSet = rs;
            return a;
            
        }
        else{
            return null;
        }
    }      
    
    public static Service getFromMemberID(JdbcTemplate j, Integer id) throws SQLException{
         String sql = "select * from "+C.TB_SERVICE + " inner join " + C.TB_MEMBER 
                    + " on " +C.TB_MEMBER+"."+C.CL_ID + " = " +C.TB_SERVICE+"."+ C.CL_M_ID 
                + " where "+C.TB_SERVICE+"."+ C.CL_M_ID + " = " + id;
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            Service a = new Service();
            a.member = new Member();
            a.member.id = rs.getInt(C.CL_M_ID);
            a.member.curve = BNCurve.createBNCurveFromName("TPM_ECC_BN_P256");
            a.member.epk = rs.getBytes(C.CL_EPK);
            a.member.esk = rs.getBytes(C.CL_ESK);
            a.member.M = rs.getString(C.CL_M);
            a.id =id;
            a.service_name = rs.getString(C.CL_SERNAME);
            a.service_permission = rs.getString(C.CL_PERMISSION);
            
            return a;
            
        }
        else{
            return null;
        }
    }
    
}
