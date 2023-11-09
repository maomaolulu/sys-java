package may.yuntian.common.config;

import may.yuntian.modules.sys.oauth2.OAuth2Filter;
import may.yuntian.modules.sys.oauth2.OAuth2Realm;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 *
 * @author MaYong
 * @email mustang.ma@qq.com
 * @date 2017年10月16日
 */
@Configuration
public class ShiroConfig {

    @Bean("sessionManager")
    public SessionManager sessionManager(){
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionIdCookieEnabled(true);
        return sessionManager;
    }

    @Bean("securityManager")
    public SecurityManager securityManager(OAuth2Realm oAuth2Realm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(oAuth2Realm);
        securityManager.setSessionManager(sessionManager);

        return securityManager;
    }

    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shirFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        //oauth过滤
        Map<String, Filter> filters = new HashMap<>();
        filters.put("oauth2", new OAuth2Filter());
        shiroFilter.setFilters(filters);

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/druid/**", "anon");
        filterMap.put("/app/**", "anon");
        filterMap.put("/anlian/project/testLink", "anon");//测试访问路径
        filterMap.put("/anlian/project/getInfomation", "anon");//OA访问项目金额信息接口
        filterMap.put("/anlian/project/updateProAmount", "anon");//OA访问项目金额信息接口
        filterMap.put("/anlianwage/performanceAllocation/caiyangPjCommission", "anon");//评价采样提成
        filterMap.put("/sys/validation", "anon");//验证用户是否存在
        filterMap.put("/anlian/mail/forgetPassword", "anon");//忘记密码
        filterMap.put("/anlian/mail/resetPassWord", "anon");//重置密码
        filterMap.put("/sys/noPasswordLogin", "anon");//免密登录

        filterMap.put("/ping", "anon");//测试数据库连接
        filterMap.put("/app/appVersion/getNewestAppVersion", "anon");//获取pad小程序版本
//        filterMap.put("/generateWord/**", "anon");//测试

        filterMap.put("/anlian/uploadFile/downLoadPdf", "anon");//下载pdf
        filterMap.put("/anlian/wecommessage/save","anon");//企业微信保存
        ///
        filterMap.put("/sys/user/updatePassword", "anon");//修改密码
        filterMap.put("/sys/login", "anon");
        //运营系统对外暴露登录接口
        filterMap.put("/anlian/interface/sys/out/login", "anon");
        filterMap.put("/oa/projects/**", "anon");
        filterMap.put("/sys/dynamicCodeLogin", "anon");
        filterMap.put("/oa/advance/**", "anon");
//        filterMap.put("/sys/logout", "anon");
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/swagger-ui/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/swagger-resources/**", "anon");
        filterMap.put("/captcha.jpg", "anon");
        // sf-路由回调接口对外免token访问
        filterMap.put("/external/sf/order/route/push", "anon");
        // sf-订单状态回调接口对外免token访问
        filterMap.put("/external/sf/order/status/back", "anon");
        filterMap.put("/statics/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/**", "oauth2");


        shiroFilter.setFilterChainDefinitionMap(filterMap);

        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

}
