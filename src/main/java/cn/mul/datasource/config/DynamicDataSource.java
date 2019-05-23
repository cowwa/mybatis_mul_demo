package cn.mul.datasource.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.lang.Nullable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: cowboy
 * @Date: 2019/5/22 10:18
 * @Description: 数据源路由
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    Logger logger= LoggerFactory.getLogger(DynamicDataSource.class);
    private static Map<Object, Object> targetDataSources = new HashMap<>();
    /**
     * 代码中的determineCurrentLookupKey方法取得一个字符串
     * 该字符串将与配置文件中的相应字符串进行匹配以定位数据源，配置文件
     * @return
     */
    @Nullable
    @Override
    protected Object determineCurrentLookupKey() {
        /*
        * DynamicDataSourceContextHolder代码中使用setDataSourceType
        * 设置当前的数据源，在路由类中使用getDataSourceType进行获取，
        *  交给AbstractRoutingDataSource进行注入使用。
        */
        logger.info("当前数据源:{}", DynamicDataSourceContextHolder.getDataSourceType());
        return DynamicDataSourceContextHolder.getDataSourceType();
    }
    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        DynamicDataSource.targetDataSources = targetDataSources;
    }

    /**
     * 是否存在当前key的 DataSource
     *
     * @param key
     * @return 存在返回 true, 不存在返回 false
     */
    public  boolean isExistDataSource(String key) {
        return targetDataSources.containsKey(key);
    }

    public synchronized void addDataSource( Map<String, Object> dsMap) throws Exception{
        DataSource ds =DynamicDataSourceContextHolder.buildDataSource(dsMap);
        createTenantDb( dsMap);
        createTenantDbTable( dsMap.get("dbName").toString(), ds);
        DynamicDataSource.targetDataSources.put(dsMap.get("dbCode").toString(),ds);
        this.afterPropertiesSet();
        logger.info("动态添加数据源:{} 成功",dsMap.get("dbName"));
    }

    /**
     * 创建数据库
     * @param
     * @param
     */
    private void createTenantDb(Map<String, Object> dsMap) throws Exception{
        Connection cnn=null;
        Statement stat=null;
        //获取默认的数据连接用于数据库创建
        DataSource defaultDataSource= (DataSource) DynamicDataSource.targetDataSources.get("defaultDataSource");
        //创建数据库
        try {
            //创建连接
            cnn= defaultDataSource.getConnection();
            stat = cnn.createStatement() ;
            String cDbSql="create database ".concat(dsMap.get("dbName").toString()).concat(" DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci");
            stat.execute(cDbSql);
            logger.info("创建数据库:{} 成功",dsMap.get("dbName").toString());
        }catch (Exception e){
            logger.error("创建数据库:{} 失败",dsMap.get("dbName").toString(),e);
            throw e;
        }finally {
            if (Objects.nonNull(stat)) {
                stat.close();
            }
            if (Objects.nonNull(cnn)) {
                cnn.close();
            }
        }
    }

    /**
     * 创建基础表
     * @param
     * @param
     */
    private void createTenantDbTable(String dbName, DataSource ds) throws Exception{
        Connection cnn=null;
        Statement stat=null;
        try {
            cnn= ds.getConnection();
            stat = cnn.createStatement() ;
            String cTableSql="CREATE TABLE `user` (" +
                    "  `id` bigint(20) NOT NULL ," +
                    "  `name` varchar(255) DEFAULT NULL COMMENT '名称'," +
                    "  `remark` varchar(64) DEFAULT NULL COMMENT '备注'" +
                    ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4";
            stat.execute(cTableSql);
            logger.info("创建数据库:{} 的表：user 成功",dbName);
        }catch (Exception e){
            logger.error("创建数据库:{} 的表：user 失败",dbName,e);
            throw e;
        }finally {
            if (Objects.nonNull(stat)) {
                stat.close();
            }
            if (Objects.nonNull(cnn)) {
                cnn.close();
            }
        }
    }

}
