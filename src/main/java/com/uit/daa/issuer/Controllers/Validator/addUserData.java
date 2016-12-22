/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;


import com.uit.daa.issuer.Controllers.Config;
import com.uit.daa.issuer.Jdbc.C;
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
    public String user_account;
    
    @NotNull
    public String user_drive_expire;
    
   
    
    
    @NotNull
    public String M;
    
    
    public Member createNewMember(JdbcTemplate j) throws SQLException{
        Member m = new Member(M,BNCurve.createBNCurveFromName(Config.curveName),MemberType.USER_TYPE);
        m.save(j);
        
        return m;
    }
    public User createNewUser(JdbcTemplate j, Member m) throws SQLException{
        User user = new User(m);
        user.setInfo(C.CL_NAME, user_name);
        user.setInfo(C.CL_JOB, user_job);
        user.setInfo(C.CL_USER_ACCOUNT, user_account);
        user.setInfo(C.CL_USER_DRIVE, user_drive_expire);
        
   
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

    public String getUser_account() {
        return user_account;
    }

    public void setUser_account(String user_account) {
        this.user_account = user_account;
    }

    public String getUser_drive_expire() {
        return user_drive_expire;
    }

    public void setUser_drive_expire(String user_drive_expire) {
        this.user_drive_expire = user_drive_expire;
    }
    
    
    
    
}
