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
public class School extends Model<School> {

	public final static School dao=new School();
	
	public List<School> findAllSchool(String province_id) {
		System.out.println("select school_id,school_name from school where province_id='"+province_id+"';");
		return find("select school_name from school where province_id='"+province_id+"';");
	}
	public String getID() {
		return getAttrs().get("school_id").toString();
	}
	public String getName() {
		return getAttrs().get("school_name").toString();
	}
}
