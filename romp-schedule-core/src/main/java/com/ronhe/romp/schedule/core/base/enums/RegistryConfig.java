package com.ronhe.romp.schedule.core.base.enums;

/**
 * Created by xuxueli on 17/5/10.
 */
public class RegistryConfig {

    public static final int BEAT_TIMEOUT = 30;
    public static final int DEAD_TIMEOUT = BEAT_TIMEOUT * 3;

    public enum RegistType{ EXECUTOR, ADMIN }

    public static void main(String[] args){
        System.out.print(RegistryConfig.RegistType.EXECUTOR.name());

    }
}
