package com.lris.excel.utils.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解作用域为ExcelUtil
 * <p>指定单元格的列名，若不指定，默认以实体字段名作为列名</p>
 * @author CYX
 * @email chengyuxingo@gmail.com
 *
 * <br>2017年11月22日 上午10:13:55
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface ExcelColumn {
	
	/**
	 * 列名
	 * @return
	 * <br>2017年11月22日上午8:57:26
	 */
	String value() default "";
}
