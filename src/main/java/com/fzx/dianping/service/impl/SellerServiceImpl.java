package com.fzx.dianping.service.impl;
import	java.awt.Desktop.Action;

import com.fzx.dianping.common.BusinessException;
import com.fzx.dianping.common.EmBusinessError;
import com.fzx.dianping.dal.SellerModelMapper;
import com.fzx.dianping.model.SellerModel;
import com.fzx.dianping.service.SellerServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author: fanZhengxu
 * @Description:
 * @Date: Create in 20:33 2020/2/14
 */
@Service
public class SellerServiceImpl implements SellerServie {

    @Autowired
    private SellerModelMapper sellerModelMapper;

    @Override
    @Transactional
    public SellerModel create(SellerModel sellerModel) {
        sellerModel.setCreatedAt(new Date());
        sellerModel.setUpdatedAt(new Date());
        sellerModel.setRemarkScore(new BigDecimal(0));
        sellerModel.setDisabledFlag(0);
        sellerModelMapper.insertSelective(sellerModel);
        return get(sellerModel.getId());
    }

    @Override
    public SellerModel get(Integer id) {
        return sellerModelMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<SellerModel> selectAll() {
        return sellerModelMapper.selectAll();
    }

    @Override
    public SellerModel changeStatus(Integer id, Integer disabledFlag) throws BusinessException {
        SellerModel sellerModel = get(id);
        if (sellerModel == null) {
            throw new BusinessException(EmBusinessError.PARAMETER_VALIDATION_ERROR);
        }
        sellerModel.setDisabledFlag(disabledFlag);
        sellerModelMapper.updateByPrimaryKeySelective(sellerModel);
        return sellerModel;
    }

    @Override
    public Integer countAllSeller() {
        return sellerModelMapper.countAllSeller();
    }
}
