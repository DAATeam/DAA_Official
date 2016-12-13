/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import com.uit.daa.issuer.Controllers.Config;
import com.uit.daa.issuer.Controllers.DirtyWork;
import com.uit.daa.issuer.Models.App;
import com.uit.daa.issuer.Models.AppFileBuilder;
import com.uit.daa.issuer.Models.AppFileField;
import com.uit.daa.issuer.Models.Issuer;
import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.MemberType;
import com.uit.daa.issuer.Models.User;
import com.uit.daa.issuer.Models.crypto.AESEncryptor;
import com.uit.daa.issuer.Models.crypto.BitKeySelector;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.validation.constraints.NotNull;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class buildAppData {
    @NotNull
    public String appId;
      private String IV = "0123456789abcdef";
    public buildAppData(){
        
    }
    public App getApp(JdbcTemplate j) throws SQLException{
        return App.getFromID(j, Integer.parseInt(appId));
    }
    public byte[] buildEncodedJSON(JdbcTemplate j, Issuer issuer) throws SQLException, NoSuchAlgorithmException{
        App app = getApp(j);
        Member member = app.M;
        ArrayList<AppFileField> aaff = new ArrayList<>();
        switch(member.type){
            case MemberType.USER_TYPE : 
                buildUserData bud = new buildUserData();
                bud.setMemberId(member.id.toString());
                aaff = bud.buildFields(j, issuer);
                break;
            case MemberType.SERVICE_TYPE:
                buildServiceData bsd = new buildServiceData();
                bsd.setMemberId(member.id.toString());
                aaff = bsd.buildFields(j, issuer);
                break;
        }
        JSONObject json = new JSONObject();
        AppFileBuilder afb = new AppFileBuilder();
        afb.setFields(aaff);
        afb.setAppId(app.appID.toString());
        afb.setCurve(issuer.getCurve().getName());
        afb.setEsk(member.esk);
        afb.setDecryptKey(member.M.getBytes());
        afb.setIpk(issuer.pk.toJSON(issuer.getCurve()));
        String res = afb.toJSON();
        //FIXME : encode data here
        byte[] encoded = AESEncryptor.encrypt(BitKeySelector.getAES128Key(afb.getDecryptKey()), IV, res);
        return encoded;
        
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    
}
