package com.lris.test;

//import org.apache.logging.log4j.LogManager;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
//import org.apache.logging.log4j.Logger;


public class Log4j2 {

	private static Logger logger2 = LogManager.getLogger(Test.class);

    /**
     * @param args
     */
    public static void main(String[] args) {

		// 记录debug级别的信息
		logger2.debug("This is debug message!");
		// 记录info级别的信息
		logger2.info("This is info message!");
		// 记录error级别的信息
		logger2.error("This is error message!");
		// 记录warn级别的信息
		logger2.warn("This is warn message!");
    }


}
