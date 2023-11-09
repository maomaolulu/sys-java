package may.yuntian.norepeatsubmit.aspect;

import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import may.yuntian.exception.custom.RepeatSubmitException;
import may.yuntian.norepeatsubmit.annotion.RepeatSubmit;
import may.yuntian.norepeatsubmit.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @Description
 * @Date 2023/4/19 16:43
 * @Author maoly
 **/
@Aspect
@Component
@Slf4j
public class RepeatSubmitAspect {

    @Autowired
    private RedisService redisService;

    @Pointcut("@annotation(may.yuntian.norepeatsubmit.annotion.RepeatSubmit)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws RepeatSubmitException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        RepeatSubmit repeatSubmit = method.getAnnotation(RepeatSubmit.class);
        String redisKey = getLockKey(signature,request);
        //  检查表单是否重复提交
        checkIsFormResubmit(redisKey);
        //  设置表单UUID到Redis
        addFormTagToRedis(repeatSubmit, redisKey);
        //  执行方法
        Object proceed = null;
        try {
            proceed = point.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return proceed;
    }

    private void checkIsFormResubmit(String key) throws RepeatSubmitException {
        if (redisService.hasKey(key)) {
            throw new RepeatSubmitException("请勿重复提交！");
        }
    }

    private void addFormTagToRedis(RepeatSubmit repeatSubmit, String redisKey) {
        if (repeatSubmit != null) {
            redisService.set(redisKey, IdUtil.simpleUUID(), repeatSubmit.expire());
        }
    }

    private String getToken(HttpServletRequest request) {
        //从header中获取token
        String token = request.getHeader("token");
        //如果header中不存在token，则从参数中获取token
        if(StringUtils.isBlank(token)){
            token = request.getParameter("token");
        }
        return token;
    }

    private String getLockKey(MethodSignature methodSignature,HttpServletRequest request) {
        Method method = methodSignature.getMethod();
        String methodName = methodSignature.getName();
        String className = method.getDeclaringClass().getSimpleName();
        int paramCount = method.getParameterCount();
        String uri = request.getRequestURI();
        String[] parameterNames = methodSignature.getParameterNames();
        StringBuffer buffer = new StringBuffer();
        if(parameterNames.length > 0){
            for (int index = 0; index < parameterNames.length; index++) {
                buffer.append(parameterNames[index] + "_");
            }
        }
        String redisKey = uri + "_" +className + "_" + methodName + "_" + paramCount + "_" + buffer + getToken(request);
        return redisKey;
    }
}