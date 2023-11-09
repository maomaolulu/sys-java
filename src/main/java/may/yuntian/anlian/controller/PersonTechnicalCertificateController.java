package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.PersonTechnicalCertificateEntity;
import may.yuntian.anlian.service.PersonTechnicalCertificateService;
import may.yuntian.anlian.utils.StringUtils;
import may.yuntian.common.annotation.SysLog;
import may.yuntian.common.utils.R;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.untils.AlRedisUntil;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

/**
 * 人员技能证书
 * WEB请求处理层
 * @author ZhangHao
 * @data 2021-06-04
 */
@RestController
@Api(tags="人员技能证书")
@RequestMapping("anlian/personTechnicalCertificate")
public class PersonTechnicalCertificateController {

	@Autowired
	private PersonTechnicalCertificateService personTechnicalCertificateService;
    @Autowired
    private AlRedisUntil alRedisUntil;



	@GetMapping("/info/{id}")
	@ApiOperation("根据ID查询详情")
	@RequiresPermissions("anlian:personHonorCrtificate:list")
	public R info(@PathVariable("id") Long id) {

		List<PersonTechnicalCertificateEntity> list = personTechnicalCertificateService.selectByUserIdList(id);

		return R.ok().put("list", list);

	}

	//新增
	@PostMapping("/save")
	@SysLog("新增人员技能证书记录")
	@ApiOperation("新增人员技能证书记录")
	@RequiresPermissions("anlian:personHonorCrtificate:save")
	public R save(@RequestBody PersonTechnicalCertificateEntity personTechnicalCertificateEntity){
        if (StringUtils.isNotBlank(personTechnicalCertificateEntity.getPath())){
            Object o = alRedisUntil.hget("anlian-java",personTechnicalCertificateEntity.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",personTechnicalCertificateEntity.getPath());
            }
        }
		personTechnicalCertificateService.save(personTechnicalCertificateEntity);
		return R.ok();
	}

	//批量新增
	@PostMapping("/saveBatch")
	@SysLog("批量新增人员技能证书记录")
	@ApiOperation("批量新增人员技能证书记录")
	@RequiresPermissions("anlian:personHonorCrtificate:save")
	public R saveBatch(@RequestBody List<PersonTechnicalCertificateEntity> personTechnicalCertificateEntitys){
        for (PersonTechnicalCertificateEntity personTechnicalCertificate:personTechnicalCertificateEntitys){
            if (StringUtils.isNotBlank(personTechnicalCertificate.getPath())){
                Object o = alRedisUntil.hget("anlian-java",personTechnicalCertificate.getPath());
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",personTechnicalCertificate.getPath());
                }
            }
        }
		personTechnicalCertificateService.saveBatch(personTechnicalCertificateEntitys);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@SysLog("修改人员技能证书记录")
	@ApiOperation("修改人员技能证书记录")
	@RequiresPermissions("anlian:personHonorCrtificate:update")
	public R update(@RequestBody PersonTechnicalCertificateEntity personTechnicalCertificateEntity){
        PersonTechnicalCertificateEntity oldPersonTechnicalCertificate = personTechnicalCertificateService.getById(personTechnicalCertificateEntity.getId());
        if (StringUtils.isNotBlank(oldPersonTechnicalCertificate.getPath()) && !oldPersonTechnicalCertificate.getPath().equals(personTechnicalCertificateEntity.getPath())){
            MinioUtil.remove(oldPersonTechnicalCertificate.getPath());
        }
        if (StringUtils.isNotBlank(personTechnicalCertificateEntity.getPath()) && StringUtils.isNotBlank(oldPersonTechnicalCertificate.getPath()) && !oldPersonTechnicalCertificate.getPath().equals(personTechnicalCertificateEntity.getPath())){
            Object o = alRedisUntil.hget("anlian-java",personTechnicalCertificateEntity.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",personTechnicalCertificateEntity.getPath());
            }
        }
		personTechnicalCertificateService.updateById(personTechnicalCertificateEntity);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@SysLog("删除人员技能证书记录")
	@ApiOperation("删除人员技能证书记录")
	@RequiresPermissions("anlian:personHonorCrtificate:delete")
	public R delete(@RequestBody Long[] ids) {
        List<PersonTechnicalCertificateEntity> personTechnicalCertificateEntities = personTechnicalCertificateService.listByIds(Arrays.asList(ids));
        for (PersonTechnicalCertificateEntity personTechnicalCertificate:personTechnicalCertificateEntities){
            if (StringUtils.isNotBlank(personTechnicalCertificate.getPath())){
                Object o = alRedisUntil.hget("anlian-java",personTechnicalCertificate.getPath());
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",personTechnicalCertificate.getPath());
                }
            }
        }
		personTechnicalCertificateService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}


}

