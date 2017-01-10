/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import com.uit.daa.issuer.Controllers.Config;
import com.uit.daa.issuer.Controllers.DirtyWork;
import com.uit.daa.issuer.Jdbc.C;
import com.uit.daa.issuer.Models.App;
import com.uit.daa.issuer.Models.AppFileBuilder;
import com.uit.daa.issuer.Models.AppFileField;
import com.uit.daa.issuer.Models.Issuer;
import com.uit.daa.issuer.Models.Issuer.JoinMessage2;
import com.uit.daa.issuer.Models.Level;
import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.MemberInfo;
import com.uit.daa.issuer.Models.MemberType;
import com.uit.daa.issuer.Models.Service;
import com.uit.daa.issuer.Models.User;
import com.uit.daa.issuer.Models.crypto.AESEncryptor;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import com.uit.daa.issuer.Models.crypto.BitKeySelector;
import iaik.security.ec.math.curve.ECPoint;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;
import javax.validation.constraints.NotNull;
import org.json.JSONException;
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
      Date expire_date;
    public buildAppData(){
        expire_date = new Date();
       Calendar c = Calendar.getInstance();
        c.setTime(expire_date); 
        c.add(Calendar.DATE, 1);
        expire_date = c.getTime();
    }
    public App getApp(JdbcTemplate j) throws SQLException{
        return App.getFromID(j, Integer.parseInt(appId));
    }
    public byte[] buildEncodedJSON(JdbcTemplate j, Issuer issuer) throws SQLException, NoSuchAlgorithmException{
        App app = getApp(j);
        Member member = app.M;
        ArrayList<AppFileField> aaff = new ArrayList<>();
        MemberInfo memberInfo = new MemberInfo() ;
        
        Map<Integer,String> sendablePremission = new HashMap<Integer, String>();
        Map<String,String> basenameWrtFields = new HashMap<String, String>();
        switch(member.type){
            case MemberType.USER_TYPE : 
                memberInfo = User.getFromMemberID(j, member.id);
                sendablePremission = Level.getSetOfPermissionForMemberType(j, MemberType.USER_TYPE);
                basenameWrtFields = Level.getSetBasenameWrtFields(j, MemberType.USER_TYPE);
                
                break;
            case MemberType.SERVICE_TYPE:
                memberInfo = Service.getFromMemberID(j, member.id);
                sendablePremission = Level.getSetOfPermissionForMemberType(j, MemberType.SERVICE_TYPE);
                basenameWrtFields = Level.getSetBasenameWrtFields(j, MemberType.SERVICE_TYPE);
                break;
        }
        memberInfo.getMember().type = member.type;
        String res = buildAppDataFromMemberInfo(memberInfo,sendablePremission,basenameWrtFields,issuer);
        
        //FIXME : encode data here
        //byte[] encoded = AESEncryptor.encrypt(BitKeySelector.getAES128Key(afb.getDecryptKey()), IV, res);
        return res.getBytes();
        
    }
    

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    /**
     * 
     * @param mi MemberInfo 
     * @param sap set of permisson/membertype that a app can request to another app
     * @param bwf set of basename<->fields that a app use to create credentials/basename
     * @return JSONObject.toString()
     */
    public String buildAppDataFromMemberInfo(MemberInfo mi, Map<Integer,String> sap, Map<String,String> bwf, Issuer issuer){
        ArrayList<AppFileField> aaff = new ArrayList<>();
        JSONObject res = new JSONObject();
        JSONObject tmp = new JSONObject();
        BNCurve curve = issuer.getCurve();
        SecureRandom random = new SecureRandom();
        JoinMessage2 jm2;
        //Create Ano-Id for permission
        //basename : permission
        for(Entry<Integer,String> e : sap.entrySet()){
            try {
                tmp.put(e.getKey().toString(), e.getValue());
                
            } catch (JSONException ex) {
                Logger.getLogger(buildAppData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
                return null;
            }
        }
        try{
        AppFileField aff = new AppFileField("permission");
        BigInteger esk = curve.getRandomModOrder(random);
            ECPoint epk = curve.getG2().multiplyPoint(esk);
            aff.setEsk(esk.toString());
            aff.setEpk(DirtyWork.bytesToHex(epk.encodePoint()));
            BigInteger gsk = curve.getRandomModOrder(random);
            
            aff.setGsk(gsk.toString());
            
            tmp.put("expire_date",expire_date.toString());
            tmp.put(C.CL_M_TYPE_ID,mi.getMember().type);
            
            jm2 = issuer.createStaticCredential(gsk, tmp.toString().getBytes());
            aff.setCredential(jm2.toJson(curve));
            aff.setValue(tmp.toString());
            aaff.add(aff);
        }catch(Exception e){
            Logger.getLogger(buildAppData.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
            return null;
        }
        
        
        // AnoId for each in bwf
        
        for(Entry<String,String> e : bwf.entrySet()){
            try{
            tmp = new JSONObject();
            tmp = mi.getJSONFromASetOfField(e.getValue());
            AppFileField aff = new AppFileField(e.getKey());
        BigInteger esk = curve.getRandomModOrder(random);
            ECPoint epk = curve.getG2().multiplyPoint(esk);
            aff.setEsk(esk.toString());
            aff.setEpk(DirtyWork.bytesToHex(epk.encodePoint()));
            BigInteger gsk = curve.getRandomModOrder(random);
            tmp.put("expire_date",expire_date.toString());
            aff.setGsk(gsk.toString());
            jm2 = issuer.createStaticCredential(gsk, tmp.toString().getBytes());
            aff.setCredential(jm2.toJson(curve));
            aff.setValue(tmp.toString());
            aaff.add(aff);
        }catch(Exception ex){
            Logger.getLogger(buildAppData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        }
        AppFileBuilder afb = new AppFileBuilder();
        afb.setFields(aaff);
        afb.setAppId(appId);
        afb.setCurve(curve.getName());
        afb.setIpk(issuer.pk.toJSON(curve));
        afb.setMember_type(mi.getMember().getType());
        String r = afb.toJSON();
        return r;
        
        
    }
    
}
