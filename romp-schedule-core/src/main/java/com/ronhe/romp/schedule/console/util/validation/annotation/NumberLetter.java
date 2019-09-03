package com.ronhe.romp.schedule.console.util.validation.annotation;
/* 
 *  数字字母效验 
 *  
 */ 
public @interface NumberLetter {
	 String message () default"";
	 String fieldName ()default"";
}
