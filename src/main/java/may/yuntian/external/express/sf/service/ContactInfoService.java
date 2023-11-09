package may.yuntian.external.express.sf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.external.express.sf.pojo.entity.ContactInfo;

import java.util.List;

/**
 * @author: liyongqiang
 * @create: 2023-05-23 16:39
 */
public interface ContactInfoService extends IService<ContactInfo> {

    /**
     * 寄方信息-通信录
     * @param contactInfo 查询条件
     * @return list
     */
    List<ContactInfo> getSendAddressBook(ContactInfo contactInfo);

    /**
     * 新增-寄件人
     * @param contactInfo
     * @return
     */
    int addReceiveSendInfo(ContactInfo contactInfo);

    /**
     * 根据id删除-寄件人
     * @param id
     * @return
     */
    int deleteContactInfoById(Long id);
}
