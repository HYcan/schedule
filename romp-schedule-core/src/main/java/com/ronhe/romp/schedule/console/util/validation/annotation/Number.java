package com.ronhe.romp.schedule.console.util.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 验证必须为整数
 * @author 
 *
 */
@Target({ java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Number {
  String message () default"";
  String fieldName ()default"";
}
