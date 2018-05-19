package com.lris.jdbc.service;

import java.util.List;

import org.junit.Test;

import com.lris.jdbc.dao.PearlJdbcDao;
import com.lris.jdbc.po.Pearl;

public class PearlJdbcService {

	@Test
	public void testList() {
		
		PearlJdbcDao pearlJdbcDao = new PearlJdbcDao();
		String sql = "select * from pearl.pearl where id >?";
		String sql2 = "update pearl.pearl t set t.all=?,t.red=? where t.id=?";
		List<Pearl> list = pearlJdbcDao.baseQuery(sql, Pearl.class, 0);
		for(Pearl pearl:list) {
			System.out.println(pearl);
			pearl.setAll(
					pearl.getF01()+"-"+
					pearl.getF02()+"-"+
					pearl.getF03()+"-"+
					pearl.getF04()+"-"+
					pearl.getF06()+"-"+
					pearl.getF07()
			);
			pearl.setRed(
					pearl.getF01()+"-"+
					pearl.getF02()+"-"+
					pearl.getF03()+"-"+
					pearl.getF04()+"-"+
					pearl.getF05()+"-"+
					pearl.getF06()
					);
			System.out.println(pearl);
			int rs = pearlJdbcDao.baseUpdate(sql2,pearl.getAll(),pearl.getRed(),pearl.getId());
			System.out.println(rs);
		}
	}
	
}
