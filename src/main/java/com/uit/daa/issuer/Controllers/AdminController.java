/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers;

import static com.uit.daa.issuer.Controllers.IssuerController.ERROR;
import static com.uit.daa.issuer.Controllers.IssuerController.MESSAGE;
import static com.uit.daa.issuer.Controllers.IssuerController.OK;
import static com.uit.daa.issuer.Controllers.IssuerController.STATUS;
import com.uit.daa.issuer.Controllers.Validator.addAppData;
import com.uit.daa.issuer.Controllers.Validator.addMTypeData;
import com.uit.daa.issuer.Controllers.Validator.addServiceData;
import com.uit.daa.issuer.Controllers.Validator.addUserData;
import com.uit.daa.issuer.Controllers.Validator.buildAppData;
import com.uit.daa.issuer.Controllers.Validator.buildMemberData;
import com.uit.daa.issuer.Jdbc.C;

import com.uit.daa.issuer.Jdbc.IssuerJdbcTemplate;
import com.uit.daa.issuer.Jdbc.ManipulateQueryHelper;
import com.uit.daa.issuer.Models.App;
import com.uit.daa.issuer.Models.Issuer;
import com.uit.daa.issuer.Models.Level;
import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.MemberType;
import com.uit.daa.issuer.Models.Service;
import com.uit.daa.issuer.Models.User;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.json.JSONException;
import org.json.JSONObject;
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
@Controller
public class AdminController {
    @Autowired
    ApplicationContext context;
    Issuer issuer = null;
    IssuerJdbcTemplate ijt = null;
    SecureRandom random;
    private void prepare() throws SQLException, NoSuchAlgorithmException{
        if(ijt == null){
            ijt = (IssuerJdbcTemplate) context.getBean("issuerJDBCTemplate");
        }
        if(issuer == null){
            issuer = ijt.getIssuer();
        }
    }
    @RequestMapping(value="/admin/addUser", method = RequestMethod.GET)
    public ModelAndView getAddUserView(){
        ModelAndView mav  = new ModelAndView("addUser");
        return mav;
    }
    @RequestMapping(value="/admin/addUser", method = RequestMethod.POST)
    public ModelAndView addUser(HttpServletResponse res,
            @ModelAttribute("addUserData") @Valid addUserData data,
            BindingResult result) throws JSONException, SQLException, IOException, NoSuchAlgorithmException, IOException{
        JSONObject json = new JSONObject();
        prepare();
        ModelAndView mav = new ModelAndView("registerSuccessful");
        if(result.hasErrors()){
            mav.addObject(STATUS, ERROR);
            mav.addObject(MESSAGE, "Thông tin thiếu hoặc không hợp lệ");
            
        }
        else{
            Member m = data.createNewMember(ijt.jdbcTemplate);
            data.createNewUser(ijt.jdbcTemplate,m);
            mav.addObject(STATUS, OK);
            mav.addObject(MESSAGE,"" );
            mav.addObject("memberId", m.id);
            
        }
        return mav;
    }
    @RequestMapping(value="/admin/addService", method = RequestMethod.GET)
    public ModelAndView getAddServiceView(){
        return new ModelAndView("addService");
    }
    @RequestMapping(value="/admin/addService", method = RequestMethod.POST)
    public ModelAndView addService(HttpServletResponse res,
            @ModelAttribute("addServiceData") @Valid addServiceData data,
            BindingResult result) throws JSONException, SQLException, IOException, NoSuchAlgorithmException{
        JSONObject json = new JSONObject();
        prepare();
        ModelAndView mav =  new ModelAndView("registerSuccessful");
        if(result.hasErrors()){
            mav.addObject(STATUS, ERROR);
            mav.addObject(MESSAGE, "Thông tin không hợp lệ");
        }
        else{
            Member m = data.createNewMember(ijt.jdbcTemplate);
            data.createNewService(ijt.jdbcTemplate);
            mav.addObject(STATUS, OK);
            mav.addObject(MESSAGE,"" );
            mav.addObject("memberId",m.id );
        }
        return mav;
    }
    @RequestMapping(value="/admin/addApp", method = RequestMethod.GET)
    public ModelAndView getAddAppView(){
        return new ModelAndView("addApp");
    }
    @RequestMapping(value="/admin/addApp", method = RequestMethod.POST)
    public ModelAndView addApp(HttpServletResponse res,
            @ModelAttribute("addAppData") @Valid addAppData data,
            BindingResult result) throws JSONException, SQLException, IOException, NoSuchAlgorithmException{
        JSONObject json = new JSONObject();
        prepare();
        ModelAndView mav = new ModelAndView("registerSuccessful");
        if(result.hasErrors()){
            mav.addObject(STATUS, ERROR);
            mav.addObject(MESSAGE, "Invalid input");
        }
        else{
            
            App app = data.createNewApp(ijt.jdbcTemplate);
            if(app != null){
            mav.addObject(STATUS, OK);
            mav.addObject(MESSAGE,"" );
            mav.addObject("memberId",app.appID);
            }
            else{
                 mav.addObject(STATUS, ERROR);
            mav.addObject(MESSAGE, "Wrong identity");
            }
        }
        return mav;
    }
    @RequestMapping("/app")
    public void data (HttpServletResponse res,
            @ModelAttribute("buildAppData") @Valid buildAppData data,
            BindingResult result) throws JSONException, SQLException, IOException, NoSuchAlgorithmException{
        JSONObject json = new JSONObject();
        prepare();
        if(result.hasErrors()){
            json.put(STATUS, ERROR);
            json.put(MESSAGE, "Invalid input");
            res.getWriter().println(json.toString());
        }
        else{
            String s = "";
            byte[] r;
            try{
             r = data.buildEncodedJSON(ijt.jdbcTemplate, issuer);
             OutputStream out = res.getOutputStream();
            ByteArrayInputStream bais = new ByteArrayInputStream(r);
            int length;
            byte[] buffer = new byte[4096];
            while ((length = bais.read(buffer)) > 0){
                out.write(buffer, 0, length);
            }
            bais.close();
            out.flush();
            }catch(Exception e){
                Logger.getLogger(buildAppData.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
                res.sendRedirect("/welcome");
            }
            
            
            
        }
    }
    @RequestMapping("/admin/")
    public ModelAndView getAdminView(){
        return new ModelAndView("admin/dashboard");
    }
    @RequestMapping("/admin/member")
    public ModelAndView getListMemberView() throws SQLException, NoSuchAlgorithmException{
        ModelAndView mav = new ModelAndView("admin/member");
        prepare();
        ArrayList<User> au = ijt.getALlUserMember();
        if(au != null){
            mav.addObject("allUser", au);
        }
        ArrayList<Service> al = ijt.getALlServiceMember();
        if(al != null){
            mav.addObject("allService", al);
        }
        return mav;
        
    }
    @RequestMapping("/admin/level")
    public ModelAndView getListLevelView() throws SQLException, NoSuchAlgorithmException{
        ArrayList<Level> al = new ArrayList<Level>();
        ArrayList<MemberType> am = new ArrayList<>();
        prepare();
        al = ijt.getAllLevel();
        am = ijt.getAllMemberType();
        ModelAndView mav = new ModelAndView("admin/level");
        mav.addObject("allLevel", al);
        mav.addObject("allType",am);
        return mav;
    }
    @RequestMapping(value = "/admin/addType", method = RequestMethod.POST)
    public void getAddTypeView(HttpServletResponse res,
            @ModelAttribute("addMTypeData") @Valid addMTypeData data,
            BindingResult result) throws JSONException, SQLException, IOException, NoSuchAlgorithmException{
         JSONObject json = new JSONObject();
         prepare();
        if(result.hasErrors()){
            json.put(STATUS, ERROR);
            json.put(MESSAGE,"Invalid input");
        }
        else{
            MemberType mtype = new MemberType(data.getPrefix());
            mtype.save(ijt.jdbcTemplate);
            json.put(STATUS , OK);
            json.put(MESSAGE,"New MType : "+mtype.getPrefix() );
            json.put("id",mtype.getId());
            json.put("prefix",mtype.prefix);
            
        }
        res.getWriter().println(json.toString());
    }
    
    
    
    
}
