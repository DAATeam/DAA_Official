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
    
    public Jm1Data(String json){
        
        jm1 = json;
    }

    public Issuer.JoinMessage1 getJm1() {
        BNCurve curve = BNCurve.createBNCurveFromName(Config.curveName);
        return new Issuer.JoinMessage1(curve, jm1);
    }
    public String[] getFields(){
        return field.split(",");
    }
   
}
