package cn.mul.datasource.service;

import cn.mul.datasource.config.DynamicDataSource;
import cn.mul.datasource.entity.TenantParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: cowboy
 * @Date: 2019/5/22 14:33
 * @Description: 动态添加租户数据源及相关的数据库、表
 */
@Service("initTenantDbService")
public class InitTenantDbService  {
    Logger logger= LoggerFactory.getLogger(InitTenantDbService.class);

    @Autowired
    Environment environment;

    @Autowired
    DynamicDataSource dynamicDataSource;


    /**
     * 创建数据源
     * @param tenantParam
     */
    public void initDataSource(TenantParam tenantParam) throws Exception{
        Map<String, Object> dsMap = new HashMap<>(5);
        dsMap.put("driver-class-name", environment.getProperty("spring.datasource.driver-class-name"));
        dsMap.put("type",environment.getProperty("spring.datasource.type"));
        dsMap.put("url", environment.getProperty("spring.datasource.url")
                .replace("{0}",tenantParam.getTenantDbIp())
                .replace("{1}",tenantParam.getTenantDbName()));
        dsMap.put("username",tenantParam.getTenantDbAccount());
        dsMap.put("password", tenantParam.getTenantDbPsw());
        dsMap.put("dbCode",tenantParam.getTenantCode());
        dsMap.put("dbName",tenantParam.getTenantDbName());
        dynamicDataSource.addDataSource(dsMap);
        logger.info("创建数据源:{} 成功",tenantParam.getTenantCode());
    }

    /**
     * 检查数据源是否存在
     * @param key
     * @return
     */
    public  boolean isExistDataSource(String key){
        return dynamicDataSource.isExistDataSource(key);
    }
}

