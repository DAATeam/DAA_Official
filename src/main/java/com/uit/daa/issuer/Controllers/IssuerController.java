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
import com.uit.daa.issuer.Controllers.Validator.addAppData;
import com.uit.daa.issuer.Controllers.Validator.addServiceData;
import com.uit.daa.issuer.Controllers.Validator.addUserData;
import com.uit.daa.issuer.Controllers.Validator.buildAppData;
import com.uit.daa.issuer.Jdbc.C;
import com.uit.daa.issuer.Jdbc.IssuerJdbcTemplate;
import com.uit.daa.issuer.Models.App;
import com.uit.daa.issuer.Models.Authenticator;
import com.uit.daa.issuer.Models.Issuer;
import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.MemberType;
import com.uit.daa.issuer.Models.Nonce;
import com.uit.daa.issuer.Models.Service;
import com.uit.daa.issuer.Models.User;
import com.uit.daa.issuer.Models.Verifier;
import com.uit.daa.issuer.Models.crypto.AESEncryptor;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import com.uit.daa.issuer.Models.crypto.BitKeySelector;
import com.uit.daa.issuer.Models.crypto.DESEncryptor;
import iaik.security.ec.math.curve.ECPoint;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
import org.springframework.web.servlet.ModelAndView;

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
    * @return : a nonce number if member is valid + issuer public key
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
                json.put(IPK, issuer.pk.toJSON(issuer.getCurve()));
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
         if(result.hasErrors() || jm1.getJoinMessage1() == null){
             json.put(STATUS,ERROR);
             json.put(MESSAGE,"Invalid input");
         }
         else{
             
             Nonce nonce = checkNonce(jm1.getJoinMessage1().nonce);
             boolean valid = nonce == null ? false : true;
             if(valid){
             Issuer.JoinMessage2 jm2 = issuer.EcDaaIssuerJoin(jm1.getJoinMessage1());
             if(jm2 != null){
                 json.put(STATUS,OK);
                 json.put(MESSAGE, "Certificated");
                 json.put(JM2,jm2.toJson(issuer.getCurve()));
     
                 
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
     private void prepareServiceSigData(String[] fields,Member member, Nonce nonce, IssuerJdbcTemplate ijt) throws SQLException, JSONException{
        Service service = Service.getFromMemberID(ijt.jdbcTemplate, member.id);
         JSONObject json = new JSONObject();
         for(int i = 0; i< fields.length; i++){
             json.put(fields[i],service.resultSet.getString(fields[i]));
         }
         String message = json.toString();
         nonce.updateMessage(ijt.jdbcTemplate, message); 
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
                if(nonce.message != null){
                    
                    if(verifyEcDaaSig(issuer, sig.sig, nonce.message, sig.basename)){
                        
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
        auth.EcDaaJoin2(issuer.EcDaaIssuerJoin(auth.EcDaaJoin1(issuer.GetNonce())));
        String basename = CERT_BASENAME;
        Authenticator.EcDaaSignature cert = auth.EcDaaSign(basename, sig);
        return DirtyWork.bytesToHex(cert.encode(curve));
    }
    private boolean verifyEcDaaSig(Issuer issuer, String sig, String message, String basename) throws NoSuchAlgorithmException{
        BNCurve curve = BNCurve.createBNCurveFromName(Config.curveName);
        Verifier ver  = new Verifier(curve);
        Authenticator.EcDaaSignature signature = new Authenticator.EcDaaSignature(
                DirtyWork.hexStringToByteArray(sig), message.getBytes(), curve);
        return ver.verify(signature,basename , issuer.pk, null);
    }
    private boolean linkEcDaaSig(Authenticator.EcDaaSignature sig1,Authenticator.EcDaaSignature sig2, BNCurve curve){
            Verifier ver  = new Verifier(curve);
            return ver.link(sig1, sig2);
    }
    
    @RequestMapping("/test")
    public ModelAndView test() throws SQLException, NoSuchAlgorithmException{
        prepare();
        ModelAndView mav = new ModelAndView("test");
        Map<String, String> map;
        BNCurve curve  = BNCurve.createBNCurveFromName(Config.curveName);
        Member muser = new Member("LovelyGirl",curve,MemberType.USER_TYPE);
        Member mservice = new Member("theCoffeeHouse", BNCurve.createBNCurveFromName(Config.curveName), MemberType.SERVICE_TYPE);
        User user = new User(muser,"Thanh Uyen","Manager");
        Service  service= new Service(mservice,"TheCofeeHouse","user_job");
        Authenticator user_auth = new Authenticator(curve, issuer.pk);
        Authenticator service_auth = new Authenticator(curve, issuer.pk);
        Authenticator issuer_auth = new Authenticator(curve, issuer.pk,issuer.getSk().x);
        user_auth.EcDaaJoin2(issuer.EcDaaIssuerJoin(user_auth.EcDaaJoin1(issuer.GetNonce())));
        service_auth.EcDaaJoin2(issuer.EcDaaIssuerJoin(service_auth.EcDaaJoin1(issuer.GetNonce())));
        issuer_auth.EcDaaJoin2(issuer.EcDaaIssuerJoin(issuer_auth.EcDaaJoin1(issuer.GetNonce())));
        Authenticator.EcDaaSignature permission_sig = service_auth.EcDaaSignWithNym(
                C.CL_PERMISSION, service.service_permission,"sessionId");
        Authenticator.EcDaaSignature session_sig = service_auth.EcDaaSignWithNym(
                C.CL_PERMISSION, "sessionNumber","sessionId");
        String permission_cert = createCertificate(issuer, 
                DirtyWork.bytesToHex(permission_sig.encode(curve)));
        
        Authenticator.EcDaaSignature job_sig = user_auth.EcDaaSign(C.CL_JOB,user.job);
        String job_cert = createCertificate(issuer, 
                DirtyWork.bytesToHex(job_sig.encode(curve)));
        
        Verifier  v = new Verifier(curve);
        boolean valid_ser = true;
        boolean valid_user = true;
        valid_ser &= verifyEcDaaSig(issuer,DirtyWork.bytesToHex(permission_sig.encode(curve)),service.service_permission,C.CL_PERMISSION);
        valid_ser &= verifyEcDaaSig(issuer,permission_cert,DirtyWork.bytesToHex(permission_sig.encode(curve)),CERT_BASENAME);
        valid_user &= verifyEcDaaSig(issuer,DirtyWork.bytesToHex(job_sig.encode(curve)),user.job,C.CL_JOB);
        valid_user &= verifyEcDaaSig(issuer,job_cert,DirtyWork.bytesToHex(job_sig.encode(curve)), CERT_BASENAME);
        valid_ser &= linkEcDaaSig(permission_sig, session_sig, curve);
       
        mav.addObject("verify_service", valid_ser);
        mav.addObject("verify_user",valid_user);
        mav.addObject("user_name", user.name);
        mav.addObject("user_job",user.job);
        mav.addObject("service_name", service.service_name);
        mav.addObject("service_permission",service.service_permission);
        mav.addObject("user_esk",(new BigInteger(user.member.esk)).toString());
        mav.addObject("user_epk", DirtyWork.bytesToHex(user.member.epk));
        mav.addObject("service_esk",(new BigInteger(service.member.esk)).toString());
        mav.addObject("service_epk", DirtyWork.bytesToHex(service.member.epk));
        mav.addObject("user_job_cert",job_cert);
        mav.addObject("service_permission_cert",permission_cert);
        
        //test share key
        ECPoint usharekey = issuer.pk.X.multiplyPoint(new BigInteger(user.member.esk));
        ECPoint user_pk  = curve.getG2().multiplyPoint(new BigInteger(user.member.esk));
        ECPoint isharekey = user_pk.multiplyPoint(issuer.getSk().x);
        mav.addObject("user_share_key",DirtyWork.bytesToHex(usharekey.encodePoint()));
        mav.addObject("issuer_share_key_with_user", DirtyWork.bytesToHex(isharekey.encodePoint()));
        boolean correct = usharekey.getCoordinate().getX().getField().getCardinality()
                .equals(isharekey.getCoordinate().getX().getField().getCardinality());
        
        
        try{
        BigInteger k = isharekey.getCoordinate().getX().getField().getCardinality();
        SecretKeySpec key = BitKeySelector.getDES56Key(k.toByteArray());
        DESEncryptor des = new DESEncryptor(key);
        
        String e = des.encrypt("plaintext");
        String d = des.decrypt(e);
        mav.addObject("ciphertext",e);
        mav.addObject("plaintext",d);
        correct &= d.equals("plaintext");
        }
        catch(Exception e){
            mav.addObject("ciphertext",e.getCause());
            mav.addObject("plaintext",e.getMessage());
        }
        
        mav.addObject("correct_share_key",correct);
        return mav;
        
        
        
    }   
 
    
}
