package anlian;

import java.math.BigDecimal;
import java.util.Map;

import may.yuntian.wordgenerate.service.WordGenerateService;
import org.apache.commons.collections.map.HashedMap;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import may.yuntian.AnlianApplication;
//import may.yuntian.anlian.service.ProjectService;
import may.yuntian.common.utils.PageUtils;

@SpringBootTest(classes = AnlianApplication.class)
class ProjectServiceTest {

	@Autowired
	private WordGenerateService wordGenerateService;
	
	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	final void testQueryOneLayeredPage() {
		long startTime = System.currentTimeMillis();
//		System.out.println("projectService="+projectService);
		Map<String, Object> params = new HashedMap();
//    	params.put("projectId", "604");
    	params.put("id", "24056");
    	params.put("page", "1");
    	params.put("limit", "5000");
    	
    	System.out.println("pageUtils params："+params.toString());

//        Map<String,Map<String,String>> map = wordGenerateService.getWordType();
//        System.out.println("map = " + map);
        
        
//    	calculationAmount(24056L);
//    	calculationAmount(1934L);
    	
//		PageUtils pageUtils = projectService.queryPageByAccount(params,false);
//		PageUtils pageUtils = projectService.queryChildPage(params);
		//PageUtils pageUtils = projectService.queryOneLayeredPage(params);
//		System.out.println("pageUtils："+pageUtils.getTotalCount());
//		pageUtils.getList().forEach(System.out::println);
		long endTime = System.currentTimeMillis();
    	System.out.println("性能总耗时计算结果："+(endTime-startTime)+"ms");
    	//原性能
//    	性能耗时计算1：5074ms
//    	性能耗时计算2：69331ms
//    	pageUtils：24058
//    	性能总耗时计算结果：74434ms
    	
    	//第一次优化性能
//    	性能耗时计算1：2793ms   性能耗时计算1：3524ms 性能耗时计算1：2395ms
//    	性能耗时计算2：0ms
//    	pageUtils：24058
//    	性能总耗时计算结果：2830ms
	}
	
//	private void calculationAmount(Long id) {
//		ProjectEntity project = projectService.getById(id);
//    	System.out.println(id+", 查询到的数据："+project.toString());
//
//    	System.out.println("查询到的数据判断："+project.getTotalMoney()+" | "+project.getTotalMoney().compareTo(BigDecimal.ZERO));
//    	BigDecimal commissionRatio = newCommission BigDecimal(0);//佣金比例,佣金/总金额  "0.00%"
//    	if(project.getTotalMoney()!=null && project.getTotalMoney().compareTo(BigDecimal.ZERO)>0) {
//			commissionRatio = project.getCommission().divide(project.getTotalMoney(), 4, BigDecimal.ROUND_HALF_UP).multiply(newCommission BigDecimal(100));//佣金比例,佣金/总金额  "0.00%"
//		}
//    	System.out.println("佣金比例数据："+commissionRatio);
//	}

}
