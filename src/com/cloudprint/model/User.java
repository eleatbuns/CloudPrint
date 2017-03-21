/**
 * 
 */
package com.cloudprint.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author gaoli
 *
 *实现普通用户的登录注册（用户模型）
 */
public class User extends Model<User> {
	private static final long serialVersionUID = 1L;

    public final static User dao = new User();
    
    //查询所有的用户信息
    public List<User> findAll(){  
        return find("select * from user");  
    }  
    //根据用户ID和密码查询用户信息
    public List<User> findByPhoneAndPwd(String phone, String password){  
//    	System.out.println("select * from user where user_name='"+phone+"' and user_password='"+password+"'");
        return find("select * from user where user_phone=? and user_password=?;",phone,password);  
    }  
    //根据用户ID查询用户信息
    public List<User> findByPhone(String phone){  
//    	System.out.println("select * from user where user_phone='"+phone+"';");
        return find("select * from user where user_phone=?;",phone);  
    }  
    
    //根据用户ID和密保问题查询用户信息(待优化，需要加入学校）
    public List<User> findByPhoneAndSecurity(String phone,String questionID,String answer ) {
		return find("select * from user where user_phone=? and security_question= ? and security_answer=?;",phone,questionID,answer);
		
	}
    
    
    //获得用户信息
    public int getUserPhone() {
		String pString = getAttrs().get("user_phone").toString();
		return Integer.parseInt(pString);
	}
    public String getUserName() {
		return getAttrs().get("user_name").toString();
	}
    public String getUserAddress() {
		return getAttrs().get("user_address").toString();
	}
}
