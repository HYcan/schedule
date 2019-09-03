package com.ronhe.romp.schedule.console.util;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
public class PageSqlUtil {
	private static final Logger log = LoggerFactory.getLogger(PageSqlUtil.class);
	/**
	 * 数据库类型
	 */
	public static final String DATABSE_TYPE_MYSQL ="mysql";
	public static final String DATABSE_TYPE_POSTGRE ="postgresql";
	public static final String DATABSE_TYPE_ORACLE ="oracle";
	public static final String DATABSE_TYPE_SQLSERVER ="sqlserver";
	public static final String DATABSE_TYPE_DB2SERVER ="db2";
    /**
	 * 按照数据库类型，封装SQL
	 */
    public static String createPageSql(JdbcTemplate jdbcTemplate,String sql, int page, int rows) {
		int beginNum = (page - 1) * rows;
		String[] sqlParam = new String[3];
		sqlParam[0] = sql;
		sqlParam[1] = beginNum + "";
		sqlParam[2] = rows + "";
		Connection con = null;
		try {
			con = jdbcTemplate.getDataSource().getConnection();
			String dbType =Dialect.getDataBaseType(con);
			if (dbType.indexOf(DATABSE_TYPE_MYSQL) != -1) {
				sql = MessageFormat.format(
						"select * from ( {0}) sel_tab00 limit {1},{2}",
						sqlParam);
			} else if (dbType.indexOf(DATABSE_TYPE_POSTGRE) != -1) {
				sql = MessageFormat.format(
						"select * from ( {0}) sel_tab00 limit {2} offset {1}",
						sqlParam);
			} else {
				int beginIndex = (page - 1) * rows;
				int endIndex = beginIndex + rows;
				sqlParam[2] = Integer.toString(beginIndex);
				sqlParam[1] = Integer.toString(endIndex);
				if (dbType.indexOf(DATABSE_TYPE_ORACLE) != -1) {
					sql = MessageFormat
							.format("select * from (select row_.*,rownum rownum_ from ({0}) row_ where rownum <= {1}) where rownum_>{2}",
									sqlParam);
				} else if (dbType.indexOf(DATABSE_TYPE_SQLSERVER) != -1) {
					sqlParam[0] = sql.substring(getAfterSelectInsertPoint(sql));
					sql = MessageFormat
							.format("select * from ( select row_number() over(order by tempColumn) tempRowNumber, * from (select top {1} tempColumn = 0, {0}) t ) tt where tempRowNumber > {2}",
									sqlParam);
				} else if (dbType.indexOf(DATABSE_TYPE_DB2SERVER) != -1) {
					sql = MessageFormat
							.format("select * from (select rownumber() over() as rowid,t.* from ( {0}) t ) AS a1 where a1.rowid > {2} and a1.rowid <={1}",
									sqlParam);
				}
			}
			con.close();
			try {
				con.close();
				con = null;
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
		} catch (Exception e) {
			try {
				con.close();
				con = null;
			} catch (Exception ex) {
				log.error(ex.getMessage());
			}
		} finally {
			if (con != null) {
				try {
					con.close();
					con = null;
				} catch (SQLException e) {
					log.error(e.getMessage());
				}
			}
		}
		return sql;
	}
    
	private static int getAfterSelectInsertPoint(String sql) {
	    int selectIndex = sql.toLowerCase().indexOf("select");
	    int selectDistinctIndex = sql.toLowerCase().indexOf("select distinct");
	    return selectIndex + (selectDistinctIndex == selectIndex ? 15 : 6);
    }
}
