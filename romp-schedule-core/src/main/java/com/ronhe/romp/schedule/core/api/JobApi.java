package com.ronhe.romp.schedule.core.api;

public class JobApi {

    private static RepositoryService repositoryService = new RepositoryService();
    private static OperateService operateService = new OperateService();

    public static RepositoryService repositoryService(){
        return repositoryService;
    }

    public static OperateService operateService(){
        return operateService;
    }





}
