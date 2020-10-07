package com.fzx.dianping.dal;

import com.fzx.dianping.model.UserModel;
import org.apache.ibatis.annotations.Param;

public interface UserModelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user
     *
     * @mbg.generated Thu Jan 30 19:34:47 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user
     *
     * @mbg.generated Thu Jan 30 19:34:47 CST 2020
     */
    int insert(UserModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user
     *
     * @mbg.generated Thu Jan 30 19:34:47 CST 2020
     */
    int insertSelective(UserModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user
     *
     * @mbg.generated Thu Jan 30 19:34:47 CST 2020
     */
    UserModel selectByPrimaryKey(Integer id);

    UserModel selectByTelphoneAndPassword(@Param("telphone") String telphone, @Param("password") String password);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user
     *
     * @mbg.generated Thu Jan 30 19:34:47 CST 2020
     */
    int updateByPrimaryKeySelective(UserModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_user
     *
     * @mbg.generated Thu Jan 30 19:34:47 CST 2020
     */
    int updateByPrimaryKey(UserModel record);

    Integer countAllUser();
}