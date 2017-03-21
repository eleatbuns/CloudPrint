/**
 * 
 */
package com.cloudprint.config;

import com.cloudprint.controller.ShowSchool;
import com.cloudprint.controller.UserLoginController;
import com.cloudprint.controller.SuLoginController;
import com.cloudprint.controller.UpLoadFile;
import com.cloudprint.controller.UpLoadPicture;
import com.cloudprint.model.Manager;
import com.cloudprint.model.Paper;
import com.cloudprint.model.Province;
import com.cloudprint.model.School;
import com.cloudprint.model.User;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.ext.handler.ContextPathHandler;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.c3p0.C3p0Plugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;
import com.jfinal.upload.UploadFile;


/**
 * @author gaoli
 *
 */
public class CommonConfig extends JFinalConfig {

	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configConstant(com.jfinal.config.Constants)
	 */
	@Override
	public void configConstant(Constants me) {
		// TODO Auto-generated method stub
		me.setEncoding("UTF-8");
		me.setDevMode(true);
		me.setViewType(ViewType.FREE_MARKER);
		me.setBaseUploadPath("D:/CloudPrint/upLoadFolder/");
	}

	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configRoute(com.jfinal.config.Routes)
	 */
	@Override
	public void configRoute(Routes me) {
		// TODO Auto-generated method stub
		 me.add("/",UserLoginController.class);
		 me.add("/manager", SuLoginController.class);
		 me.add("/showSchool", ShowSchool.class);
		 me.add("/upLoadFile",UpLoadFile.class);
		 me.add("/upLoadPicture",UpLoadPicture.class);

	}

	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configEngine(com.jfinal.template.Engine)
	 */
	@Override
	public void configEngine(Engine me) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configPlugin(com.jfinal.config.Plugins)
	 */
	@Override
	public void configPlugin(Plugins me) {
		// TODO Auto-generated method stub
		C3p0Plugin c3p0Plugin = new C3p0Plugin("jdbc:mysql://localhost:3306/cloud_print",
				"root", "123456");
		me.add(c3p0Plugin);

		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0Plugin);
		me.add(arp);
		
		arp.addMapping("user", "user_phone", User.class);
		arp.addMapping("manager","manager_id", Manager.class);
		arp.addMapping("school", "school_id", School.class);
		arp.addMapping("province", "province_num",Province.class);
		arp.addMapping("paper", "paper_id", Paper.class);
//		arp.addMapping("diary", "id", Diary.class);
//		arp.addMapping("photo", "id", Image.class);
//		arp.addMapping("study", "id", Study.class);
//		arp.addMapping("admin", "id", User.class);
	}

	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configInterceptor(com.jfinal.config.Interceptors)
	 */
	@Override
	public void configInterceptor(Interceptors me) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see com.jfinal.config.JFinalConfig#configHandler(com.jfinal.config.Handlers)
	 */
	@Override
	public void configHandler(Handlers me) {
		// TODO Auto-generated method stub
		me.add(new ContextPathHandler("basePath"));

	}

}
