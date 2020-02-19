package com.fun.daily.common.config;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.PostConstruct;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: cyz
 * @date: 2019/9/3 上午11:52
 * @description: DruidDataSource
 */
@Slf4j
@Configuration
@MapperScan("com.fun.daily.charging.dao")
public class DruidConfiguration {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;
    @Value("${spring.datasource.initialSize}")
    private int initialSize;
    @Value("${spring.datasource.minIdle}")
    private int minIdle;
    @Value("${spring.datasource.maxActive}")
    private int maxActive;
    @Value("${spring.datasource.maxWait}")
    private int maxWait;
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.minEvictableIdleTimeMillis}")
    private int minEvictableIdleTimeMillis;
    @Value("${spring.datasource.validationQuery}")
    private String validationQuery;
    @Value("${spring.datasource.testWhileIdle}")
    private boolean testWhileIdle;
    @Value("${spring.datasource.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${spring.datasource.testOnReturn}")
    private boolean testOnReturn;
    @Value("${spring.datasource.poolPreparedStatements}")
    private boolean poolPreparedStatements;
    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize}")
    private int maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.filters}")
    private String filters;
    @Value("${spring.datasource.connectionProperties}")
    private String connectionProperties;
    @Value("${spring.datasource.removeAbandoned}")
    private boolean removeAbandoned;
    @Value("${spring.datasource.removeAbandonedTimeout}")
    private int removeAbandonedTimeout;
    @Value("${spring.datasource.logAbandoned}")
    private boolean logAbandoned;
    @Value("${spring.datasource.validationQueryTimeout}")
    private int validationQueryTimeout;
    @Value("${spring.datasource.keepAlive}")
    private boolean keepAlive;

    /**
     * 若连接池配置了maxWait,在连接不够用的时候,会进入竞争的状态,unfair模式会存在不公平的现象,
     * 个别线程可能过了超时时间还获取不到连接,但是这种方式的并发性能比较好;若没配置unfair,
     * 那么druid默认是fair模式,这种模式是公平的,但是并发性能比较差.因此为了提高并发性能,设置锁为非公平的.
     */
    @Value("${spring.datasource.useUnfairLock:true}")
    private boolean useUnfairLock;

    @PostConstruct
    private void init() {
        log.info("data source init....");
    }

    @Bean
    @Primary
    public DruidDataSource dataSource() {

        log.info("data source init....");

        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(this.dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);

        //configuration
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setValidationQuery(validationQuery);
        datasource.setTestWhileIdle(testWhileIdle);
        datasource.setTestOnBorrow(testOnBorrow);
        datasource.setTestOnReturn(testOnReturn);
        datasource.setPoolPreparedStatements(poolPreparedStatements);
        datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
        try {
            datasource.setFilters(filters);
        } catch (SQLException e) {
            log.error("druid configuration initialization filter: {}", e.getMessage(), e);
        }
        datasource.setConnectionProperties(connectionProperties);
        datasource.setUseUnfairLock(useUnfairLock);

        datasource.setRemoveAbandoned(removeAbandoned);
        datasource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        datasource.setLogAbandoned(logAbandoned);
        datasource.setValidationQueryTimeout(validationQueryTimeout);
        datasource.setKeepAlive(keepAlive);

        //-----------------------druid日志处理过滤器-------------------
        List<Filter> list = new ArrayList<>();
        list.add(logFilter());
        datasource.setProxyFilters(list);

        try {
            datasource.init();
        } catch (SQLException e) {
            log.error("datasource init failed", e);
        }

        return datasource;

    }

    /**
     * 提供SqlSeesion
     * @return
     * @throws Exception
     */
    @Bean
    public SqlSessionFactory sqlSessionFactoryBean() throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource());
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> mapperResourceList = new ArrayList<>();
        mapperResourceList.addAll(Lists.newArrayList(resolver.getResources("classpath*:mybatis/mapper/*Mapper.xml")));
        mapperResourceList.addAll(Lists.newArrayList(resolver.getResources("classpath*:mybatis/mapper/*Dao.xml")));
        sqlSessionFactoryBean.setMapperLocations(mapperResourceList.toArray(new Resource[]{}));
        sqlSessionFactoryBean.setConfigLocation(resolver.getResource("classpath:mybatis/config/mybatis-config.xml"));

        return sqlSessionFactoryBean.getObject();
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dataSource());
    }

    /**
     * druid监控
     * @return
     */
    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        //白名单：
        //IP黑名单 (存在共同时，deny优先于allow) : 如果满足deny的话提示:Sorry, you are not permitted to view this page.
        //登录查看信息的账号密码.
        servletRegistrationBean.addInitParameter("loginUsername","admin");
        servletRegistrationBean.addInitParameter("loginPassword","admin123456");
        //是否能够重置数据.
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        return filterRegistrationBean;
    }

    @Bean
    public Slf4jLogFilter logFilter(){
        Slf4jLogFilter filter = new Slf4jLogFilter();
        filter.setResultSetLogEnabled(false);
        filter.setConnectionLogEnabled(false);
        filter.setStatementParameterClearLogEnable(false);
        filter.setStatementCreateAfterLogEnabled(false);
        filter.setStatementCloseAfterLogEnabled(false);
        filter.setStatementParameterSetLogEnabled(false);
        filter.setStatementPrepareAfterLogEnabled(false);
        return filter;
    }

}
