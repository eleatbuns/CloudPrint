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
import com.cloudprint.model.Paper;
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
	 * 文件上传根路径
	 */
	public static final String config_fileUploadRoot = "/DOCLoadFolder/";
	
	
	private final double BASE_PRICE = 1.0;// 基础价格
	private final double SINGLE_PRICE = 0.07;// 单面黑白打印价格
	private final double DOUBLE_PRICE = 0.1;// 双面黑白打印价格
	private final double COLOR_PRICE = 0.5;// 彩色单面打印价格
	private final double COLOR_DOUB_PRICE = 0.8;// 彩色双面打印

	public void uploadFile() {

		/**
		 * 文件上传根路径
		 */
		String upLoadPath=config_fileUploadRoot;
		String fileRoot = "";

		try {
			// 保存文件
			List<UploadFile> files = getFiles(upLoadPath);
			/*
			 * 获取用户信息
			 */
			User user = getSessionAttr("userMessage");
			int user_phone = user.getUserPhone();
			String school_id = user.getUserAddress();

			SimpleDateFormat df = new SimpleDateFormat("YYYYMMdd-HHmmss");// 设置日期格式
			String data = df.format(new Date());
			// 获得订单编号格式为：学校代码—提交日期-提交数量
			String OrderNumber = school_id + data + files.size();

			for (int i = 0; i < files.size(); i++) {
				fileRoot =  files.get(i).getUploadPath() + "/" + files.get(i).getFileName();

				String extensionName = FileConfig.getExtensionName(files.get(i).getFileName());

				int pageNum = 0;
				// 根据扩展名调用不同的方法
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

				setAttr("file_name", files.get(i).getFileName());
				setAttr("fileRoot", fileRoot);
				setAttr("pageNum", pageNum);

				short is_single = 1;
				short is_color = 0;
				int copies = getParaToInt("copies");
				double sum_price = 0;
//				System.out.println("is_single=" + getPara("is_single"));
//				System.out.println("is_color=" + getPara("is_color"));

				switch (getPara("is_single")) {
				case "NO":
					is_single = 0;
					break;
				case "YES":
					is_single = 1;
					break;
				}
				switch (getPara("is_color")) {
				case "NO":
					is_color = 0;
					break;
				case "YES":
					is_color = 1;
					break;
				}

				if(is_single==0&&is_color==0){//双面单色
					sum_price = copies * DOUBLE_PRICE* Math.ceil(((double)pageNum) / 2)/10;
				}
				if(is_single==0&&is_color==1){//双面彩色
					sum_price = copies * COLOR_DOUB_PRICE * Math.ceil(((double)pageNum) / 2)/10;
				}
				if(is_single==1&&is_color==0){//单面单色
					sum_price = copies * SINGLE_PRICE * pageNum;
				}
				if(is_single==1&&is_color==1){//单面彩色
					sum_price = copies * COLOR_PRICE * pageNum;
				}
				
				String paper_id = school_id + data;// 添加文件编号，使用school_id+日期的形式
				String paper_name = files.get(i).getFileName();

				insertFile(paper_id, paper_name, user_phone, school_id, pageNum, is_single, is_color, copies, fileRoot,
						sum_price);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		renderJson();

	}

	/**
	 * @param paper_id
	 * @param paper_name
	 * @param user_phone
	 * 向数据库中插入文件信息
	 */
	
	public void insertFile(String paper_id, String paper_name, int user_phone, String school_id, int page_count,
			short is_single, short is_color, int copies, String url, double sum_price) {
		Paper paper = getModel(Paper.class, "paper");
		paper.set("paper_id", paper_id).set("paper_name", paper_name).set("user_phone", user_phone)
				.set("school_id", school_id).set("page_count", page_count).set("is_single", is_single)
				.set("is_color", is_color).set("copies", copies).set("url", url).set("sum_price", sum_price).save();
	}

}
