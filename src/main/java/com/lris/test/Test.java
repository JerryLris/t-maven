package com.lris.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;

import com.lris.excel.po.Pearl;
import com.lris.excel.util.ExcelUtil;
import com.lris.excel.util.ExcelUtil.excelType;



public class Test {
	//会话工厂
		private SqlSessionFactory sqlSessionFactory;

		@Before
		public void createSqlSessionFactory() throws IOException {
			// 配置文件
			String resource = "SqlMapConfig.xml";
			InputStream inputStream = Resources.getResourceAsStream(resource);

			// 使用SqlSessionFactoryBuilder从xml配置文件中创建SqlSessionFactory
			sqlSessionFactory = new SqlSessionFactoryBuilder()
					.build(inputStream);

		}
		// 添加用户信息
		@org.junit.Test
		public void testInsertT() {
			// 数据库会话实例
			SqlSession sqlSession = null;
			try {
				// 创建数据库会话实例sqlSession
				sqlSession = sqlSessionFactory.openSession();
				// 添加用户信息
				Pearl pearl = new Pearl();
				pearl.setId(11);
				pearl.setF01(2);
				pearl.setF02(2);
				pearl.setF03(2);
				pearl.setF04(2);
				pearl.setF05(2);
				pearl.setF06(2);
				pearl.setF07(2);
				int rs = sqlSession.insert("com.lris.test.insertPeal", pearl);
				System.out.println("insertPeal:"+rs);
				//提交事务
				sqlSession.commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (sqlSession != null) {
					sqlSession.close();
				}
			}
		}
		@org.junit.Test
		public void testInsertTAll() {
			// 数据库会话实例
			SqlSession sqlSession = null;
			try {
				// 创建数据库会话实例sqlSession
				sqlSession = sqlSessionFactory.openSession();
				ExcelUtil excel = new ExcelUtil(new FileInputStream("F:\\Documents\\Desktop\\0514\\222.xlsx"), excelType.xlsx);
				excel.setDatePattern("yyyy-MM-dd HH:mm:ss");
				excel.setStartRow(0);
				List<Pearl> list = excel.importExcel(Pearl.class);
				for (Pearl pearl : list) {
					System.out.println(pearl);
					int rs = sqlSession.insert("com.lris.test.insertPeal", pearl);
					System.out.println("insertPeal:"+rs);
				}
				//提交事务
				sqlSession.commit();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (sqlSession != null) {
					sqlSession.close();
				}
			}
		}

}

