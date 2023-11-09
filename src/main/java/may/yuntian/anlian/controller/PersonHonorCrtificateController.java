package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.PersonHonorCrtificateEntity;
import may.yuntian.anlian.service.PersonHonorCrtificateService;
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
 * 人员荣誉证书管理
 * WEB请求处理层
 * @author ZhangHao
 * @data 2021-06-04
 */
@RestController
@Api(tags="人员荣誉证书")
@RequestMapping("anlian/personHonorCrtificate")
public class PersonHonorCrtificateController {

	@Autowired
	private PersonHonorCrtificateService personHonorCrtificateService;
    @Autowired
    private AlRedisUntil alRedisUntil;


	@GetMapping("/info/{id}")
	@ApiOperation("根据ID查询详情")
	@RequiresPermissions("anlian:personHonorCrtificate:list")
	public R info(@PathVariable("id") Long id) {

		List<PersonHonorCrtificateEntity> list = personHonorCrtificateService.selectByUserIdList(id);

		return R.ok().put("list", list);

	}

	//新增
	@PostMapping("/save")
	@SysLog("新增人员荣誉证书记录")
	@ApiOperation("新增人员荣誉证书记录")
	@RequiresPermissions("anlian:personHonorCrtificate:save")
	public R save(@RequestBody PersonHonorCrtificateEntity personHonorCrtificateEntity){
        if (StringUtils.isNotBlank(personHonorCrtificateEntity.getPath())) {
            Object o = alRedisUntil.hget("anlian-java",personHonorCrtificateEntity.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",personHonorCrtificateEntity.getPath());
            }
        }
		personHonorCrtificateService.save(personHonorCrtificateEntity);
		return R.ok();
	}

	//批量新增
	@PostMapping("/saveBatch")
	@SysLog("批量人员荣誉证书记录")
	@ApiOperation("批量新增人员荣誉证书记录")
	@RequiresPermissions("anlian:personHonorCrtificate:save")
	public R saveBatch(@RequestBody List<PersonHonorCrtificateEntity> personHonorCrtificateEntitys){
        for (PersonHonorCrtificateEntity personHonorCrtificateEntity:personHonorCrtificateEntitys){
            if (StringUtils.isNotBlank(personHonorCrtificateEntity.getPath())) {
                Object o = alRedisUntil.hget("anlian-java",personHonorCrtificateEntity.getPath());
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",personHonorCrtificateEntity.getPath());
                }
            }
        }
		personHonorCrtificateService.saveBatch(personHonorCrtificateEntitys);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@SysLog("修改人员荣誉证书记录")
	@ApiOperation("修改人员荣誉证书记录")
	@RequiresPermissions("anlian:personHonorCrtificate:update")
	public R update(@RequestBody PersonHonorCrtificateEntity personHonorCrtificateEntity){
        PersonHonorCrtificateEntity oldPersonHonorCrtificate = personHonorCrtificateService.getById(personHonorCrtificateEntity.getId());
        if (StringUtils.isNotBlank(oldPersonHonorCrtificate.getPath()) && !oldPersonHonorCrtificate.getPath().equals(personHonorCrtificateEntity.getPath())){
            MinioUtil.remove(oldPersonHonorCrtificate.getPath());
        }

        if (StringUtils.isNotBlank(personHonorCrtificateEntity.getPath()) && StringUtils.isNotBlank(oldPersonHonorCrtificate.getPath())  && !oldPersonHonorCrtificate.getPath().equals(personHonorCrtificateEntity.getPath())) {
            Object o = alRedisUntil.hget("anlian-java",personHonorCrtificateEntity.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",personHonorCrtificateEntity.getPath());
            }
        }
		personHonorCrtificateService.updateById(personHonorCrtificateEntity);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@SysLog("删除人员荣誉证书记录")
	@ApiOperation("删除人员荣誉证书记录")
	@RequiresPermissions("anlian:personHonorCrtificate:delete")
	public R delete(@RequestBody Long[] ids) {
        List<PersonHonorCrtificateEntity> personHonorCrtificateEntities = personHonorCrtificateService.listByIds(Arrays.asList(ids));
        for (PersonHonorCrtificateEntity personHonorCrtificateEntity:personHonorCrtificateEntities){
            if (StringUtils.isNotBlank(personHonorCrtificateEntity.getPath())) {
                Object o = alRedisUntil.hget("anlian-java",personHonorCrtificateEntity.getPath());
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",personHonorCrtificateEntity.getPath());
                }
            }
        }

		personHonorCrtificateService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}


}

