package com.fzx.dianping.dal;

import com.fzx.dianping.model.SellerModel;

import java.util.List;

public interface SellerModelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_seller
     *
     * @mbg.generated Fri Feb 14 20:30:47 CST 2020
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_seller
     *
     * @mbg.generated Fri Feb 14 20:30:47 CST 2020
     */
    int insert(SellerModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_seller
     *
     * @mbg.generated Fri Feb 14 20:30:47 CST 2020
     */
    int insertSelective(SellerModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_seller
     *
     * @mbg.generated Fri Feb 14 20:30:47 CST 2020
     */
    SellerModel selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_seller
     *
     * @mbg.generated Fri Feb 14 20:30:47 CST 2020
     */
    int updateByPrimaryKeySelective(SellerModel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table t_seller
     *
     * @mbg.generated Fri Feb 14 20:30:47 CST 2020
     */
    int updateByPrimaryKey(SellerModel record);

    List<SellerModel> selectAll();

    Integer countAllSeller();
}