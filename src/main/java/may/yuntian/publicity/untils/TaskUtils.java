package may.yuntian.publicity.untils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import may.yuntian.anlian.entity.CompanyEntity;
import may.yuntian.anlian.mapper.CompanyMapper;
import may.yuntian.external.oa.entity.CustomAdvanceTaskEntity;
import may.yuntian.external.oa.mapper.CustomAdvanceTaskMapper;
import may.yuntian.minio.utils.MinioUtil;
import may.yuntian.publicity.service.PublicityService;
import may.yuntian.untils.AlRedisUntil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component // 把此类托管给 Spring，不能省略
public class TaskUtils {
    @Autowired
    private PublicityService publicityService;
    @Autowired
    private AlRedisUntil alRedisUntil;
    @Autowired
    private CompanyMapper companyMapper;
    @Autowired
    private CustomAdvanceTaskMapper customAdvanceTaskMapper;

    @Scheduled(cron = "0 0 14 * * ?") // cron表达式：每天下午两点 执行
    public void automaticPublicity(){
        String ip = null;
        try {
            ip = InetAddress.getLocalHost().getHostAddress();
        }catch (Exception e){
            e.printStackTrace();
        }
        if (ip.equals("47.111.249.220")||ip.equals("47.114.182.181")||ip.equals("127.0.0.1")){
            publicityService.automaticPublicity();
        }
    }

    @Scheduled(cron = "0 0 02 * * ?")
    public Map<Object, String> minioFilePath(){

        Map<Object, Object> hmget = alRedisUntil.hmget("anlian-java");
        Map<Object, String> collect = hmget.entrySet().stream().filter(i -> DateUtil.compare(DateUtil.yesterday(), DateUtil.parseDate(String.valueOf(i.getValue()))) > 0).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
//        Map<Object, String> collect = hmget.entrySet().stream().filter(i -> DateUtil.compare(DateUtil.parseDate("2023-08-05"), DateUtil.parseDate(String.valueOf(i.getValue()))) > 0).collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().toString()));
//        System.out.println("hmget = " + hmget);
//        System.out.println("collect = " + collect);
        Set<Object> keys = collect.keySet();
//        System.out.println("keys = " + keys);
        if(keys != null && !keys.isEmpty()) {
            for (Object o:keys){
                MinioUtil.remove(String.valueOf(o));
                alRedisUntil.hdel("anlian-java",o);
            }
        }

        return collect;
    }
//
//
    // 添加定时任务
//    @Scheduled(cron = "0 0/2 * * * ?") // cron表达式：每过两分钟 执行
//    public void doTask(){
//        InetAddress ip = null;
//        try {
//            ip = InetAddress.getLocalHost();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//        if (ip.equals("47.111.249.220")||ip.equals("47.114.182.181")){
//            System.out.println("ip正确~~~");
//            System.out.println("我是定时任务~"+"ip="+ip);
//        }else {
//            System.out.println("我是定时任务~"+"ip="+ip);
//        }
//
//    }
    /**
     * 定时释放客户至公海
     * 1.首次跟进至今2年,若没有跟进任务在进行,设置最新的跟进任务状态为跟进结束,释放至客户公海.
     * 2.最近跟进至今3个月,没有再跟新这个时间,设置最新的跟进任务状态为跟进结束,释放至客户公海.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional(rollbackFor = Exception.class)
    public void cleanCompany(){
        Date twoYearBefore = DateUtil.offset(new Date(), DateField.YEAR, -2);
        Date threeMonthBefore = DateUtil.offsetMonth(new Date(), -3);
        QueryWrapper<Object> wrapper1 = new QueryWrapper<>();
        QueryWrapper<Object> wrapper2 = new QueryWrapper<>();
        wrapper1.isNotNull("cat1.advance_first_time");
        wrapper1.eq("tc.if_has_finished", 0);
        wrapper1.le("cat1.advance_first_time", twoYearBefore);
        wrapper2.isNotNull("cat2.advance_last_time");
        wrapper2.eq("tc.if_has_finished", 1);
        wrapper2.le("cat2.advance_last_time", threeMonthBefore);
        List<Long> list1 = companyMapper.selectToBeOpenData(wrapper1);
        List<Long> list2 = companyMapper.selectToBeOpenData(wrapper2);
        for1 : for (Long id : list1){
            QueryWrapper<CustomAdvanceTaskEntity> query = new QueryWrapper<>();
            query.eq("company_id", id);
            query.orderByDesc("id");
            List<CustomAdvanceTaskEntity> tasks = customAdvanceTaskMapper.selectList(query);
            if (!tasks.isEmpty()) {
                //所有跟进任务都处于结束状态
                for (CustomAdvanceTaskEntity e : tasks) {
                    if (e.getBusinessStatus() != 5) {
                        continue for1;
                    }
                }
            }
            setPublicCompany(id);
        }

        for1 : for (Long id : list2){
            QueryWrapper<CustomAdvanceTaskEntity> query = new QueryWrapper<>();
            query.eq("company_id", id);
            query.orderByDesc("id");
            List<CustomAdvanceTaskEntity> tasks = customAdvanceTaskMapper.selectList(query);
            for (CustomAdvanceTaskEntity task :tasks){
                if (task.getAdvanceLastTime() != null){
                    if (task.getAdvanceLastTime().getTime() < threeMonthBefore.getTime()) {
                        // 公海来的客户 任务上次跟进跟当前时间差3个月以上则结束任务
                        task.setBusinessStatus(5);
                        customAdvanceTaskMapper.updateById(task);
                    }
                }
            }
        }

    }

    /**
     * 客户隶属设置为公司
     */
    private void setPublicCompany(Long companyId){
        CompanyEntity update = new CompanyEntity();
        update.setId(companyId);
        update.setIfHasFinished(1);
        companyMapper.updateById(update);
    }
}
