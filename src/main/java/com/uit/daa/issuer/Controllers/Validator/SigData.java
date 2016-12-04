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
    public String sig; //hex string
    
    @NotNull
    public String nonce;
    
    @NotNull
    public String basename;

    public SigData(String sig, String nonce, String basename) {
        this.sig = sig;
        this.nonce = nonce;
        this.basename = basename;
    }

    public SigData() {
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
