package cn.mul.datasource.dao;

import cn.mul.datasource.entity.TenantParam;
import org.apache.ibatis.annotations.Insert;

/**
 * @author: cowboy
 * @Date: 2019/5/22 14:11
 * @Description:
 */
public interface TenantParamDao {
    @Insert({ "insert into tenant_param(id, tenant_name, tenant_code, tenant_db_ip, tenant_db_name,tenant_db_account,tenant_db_psw) " +
            "values(#{id}, #{tenantName}, #{tenantCode}, #{tenantDbIp}, #{tenantDbName},#{tenantDbAccount},#{tenantDbPsw})" })
    int insertTenant(TenantParam tenantParam);
}
