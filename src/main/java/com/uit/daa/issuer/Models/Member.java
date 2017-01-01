/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Models;

import com.mysql.jdbc.PreparedStatement;
import com.uit.daa.issuer.Controllers.Config;
import com.uit.daa.issuer.Jdbc.C;
import com.uit.daa.issuer.Jdbc.ManipulateQueryHelper;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.ECPoint;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Random;
import org.springframework.jdbc.core.JdbcTemplate;
import sun.security.pkcs11.Secmod;

/**
 *
 * @author nguyenduyy
 */
public class Member {
    public Integer id;
    public byte[] esk;
    public byte[] epk;
    public String M;
    public BNCurve curve;
    public Integer type;
    public Member(String M, BNCurve c , Integer type){
        SecureRandom random =  new SecureRandom();
        this.curve = c;
        this.M =M;
        BigInteger k = c.getRandomModOrder(random);
        //check k whether exists
                
        iaik.security.ec.math.curve.ECPoint p = curve.getG1().multiplyPoint(k);
        esk = k.toByteArray();
        epk = p.encodePoint();
        this.type = type;
        
    }
    public Member(){
        
    }
    public void save(JdbcTemplate j) throws SQLException{
        ManipulateQueryHelper qh = new ManipulateQueryHelper();
        qh.addTableName(C.TB_MEMBER);
        qh.addColumnName(C.CL_ESK, C.CL_EPK, C.CL_M, C.CL_M_TYPE_ID);
        String sql = qh.getInsertSQL();
        if(sql!= null){
            PreparedStatement pp = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pp.setBytes(1, esk);
            pp.setBytes(2, epk);
            pp.setString(3, M);
            pp.setInt(4, type);
            pp.executeUpdate();
            ResultSet gk = pp.getGeneratedKeys();
            if(gk.next()){
                this.id = gk.getInt(1);
            }
            pp.close();
        }
    }
    public static Member getFromID(JdbcTemplate j, Integer id) throws SQLException{
        String sql = "select * from "+C.TB_MEMBER 
                
                + " where "+C.CL_ID + " = " + id;
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            Member m = new Member();
            m.esk = rs.getBytes(C.CL_ESK);
            m.epk = rs.getBytes(C.CL_EPK);
            m.M = rs.getString(C.CL_M);
            m.curve = BNCurve.createBNCurveFromName(Config.curveName);
            m.id = rs.getInt(C.CL_ID);
            m.type = rs.getInt(C.CL_M_TYPE_ID);
            return m;
            
        }
        else{
            return null;
        }
    }
    public Member constructFromResultSet(ResultSet rs) throws SQLException{
        M = rs.getString(C.CL_M);
        id = rs.getInt(C.CL_ID);
        epk= rs.getBytes(C.CL_EPK);
        esk = rs.getBytes(C.CL_ESK);
        curve = BNCurve.createBNCurveFromName(Config.curveName);
        type = rs.getInt(C.CL_M_TYPE_ID);
        return this;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    
    
}
