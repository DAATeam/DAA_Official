package com.uit.daa.issuer.Jdbc;
public class Column {
    public enum DataTypeEnum {INT, BIGINT, TEXT, BLOB, TIMESTAMP};
    public String field;
    public DataTypeEnum dataType;
    public String tag;
    public String foreign_key = null;
    public boolean isPrimary = false;
    public boolean isNotNull = false;
    public boolean isAutoIncrement = false;
    public boolean isAutoTimeStamp = false;
    
    public Column(){
        
    }
    public Column(String f, DataTypeEnum d, String t){
        this.field = f;
        this.dataType = d;
        this.tag = t;
    }
    public Column(String f, DataTypeEnum d){
        this.field = f;
        this.dataType = d;
        this.tag = "undefined";
    }
    public Column setToPrimaryKey(){
        this.isPrimary = true;
        return this;
    }
    public Column setToAutoIncrement(){
        if(this.dataType == DataTypeEnum.INT || this.dataType == DataTypeEnum.BIGINT){
        this.isAutoIncrement = true;
        }
        return this;
    }
    public Column setToNotNull(){
        this.isNotNull = true;
        return this;
    }
    public Column setToAutoTimeStamp(){
        if(this.dataType == DataTypeEnum.TIMESTAMP){
        this.isAutoTimeStamp = true;
        }
        return this;
    }
    public Column setToDefaultIdField(){
        field = "_id";
        dataType = DataTypeEnum.BIGINT;
        tag= "auto id";
        isPrimary = true;
        isAutoIncrement = true;
        isNotNull  = true;
        return this;
        
    }
    public Column setToDefaultTimeStampWithName(String name){
        field = name;
        dataType = DataTypeEnum.TIMESTAMP;
        tag= "auto timestamp";
        isAutoTimeStamp = true;
        isNotNull  = true;
        return this;
    }
    public void setForiegnKey(String tableName, String column){
        this.foreign_key = tableName + "("+column+")";
    }
    public String getSqlString(){
        String sql = null;
        sql = field + " ";
        String f = "";
        switch (dataType){
            case BLOB : f = "blob";break;
            case INT : f = "int"; break;
            case BIGINT : f= "bigint"; break;
            case TEXT : f = "text"; break;
            case TIMESTAMP : f = "timestamp"; break;
            default: f = "text";
        }
        sql += f;
        if(this.isNotNull) sql += " not null";
        if(this.isAutoIncrement) sql += " auto_increment";
        if(this.isAutoTimeStamp) sql += " default current_timestamp on update current_timestamp";
        
        
        
        return sql;
    }
}