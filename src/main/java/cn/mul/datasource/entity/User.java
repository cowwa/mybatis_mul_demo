package cn.mul.datasource.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @author: cowboy
 * @Date: 2019/5/22 13:45
 * @Description:
 */
@Data
public class User implements Serializable {

    @Id
    @GeneratedValue(generator = "UUID",strategy = GenerationType.AUTO)
    @ApiModelProperty(notes = "主键")
    private Integer id;

    private String name;

    private String remark;

}
