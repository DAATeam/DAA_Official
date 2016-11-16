/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import com.uit.daa.issuer.Models.App;
import com.uit.daa.issuer.Models.Member;
import java.sql.SQLException;
import javax.validation.constraints.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class addAppData {
    @NotNull
    String device_id;
    
    @NotNull
    public String member_id;
    
    @NotNull
    public String M;
    
    public App createNewApp(JdbcTemplate j) throws SQLException{
        Member m = Member.getFromID(j, Integer.valueOf(member_id));
        if(m!= null &&m.M.equals(M)){
            App a = new App(m,device_id);
            a.save(j);
            return a;
        }
        else{
            return null;
        }
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getM() {
        return M;
    }

    public void setM(String M) {
        this.M = M;
    }
    
    
}
