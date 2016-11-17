/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Models.crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author nguyenduyy
 */
public class BitKeySelector {
    /**
     * AES128 required 128 bits key
     * @param m : byte[] converted to key
     * @return SecretKeySpec
     */
    public static SecretKeySpec getAES128Key(byte[] m) throws NoSuchAlgorithmException{
        
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] key = sha.digest(m);
        key = Arrays.copyOf(key, 16); // use only first 128 bit

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
        
                
    }
    /**
     * DES requires 56 bits key
     * @param m : byte[] converted to key
     * @return SecretKeySpec
     * @throws NoSuchAlgorithmException 
     */
    public static SecretKeySpec getDES56Key(byte[] m) throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] key = sha.digest(m);
        key = Arrays.copyOf(key, 8); // use only first 56 bit

        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "DES");
        return secretKeySpec;
    }
}
