package com.ronhe.romp.schedule.console.util.validation;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ronhe.romp.schedule.console.util.validation.parser.IAnnotationParser;
import com.ronhe.romp.schedule.console.util.validation.parser.impl.DateFormatParser;
import com.ronhe.romp.schedule.console.util.validation.parser.impl.EmailParser;
import com.ronhe.romp.schedule.console.util.validation.parser.impl.IPParser;
import com.ronhe.romp.schedule.console.util.validation.parser.impl.LengthParser;
import com.ronhe.romp.schedule.console.util.validation.parser.impl.MaxParser;
import com.ronhe.romp.schedule.console.util.validation.parser.impl.MinParser;
import com.ronhe.romp.schedule.console.util.validation.parser.impl.NotBlankParser;
import com.ronhe.romp.schedule.console.util.validation.parser.impl.NumberLetterParser;
import com.ronhe.romp.schedule.console.util.validation.parser.impl.PatternParser;

/**
 * 注解校验器
 *
 */
public class AnnotationValidator {
	private static final Logger log = LoggerFactory.getLogger(AnnotationValidator.class);
	private final static List<IAnnotationParser> vList = new ArrayList<IAnnotationParser>();
	static {
        vList.add(new NotBlankParser());
        vList.add(new EmailParser());
        vList.add(new MaxParser());
        vList.add(new MinParser());
        vList.add(new LengthParser());
        vList.add(new NumberLetterParser());
        vList.add(new IPParser());
        vList.add(new PatternParser());
        vList.add(new DateFormatParser());
    }
	 /**
     * 遍历所有字段，用所有解析器进行校验，如果校验失败，则终止校验返回结果，如果校验成功，同样返回校验结果
     * @param t
     * @return
     */
    public static <T> ValidateResult validate(T t){
        ValidateResult result = null;
        for (Field f : t.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            Object value = null;
            if(Modifier.isStatic(f.getModifiers())){
				continue;
		    }
            try {
                value = f.get(t);
            } catch (Exception e) {
            	log.error(e.getMessage());
            }
            for (IAnnotationParser va : vList) {
                result = va.validate(f, value);
                if(!result.isValid()){
                    return result;
                }
            }
        }
        return result;
    }
    /**
     * 注册解析器、实现自定义
     * @param parser
     */
    public static void register(IAnnotationParser parser){
        vList.add(parser);
    }
}
