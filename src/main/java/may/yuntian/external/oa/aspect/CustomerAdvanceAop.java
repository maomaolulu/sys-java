package may.yuntian.external.oa.aspect;

import cn.hutool.core.exceptions.StatefulException;
import may.yuntian.untils.Result;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 客户跟进切面
 *
 * @Author yrb
 * @Date 2023/8/31 15:13
 * @Version 1.0
 * @Description 客户跟进切面
 */
@Aspect
@Component
public class CustomerAdvanceAop {
    @Pointcut("execution(* may.yuntian.external.oa.controller.CustomAdvanceController.*(..))")
    public void validatePointCut() {

    }

    @Before("validatePointCut()")
    public void validate(JoinPoint joinPoint) {
        String authCode = "d537fa8d-3ec2-48a4-916e-fa834f7f2922";
        HttpServletRequest request = (HttpServletRequest) joinPoint.getArgs()[0];
        if (!authCode.equals(request.getHeader("authCode"))) {
            throw new StatefulException("系统异常");
        }
    }
}
