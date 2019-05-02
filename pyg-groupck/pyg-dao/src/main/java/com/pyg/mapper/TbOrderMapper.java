package com.pyg.mapper;

import com.pyg.pojo.TbOrder;
import com.pyg.pojo.TbOrderExample;
import java.util.List;

import com.pyg.vo.Orders;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;

public interface TbOrderMapper {
    int countByExample(TbOrderExample example);

    int deleteByExample(TbOrderExample example);

    int deleteByPrimaryKey(Long orderId);

    int insert(TbOrder record);

    int insertSelective(TbOrder record);

    List<TbOrder> selectByExample(TbOrderExample example);

    TbOrder selectByPrimaryKey(Long orderId);

    int updateByExampleSelective(@Param("record") TbOrder record, @Param("example") TbOrderExample example);

    int updateByExample(@Param("record") TbOrder record, @Param("example") TbOrderExample example);

    int updateByPrimaryKeySelective(TbOrder record);

    int updateByPrimaryKey(TbOrder record);

//    @ResultMap(
//            @Result(column="order_id",property="orderId"),
//            @Result(column="payment", property="payment"),
//            @Result(column="payment_type",property="paymentType"),
//            @Result(column="post_fee",property="postFee"),
//            @Result(column="status",property="status"),
//            @Result(column="create_time",property="createTime"),
//            @Result(column="update_time" jdbcType="TIMESTAMP" property="updateTime"),
//            @Result(column="payment_time" jdbcType="TIMESTAMP" property="paymentTime"),
//            @Result(column="consign_time" jdbcType="TIMESTAMP" property="consignTime"),
//            @Result(column="end_time" jdbcType="TIMESTAMP" property="endTime"),
//            @Result(column="close_time" jdbcType="TIMESTAMP" property="closeTime"),
//            @Result(column="shipping_name" jdbcType="VARCHAR" property="shippingName"),
//            @Result(column="shipping_code" jdbcType="VARCHAR" property="shippingCode"),
//            @Result(column="user_id" jdbcType="VARCHAR" property="userId"),
//            @Result(column="buyer_message" jdbcType="VARCHAR" property="buyerMessage"),
//            @Result(column="buyer_nick" jdbcType="VARCHAR" property="buyerNick"),
//            @Result(column="buyer_rate" jdbcType="VARCHAR" property="buyerRate"),
//            @Result(column="receiver_area_name" jdbcType="VARCHAR" property="receiverAreaName"),
//            @Result(column="receiver_mobile" jdbcType="VARCHAR" property="receiverMobile"),
//            @Result(column="receiver_zip_code" jdbcType="VARCHAR" property="receiverZipCode"),
//            @Result(column="receiver" jdbcType="VARCHAR" property="receiver"),
//            @Result(column="expire" jdbcType="TIMESTAMP" property="expire"),
//            @Result(column="invoice_type" jdbcType="VARCHAR" property="invoiceType"),
//            @Result(column="source_type" jdbcType="VARCHAR" property="sourceType"),
//            @Result(column="seller_id" jdbcType="VARCHAR" property="sellerId"),
//    )
//    @Select("SELECT order_id orderId,receiver FROM tb_order WHERE user_id=#{2} limit #{0},#{1}")
    List<TbOrder> findByPage(int pageNum, int pageSize, String userId);
}