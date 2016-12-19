/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import com.uit.daa.issuer.Controllers.DirtyWork;
import com.uit.daa.issuer.Controllers.IssuerController;
import com.uit.daa.issuer.Jdbc.C;
import com.uit.daa.issuer.Models.AppFileField;
import com.uit.daa.issuer.Models.Authenticator;
import com.uit.daa.issuer.Models.Issuer;
import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.User;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import iaik.security.ec.math.curve.ECPoint;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class buildUserData {
    
    @NotNull
    public String userId;
    public String memberId;
    public buildUserData(){
        
    }
    public User getUserFromUserId(JdbcTemplate j) throws SQLException{
        return User.getFromID(j,Integer.parseInt(userId));
    }
    public User getUserFromMemberId(JdbcTemplate j) throws SQLException{
        return User.getFromMemberID(j,Integer.parseInt(memberId));
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    public ArrayList<AppFileField> buildFields(JdbcTemplate j, Issuer issuer) throws SQLException, NoSuchAlgorithmException{
        User u = getUserFromMemberId(j);
        SecureRandom random = new SecureRandom();
        BNCurve curve = issuer.getCurve();
                        
        ArrayList<AppFileField> aaff = new ArrayList<>();
        AppFileField aff_name = new AppFileField("user_name");
        AppFileField aff_job = new AppFileField("user_job");
        Issuer.JoinMessage2 jm2 ;
        //credential for user_name 
        JSONObject json = new JSONObject();
        try {
            BigInteger esk = curve.getRandomModOrder(random);
            ECPoint epk = curve.getG2().multiplyPoint(esk);
            aff_name.setEsk(esk.toString());
            aff_name.setEpk(DirtyWork.bytesToHex(epk.encodePoint()));
            BigInteger gsk = curve.getRandomModOrder(random);
            json.put("user_name",u.name);
            aff_name.setValue(json.toString());
            aff_name.setGsk(gsk.toString());
            jm2 = issuer.createStaticCredential(gsk, json.toString().getBytes());
            aff_name.setCredential(jm2.toJson(curve));
            
        } catch (JSONException ex) {
            Logger.getLogger(buildUserData.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        json = new JSONObject();
        try {
            BigInteger esk = curve.getRandomModOrder(random);
            ECPoint epk = curve.getG2().multiplyPoint(esk);
            aff_job.setEsk(esk.toString());
            aff_job.setEpk(DirtyWork.bytesToHex(epk.encodePoint()));
            BigInteger gsk = curve.getRandomModOrder(random);
            json.put("user_job",u.job);
            aff_job.setValue(json.toString());
            aff_job.setGsk(gsk.toString());
            jm2 = issuer.createStaticCredential(gsk, json.toString().getBytes());
            aff_job.setCredential(jm2.toJson(curve));
        } catch (JSONException ex) {
            Logger.getLogger(buildUserData.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        aaff.add(aff_job);
        aaff.add(aff_name);
        return aaff;
                
    }
    
}
