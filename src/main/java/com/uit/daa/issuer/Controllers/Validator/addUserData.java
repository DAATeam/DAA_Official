/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;


import com.uit.daa.issuer.Controllers.Config;
import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.MemberType;
import com.uit.daa.issuer.Models.User;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import java.sql.SQLException;
import javax.validation.constraints.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class addUserData {
    @NotNull
    public String user_name;
    
    @NotNull
    public String user_job;
    
    @NotNull
    public String M;
    
    
    public Member createNewMember(JdbcTemplate j) throws SQLException{
        Member m = new Member(M,BNCurve.createBNCurveFromName(Config.curveName),MemberType.USER_TYPE);
        m.save(j);
        
        return m;
    }
    public User createNewUser(JdbcTemplate j, Member m) throws SQLException{
        User user = new User(m,user_name, user_job);
        user.save(j);
        return user;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_job() {
        return user_job;
    }

    public void setUser_job(String user_job) {
        this.user_job = user_job;
    }

    public String getM() {
        return M;
    }

    public void setM(String M) {
        this.M = M;
    }
    
    
    
}
