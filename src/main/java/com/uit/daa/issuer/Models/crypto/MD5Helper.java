/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Models.crypto;

import com.uit.daa.issuer.Controllers.DirtyWork;
import java.io.Console;
import java.security.MessageDigest;


/**
 *
 * @author nguyenduyy
 */
public class MD5Helper {
    private static final String TAG = "MD5 Helper : ";

    /**
     *
     * @param input
     * @return hexString
     */
    public static String hashString(String input){
        if(input == null){
            return null;
        }
        try{
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] h = digest.digest(input.getBytes());
        return DirtyWork.bytesToHex(h);
        }catch(Exception e){
            System.out.println(TAG + e.getMessage());
            return null;
        }
        
    }

    /**
     *
     * @param input
     * @return byte[]
     */
    public static byte[] hashStringToByte(String input){
        if(input == null){
            return null;
        }
        try{
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] h = digest.digest(input.getBytes());
        return h;
        }catch(Exception e){
            System.out.println(TAG + e.getMessage());
            return null;
        }
    }
    public static byte[] hashBytesToByte(byte[] input){
        if(input == null){
            return null;
        }
        try{
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] h = digest.digest(input);
        return h;
        }catch(Exception e){
            System.out.println(TAG + e.getMessage());
            return null;
        }
    }
    
}
