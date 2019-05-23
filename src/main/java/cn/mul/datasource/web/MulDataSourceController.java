package cn.mul.datasource.web;

import cn.mul.datasource.config.DynamicDataSource;
import cn.mul.datasource.dao.TenantParamDao;
import cn.mul.datasource.dao.UserDao;
import cn.mul.datasource.entity.TenantParam;
import cn.mul.datasource.entity.User;
import cn.mul.datasource.model.req.MulDataSourceReq;
import cn.mul.datasource.config.DynamicDataSourceContextHolder;
import cn.mul.datasource.service.InitTenantDbService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author: cowboy
 * @Date: 2019/5/22 11:51
 * @Description:
 */
@Api(value = "测试动态多数据源",description = "测试动态多数据源")
@RestController
@RequestMapping("/api/mulDataSourceController")
public class MulDataSourceController {

    Logger logger= LoggerFactory.getLogger(MulDataSourceController.class);

    @Autowired
    UserDao userDao;

    @Autowired
    TenantParamDao tenantParamDao;

    @Autowired
    InitTenantDbService initTenantDbService;



    @ApiOperation(value = "测试数据源",notes = "测试数据源")
    @PostMapping("/getAllByCondition")
    public ResponseEntity<List<User>> getAllByCondition(@RequestBody MulDataSourceReq req) {
        /**
         * 根据租户码，设置数据源来选择使用那个数据源
         */
        logger.info("数据源是否存在:{}",initTenantDbService.isExistDataSource(req.getDbName()));
        DynamicDataSourceContextHolder.setDataSourceType(req.getDbName());
        return ResponseEntity.ok().body(userDao.findAllUser());
    }

    @ApiOperation(value = "添加租户",notes = "添加租户")
    @PostMapping("/createTenantDb")
    public ResponseEntity createTenantDb(@RequestBody TenantParam req) throws Exception{
        /**
         * 创建数据库及数据源
         */
        initTenantDbService.initDataSource(req);
        tenantParamDao.insertTenant(req);
        return ResponseEntity.ok().body(null);
    }
}
