package com.whpe.dao.yckq;

import com.whpe.bean.LeaseVouchers;

public interface LeaseVouchersMapper {
    int deleteByPrimaryKey(String id);

    int insert(LeaseVouchers record);

    int insertSelective(LeaseVouchers record);

    LeaseVouchers selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(LeaseVouchers record);

    int updateByPrimaryKey(LeaseVouchers record);

    LeaseVouchers selectByVouchers(String vouchers);

    LeaseVouchers selectByOrderId(String orderId);
}