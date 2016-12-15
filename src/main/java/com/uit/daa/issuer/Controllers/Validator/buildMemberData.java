/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import com.uit.daa.issuer.Models.Member;
import com.uit.daa.issuer.Models.MemberType;
import com.uit.daa.issuer.Models.Service;
import com.uit.daa.issuer.Models.User;
import java.sql.SQLException;
import javax.validation.constraints.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class buildMemberData {
    @NotNull
    public String memberId;
    //FIXME : something identify admin machine
    public buildMemberData(){
        
    }
    public Object getMember(JdbcTemplate j) throws SQLException{
        Member m= Member.getFromID(j,Integer.parseInt(memberId));
        switch(m.type){
            case MemberType.USER_TYPE: 
                return User.getFromMemberID(j, Integer.parseInt(memberId));
                
            case MemberType.SERVICE_TYPE:
                return Service.getFromMemberID(j, Integer.parseInt(memberId));
            default : return null;
        }
        
    }
    

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }
    
}
