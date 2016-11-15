/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Jdbc;

import com.mysql.jdbc.PreparedStatement;
import com.sun.javafx.collections.MappingChange.Map;
import com.uit.daa.issuer.Models.App;
import com.uit.daa.issuer.Models.Authenticator;
import com.uit.daa.issuer.Models.Issuer;
import com.uit.daa.issuer.Models.Issuer.JoinMessage1;
import com.uit.daa.issuer.Models.Issuer.JoinMessage2;
import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.Nonce;
import com.uit.daa.issuer.Models.User;
import com.uit.daa.issuer.Models.Verifier;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author nguyenduyy
 */
public class IssuerJdbcTemplate {
    DataSource dataSource;
    public JdbcTemplate jdbcTemplate;
    
    @Autowired
    public void setDataSource(DataSource dataSource) throws SQLException, NoSuchAlgorithmException, IOException, JSONException {
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        
        
        createTableIfNotExists();
        
        
    }
    private void createTableIfNotExists() throws SQLException, NoSuchAlgorithmException, IOException, JSONException{
        ArrayList<Table> tables = new ArrayList<>();
         DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
        ResultSet rs = metaData.getTables(null, null,C.TB_MEMBER,null);
        if(rs.next() == false){
        
        Table member = new Table(C.TB_MEMBER);
        member.addColumn(new Column().setToDefaultIdField());
        member.addColumn(new Column(C.CL_ESK,Column.DataTypeEnum.BLOB,"endor private key"));
        member.addColumn(new Column(C.CL_EPK,Column.DataTypeEnum.BLOB,"endor public key"));
        member.addColumn(new Column(C.CL_M,Column.DataTypeEnum.TEXT,"PIN"));
        member.addColumn(new Column(C.CL_M_TYPE_ID,Column.DataTypeEnum.INT,"user / service"));
        member.addColumn(new Column().setToDefaultTimeStampWithName(C.CL_CREATETIME));
        jdbcTemplate.execute(member.getCreateTableSql());
        }
               
        rs = metaData.getTables(null, null,C.TB_M_TYPE,null);
        if(!rs.next()){
        Table t  = new Table(C.TB_M_TYPE);
        t.addColumn(new Column().setToDefaultIdField());
        
        t.addColumn(new Column(C.CL_M_TYPE_PREFIX,Column.DataTypeEnum.TEXT,"prefix"));
        
        t.addColumn(new Column().setToDefaultTimeStampWithName(C.CL_CREATETIME));
          jdbcTemplate.execute(t.getCreateTableSql());
       
        }
        
        rs = metaData.getTables(null, null,C.TB_USER,null);
        if(!rs.next()){
        Table user  = new Table(C.TB_USER);
        user.addColumn(new Column().setToDefaultIdField());
        user.addColumn(new Column(C.CL_NAME,Column.DataTypeEnum.TEXT,"name"));
        user.addColumn(new Column(C.CL_JOB,Column.DataTypeEnum.TEXT,"job"));
        user.addColumn(new Column(C.CL_M_ID,Column.DataTypeEnum.BIGINT,"member id"));
        user.addColumn(new Column().setToDefaultTimeStampWithName(C.CL_CREATETIME));
       
        jdbcTemplate.execute(user.getCreateTableSql());
       
        }
        rs = metaData.getTables(null, null,C.TB_SERVICE,null);
        if(!rs.next()){
        Table service  = new Table(C.TB_SERVICE);
        service.addColumn(new Column().setToDefaultIdField());
        service.addColumn(new Column(C.CL_SERNAME,Column.DataTypeEnum.TEXT,"name"));
        service.addColumn(new Column(C.CL_PERMISSION,Column.DataTypeEnum.TEXT,"job"));
        service.addColumn(new Column(C.CL_M_ID,Column.DataTypeEnum.BIGINT,"member id"));
        service.addColumn(new Column().setToDefaultTimeStampWithName(C.CL_CREATETIME));
         jdbcTemplate.execute(service.getCreateTableSql());
        
        }
        rs = metaData.getTables(null, null,C.TB_LEVEL,null);
        if(!rs.next()){
        Table level  = new Table(C.TB_LEVEL);
        level.addColumn(new Column().setToDefaultIdField());
        level.addColumn(new Column(C.CL_FIELD,Column.DataTypeEnum.TEXT,"field"));
        level.addColumn(new Column(C.CL_CODE,Column.DataTypeEnum.INT,"id"));
        level.addColumn(new Column(C.CL_LEVEL,Column.DataTypeEnum.INT,"level "));
        level.addColumn(new Column().setToDefaultTimeStampWithName(C.CL_CREATETIME));
          jdbcTemplate.execute(level.getCreateTableSql());
        
        }
        rs = metaData.getTables(null, null,C.TB_CONFIG,null);
        if(!rs.next()){       
        Table config  = new Table(C.TB_CONFIG);
        config.addColumn(new Column().setToDefaultIdField());
        config.addColumn(new Column(C.CL_ISK,Column.DataTypeEnum.TEXT,"private key"));
        config.addColumn(new Column(C.CL_IPK ,Column.DataTypeEnum.TEXT,"public key"));
        config.addColumn(new Column(C.CL_BNCURVE ,Column.DataTypeEnum.TEXT,"curve"));
        config.addColumn(new Column(C.CL_MAXJOIN,Column.DataTypeEnum.INT,"max"));
        jdbcTemplate.execute(config.getCreateTableSql());
        }
        
        rs = metaData.getTables(null, null,C.TB_NONCE,null);
        if(!rs.next()){
            Table nonce = new Table(C.TB_NONCE);
            nonce.addColumn(new Column().setToDefaultIdField());
        nonce.addColumn(new Column(C.CL_NONCE,Column.DataTypeEnum.BLOB,"nonce"));
        nonce.addColumn(new Column(C.CL_USED ,Column.DataTypeEnum.INT,"use status"));
        nonce.addColumn(new Column(C.CL_APPID,Column.DataTypeEnum.BIGINT,"app id"));
        nonce.addColumn(new Column(C.CL_MESSAGE, Column.DataTypeEnum.TEXT,"signature message"));
        nonce.addColumn(new Column().setToDefaultTimeStampWithName(C.CL_CREATETIME));
        jdbcTemplate.execute(nonce.getCreateTableSql());
        }
             
        //addExampleData();

        
        
    }
    private void addExampleData() throws NoSuchAlgorithmException, SQLException{
        //config
        String curveName = "TPM_ECC_BN_P256";
        BNCurve curve = BNCurve.createBNCurveFromName(curveName);
        Issuer i = new Issuer(curve);
        int max_join = 10;
        ManipulateQueryHelper qh = new ManipulateQueryHelper();
        qh.addTableName(C.TB_CONFIG);
        qh.addColumnName(C.CL_ISK, C.CL_IPK, C.CL_BNCURVE, C.CL_MAXJOIN);
        String sql = qh.getInsertSQL();
        if(sql != null){
            PreparedStatement pp= (PreparedStatement) dataSource.getConnection().prepareStatement(sql);
            pp.setString(1, i.getSk().toJson(curve));
            pp.setString(2, i.pk.toJSON(curve));
            pp.setString(3, curveName);
            pp.setInt(4, max_join);
            pp.executeUpdate();
            pp.close();
            
        }
        //Member
        Member m = new Member("LoveCat",curve, 1);
        m.save(jdbcTemplate);
        //App
        App a = new App(m,"ASUS Zenfone");
        a.save(jdbcTemplate);
        //User
        User u = new User(m, "Thanh Uyen","Manager");
        u.save(jdbcTemplate);
        
        
        
    }
    
    public void testScript() throws SQLException, NoSuchAlgorithmException, IOException, JSONException{
        String res = "";
        //Submit data
        
        String PIN = "LoveCat";
        Integer appId = 1;
        String deviceId = "ASUS ZenFone";
        Integer memberId = 1;
        org.json.JSONObject json = new JSONObject().put(C.CL_NAME, "Thanh Uyen");
        
        //Retrive data
        Issuer i = Issuer.getFromID(jdbcTemplate);
        Member member = Member.getFromID(jdbcTemplate,memberId );
        User user = User.getFromMemberID(jdbcTemplate, memberId);
        //Simulate
        SecureRandom random = new SecureRandom();
        BigInteger gsk = member.curve.getRandomModOrder(random);
        Authenticator auth = new Authenticator(member.curve, i.pk,gsk);
        boolean trust_member = true;
        Iterator<String> keys = json.keys();
        while(keys.hasNext()){
            String k = (String)keys.next();
            trust_member &= user.contain(k, json.getString(k));
        }
        
        if(trust_member){
            res += "trusted \n";
            boolean success = true;
            BigInteger nonce = i.getCurve().getRandomModOrder(random);
            JoinMessage1 jm1 = auth.EcDaaJoin1(nonce);
            JoinMessage2 jm2 = i.EcDaaIssuerJoin(jm1, false);
            success &= auth.EcDaaJoin2(jm2);
            Authenticator.EcDaaSignature sig = auth.EcDaaSign("Coffee",json.getString(C.CL_NAME));
            //Verify
            Verifier v = new Verifier(i.getCurve());
            boolean valid = v.verify(sig, "Coffees", i.pk, null);
            if(valid){
                res+="valid\n";
            }
            else{
                res+="invalid\n";
            }
            
        }
        else{
            res+="untrusted\n";
        }
        System.out.println(res);
        
    }
    
    public Member getMember(Integer appId, String M) throws SQLException{
        SelectQueryHelper sq = new SelectQueryHelper();
        sq.addColumnName("*");
        sq.addTableInnerJoin(C.TB_MEMBER, C.CL_ID, C.TB_APP, C.CL_M_ID);
        sq.addWhereClause(null, C.CL_APPID, "=", Integer.toString(appId));
        sq.addWhereClause("and", C.CL_M, "=", "?");
        String sql =sq.getSelectSQL();
        
        PreparedStatement ps = (PreparedStatement) dataSource.getConnection().prepareStatement(sql);
        ps.setString(1, M);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Member member = new Member().constructFromResultSet(rs);
            return member;
        }
        else{
            return null;
        }
        
    }
    public Member getMemberDataByNonce(BigInteger n) throws SQLException{
        SelectQueryHelper sq = new SelectQueryHelper();
        sq.addColumnName("*");
        sq.addTableName(C.TB_NONCE).addTableName(C.TB_APP).addTableName(C.TB_MEMBER);
        sq.addWhereClause(null,C.TB_NONCE+"."+C.CL_APPID, "=", C.TB_APP+"."+C.CL_APPID);
        sq.addWhereClause("and", C.TB_APP+"."+C.CL_M_ID, "=", C.TB_MEMBER+"."+C.CL_ID);
        String sql = sq.getSelectSQL();
        PreparedStatement ps = (PreparedStatement) dataSource.getConnection().prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Member m = new Member().constructFromResultSet(rs);
            return m;
        }
        else{
            return null;
        }
    }
    public Issuer getIssuer() throws SQLException, NoSuchAlgorithmException{
        return Issuer.getFromID(jdbcTemplate);
    }
    public Nonce getNonce(BigInteger n) throws SQLException{
        SelectQueryHelper sq = new SelectQueryHelper();
        sq.addTableName(C.TB_NONCE);
        sq.addColumnName("*");
        sq.addWhereClause(null, C.CL_NONCE, "=", " ?");
        String sql = sq.getSelectSQL();
        PreparedStatement ps = (PreparedStatement) dataSource.getConnection().prepareStatement(sql);
        ps.setBytes(1, n.toByteArray());
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
        Nonce nonce = new Nonce().constructFromResultSet(rs);
        return nonce;
        }
        else{ return null;}
        
    }
    public Nonce createNonce(Integer appId,Issuer issuer, SecureRandom random) throws SQLException{
         BigInteger n = issuer.getCurve().getRandomModOrder(random);
         while(getNonce(n) != null){
             n = issuer.getCurve().getRandomModOrder(random);
         }
         Nonce nonce = new Nonce(n, appId);
         nonce.save(jdbcTemplate);
         return nonce;
    }
}
