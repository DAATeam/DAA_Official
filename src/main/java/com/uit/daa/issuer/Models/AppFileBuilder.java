/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Models;

import com.uit.daa.issuer.Controllers.DirtyWork;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.Observer;
import java.util.logging.Logger;
import javafx.application.Application;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author nguyenduyy
 */
public class AppFileBuilder {
     

    
    private ArrayList<AppFileField> fields;
    private ArrayList<String> TAGS ;
    private String esk;
    private String ipk;
    
    
    private String appId , curve;
    private BNCurve BNCurve = null;
    
    private File file = null;
    private Application application = null;
    private byte[] decryptKey = null;
    
    public final String MESSAGE = "message";
    public final String SIG = "sig";
    public final String CERT = "cert";
    

   

    public String getCurve() {
        return curve;
    }

    public void setCurve(String curve) {
        this.curve = curve;
    }
    
    
   
    public byte[] getDecryptKey() {
        return decryptKey;
    }

    public void setDecryptKey(byte[] decryptKey) {
        this.decryptKey = decryptKey;
    }
    
   
    public AppFileBuilder(){
        TAGS = new ArrayList<>();
        fields = new ArrayList<>();
        
        
    }
    public String toJSON(){
        JSONObject json = new JSONObject();
        
        try {
            
            json.put("appId", appId);
            json.put("curve", curve);
            json.put("esk", esk);
            json.put("ipk",ipk);
            for(AppFileField f : fields){
                json = f.putToJSONObject(json);
            }
            return json.toString();
        } catch (JSONException ex) {
            
            return null;
        }
        
    }
   
    
    public void addField(String TAG){
        if(!TAGS.contains(TAG)){
        TAGS.add(TAG);
        fields.add(new AppFileField(TAG));
        }
    }
    public void addGsk(String TAG, String gsk){
        int id = findFieldId(TAG);
        if(id >=0){
            AppFileField f = fields.get(id);
            f.setGsk(gsk);
        }
    }
    public void addCert(String TAG, String cert){
        int id = findFieldId(TAG);
        if(id >=0){
            AppFileField f = fields.get(id);
            f.setCert(cert);
        }
    }
    public void addValue(String TAG, String value){
        int id = findFieldId(TAG);
        if(id >=0){
            AppFileField f = fields.get(id);
            f.setValue(value);
        }
    }
    public void addSig(String TAG, String sig){
        int id = findFieldId(TAG);
        if(id >=0){
            AppFileField f = fields.get(id);
            f.setSig(sig);
        }
    }
    public void addCredential(String TAG, String cr){
        int id = findFieldId(TAG);
        if(id >=0){
            AppFileField f = fields.get(id);
            f.setCredential(cr);
        }
    }
    public int findFieldId(String TAG){
        for(int i = 0; i< TAGS.size(); i++){
            String s = TAGS.get(i);
            if(s.equals(TAG)){
                return i;
            }
        }
        return -1;
    }
    
    public String getValueOfField(String field){
        AppFileField f = fields.get(findFieldId(field));
        if(f!= null){
            return f.getValue();
        }
        else return null;
    }
    public String getCertOfField(String field){
        AppFileField f = fields.get(findFieldId(field));
        if(f!= null){
            return f.getCert();
        }
        else return null;
    }
    public String getCredentialOfField(String field){
        AppFileField f = fields.get(findFieldId(field));
        if(f!= null){
            return f.getCredential();
        }
        else return null;
    }
    public String getGskOfField(String field){
        AppFileField f = fields.get(findFieldId(field));
        if(f!= null){
            return f.getGsk();
        }
        else return null;
    }
    public String getSigOfField(String field){
        AppFileField f = fields.get(findFieldId(field));
        if(f!= null){
            return f.getSig();
        }
        else return null;
    }
   
   
   

    public ArrayList<AppFileField> getFields() {
        return fields;
    }

    public void setFields(ArrayList<AppFileField> fields) {
        this.fields = fields;
    }

    public byte[] getEsk() {
        return DirtyWork.hexStringToByteArray(esk);
    }

    public void setEsk(byte[] esk) {
        this.esk = DirtyWork.bytesToHex(esk);
    }

    public ArrayList<String> getTAGS() {
        return TAGS;
    }

    public void setTAGS(ArrayList<String> TAGS) {
        this.TAGS = TAGS;
    }

    public String getIpk() {
        return ipk;
    }

    public void setIpk(String ipk) {
        this.ipk = ipk;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
    
    public BNCurve getBNCurve(){
        if(BNCurve == null){
            BNCurve = BNCurve.createBNCurveFromName(curve);
            return BNCurve;
        }
        else{
            return BNCurve;
        }
    }
    public Issuer.IssuerPublicKey getIssuerPubicKey(){
        if(ipk != null){
            Issuer.IssuerPublicKey pk = new Issuer.IssuerPublicKey(getBNCurve(),ipk);
            return pk;
        }
        else return null;
        
    }
    public Authenticator getAuthenticator(String field){
        try{
            Issuer.IssuerPublicKey ipk = getIssuerPubicKey();
            BNCurve curve = getBNCurve();
            BigInteger sk = new BigInteger(getGskOfField(field));
            Authenticator a = new Authenticator(curve, ipk, sk);
            Issuer.JoinMessage2 jm2 = new Issuer.JoinMessage2(curve,getCredentialOfField(field) );
            a.EcDaaJoin2(jm2);
            return a;
        }catch(Exception e){
            return null;
        }
    }
    
}
