/**
 * 
 */
package com.cloudprint.controller;

import java.util.List;

import com.cloudprint.model.Province;
import com.cloudprint.model.School;
import com.jfinal.core.Controller;

/**
 * @author gaoli
 *
 */
public class ShowSchool extends Controller {
	
	public void index() {
		render("/showArea.html");
	}
	
	public void schoolList() {
		System.out.println("提交到这里");
		String province=getPara("province");
		List<Province> province_num=Province.dao.findProvinceName(province);
		
		String num=province_num.get(0).getID();
		List<School> schools=School.dao.findAllSchool(num);
//		System.out.println(schools);
		renderJson(schools);
	}
}
