package may.yuntian.external.express.sf.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import may.yuntian.external.express.sf.mapper.ContactInfoMapper;
import may.yuntian.external.express.sf.pojo.entity.ContactInfo;
import may.yuntian.external.express.sf.service.ContactInfoService;
import may.yuntian.sys.utils.ShiroUtils;
import may.yuntian.untils.pageUtil2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-05-23 16:40
 */
@Service("contactInfoService")
public class ContactInfoServiceImpl extends ServiceImpl<ContactInfoMapper, ContactInfo> implements ContactInfoService {


    /**
     * 寄方信息-通讯录
     */
    @Override
    public List<ContactInfo> getSendAddressBook(ContactInfo contactInfo) {
        QueryWrapper<ContactInfo> wrapper = new QueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(contactInfo.getContact()), "contact", contactInfo.getContact());
        wrapper.like(StrUtil.isNotBlank(contactInfo.getCompany()), "company", contactInfo.getCompany());
        wrapper.like(StrUtil.isNotBlank(contactInfo.getMobile()), "mobile", contactInfo.getMobile());
        wrapper.like(StrUtil.isNotBlank(contactInfo.getTelephone()), "telephone", contactInfo.getTelephone());
        wrapper.like(StrUtil.isNotBlank(contactInfo.getRemark()), "remark", contactInfo.getRemark());
        wrapper.eq("contact_type", 1);
        wrapper.eq("send_common", 1);
        pageUtil2.startPage();
        return this.list(wrapper);
    }


    /**
     * 新增-寄件人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addReceiveSendInfo(ContactInfo contactInfo) {
        contactInfo.setContactType(1);
        contactInfo.setSendCommon(1);
        contactInfo.setCreateTime(DateUtil.dateSecond());
        contactInfo.setCreateBy(ShiroUtils.getUserEntity().getUsername());
        return baseMapper.insert(contactInfo);
    }


    /**
     * 根据id删除-寄件人
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteContactInfoById(Long id) {
        return baseMapper.deleteById(id);
    }


}
