package cn.mul.datasource.config;

import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author: cowboy
 * @Date: 2019/5/23 09:23
 * @Description: 多数据源配置
 */
@Configuration
public class DataSourceConfig  {

    Logger logger= LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    Environment environment;


    /**
     * 默认数据源
     */
    private DataSource defaultDataSource;
    /**
     * 动态数据源
     */
    private Map<String, DataSource> dynamicDataSources = new HashMap<>(16);

    private static Map<Object, Object> targetDataSources = new HashMap<>();
    /**
     * 初始化默认数据源
     * @return
     */
    public void initDataSource() throws Exception{
        Map<String, Object> dsMap = new HashMap<>(16);
        dsMap.put("driver-class-name", environment.getProperty("spring.datasource.driver-class-name"));
        dsMap.put("type",environment.getProperty("spring.datasource.type"));
        dsMap.put("url", environment.getProperty("spring.datasource.url").
                replace("{0}","127.0.0.1")
                .replace("{1}","tenancy_sys_db"));
        logger.info("默认数据库URL:{}",dsMap.get("url").toString());
        dsMap.put("username", environment.getProperty("spring.datasource.username"));
        dsMap.put("password", environment.getProperty("spring.datasource.password"));
        defaultDataSource=  DynamicDataSourceContextHolder.buildDataSource(dsMap);
        logger.info("初始化默认数据库成功");
    }

    public void initDynamicDataSource() throws Exception{
        Statement stmt=null;
        ResultSet rs=null;
        try {
            // 读取其他数据源
            if(Objects.nonNull(defaultDataSource)){
                //创建连接
                Connection defConnection= defaultDataSource.getConnection();
                stmt = defConnection.createStatement() ;
                rs = stmt.executeQuery("SELECT id, tenant_name,tenant_code,tenant_db_ip,tenant_db_name,tenant_db_account,tenant_db_psw FROM TENANT_PARAM ") ;
                //动态加载多个数据源
                while (rs.next()){
                    Map<String, Object> dsMap = new HashMap<>();
                    dsMap.put("driver-class-name", environment.getProperty("spring.datasource.driver-class-name"));
                    dsMap.put("type",environment.getProperty("spring.datasource.type"));
                    dsMap.put("url", environment.getProperty("spring.datasource.url").replace("{0}",rs.getString("tenant_db_ip"))
                            .replace("{1}",rs.getString("tenant_db_name")));
                    dsMap.put("username",rs.getString("tenant_db_account"));
                    dsMap.put("password", rs.getString("tenant_db_psw"));
                    DataSource ds =DynamicDataSourceContextHolder.buildDataSource(dsMap);
                    dynamicDataSources.put(rs.getString("tenant_code"), ds);
                }
                logger.error("初始化多租户数据源成功:{}",dynamicDataSources);
            }
        }catch (Exception e){
            logger.error("初始化多租户数据源失败",e);
            throw e;
        }finally {
                if (Objects.nonNull(rs)) {
                    rs.close();
                }
                if(Objects.nonNull(stmt)){
                    stmt.close();
                }
        }
    }


    /**
     * 注册动态数据源
     *
     * @return
     */
    @Bean("dynamicDataSource")
    public DynamicDataSource dynamicDataSource() throws Exception{
        initDataSource();
        initDynamicDataSource();
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        // 设置默认数据源
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        targetDataSources.put("defaultDataSource", defaultDataSource);
        targetDataSources.putAll(dynamicDataSources);
        //设置目标数据库
        dynamicDataSource.setTargetDataSources(targetDataSources);
        logger.error("注册数据源成功");
        return dynamicDataSource;
    }

    @Bean
    public SqlSessionFactoryBean sqlSessionFactoryBean() throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        // 必须将动态数据源添加到 sqlSessionFactoryBean
        sqlSessionFactoryBean.setDataSource(dynamicDataSource());
        logger.error("初始化SqlSession成功");
        return sqlSessionFactoryBean;
    }

    /**
     * 事务管理器
     *
     * @return the platform transaction manager
     */
    @Bean
    public PlatformTransactionManager transactionManager() throws Exception{
        return new DataSourceTransactionManager(dynamicDataSource());
    }




}
