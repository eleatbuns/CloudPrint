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
public class Paper extends Model<Paper> {
	
	public final static Paper dao=new Paper();
	
	public List<Paper> findAll() {
		return find("select * from paper");
	}
	
}
