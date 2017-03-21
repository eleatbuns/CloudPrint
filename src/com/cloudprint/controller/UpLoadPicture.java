/**
 * 
 */
package com.cloudprint.controller;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * @author gaoli
 *
 */
public class UpLoadPicture extends Controller {
	
	public void index() {
		render("/PictureupLoad.html");
	}
	
	//定义图片上传根路径
	private static final String config_fileUploadRoot="/PictureLoadFolder/";
	
	public void PrintPicture(){

		String upLoadPath=config_fileUploadRoot;

		String fileRoot = "";
		
		try {
			// 保存文件
			List<UploadFile> files = getFiles(upLoadPath);
			fileRoot = files.get(0).getUploadPath() + files.get(0).getFileName();
			System.out.println(files.get(0).getUploadPath());
			/*
			 * 获取用户信息
			 */
//			User user = getSessionAttr("userMessage");
//			int user_phone = user.getUserPhone();
//			String school_id = user.getUserAddress();
System.out.println(fileRoot);
			SimpleDateFormat df = new SimpleDateFormat("YYYYMMdd-HHmmss");// 设置日期格式
			String data = df.format(new Date());
			// 获得订单编号格式为：学校代码—提交日期-提交数量
//			String OrderNumber = school_id + data + files.size();
			
			setAttr("file_name", files.get(0).getFileName());
			setAttr("fileRoot", fileRoot);
			
			
			renderJson();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
