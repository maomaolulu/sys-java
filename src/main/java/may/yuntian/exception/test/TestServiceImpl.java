package may.yuntian.exception.test;

import io.swagger.models.auth.In;
import may.yuntian.anlian.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description
 * @Date 2023/4/18 13:40
 * @Author maoly
 **/
@Service("testServiceImpl")
public class TestServiceImpl implements TestService{


    @Override
    public Integer list(String id) {

        if("test".equals(id)){
            return 1 / 0 ;
        }
        String nullStr = null;
        nullStr.substring(1);
        return 2;
    }

    @Override
    public Integer list1(String id) {
        try {
            if("test".equals(id)){
                return 1 / 0 ;
            }
            return 2;
        }catch (Exception e){
            e.getStackTrace();
        }
        return null;

    }
}


