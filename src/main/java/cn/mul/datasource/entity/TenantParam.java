package cn.mul.datasource.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author: cowboy
 * @Date: 2019/5/22 09:44
 * @Description:
 */
@Data
public class TenantParam implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID",strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "主键")
    private Integer id;

    @ApiModelProperty(notes = "租户名称")
    private String tenantName;

    @ApiModelProperty(notes = "租户码")
    private String tenantCode;

    @ApiModelProperty(notes = "租户数据库IP")
    private String tenantDbIp;

    @ApiModelProperty(notes = "租户数据库名称")
    private String tenantDbName;

    @ApiModelProperty(notes = "租户数据库账号")
    private String tenantDbAccount;

    @ApiModelProperty(notes = "租户数据库密码")
    private String tenantDbPsw;

}
