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
public class App {
    public Member M;
    public Integer appID ;
    public String deviceID;
    public App(){
        
    }
    public App(Member m, String d){
        M = m;
        //appID = a;
        deviceID = d;
    }
    public void save(JdbcTemplate j) throws SQLException{
        ManipulateQueryHelper qh = new ManipulateQueryHelper();
        qh.addTableName(C.TB_APP);
        qh.addColumnName(C.CL_M_ID, C.CL_DEVICEID);
        String sql = qh.getInsertSQL();
        if(sql != null){
        PreparedStatement pp = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        pp.setInt(1, M.id);
        //pp.setIn(2, appID);
        pp.setString(2, deviceID);
        pp.executeUpdate();
        ResultSet gk = pp.getGeneratedKeys();
            if(gk.next()){
                this.appID = gk.getInt(1);
            }
        pp.close();
        }
    }
    public static App getFromID(JdbcTemplate j, Integer id) throws SQLException{
            String sql = "select * from "+C.TB_APP + " inner join " + C.TB_MEMBER 
                    + " on " +C.TB_MEMBER+"."+C.CL_ID + " = " +C.TB_APP+"."+ C.CL_M_ID 
                + " where "+C.CL_APPID + " = " + id;
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            App a = new App();
            a.M = new Member();
            a.M.id = rs.getInt(C.CL_M_ID);
            a.M.curve = BNCurve.createBNCurveFromName("TPM_ECC_BN_P256");
            a.M.epk = rs.getBytes(C.CL_EPK);
            a.M.esk = rs.getBytes(C.CL_ESK);
            a.M.M = rs.getString(C.CL_M);
            a.M.type = rs.getInt(C.CL_M_TYPE_ID);
            a.appID =id;
            a.deviceID = rs.getString(C.CL_DEVICEID);
            
            return a;
            
        }
        else{
            return null;
        }
    }
    
}
