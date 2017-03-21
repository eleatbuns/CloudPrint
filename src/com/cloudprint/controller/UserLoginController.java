/**
 * 
 */
package com.cloudprint.controller;

import java.util.List;

import com.cloudprint.config.MyInterceptor;
import com.cloudprint.model.User;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;


/**
 * @author gaoli
 *普通用户的登录注册（用户控制器）
 */
public class UserLoginController extends Controller {
	static String Loginfalse="none";
	static String validateFalse="none";
//****************登录界面*****************
	public void index() {
		setAttr("loginFalse", Loginfalse);
		setAttr("validateFalse", validateFalse);
		render("/Login.html");
	}
	@Before(MyInterceptor.class)
	public void ToIndex() {
		render("/index.html");
	}
//*****************************************
//********************注册界面**************
	public void register() {
		setAttr("loginFalse", Loginfalse);
		render("/Register.html");
	}

	public void reRegister() {
		render("/Register.html");
	}
//*************************************
//***********找回密码*******************
	
	public void ForgetPwd(){
		render("/ForgetPassword.html");
	}
	@Before(MyInterceptor.class)
	public void resetPwd(){
		render("/ResetPassword.html");
	}
//*************************************
	
	/*
	 * 验证码 由JFinal自动实现
	 */
	private static final String FORM_ITEM_CODE = "code";

	/** 
	 * 返回验证码 
	*/
	public void code() {
		renderCaptcha();
	}
	
	/*
	 * 用户登录验证
	 */
	public void userlogin() {
		// System.out.println(getPara("userName")+" "+getPara("password"));
		List<User> users = User.dao.findByPhoneAndPwd(getPara("user.user_phone"), getPara("user.user_password"));
		if (users.size() > 0) {
			// 找到用户
			setSessionAttr("userMessage", users.get(0));
			
			if(validateCaptcha(FORM_ITEM_CODE)){ // 验证码验证成功 
				redirect("/ToIndex");
				
			}else{ 
				Loginfalse="none";
				validateFalse="block";
				redirect("/");
			} 
		} else {
			Loginfalse="block";
			validateFalse="none";
			redirect("/");

		}
	}

	

/*
 * 用户注册验证
 */
	public void userRegister() {
		User user = getModel(User.class, "user");
		List<User> users = User.dao.findByPhone(getPara("user.user_phone"));
		
		if (users.size() == 0) {//当前用户不存在，可以注册
			user.save();
			// 注册成功
			setSessionAttr("username", getPara("userName"));
			System.out.println("执行到这里");
			redirect("/ToIndex");
		} else {
			Loginfalse="block";
			redirect("/register");
		}
	}
	
	
	/*
	 * 用户找回密码
	 */
	public void FindPassword(){
		List<User> users = User.dao.findByPhoneAndSecurity(getPara("user.user_phone"),getPara("user.security_question"),getPara("user.security_answer"));
		if (users.size() > 0) {
			// 找到用户
			setSessionAttr("userMessage", users.get(0));
			
			if(validateCaptcha(FORM_ITEM_CODE)){ // 验证码验证成功 
				redirect("/resetPwd");
				
			}else{ 
				Loginfalse="none";
				validateFalse="block";
				redirect("/ForgetPwd");
			} 
		} else {
			Loginfalse="block";
			validateFalse="none";
			redirect("/ForgetPwd");

		}
	}
	
	public void resetPassword() {
		String password=getPara("user.password");
		User user=getSessionAttr("userMessage");
		int user_phone = user.getUserPhone();
		int update=Db.update("UPDATE USER SET user_password=? WHERE user_phone=?;",password,user_phone);
		if (update==1) {
			renderHtml("<center><h2>密码修改成功</h2></center>");
//			redirect("/ToIndex");
		}
	}
	
}
