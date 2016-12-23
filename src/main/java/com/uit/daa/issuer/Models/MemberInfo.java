/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uit.daa.issuer.Models;


import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/**
 *
 * @author nguyenduyy
 */
public class MemberInfo {
    Map<String,String> infoMap;
    Member member;
    Integer id;
    Level level;
    public MemberInfo(){
      infoMap = new HashMap<String, String>();
    }
    public MemberInfo(Member member){
          infoMap = new HashMap<String, String>();
          this.member = member;
    }
    public void setInfo(String field, String value){
        if(infoMap.containsKey(field)){
            infoMap.replace(field, value);
        }
        else infoMap.put(field, value);
    }
    public String getInfo(String field){
        if(infoMap.containsKey(field)){
            return infoMap.get(field);
        }
        else return null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    

    public Map<String, String> getInfoMap() {
        return infoMap;
    }

    public void setInfoMap(Map<String, String> infoMap) {
        this.infoMap = infoMap;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }
    /**
     * 
     * @param fields : ex : user_name,user_job,user_permisison
     * @return 
     */
    public JSONObject getJSONFromASetOfField(String fields){
        String[] f = fields.split(",");
        JSONObject json = new JSONObject();
        for(int i=0; i< f.length; i++){
            try{
                json.put(f[i], infoMap.get(f[i]));
            }catch(Exception e){
                return new JSONObject();
            }
        }
        return json;
    }
    
    
}
