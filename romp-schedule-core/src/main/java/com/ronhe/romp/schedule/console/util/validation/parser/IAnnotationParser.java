package com.ronhe.romp.schedule.console.util.validation.parser;

import java.lang.reflect.Field;

import com.ronhe.romp.schedule.console.util.validation.ValidateResult;

public interface IAnnotationParser {
	public ValidateResult validate(Field f, Object value) ;
}
