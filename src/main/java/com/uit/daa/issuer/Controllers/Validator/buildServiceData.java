/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import com.uit.daa.issuer.Controllers.DirtyWork;
import com.uit.daa.issuer.Controllers.IssuerController;
import com.uit.daa.issuer.Models.AppFileField;
import com.uit.daa.issuer.Models.Authenticator;
import com.uit.daa.issuer.Models.Issuer;
import com.uit.daa.issuer.Models.Service;
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
public class buildServiceData {
    @NotNull
    public String serviceId;
    public String memberId;
    public buildServiceData(){
        
    }
    public Service getServiceFromServiceId(JdbcTemplate j) throws SQLException{
        return Service.getFromID(j, Integer.parseInt(serviceId));
    }
    public Service getServiceFromMemberId(JdbcTemplate j) throws SQLException{
        return Service.getFromMemberID(j, Integer.parseInt(memberId));
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    

    public String getServiceId() {
        return serviceId;
    }
    

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
    public ArrayList<AppFileField> buildFields(JdbcTemplate j, Issuer issuer) throws SQLException, NoSuchAlgorithmException{
       Service s = getServiceFromMemberId(j);
        SecureRandom random = new SecureRandom();
        BNCurve curve = issuer.getCurve();
                        
        ArrayList<AppFileField> aaff = new ArrayList<>();
        AppFileField aff_name = new AppFileField("service_name");
        AppFileField aff_per = new AppFileField("service_permission");
        Issuer.JoinMessage2 jm2 ;
        //credential for user_name 
        JSONObject json = new JSONObject();
        try {
            json.put("service_name",s.service_name);
            aff_name.setValue(json.toString());
            BigInteger esk = curve.getRandomModOrder(random);
            ECPoint epk = curve.getG2().multiplyPoint(esk);
            aff_name.setEsk(esk.toString());
            aff_name.setEpk(DirtyWork.bytesToHex(epk.encodePoint()));
            BigInteger gsk = curve.getRandomModOrder(random);
            
            aff_name.setGsk(gsk.toString());
            jm2 = issuer.createStaticCredential(gsk, json.toString().getBytes());
            aff_name.setCredential(jm2.toJson(curve));
        } catch (JSONException ex) {
            Logger.getLogger(buildUserData.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        json = new JSONObject();
        try {
            json.put("service_permission",s.service_permission);
            aff_per.setValue(json.toString());
            
            BigInteger esk = curve.getRandomModOrder(random);
            ECPoint epk = curve.getG2().multiplyPoint(esk);
            aff_per.setEsk(esk.toString());
            aff_per.setEpk(DirtyWork.bytesToHex(epk.encodePoint()));
            BigInteger gsk = curve.getRandomModOrder(random);
            
            aff_per.setGsk(gsk.toString());
            jm2 = issuer.createStaticCredential(gsk, json.toString().getBytes());
            aff_per.setCredential(jm2.toJson(curve));
        } catch (JSONException ex) {
            Logger.getLogger(buildUserData.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        aaff.add(aff_per);
        aaff.add(aff_name);
        return aaff;
    }
    
}
