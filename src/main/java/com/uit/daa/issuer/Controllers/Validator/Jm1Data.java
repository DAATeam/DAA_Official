/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import com.uit.daa.issuer.Controllers.Config;
import com.uit.daa.issuer.Models.Issuer;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import java.util.ArrayList;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nguyenduyy
 */
public class Jm1Data {
    @NotNull
    public String jm1;
    
    
    @NotNull
    public String field; //array , delimit by ","
    
    public Jm1Data(String json , String f){
        
        jm1 = json;
        field = f;
    }
    public Jm1Data(){
        jm1 = null;
        field = null;
    }

    public void setJm1(String jm1) {
        this.jm1 = jm1;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getJm1() {
        return jm1;
    }

    public String getField() {
        return field;
    }
    

    public Issuer.JoinMessage1 getJoinMessage1() {
        BNCurve curve = BNCurve.createBNCurveFromName(Config.curveName);
        try{
        Issuer.JoinMessage1 joinMessage1 = new Issuer.JoinMessage1(curve, jm1);
        return joinMessage1;
        }catch(Exception e){
            return null;
        }
        
    }
    public String[] getFields(){
        return field.split(",");
    }
   
}
