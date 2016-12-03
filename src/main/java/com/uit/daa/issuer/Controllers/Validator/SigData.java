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
    public String sig; //hex string
    
    @NotNull
    public String nonce;
    
    @NotNull
    public String basename;
    
    public Authenticator.EcDaaSignature getEcDaaSignature(){
        BNCurve curve = BNCurve.createBNCurveFromName(Config.curveName);
        byte[] encoded = DirtyWork.hexStringToByteArray(sig);
        byte[] krd = DirtyWork.hexStringToByteArray(m);
        return new Authenticator.EcDaaSignature(encoded, krd, curve);
    }

    public SigData(String m, String sig, String nonce, String basename) {
        this.m = m;
        this.sig = sig;
        this.nonce = nonce;
        this.basename = basename;
    }

    public SigData() {
    }

    public String getM() {
        return m;
    }

    public void setM(String m) {
        this.m = m;
    }

    public String getSig() {
        return sig;
    }

    public void setSig(String sig) {
        this.sig = sig;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getBasename() {
        return basename;
    }

    public void setBasename(String basename) {
        this.basename = basename;
    }
}
