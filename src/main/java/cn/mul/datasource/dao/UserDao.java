package cn.mul.datasource.dao;

import cn.mul.datasource.entity.User;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: cowboy
 * @Date: 2019/5/22 13:48
 * @Description:
 */
public interface UserDao {
    /**
     * 查询所有用户信息
     */
    @Select("SELECT * FROM user")
    List<User> findAllUser();

}
