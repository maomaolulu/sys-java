package may.yuntian.external.express.sf.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.external.express.sf.pojo.entity.ContactInfo;
import may.yuntian.external.express.sf.service.ContactInfoService;
import may.yuntian.untils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * @author: liyongqiang
 * @create: 2023-05-23 16:51
 */
@RestController
@Api(tags="SF-寄方信息")
@RequestMapping("/external/sf/contact")
public class ContactInfoController {

    @Autowired
    private ContactInfoService contactInfoService;


    /**
     * 寄方信息-通讯录
     */
    @ApiOperation("寄方信息-通讯录")
    @GetMapping("/sendAddressBook")
    public Result sendAddressBook(ContactInfo contactInfo) {
        return Result.resultData(contactInfoService.getSendAddressBook(contactInfo));
    }


    /**
     * 新增-寄件人
     */
    @PostMapping("/add")
    @ApiOperation("新增-寄件人")
    public Result addReceiveSendInfo(@RequestBody ContactInfo contactInfo) {
        int row = contactInfoService.addReceiveSendInfo(contactInfo);
        return row > 0 ? Result.ok("新增成功！") : Result.error("新增失败！");
    }


    /**
     * 删除-寄件人
     */
    @DeleteMapping("/{id}")
    @ApiOperation("删除-寄件人")
    public Result deleteContactInfoById(@PathVariable Long id) {
        return contactInfoService.deleteContactInfoById(id) >= 0 ? Result.ok("删除成功！") : Result.error("删除失败！");
    }


}
