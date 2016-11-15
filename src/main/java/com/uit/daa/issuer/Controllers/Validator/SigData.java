/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Controllers.Validator;

import com.uit.daa.issuer.Controllers.Config;
import com.uit.daa.issuer.Controllers.DirtyWork;
import com.uit.daa.issuer.Models.Authenticator;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import javax.validation.constraints.NotNull;

/**
 *
 * @author nguyenduyy
 */

public class SigData {
    @NotNull
    public String m;
    
    @NotNull
    public String sig ; //hex string
    
    @NotNull
    public String nonce;
    
    public Authenticator.EcDaaSignature getEcDaaSignature(){
        BNCurve curve = BNCurve.createBNCurveFromName(Config.curveName);
        byte[] encoded = DirtyWork.hexStringToByteArray(sig);
        byte[] krd = DirtyWork.hexStringToByteArray(m);
        return new Authenticator.EcDaaSignature(encoded, krd, curve);
    }
    
}
