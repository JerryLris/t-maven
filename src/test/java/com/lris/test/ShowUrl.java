package com.lris.test;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class ShowUrl {

	public static void main(String[] args) {
		ShowUrl muDemo = new ShowUrl();
         try {
             muDemo.showURL();
         } catch (IOException e) {
             e.printStackTrace();
         }
	}
	
    public void showURL() throws IOException {

        // 第一种：获取类加载的根路径   E:\java\agentv2\t-maven\target\test-classes
        File f = new File(this.getClass().getResource("/").getPath());
        System.out.println(f);

        // 获取当前类的所在工程路径; 如果不加“/”  获取当前类的加载目录  E:\java\agentv2\t-maven\target\test-classes\com\lris\test
        File f2 = new File(this.getClass().getResource("").getPath());
        System.out.println(f2);

        // 第二种：获取项目路径    E:\java\agentv2\t-maven
        File directory = new File("");// 参数为空
        String courseFile = directory.getCanonicalPath();
        System.out.println(courseFile);


        // 第三种：  file:/E:/java/agentv2/t-maven/target/test-classes/
        URL xmlpath = this.getClass().getClassLoader().getResource("");
        System.out.println(xmlpath);

        /*
         * 结果： 获取当前工程路径
         */
        // 第四种： E:\java\agentv2\t-maven
        System.out.println(System.getProperty("user.dir"));

        // 第五种：  获取所有的类路径 包括jar包的路径
        System.out.println(System.getProperty("java.class.path"));

    }
}
