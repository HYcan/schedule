package com.ronhe.romp.schedule.config;

import java.util.Properties;

/**
 * 实体属性配置
 */
public abstract  class EntityConfiguration {
    private String table;
    private boolean enabled = true;
    private Properties columns;
    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Properties getColumns() {
        return columns;
    }

    public void setColumns(Properties columns) {
        this.columns = columns;
    }
}
