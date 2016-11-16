/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import com.uit.daa.issuer.Controllers.Config;
import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.MemberType;
import com.uit.daa.issuer.Models.Service;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import java.sql.SQLException;
import javax.validation.constraints.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class addServiceData {
    @NotNull
    public String service_name;
    
    @NotNull
    public String service_permission;
    
    @NotNull
    public String M;
    
    public Member member;
    
    public Member createNewMember(JdbcTemplate j) throws SQLException{
        Member m = new Member(M, BNCurve.createBNCurveFromName(Config.curveName),MemberType.SERVICE_TYPE);
        if(m!=null){
        m.save(j);
        member = m;
        return m;
        }
        else return null;
    }
    public Service createNewService(JdbcTemplate j) throws SQLException{
        if(member != null){
        Service service = new Service(member,service_name, service_permission);
        service.save(j);
        return service;
        }
        else return null;
        
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

    public String getService_permission() {
        return service_permission;
    }

    public void setService_permission(String service_permission) {
        this.service_permission = service_permission;
    }

    public String getM() {
        return M;
    }

    public void setM(String M) {
        this.M = M;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }
    
}
