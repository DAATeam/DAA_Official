/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nguyenduyy
 */
public class MemberData {
    @NotNull
    @Min(0)
    private Integer appId;
    
    @NotNull
    private String M;
    
    public MemberData(Integer a, String m){
        appId = a;
        M = m;
    }
    public MemberData(){
        appId = null;
        M = null;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public String getM() {
        return M;
    }

    public void setM(String M) {
        this.M = M;
    }
    
    
    
}
