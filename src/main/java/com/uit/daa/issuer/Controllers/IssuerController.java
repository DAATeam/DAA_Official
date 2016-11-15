/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers;

import com.sun.javafx.collections.MappingChange.Map;
import com.uit.daa.issuer.Controllers.Validator.Jm1Data;
import com.uit.daa.issuer.Controllers.Validator.MemberData;
import com.uit.daa.issuer.Controllers.Validator.SigData;
import com.uit.daa.issuer.Jdbc.C;
import com.uit.daa.issuer.Jdbc.IssuerJdbcTemplate;
import com.uit.daa.issuer.Models.Authenticator;
import com.uit.daa.issuer.Models.Issuer;
import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.MemberType;
import com.uit.daa.issuer.Models.Nonce;
import com.uit.daa.issuer.Models.User;
import com.uit.daa.issuer.Models.Verifier;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import java.io.IOException;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author nguyenduyy
 */
@MultipartConfig
@Controller
public class IssuerController {
    public static final String STATUS = "status";
    public static final String MESSAGE = "msg";
    public static final String DATA = "data";
    public static final String OK = "ok";
    public static final String ERROR = "error";
    public static final String CURVE = "curveName";
    public static final String IPK = "ipk";
    public static final String JM2 = "jm2";
    public static final String CERT = "certificate";
    
    public static final String CERT_BASENAME = "attestation";
    @Autowired
    ApplicationContext context;
    Issuer issuer = null;
    IssuerJdbcTemplate ijt = null;
    SecureRandom random;
    public IssuerController() throws SQLException, NoSuchAlgorithmException{
               
        random = new SecureRandom();
        
    }
    /**Welcome
     * 
     */
    @RequestMapping("/welcome")
    public void Welcome(HttpServletResponse res) throws IOException{
        res.getWriter().println("welcome");
    }
    /** 
    * Client authenticate with :
    * @appId : appId 
    * @M : identity for a member , work like a password 
    * @return : a nonce number if member is valid
    */
    private void prepare() throws SQLException, NoSuchAlgorithmException{
        if(ijt == null){
            ijt = (IssuerJdbcTemplate) context.getBean("issuerJDBCTemplate");
        }
        if(issuer == null){
            issuer = ijt.getIssuer();
        }
    }
     @RequestMapping(value = "/join",method = RequestMethod.POST)
     public void Join(HttpServletRequest request, HttpServletResponse response,
             @ModelAttribute("MemberData") @Valid MemberData jva,
             BindingResult result) throws JSONException, SQLException, IOException, NoSuchAlgorithmException{
         prepare();
         JSONObject json = new JSONObject();
         if(result.hasErrors()){
             json.put(STATUS,ERROR);
             json.put(MESSAGE,"Invalid input");
         }
         else{
            if(validateMember(jva)){
                json.put(STATUS, OK);
                json.put(MESSAGE,"Authenticated");
                Nonce nonce = ijt.createNonce(jva.getAppId(), issuer, random);
                json.put( C.CL_NONCE,nonce.getNonce().toString());
                json.put(CURVE, Config.curveName);
            }
            else{
                json.put(STATUS,ERROR);
                json.put(MESSAGE, "Invalid Member or AppId");
            }
         }
         String res = json.toString();
         response.getWriter().println(res);
        
     }
     private boolean validateMember(MemberData md) throws SQLException{
         
         Member m = ijt.getMember(md.getAppId(), md.getM());
         if(m != null){
             return true;
         }
         else return false;
     }
     /**Client submit JoinMessage 1 
      * @jm1 : JoinMessage1.toJSON()
      * @return : JoinMessage2.toJSON() and Issuer Public Key
      */
     @RequestMapping(value="/jm1",method = RequestMethod.POST)
     public void JM1(HttpServletRequest request, HttpServletResponse response,
             @ModelAttribute("Jm1Data") @Valid Jm1Data jm1, BindingResult result) throws SQLException, NoSuchAlgorithmException, JSONException, IOException{
         prepare();
         JSONObject json = new JSONObject();
         if(result.hasErrors()){
             json.put(STATUS,ERROR);
             json.put(MESSAGE,"Invalid input");
         }
         else{
             
             Nonce nonce = checkNonce(jm1.getJm1().nonce);
             boolean valid = nonce == null ? false : true;
             if(valid){
             Issuer.JoinMessage2 jm2 = issuer.EcDaaIssuerJoin(jm1.getJm1());
             if(jm2 != null){
                 json.put(STATUS,OK);
                 json.put(MESSAGE, "Certificated");
                 json.put(JM2,jm2.toJson(issuer.getCurve()));
                 json.put(IPK, issuer.pk.toJSON(issuer.getCurve()));
                 
                 extractMemberDataByField(jm1.getFields(),nonce,ijt);
             }
             else{
                 json.put(STATUS, ERROR);
                 json.put(MESSAGE,"Invalid JoinMessage1");
             }
             }
             else{
                 json.put(STATUS,ERROR);
                 json.put(MESSAGE,"Invalid nonce");
             }
             
             
         }
         String res = json.toString();
         response.getWriter().println(res);
     }
     private Nonce checkNonce(BigInteger n) throws SQLException{
         Nonce nonce = ijt.getNonce(n);
         if(nonce != null){
             nonce.markUsed(ijt.jdbcTemplate);
             
             return nonce;
         }
         else return null;
     }
     /**
      * 
      * @param f : fields
      * @param nonce : nonce relate to member
      * @param ijt : database
      * @throws SQLException 
      *Every fields must start with same prefix user/service/etc ... or nonce will be mark as suspicious
      * 
      */
     private void extractMemberDataByField(String[] f,Nonce nonce, IssuerJdbcTemplate ijt) throws SQLException, JSONException{
         JSONObject json = new JSONObject();
         String prefix  = null;
         Member member = ijt.getMemberDataByNonce(nonce.getNonce());
         MemberType memberType = MemberType.getFromID(member.type, ijt.jdbcTemplate);
         prefix = memberType.prefix;
        //waring if thera are any illegal field
         for(int i =0; i< f.length; i++){
             if(!f[i].split("_")[0].equals(prefix)){
                 nonce.markSuspicious(ijt.jdbcTemplate);
                 return;
             }
         }
         switch(member.type){
             case MemberType.USER_TYPE:
                 prepareUserSigData(f ,member, nonce, ijt);
                 break;
             case MemberType.SERVICE_TYPE:
                 prepareServiceSigData(f,member, nonce, ijt);
                 break;
         }
        
     }
     private void prepareUserSigData(String[] fields,Member member, Nonce nonce, IssuerJdbcTemplate ijt) throws SQLException, JSONException{
         User user = User.getFromMemberID(ijt.jdbcTemplate, member.id);
         JSONObject json = new JSONObject();
         for(int i = 0; i< fields.length; i++){
             json.put(fields[i],user.resultSet.getString(fields[i]));
         }
         String message = json.toString();
         nonce.updateMessage(ijt.jdbcTemplate, message);
     }
     private void prepareServiceSigData(String[] fields,Member member, Nonce nonce, IssuerJdbcTemplate ijt){
         
     }
     
     /** CLient submit signatures and infos to get certificates
      * @m : client info such as name, job, service name ... in json 
      *@sig : client signature on m
      * 
     */
    @RequestMapping(value="/cert",method = RequestMethod.POST)
    public void Cert(HttpServletResponse response, 
            @ModelAttribute("SigData") @Valid SigData sig, 
            BindingResult result) throws SQLException, NoSuchAlgorithmException, JSONException, IOException{
        prepare();
        JSONObject json = new JSONObject();
        
        if(result.hasErrors()){
            json.put(STATUS,ERROR);
            json.put(MESSAGE,"Invalid input");
        }
        else{
            Nonce nonce = ijt.getNonce(new BigInteger(sig.nonce));
            if(nonce.getUsed() == Nonce.SUSPICIOUS){
                json.put(STATUS,ERROR);
                json.put(MESSAGE, "Suspicious, join again");
                
            }
            else{
                if(sig.m.equals(nonce.message)){
                    
                    if(verifyEcDaaSig(issuer, sig.sig, sig.m)){
                        
                    String cert = createCertificate(issuer,sig.sig);
                    json.put(STATUS, OK);
                    json.put(MESSAGE,"Certificate");
                    json.put(CERT,cert);
                    }
                    else{
                        json.put(STATUS, ERROR);
                    json.put(MESSAGE,"Invalid Signature");
                    }
                    
                }
                else{
                    json.put(STATUS,ERROR);
                    json.put(MESSAGE,"Incorrect identity");
                }
            }
        }
        String res = json.toString();
        response.getWriter().println(res);
    }
    private String createCertificate(Issuer issuer, String sig) throws NoSuchAlgorithmException{
        BigInteger sk = issuer.getSk().x;
        BNCurve curve = BNCurve.createBNCurveFromName(Config.curveName);
        Authenticator auth = new Authenticator(curve,issuer.pk,sk);
        
        String basename = "certificate";
        Authenticator.EcDaaSignature cert = auth.EcDaaSign(basename, sig);
        return DirtyWork.bytesToHex(cert.encode(curve));
    }
    private boolean verifyEcDaaSig(Issuer issuer, String sig, String message) throws NoSuchAlgorithmException{
        BNCurve curve = BNCurve.createBNCurveFromName(Config.curveName);
        Verifier ver  = new Verifier(curve);
        Authenticator.EcDaaSignature signature = new Authenticator.EcDaaSignature(
                DirtyWork.hexStringToByteArray(sig), message.getBytes(), curve);
        return ver.verify(signature,CERT_BASENAME , issuer.pk, null);
    }
}
