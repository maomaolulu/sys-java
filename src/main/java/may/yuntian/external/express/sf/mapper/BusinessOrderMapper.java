package may.yuntian.external.express.sf.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import may.yuntian.external.express.sf.pojo.entity.BusinessOrder;
import may.yuntian.external.express.sf.pojo.vo.OrderListVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-05-25 11:22
 */
@Mapper
public interface BusinessOrderMapper extends BaseMapper<BusinessOrder> {

    /**
     * 订单列表vos
     * @param wrapper 条件
     * @return list
     */
    @Select("select ebo.id, ebo.contract_id, ebo.order_id, ebo.waybill_no, ebo.`status`, IF(ebo.`status` >= 2,2,ebo.`status`) as orderStatus, ebo.`status` as waybillStatus, ebo.`name`, ebo.count, ebo.pay_method, ebo.parcel_qty, ebo.express_type_id, ebo.logistics_company, ebo.order_type, ebo.order_create_time, ebo.generate_waybill_time, ebo.create_time, ebo.order_cancel_time, ebo.order_collection_time, ebo.order_sign_time, ebo.receive_info_id, ebo.send_info_id, ebo.total_weight, ebo.remark, \n" +
            "eci.contact as receiveContact, eci.mobile as receiveMobile, eci.telephone as receiveTelephone, eci.company as receiveCompany, eci.province as receiveProvince, eci.city as receiveCity, eci.county as receiveCounty, eci.address as receiveAddress,\n" +
            "t.contact as sendContact, t.mobile as sendMobile, t.telephone as sendTelephone, t.company as sendCompany, t.province as sendProvince, t.city as sendCity, t.county as sendCounty, t.address as sendAddress\n" +
            "from exp_business_order as ebo\n" +
            "left join exp_contact_info as eci on ebo.receive_info_id = eci.id\n" +
            "left join exp_contact_info as t on ebo.send_info_id = t.id\n" +
            "${ew.customSqlSegment} ")
    public List<OrderListVo> getOrderListVos(@Param(Constants.WRAPPER) QueryWrapper wrapper);

}
