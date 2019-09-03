package com.ronhe.romp.schedule.core.api;

import com.ronhe.romp.schedule.core.base.biz.model.ReturnT;
import com.ronhe.romp.schedule.core.base.enums.ExecutorBlockStrategyEnum;
import com.ronhe.romp.schedule.core.base.glue.GlueTypeEnum;
import com.ronhe.romp.schedule.core.scheduler.route.ExecutorRouteStrategyEnum;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class RepositoryService {

    /*public ReturnT<Map<String, Object>> chartInfo(Date startDate, Date endDate) {
        ReturnT<Map<String, Object>> chartInfo = xxlJobService.chartInfo(startDate, endDate);
        return chartInfo;
    }*/

    public static List<String> getRouteStrategyList(){
        List<String> strategyList = new ArrayList<String>();
        ExecutorRouteStrategyEnum[] routeStrategys = ExecutorRouteStrategyEnum.values();
        for(ExecutorRouteStrategyEnum strategy : routeStrategys){
            strategyList.add(strategy.getTitle());
        }
        return strategyList;
    }

    public static List<String> getBlockStrategyList(){
        List<String> strategyList = new ArrayList<String>();
        ExecutorBlockStrategyEnum[] blockStrategys = ExecutorBlockStrategyEnum.values();
        for(ExecutorBlockStrategyEnum strategy : blockStrategys){
            strategyList.add(strategy.getTitle());
        }
        return strategyList;
    }

    public static List<String> getGlueTypeList(){
        List<String> typeList = new ArrayList<String>();
        GlueTypeEnum[] glueTypes = GlueTypeEnum.values();
        for(GlueTypeEnum type : glueTypes){
            typeList.add(type.getDesc());
        }
        return typeList;
    }
}
