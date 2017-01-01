/**	
	Copyright 2016 IBM Corp.
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
	http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
	**/
package com.uit.daa.issuer.Models;

import com.uit.daa.issuer.Controllers.Config;
import com.uit.daa.issuer.Jdbc.C;
import com.uit.daa.issuer.Jdbc.ManipulateQueryHelper;
import com.uit.daa.issuer.Models.crypto.BNCurve;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author nguyenduyy
 */
public class Service extends MemberInfo {
    public final static String[] fieldSet = { C.CL_SERNAME, C.CL_SERVICE_ACCOUNT};
    public ResultSet resultSet;
    
    
    public Service(Member m){
        this.member = m;
        initData();
    }
    public Service(){
        initData();
        this.member = new Member();
    }
    private void initData(){
        for(int i=0; i< fieldSet.length; i++){
            infoMap.put(fieldSet[i], "");
        }
    }
    
    public void save(JdbcTemplate j) throws SQLException{
        ManipulateQueryHelper qh = new ManipulateQueryHelper();
        qh.addTableName(C.TB_SERVICE);
        for(int i=0; i<fieldSet.length; i++){
            qh.addColumnName(fieldSet[i]);
        }
        qh.addColumnName(C.CL_M_ID);
        String sql = qh.getInsertSQL();
        if(sql != null){
        PreparedStatement pp = (PreparedStatement) j.getDataSource().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        int i = 0;
        for(i=0; i< fieldSet.length; i++){
            pp.setString(i+1,infoMap.get(fieldSet[i]) );
        }
        
        pp.setInt(i+1, member.id);
        pp.executeUpdate();
        ResultSet gk = pp.getGeneratedKeys();
            if(gk.next()){
                this.id = gk.getInt(1);
            }
        pp.close();
        }
    }
    public static Service getFromID(JdbcTemplate j, Integer id) throws SQLException{
         String sql = "select * from "+C.TB_SERVICE + " inner join " + C.TB_MEMBER 
                    + " on " +C.TB_MEMBER+"."+C.CL_ID + " = " +C.TB_SERVICE+"."+ C.CL_M_ID 
                + " where "+C.TB_SERVICE+"."+ C.CL_ID + " = " + id;
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            Service a = new Service();
            a.member = new Member();
            a.member.id = rs.getInt(C.CL_M_ID);
            a.member.curve = BNCurve.createBNCurveFromName("TPM_ECC_BN_P256");
            a.member.epk = rs.getBytes(C.CL_EPK);
            a.member.esk = rs.getBytes(C.CL_ESK);
            a.member.M = rs.getString(C.CL_M);
            a.id =id;
            for(int i=0; i< User.fieldSet.length; i++){
               a.setInfo(User.fieldSet[i], rs.getString(User.fieldSet[i]));
            }
            
            a.resultSet = rs;
            return a;
            
        }
        else{
            return null;
        }
    }      
    
    public static Service getFromMemberID(JdbcTemplate j, Integer id) throws SQLException{
         String sql = "select * from "+C.TB_SERVICE + " inner join " + C.TB_MEMBER 
                    + " on " +C.TB_MEMBER+"."+C.CL_ID + " = " +C.TB_SERVICE+"."+ C.CL_M_ID 
                + " where "+C.TB_SERVICE+"."+ C.CL_M_ID + " = " + id;
        Statement st = j.getDataSource().getConnection().createStatement();
        ResultSet rs= st.executeQuery(sql);
        if(rs.next()){
            Service a = new Service();
            a.member = new Member();
            a.member.id = rs.getInt(C.CL_M_ID);
            a.member.curve = BNCurve.createBNCurveFromName("TPM_ECC_BN_P256");
            a.member.epk = rs.getBytes(C.CL_EPK);
            a.member.esk = rs.getBytes(C.CL_ESK);
            a.member.M = rs.getString(C.CL_M);
            a.id =id;
            Level l = new Level();
            l.getFromLevelID(j,rs.getInt(C.CL_SERVICE_LEVEL));
            String[] fs = Service.fieldSet;
            for(int i=0; i< fs.length; i++){
                String f = fs[i];
                a.setInfo(f, rs.getString(f));
            }
            
            a.resultSet = rs;
            
            return a;
            
        }
        else{
            return null;
        }
    }
        
}
