/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Models;

import com.mysql.jdbc.PreparedStatement;
import com.uit.daa.issuer.Jdbc.C;
import com.uit.daa.issuer.Jdbc.ManipulateQueryHelper;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class Nonce {
    public static int UNUSED = 0;
    public static int USED = 1;
    public static int SUSPICIOUS = 2;
    BigInteger nonce;
    Integer appId;
    Timestamp timestamp;
    Integer used;
    Integer id;
    public String message;
    public Nonce(){
        
    }

    public BigInteger getNonce() {
        return nonce;
    }

    public void setNonce(BigInteger nonce) {
        this.nonce = nonce;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getUsed() {
        return used;
    }

    public void setUsed(Integer used) {
        this.used = used;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Nonce(BigInteger n, Integer app){
        nonce = n;
        appId = app;
        used = 0;
    }
    public Nonce constructFromResultSet(ResultSet rs) throws SQLException{
        byte[] b = rs.getBytes(C.CL_NONCE);
        nonce = new BigInteger(b);
        appId = rs.getInt(C.CL_APPID);
        timestamp = rs.getTimestamp(C.CL_CREATETIME);
        used = rs.getInt(C.CL_USED);
        id = rs.getInt(C.CL_ID);
        message = rs.getString(C.CL_MESSAGE);
        return this;
        
    }
    public void save(JdbcTemplate j) throws SQLException{
        ManipulateQueryHelper qh = new ManipulateQueryHelper();
        qh.addTableName(C.TB_NONCE);
        qh.addColumnName(C.CL_NONCE,C.CL_APPID, C.CL_USED, C.CL_MESSAGE);
        String sql = qh.getInsertSQL();
        PreparedStatement ps = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        ps.setBytes(1, nonce.toByteArray());
        ps.setInt(2, appId);
        ps.setInt(3, 0);
        ps.setString(4,"");
        ps.executeUpdate();
         ResultSet gk = ps.getGeneratedKeys();
            if(gk.next()){
                this.id = gk.getInt(1);
            }
            ps.close();
        
    }
    public Nonce markUsed(JdbcTemplate j) throws SQLException{
        this.used =1;
        ManipulateQueryHelper mq = new ManipulateQueryHelper();
        mq.addTableName(C.TB_NONCE);
        mq.addColumnName(C.CL_USED);
        mq.addWhereClause(null,C.CL_ID ," = ");
        String sql = mq.getUpdateSQL();
        PreparedStatement ps = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql);
        ps.setInt(1,1);
        ps.setInt(2,this.id);
        ps.executeUpdate();
        return this;
    }
    public Nonce markSuspicious(JdbcTemplate j) throws SQLException {
        this.used =2;
        ManipulateQueryHelper mq = new ManipulateQueryHelper();
        mq.addTableName(C.TB_NONCE);
        mq.addColumnName(C.CL_USED);
        mq.addWhereClause(null,C.CL_ID ," = ");
        String sql = mq.getUpdateSQL();
        PreparedStatement ps = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql);
        ps.setInt(1,2);
        ps.setInt(2,this.id);
        ps.executeUpdate();
        return this;
    }
    public Nonce updateMessage(JdbcTemplate j, String m) throws SQLException{
        this.message = m;
        ManipulateQueryHelper mq = new ManipulateQueryHelper();
        mq.addTableName(C.TB_NONCE);
        mq.addColumnName(C.CL_MESSAGE);
        mq.addWhereClause(null,C.CL_ID ," = ");
        String sql = mq.getUpdateSQL();
        PreparedStatement ps = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql);
        ps.setString(1,m);
        ps.setInt(2,this.id);
        ps.executeUpdate();
        return this;
    }
    
}
