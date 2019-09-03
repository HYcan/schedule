package com.ronhe.romp.schedule.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.NameMatchTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

public class ScheduleConfiguration {
    private DataSource dataSource;
    
    private PlatformTransactionManager transactionManager;
    
    private static final int TX_METHOD_TIMEOUT = 5;
    
    public static final String AOP_POINTCUT_EXPRESSION = "execution (* com.ronhe.romp.schedule.console.service.*.*.*(..))";
    
	public PlatformTransactionManager getTransactionManager() {
		return transactionManager;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	public DataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Bean(name="scheduleJdbcTemplate")
	public JdbcTemplate jdbcTemplate() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		return jdbcTemplate;
	}
	
    @Bean(name="scheduleTransactionInterceptor")
	public TransactionInterceptor txAdvice() {
	    NameMatchTransactionAttributeSource source = new NameMatchTransactionAttributeSource();
	     /*只读事务，不做更新操作*/
	    RuleBasedTransactionAttribute readOnlyTx = new RuleBasedTransactionAttribute();
	    readOnlyTx.setReadOnly(true);
	    readOnlyTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_NOT_SUPPORTED );
	    /*当前存在事务就使用当前事务，当前不存在事务就创建一个新的事务*/
	    RuleBasedTransactionAttribute requiredTx = new RuleBasedTransactionAttribute();
	    requiredTx.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
	    requiredTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
	    requiredTx.setTimeout(TX_METHOD_TIMEOUT);
	    Map<String, TransactionAttribute> txMap = new HashMap<>();
	    txMap.put("save*", requiredTx);
	    txMap.put("insert*", requiredTx);
	    txMap.put("update*", requiredTx);
	    txMap.put("delete*", requiredTx);
	    txMap.put("remove*", requiredTx);
	    txMap.put("get*", readOnlyTx);
	    txMap.put("query*", readOnlyTx);
	    txMap.put("find*", readOnlyTx);
	    source.setNameMap(txMap);
	    TransactionInterceptor txAdvice = new TransactionInterceptor(transactionManager, source);
	    return txAdvice;
	  }
	  @Bean(name="scheduleAdvisor")
	  public Advisor txAdviceAdvisor() {
	    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
	    pointcut.setExpression(AOP_POINTCUT_EXPRESSION);
	    return new DefaultPointcutAdvisor(pointcut, txAdvice());
	  }
	
}
