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
	 * #文件上传大小限制 10 * 1024 * 1024 = 10M
	 */
	public static final String config_maxPostSize = "10485760";
	/**
	 * 文件上传根路径
	 */
	public static final String config_fileUploadRoot = "/upLoadFolder/";
	private final double BASE_PRICE=1.0;//基础价格
	private final double SINGLE_PRICE=0.07;//单面打印价格
	private final double DOUBLE_PRICE=0.1;//双面打印价格
	private final double COLOR_PRICE=0.5;//彩色打印价格
	
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
			/*
			 * 获取用户信息
			 */
			User user=getSessionAttr("userMessage");
			int user_phone=user.getUserPhone();
			String school_id=user.getUserAddress();
			
			SimpleDateFormat df = new SimpleDateFormat("YYYYMMdd-HHmm");//设置日期格式
			String data=df.format(new Date());
			//获得订单编号格式为：学校代码—提交日期-提交数量
			String OrderNumber=school_id+data+files.size();
			
			for (int i = 0; i < files.size(); i++) {
				fileRoot = savePath.getAbsolutePath() + "\\" + files.get(i).getFileName();
System.out.println(fileRoot);
				String extensionName = FileConfig.getExtensionName(files.get(i).getFileName());

				int pageNum = 0;
				//根据扩展名调用不同的方法
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
				
				short is_single=1;
				short is_color=0;
				int copies=getParaToInt("copies");
				double sum_price=0;
				if(getPara("is_single") == "false"){
					is_single=0;
					sum_price=BASE_PRICE*
				}
				if (getPara("is_color")=="true") {
					is_color=1;
				}
				
				String paper_id=school_id+data;//添加文件编号，使用school_id+日期的形式
				String paper_name=files.get(i).getFileName();
//				int copies=getParaToInt("copies");
				
				insertFile(paper_id, paper_name, user_phone, school_id, pageNum, 
						is_single, is_color,copies, fileRoot, sum_price);
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
	 */
	public void insertFile(String paper_id,String paper_name,
			int user_phone,String school_id,int page_count,
			short is_single,short is_color,int copies,String url,double sum_price) {
		Paper paper=getModel(Paper.class,"paper");
		paper.set("paper_id", paper_id)
		.set("paper_name", paper_name)
		.set("user_phone", user_phone)
		.set("school_id", school_id)
		.set("page_count", page_count)
		.set("is_single", is_single)
		.set("is_color", is_color)
		.set("copies", copies)
		.set("url", url)
		.set("sum_price", sum_price).save();
	}

}
