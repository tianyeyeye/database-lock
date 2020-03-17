package com.hello.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;

@Configuration
public class DruidConfig {

    // 绑定配置的bean
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean
    public DataSource druidDateSource(){
        return new DruidDataSource();
    }

    // 注册后台监控页面。SpringBoot 如何注册Servlet
    // 没有web.xml 的情况配置 Servlet 的方法  ： ServletRegistrationBean
    // 测试访问 /druid
    @Bean
    public ServletRegistrationBean statViewServlet() {
        // StatViewServlet 配置后台监控
        ServletRegistrationBean bean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        HashMap<String, String> map = new HashMap<>();

        //后台的登录用户名和密码
        map.put("loginUsername", "admin");
        map.put("loginPassword", "123456");
        // 访问权限
        // map.put("allow","localhost"); //只允许本机访问
        map.put("allow", ""); // 所有人都可以访问

        // deny拒绝访问
        // map.put("deny","192.168.1.1"); // ip会被拒绝访问

        bean.setInitParameters(map); //设置servlet的初始化参数
        return bean;
    }

    // 过滤器的配置，看看哪些请求需要被过滤
    // 没有web.xml 的情况配置 Filter 的方法！ FilterRegistrationBean
    @Bean
    public FilterRegistrationBean webStatFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setFilter(new WebStatFilter());

        // 配置内容
        // 配置哪些请求可以被过滤！
        HashMap<String, String> map = new HashMap<>();
        map.put("exclusions","*.js,*.css,/druid/*");
        bean.setInitParameters(map);

        bean.setUrlPatterns(Arrays.asList("/*"));
        return bean;
    }

}
