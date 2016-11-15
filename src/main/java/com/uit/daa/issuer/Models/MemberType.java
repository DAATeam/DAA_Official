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
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class MemberType {
    public static final int USER_TYPE  =1;
    public static final int SERVICE_TYPE = 2;
    public Integer id;
    public String prefix;
    
    public MemberType(String prefix){
        this.prefix = prefix;
    }
    public MemberType(){
        
    }
    public void save(JdbcTemplate j) throws SQLException{
        ManipulateQueryHelper mq = new ManipulateQueryHelper();
        mq.addTableName(C.TB_M_TYPE);
        mq.addColumnName(C.CL_M_TYPE_PREFIX);
        String sql= mq.getInsertSQL();
        PreparedStatement ps = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, prefix);
        ResultSet rs = ps.getGeneratedKeys();
        if(rs.next()){
            this.id = rs.getInt(1);
        }
        ps.close();
        
    }
    public static MemberType getFromID(Integer id, JdbcTemplate j) throws SQLException{
          String sql = "select * from "+C.TB_M_TYPE
                
                + " where "+C.CL_ID + " = " + id;
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            MemberType m = new MemberType();
            m.id = rs.getInt(C.CL_ID);
            m.prefix = rs.getString(C.CL_M_TYPE_PREFIX);
           return m;
            
        }
        else{
            return null;
        }
    }
}
