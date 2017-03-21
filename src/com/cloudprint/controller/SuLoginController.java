/**
 * 
 */
package com.cloudprint.controller;

import java.util.List;

import com.cloudprint.config.MyInterceptor;
import com.cloudprint.model.Manager;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;

/**
 * @author gaoli
 *
 *管理员用户登录控制与添加管理员
 */
public class SuLoginController extends Controller {
	static String Loginfalse="none";
	static String validateFalse="none";

	public void index() {
		setAttr("loginFalse", Loginfalse);
		setAttr("validateFalse", validateFalse);
        render("/suLogin.html");
    }
	@Before(MyInterceptor.class)
	public void Tobackground() {
		render("/index.html");
	}
	public void AddManager() {
		setAttr("loginFalse", Loginfalse);
		render("/addManager.html");
	}
	private static final String FORM_ITEM_CODE = "code";
	/** 
	 * 返回验证码 
	*/
	public void code() {
		renderCaptcha();
	}
	public void managerLogin() {
		List<Manager> managers=Manager.dao.findByIDAndPwd(getPara("manager.manager_id"), getPara("manager.manager_pwd"));

		if (managers.size() > 0) {
			//找到该用户
			setSessionAttr("managerMessage", managers.get(0));
			if(validateCaptcha(FORM_ITEM_CODE)){ // 验证码验证成功 
				redirect("/manager/Tobackground");
			}else{ 
				Loginfalse="none";
				validateFalse="block";
				redirect("/manager");
			}
		}else{
			Loginfalse="block";
			validateFalse="none";
			redirect("/manager");
		}
	}
	
	/*
	 * 超级管理员添加新管理员
	 */
	public void addManager(){
		Manager manager=getModel(Manager.class, "manager");
		List<Manager> managers=Manager.dao.findByID(getPara("manager.manager_id"));
		if (managers.size()==0) {
			manager.save();//用户保存成功
//			setSessionAttr("manager", getPara("userName"));
			renderHtml("alter('管理员用户创建成功");
			redirect("/manager/Tobackground");
		}
		else{
			Loginfalse="block";
			redirect("/manager/AddManager");
		}
	}
	
}
