/**
 * 
 */
package com.cloudprint.model;

import java.util.List;

import com.jfinal.plugin.activerecord.Model;

/**
 * @author gaoli
 *
 */
public class Province extends Model<Province> {
public final static Province dao=new Province();
	
	public List<Province> findProvinceName(String province_name) {
		
		return find("select province_num from province where province_name='"+province_name+"';");
	}
	public String getID() {
		return getAttrs().get("province_num").toString();
	}
}
