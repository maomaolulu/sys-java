package may.yuntian.anliantest.service;

import com.baomidou.mybatisplus.extension.service.IService;
import may.yuntian.anlian.vo.CompanySurveyVo;
import may.yuntian.anliantest.entity.MetNews;
import may.yuntian.publicity.vo.PublicityInfoVo;
import may.yuntian.publicity.vo.PublictyPjInfoVo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 项目公示
 * 业务逻辑层接口
 *
 * @author zhanghao
 * @date 2022-04-14
 */
public interface MetNewsService extends IService<MetNews> {

    /**
     * 检评公示接口
     * @param publicityInfoVo
     * @return
     */
    Boolean newSaveZjMetNews(PublicityInfoVo publicityInfoVo);

    boolean savePjNews(PublictyPjInfoVo pjInfoVo);


    Boolean saveMetNews(CompanySurveyVo companySurveyVo);
    Object ces(String path) throws IOException;
    //评价公示
    void publicityEvaluate(Long projectId);


    public void getTestMetNews(String path);
}