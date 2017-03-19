/**
 * 
 */
package com.cloudprint.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.cloudprint.fileConfig.FileConfig;
import com.cloudprint.fileConfig.FilePDF;
import com.cloudprint.fileConfig.FileWord;
import com.cloudprint.model.User;
import com.jfinal.core.Controller;
import com.jfinal.upload.UploadFile;

/**
 * @author gaoli
 *
 */
public class UpLoadFile extends Controller {

	public void index() {
		render("/FileupLoad.html");
	}

	/**
	 * #文件上传大小限制 10 * 1024 * 1024 = 10M
	 */
	public static final String config_maxPostSize = "10485760";
	/**
	 * 文件上传根路径
	 */
	public static final String config_fileUploadRoot = "/upLoadFolder/";

	public void uploadFile() {

		/**
		 * 文件上传根路径
		 */
		StringBuilder savePathStr = new StringBuilder(getAttr("basePath") + config_fileUploadRoot);

		File savePath = new File(savePathStr.toString());

		if (!savePath.exists()) {

			savePath.mkdirs();
		}
		String fileRoot = "";

		try {
			// 保存文件
			List<UploadFile> files = getFiles();
			for (int i = 0; i < files.size(); i++) {
				fileRoot = savePath.getAbsolutePath() + "\\" + files.get(i).getFileName();
System.out.println(fileRoot);
				String extensionName = FileConfig.getExtensionName(files.get(i).getFileName());

				int pageNum = 0;
				switch (extensionName) {
				case "pdf":
					FilePDF filePDF = new FilePDF();
					pageNum = filePDF.PDFExtension(fileRoot);
					break;
				case "doc":
					FileWord doc = new FileWord();
					pageNum = doc.DocPage(fileRoot);
					break;
				case "docx":
					FileWord docx = new FileWord();
					pageNum = docx.DocxPage(fileRoot);
					break;
				default:
					break;
				}
				System.out.println("页数为：" + pageNum);
				setAttr("file_name", files.get(i).getFileName());
				setAttr("fileRoot", fileRoot);
				User user=getSessionAttr("userMessage");
//				System.out.println(user.getUserPhone());
				/*
				 * 获取用户信息
				 */
				String user_phone=user.getUserPhone();
				String school_id=user.getUserAddress();
				
				SimpleDateFormat df = new SimpleDateFormat("YYYYMMdd-HHmm");//设置日期格式
				String data=df.format(new Date());
				String paper_id=school_id+data;//添加文件编号，使用school_id+日期的形式
				System.out.println("paperID="+paper_id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		renderJson();

	}

}
