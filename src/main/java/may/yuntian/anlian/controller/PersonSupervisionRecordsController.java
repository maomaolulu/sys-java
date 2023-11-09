package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.PersonSupervisionRecordsEntity;
import may.yuntian.anlian.service.PersonSupervisionRecordsService;
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
 * 人员质量监督记录
 * WEB请求处理层
 * @author ZhangHao
 * @data 2021-06-04
 */
@RestController
@Api(tags="人员质量监督记录")
@RequestMapping("anlian/personSupervisionRecords")
public class PersonSupervisionRecordsController {

	@Autowired
	private PersonSupervisionRecordsService personSupervisionRecordsService;
    @Autowired
    private AlRedisUntil alRedisUntil;



	@GetMapping("/info/{id}")
	@ApiOperation("根据ID查询详情")
	@RequiresPermissions("anlian:personHonorCrtificate:list")
	public R info(@PathVariable("id") Long id) {

		List<PersonSupervisionRecordsEntity> list = personSupervisionRecordsService.selectByUserIdList(id);

		return R.ok().put("list", list);

	}

	//新增
	@PostMapping("/save")
	@SysLog("新增人员质量监督记录记录")
	@ApiOperation("新增人员质量监督记录记录")
	@RequiresPermissions("anlian:personHonorCrtificate:save")
	public R save(@RequestBody PersonSupervisionRecordsEntity personSupervisionRecordsEntity){
        if (StringUtils.isNotBlank(personSupervisionRecordsEntity.getPath())){
            Object o = alRedisUntil.hget("anlian-java",personSupervisionRecordsEntity.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",personSupervisionRecordsEntity.getPath());
            }
        }
		personSupervisionRecordsService.save(personSupervisionRecordsEntity);
		return R.ok();
	}

	//批量新增
	@PostMapping("/saveBatch")
	@SysLog("批量新增人员质量监督记录记录")
	@ApiOperation("批量新增人员质量监督记录记录")
	@RequiresPermissions("anlian:personHonorCrtificate:save")
	public R saveBatch(@RequestBody List<PersonSupervisionRecordsEntity> personSupervisionRecordsEntity){
        for (PersonSupervisionRecordsEntity supervisionRecordsEntity:personSupervisionRecordsEntity){
            if (StringUtils.isNotBlank(supervisionRecordsEntity.getPath())){
                Object o = alRedisUntil.hget("anlian-java",supervisionRecordsEntity.getPath());
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",supervisionRecordsEntity.getPath());
                }
            }
        }
		personSupervisionRecordsService.saveBatch(personSupervisionRecordsEntity);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@SysLog("修改人员质量监督记录记录")
	@ApiOperation("修改人员质量监督记录记录")
	@RequiresPermissions("anlian:personHonorCrtificate:update")
	public R update(@RequestBody PersonSupervisionRecordsEntity personSupervisionRecordsEntity){
        PersonSupervisionRecordsEntity oldPersonSupervisionRecords = personSupervisionRecordsService.getById(personSupervisionRecordsEntity.getId());
        if (StringUtils.isNotBlank(oldPersonSupervisionRecords.getPath()) && !oldPersonSupervisionRecords.getPath().equals(personSupervisionRecordsEntity.getPath())){
            MinioUtil.remove(oldPersonSupervisionRecords.getPath());
        }
        if (StringUtils.isNotBlank(personSupervisionRecordsEntity.getPath()) && StringUtils.isNotBlank(oldPersonSupervisionRecords.getPath()) && !oldPersonSupervisionRecords.getPath().equals(personSupervisionRecordsEntity.getPath())){
            Object o = alRedisUntil.hget("anlian-java",personSupervisionRecordsEntity.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",personSupervisionRecordsEntity.getPath());
            }
        }
		personSupervisionRecordsService.updateById(personSupervisionRecordsEntity);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@SysLog("删除人员质量监督记录记录")
	@ApiOperation("删除人员质量监督记录记录")
	@RequiresPermissions("anlian:personHonorCrtificate:delete")
	public R delete(@RequestBody Long[] ids) {
        List<PersonSupervisionRecordsEntity> personSupervisionRecordsEntity = personSupervisionRecordsService.listByIds(Arrays.asList(ids));
        for (PersonSupervisionRecordsEntity supervisionRecordsEntity:personSupervisionRecordsEntity){
            if (StringUtils.isNotBlank(supervisionRecordsEntity.getPath())){
                Object o = alRedisUntil.hget("anlian-java",supervisionRecordsEntity.getPath());
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",supervisionRecordsEntity.getPath());
                }
            }
        }
		personSupervisionRecordsService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}


}

