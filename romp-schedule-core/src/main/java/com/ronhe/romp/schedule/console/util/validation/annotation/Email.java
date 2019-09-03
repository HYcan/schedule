package com.ronhe.romp.schedule.console.util.validation.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ java.lang.annotation.ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
/**
 * 验证是否是邮件地址，如果为null,不进行验证，算通过验证。
 * 
 * // 1、\\w+表示@之前至少要输入一个匹配字母或数字或下划线 \\w 单词字符：[a-zA-Z_0-9]  
        // 2、(\\w+\\.)表示域名. 如新浪邮箱域名是sina.com.cn  
        // {1,3}表示可以出现一次或两次或者三次.  
        String reg = "\\w+@(\\w+\\.){1,3}\\w+";  
        Pattern pattern = Pattern.compile(reg);  
        boolean flag = false;  
        if (email != null)  
        {  
            Matcher matcher = pattern.matcher(email);  
            flag = matcher.matches();  
        }  
        return flag;  
 */
public @interface Email {
	String message()default "";
	String fieldName()default "";
}
