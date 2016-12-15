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
import com.uit.daa.issuer.Controllers.Validator.addServiceData;
import com.uit.daa.issuer.Controllers.Validator.addUserData;
import com.uit.daa.issuer.Controllers.Validator.buildAppData;
import com.uit.daa.issuer.Controllers.Validator.buildMemberData;
import com.uit.daa.issuer.Controllers.Validator.buildUserData;
import com.uit.daa.issuer.Jdbc.IssuerJdbcTemplate;
import com.uit.daa.issuer.Models.App;
import com.uit.daa.issuer.Models.Issuer;
import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.User;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
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
    @RequestMapping(value="/admin/addUser", method = RequestMethod.POST)
    public void addUser(HttpServletResponse res,
            @ModelAttribute("addUserData") @Valid addUserData data,
            BindingResult result) throws JSONException, SQLException, IOException, NoSuchAlgorithmException, IOException{
        JSONObject json = new JSONObject();
        prepare();
        if(result.hasErrors()){
            json.put(STATUS, ERROR);
            json.put(MESSAGE, "Invalid input");
        }
        else{
            Member m = data.createNewMember(ijt.jdbcTemplate);
            data.createNewUser(ijt.jdbcTemplate,m);
            json.put(STATUS, OK);
            json.put(MESSAGE,"successful" );
        }
        res.getWriter().println(json.toString());
    }
    @RequestMapping(value="//admin/addService", method = RequestMethod.POST)
    public void addService(HttpServletResponse res,
            @ModelAttribute("addServiceData") @Valid addServiceData data,
            BindingResult result) throws JSONException, SQLException, IOException, NoSuchAlgorithmException{
        JSONObject json = new JSONObject();
        prepare();
        if(result.hasErrors()){
            json.put(STATUS, ERROR);
            json.put(MESSAGE, "Invalid input");
        }
        else{
            data.createNewMember(ijt.jdbcTemplate);
            data.createNewService(ijt.jdbcTemplate);
            json.put(STATUS, OK);
            json.put(MESSAGE,"successful" );
        }
        res.getWriter().println(json.toString());
    }
    @RequestMapping(value="/admin/addApp", method = RequestMethod.POST)
    public void addApp(HttpServletResponse res,
            @ModelAttribute("addAppData") @Valid addAppData data,
            BindingResult result) throws JSONException, SQLException, IOException, NoSuchAlgorithmException{
        JSONObject json = new JSONObject();
        prepare();
        if(result.hasErrors()){
            json.put(STATUS, ERROR);
            json.put(MESSAGE, "Invalid input");
        }
        else{
            
            App app = data.createNewApp(ijt.jdbcTemplate);
            if(app != null){
            json.put(STATUS, OK);
            json.put(MESSAGE,"successful" );
            }
            else{
                 json.put(STATUS, ERROR);
            json.put(MESSAGE, "Wrong identity");
            }
        }
        res.getWriter().println(json.toString());
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
                res.sendRedirect("/welcome");
            }
            
            
            
        }
    }
    
    
}
