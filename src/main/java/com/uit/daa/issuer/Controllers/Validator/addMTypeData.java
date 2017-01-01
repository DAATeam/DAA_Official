/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import javax.validation.constraints.NotNull;

/**
 *
 * @author nguyenduyy
 */
public class addMTypeData {
    @NotNull
    String prefix;
    //FIXME : add token of issuer
    public addMTypeData(){
        
    }
    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
    
}
