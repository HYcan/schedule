package com.ronhe.romp.schedule.core.scheduler.conf;


import com.ronhe.romp.schedule.core.base.biz.AdminBiz;
import com.ronhe.romp.schedule.core.base.biz.ExecutorBiz;
import com.ronhe.romp.schedule.core.base.enums.ExecutorBlockStrategyEnum;
import com.ronhe.romp.schedule.core.scheduler.thread.JobFailMonitorHelper;
import com.ronhe.romp.schedule.core.scheduler.thread.JobRegistryMonitorHelper;
import com.ronhe.romp.schedule.core.scheduler.thread.JobScheduleHelper;
import com.ronhe.romp.schedule.core.scheduler.thread.JobTriggerPoolHelper;
import com.ronhe.romp.schedule.core.scheduler.util.I18nUtil;
import com.xxl.rpc.remoting.invoker.XxlRpcInvokerFactory;
import com.xxl.rpc.remoting.invoker.call.CallType;
import com.xxl.rpc.remoting.invoker.reference.XxlRpcReferenceBean;
import com.xxl.rpc.remoting.invoker.route.LoadBalance;
import com.xxl.rpc.remoting.net.NetEnum;
import com.xxl.rpc.remoting.net.impl.servlet.server.ServletServerHandler;
import com.xxl.rpc.remoting.provider.XxlRpcProviderFactory;
import com.xxl.rpc.serialize.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author xuxueli 2018-10-28 00:18:17
 */
@Component
public class SchedulerStartConfig implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(SchedulerStartConfig.class);


    @Override
    public void afterPropertiesSet() throws Exception {
        // init i18n
        initI18n();

        // admin registry monitor run
        JobRegistryMonitorHelper.getInstance().start();

        // admin monitor run
        JobFailMonitorHelper.getInstance().start();

        // admin-server
        initRpcProvider();

        // start-schedule
        JobScheduleHelper.getInstance().start();

        logger.info(">>>>>>>>> init xxl-job admin success.");
    }

    @Override
    public void destroy() throws Exception {

        // stop-schedule
        JobScheduleHelper.getInstance().toStop();

        // admin trigger pool stop
        JobTriggerPoolHelper.toStop();

        // admin registry stop
        JobRegistryMonitorHelper.getInstance().toStop();

        // admin monitor stop
        JobFailMonitorHelper.getInstance().toStop();

        // admin-server
        stopRpcProvider();
    }

    // ---------------------- I18n ----------------------

    private void initI18n(){
        for (ExecutorBlockStrategyEnum item:ExecutorBlockStrategyEnum.values()) {
            item.setTitle(I18nUtil.getString("jobconf_block_".concat(item.name())));
        }
    }

    // ---------------------- admin rpc provider (no server version) ----------------------
    private static ServletServerHandler servletServerHandler;
    private void initRpcProvider(){
        // init
        XxlRpcProviderFactory xxlRpcProviderFactory = new XxlRpcProviderFactory();
        xxlRpcProviderFactory.initConfig(
                NetEnum.NETTY_HTTP,
                Serializer.SerializeEnum.HESSIAN.getSerializer(),
                null,
                0,
                SchedulerResourceConfig.getAdminConfig().getAccessToken(),
                null,
                null);

        // add services
        xxlRpcProviderFactory.addService(AdminBiz.class.getName(), null, SchedulerResourceConfig.getAdminConfig().getAdminBiz());

        // servlet handler
        servletServerHandler = new ServletServerHandler(xxlRpcProviderFactory);
    }
    private void stopRpcProvider() throws Exception {
        XxlRpcInvokerFactory.getInstance().stop();
    }
    public static void invokeAdminService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        servletServerHandler.handle(null, request, response);
    }


    // ---------------------- executor-client ----------------------
    private static ConcurrentMap<String, ExecutorBiz> executorBizRepository = new ConcurrentHashMap<String, ExecutorBiz>();
    public static ExecutorBiz getExecutorBiz(String address) throws Exception {
        // valid
        if (address==null || address.trim().length()==0) {
            return null;
        }

        // load-cache
        address = address.trim();
        ExecutorBiz executorBiz = executorBizRepository.get(address);
        if (executorBiz != null) {
            return executorBiz;
        }

        // set-cache
        executorBiz = (ExecutorBiz) new XxlRpcReferenceBean(
                NetEnum.NETTY_HTTP,
                Serializer.SerializeEnum.HESSIAN.getSerializer(),
                CallType.SYNC,
                LoadBalance.ROUND,
                ExecutorBiz.class,
                null,
                3000,
                address,
                SchedulerResourceConfig.getAdminConfig().getAccessToken(),
                null,
                null).getObject();

        executorBizRepository.put(address, executorBiz);
        return executorBiz;
    }

}
