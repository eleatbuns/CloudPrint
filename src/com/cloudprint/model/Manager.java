/**
 * 
 */
package com.cloudprint.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author gaoli
 *
 */
public class Manager extends Model<Manager> {
//	private static final long serialVersionUID = 1L;

    public final static Manager dao = new Manager();
    
    public List<Manager> findAll(){  
        return find("select * from manager");  
    }  
    
    public List<Manager> findByIDAndPwd(String id, String password){  
//    	System.out.println("select * from manager where manager_id='"+id+"' and manager_pwd='"+password+"';");
        return find("select * from manager where manager_id='"+id+"' and manager_pwd='"+password+"';");  
    }  
    public List<Manager> findByID(String id){  
//    	System.out.println("select * from manager where manager_id='"+id+"';");
        return find("select * from manager where manager_id='"+id+"';");  
    }  
}
