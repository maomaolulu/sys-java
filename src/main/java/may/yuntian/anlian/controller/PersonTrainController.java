package may.yuntian.anlian.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import may.yuntian.anlian.entity.PersonTrainEntity;
import may.yuntian.anlian.service.PersonTrainService;
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
 * 人员培训
 * WEB请求处理层
 * @author ZhangHao
 * @data 2021-06-04
 */
@RestController
@Api(tags="人员培训")
@RequestMapping("anlian/personTrain")
public class PersonTrainController {

	@Autowired
	private PersonTrainService personTrainService;
    @Autowired
    private AlRedisUntil alRedisUntil;




	@GetMapping("/info/{id}")
	@ApiOperation("根据ID查询详情")
	@RequiresPermissions("anlian:personHonorCrtificate:list")
	public R info(@PathVariable("id") Long id) {

		List<PersonTrainEntity> list = personTrainService.selectByUserIdList(id);

		return R.ok().put("list", list);

	}

	//新增
	@PostMapping("/save")
	@SysLog("新增人员培训记录")
	@ApiOperation("新增人员培训记录")
	@RequiresPermissions("anlian:personHonorCrtificate:save")
	public R save(@RequestBody PersonTrainEntity personTrainEntity){
        if (StringUtils.isNotBlank(personTrainEntity.getPath())){
            Object o = alRedisUntil.hget("anlian-java",personTrainEntity.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",personTrainEntity.getPath());
            }
        }
		personTrainService.save(personTrainEntity);
		return R.ok();
	}

	//批量新增
	@PostMapping("/saveBatch")
	@SysLog("批量新增人员培训记录")
	@ApiOperation("批量新增人员培训记录")
	@RequiresPermissions("anlian:personHonorCrtificate:save")
	public R saveBatch(@RequestBody List<PersonTrainEntity> personTrainEntitys){
        for (PersonTrainEntity personTrainEntity:personTrainEntitys){
            if (StringUtils.isNotBlank(personTrainEntity.getPath())){
                Object o = alRedisUntil.hget("anlian-java",personTrainEntity.getPath());
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",personTrainEntity.getPath());
                }
            }
        }
		personTrainService.saveBatch(personTrainEntitys);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@PostMapping("/update")
	@SysLog("修改人员培训记录")
	@ApiOperation("修改人员培训记录")
	@RequiresPermissions("anlian:personHonorCrtificate:update")
	public R update(@RequestBody PersonTrainEntity personTrainEntity){
        PersonTrainEntity oldPersonTrain = personTrainService.getById(personTrainEntity.getId());
        if (StringUtils.isNotBlank(oldPersonTrain.getPath()) && !oldPersonTrain.getPath().equals(personTrainEntity.getPath())){
            MinioUtil.remove(oldPersonTrain.getPath());
        }
        if (StringUtils.isNotBlank(personTrainEntity.getPath()) && StringUtils.isNotBlank(oldPersonTrain.getPath())  && !oldPersonTrain.getPath().equals(personTrainEntity.getPath())){
            Object o = alRedisUntil.hget("anlian-java",personTrainEntity.getPath());
            if (null!=o){
                alRedisUntil.hdel("anlian-java",personTrainEntity.getPath());
            }
        }
		personTrainService.updateById(personTrainEntity);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@PostMapping("/delete")
	@SysLog("删除人员培训记录")
	@ApiOperation("删除人员培训记录")
	@RequiresPermissions("anlian:personHonorCrtificate:delete")
	public R delete(@RequestBody Long[] ids) {
        List<PersonTrainEntity> personTrainEntities = personTrainService.listByIds(Arrays.asList(ids));
        for (PersonTrainEntity personTrainEntity:personTrainEntities){
            if (StringUtils.isNotBlank(personTrainEntity.getPath())){
                Object o = alRedisUntil.hget("anlian-java",personTrainEntity.getPath());
                if (null!=o){
                    alRedisUntil.hdel("anlian-java",personTrainEntity.getPath());
                }
            }
        }
		personTrainService.removeByIds(Arrays.asList(ids));

		return R.ok();
	}


}

