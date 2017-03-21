package com.cloudprint.config;

import javax.servlet.http.HttpSession;

import com.cloudprint.model.Manager;
import com.cloudprint.model.User;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;

public class MyInterceptor implements Interceptor{

	/**
	 * 通过过滤器实现用户登录验证
	 * 通过检查session中是否存储的用的user_message来判断当前用户是否已登录，
	 * 因为之前在login中当用户登录成功后会在session中保存用户的user_message，
	 * 如果当前session中有user_message的数据则判断当前用户是已登录的，
	 * 则允许继续访问，如果user_message为空，则停止当前访问，并跳转至登录页面让用户登录
	 */
	@Override
	public void intercept(Invocation inv) {
		
		HttpSession session=inv.getController().getSession();
		
		if (session==null) {
			inv.getController().redirect("/");
		}
		else{
			User user_message = (User) session.getAttribute("userMessage");
//			Manager manager_message = (Manager) session.getAttribute("managerMessage");

			if(user_message == null) {
				inv.getController().redirect("/");
			}
			else{
				inv.invoke();
			}
		}
	}

}
