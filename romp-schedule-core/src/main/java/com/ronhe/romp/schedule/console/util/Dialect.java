package com.ronhe.romp.schedule.console.util;
import java.sql.Connection;
import java.sql.SQLException;
public enum Dialect {
    mysql, oracle, sqlserver, db2, informix, sqlserver2012;
    public static String[] dialects() {
        Dialect[] dialects = Dialect.values();
        String[] ds = new String[dialects.length];
        for (int i = 0; i < dialects.length; i++) {
            ds[i] = dialects[i].toString();
        }
        return ds;
    }

    
    /** 
     * 判断数据库类型 
     * @return  
     * @throws SQLException 
     */  
    public static String getDataBaseType(Connection connection) throws Exception {  
        //通过driverName是否包含关键字判断  
        if (connection.getMetaData().getDriverName().toUpperCase()  
                .indexOf("MYSQL") != -1) {  
            return Dialect.mysql.name();  
        } else if (connection.getMetaData().getDriverName().toUpperCase()  
                .indexOf("SQL SERVER") != -1) {  
            //sqljdbc与sqljdbc4不同，sqlserver中间有空格  
            return Dialect.sqlserver.name();  
        }else if (connection.getMetaData().getDriverName().toUpperCase()  
                .indexOf("ORACLE") != -1) {  
            return Dialect.oracle.name();  
        }else if (connection.getMetaData().getDriverName().toUpperCase()  
                .indexOf("DB2") != -1) {  
            return Dialect.db2.name();  
        }  
        return "-1";  
    }



}
