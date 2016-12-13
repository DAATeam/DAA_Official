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
        //init issuer 
        Authenticator iauth = new Authenticator(curve,issuer.pk,issuer.getSk().x);
        BigInteger inonce = issuer.getCurve().getRandomModOrder(random);
        Issuer.JoinMessage1 ijm1 = iauth.EcDaaJoin1(inonce);
        Issuer.JoinMessage2 ijm2 = issuer.EcDaaIssuerJoin(ijm1,false);
        iauth.EcDaaJoin2(ijm2);
        
        ArrayList<AppFileField> aaff = new ArrayList<>();
        AppFileField aff_name = new AppFileField("user_name");
        AppFileField aff_job = new AppFileField("user_job");
        
        BigInteger sk = issuer.getCurve().getRandomModOrder(random);
        BigInteger nonce = issuer.getCurve().getRandomModOrder(random);
        //join 1 : user_name
        JSONObject json = new JSONObject();
        try {
            json.put("user_name",u.name);
            aff_name.setValue(json.toString());
        } catch (JSONException ex) {
            Logger.getLogger(buildUserData.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        Authenticator auth = new Authenticator(curve,issuer.pk,sk);
        
        Issuer.JoinMessage1 jm1 = auth.EcDaaJoin1(nonce);
        Issuer.JoinMessage2 jm2 = issuer.EcDaaIssuerJoin(jm1,false);
        auth.EcDaaJoin2(jm2);
        
        Authenticator.EcDaaSignature sig = auth.EcDaaSign("user_name",json.toString() );
        Authenticator.EcDaaSignature cert = iauth.EcDaaSign(IssuerController.CERT_BASENAME,DirtyWork.bytesToHex(sig.encode(curve)));
        aff_name.setCert(DirtyWork.bytesToHex(cert.encode(curve)));
        aff_name.setCredential(jm2.toJson(curve));
        aff_name.setGsk(sk.toString());
        aff_name.setSig(DirtyWork.bytesToHex(sig.encode(curve)));
        //join 2 : user_job
         json = new JSONObject();
        try {
            json.put("user_job",u.job);
            aff_job.setValue(json.toString());
        } catch (JSONException ex) {
            Logger.getLogger(buildUserData.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
         auth = new Authenticator(curve,issuer.pk,sk);
        
        jm1 = auth.EcDaaJoin1(nonce);
         jm2 = issuer.EcDaaIssuerJoin(jm1,false);
         auth.EcDaaJoin2(jm2);
        sig = auth.EcDaaSign("user_job",json.toString() );
        cert = iauth.EcDaaSign(IssuerController.CERT_BASENAME,DirtyWork.bytesToHex(sig.encode(curve)));
        aff_job.setCert(DirtyWork.bytesToHex(cert.encode(curve)));
        aff_job.setCredential(jm2.toJson(curve));
        
        aff_job.setGsk(sk.toString());
        aff_job.setSig(DirtyWork.bytesToHex(sig.encode(curve)));
        
        aaff.add(aff_name);
        aaff.add(aff_job);
        return aaff;
                
    }
    
}
