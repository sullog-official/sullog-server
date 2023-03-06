package sullog.backend.common.config;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandler;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import sullog.backend.common.mapper.typehandler.StringListTypeHandler;
import sullog.backend.record.entity.AlcoholPercentFeeling;
import sullog.backend.record.mapper.typehandler.AlcoholPercentFeelingTypeHandler;
import sullog.backend.record.mapper.typehandler.FlavorTagListTypeHandler;

import javax.sql.DataSource;

@Configuration
@MapperScan(value = "sullog.backend", sqlSessionFactoryRef = "factory", annotationClass = Mapper.class)
public class MybatisConfig {

    @Bean(name = "datasource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "factory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("datasource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactory = new SqlSessionFactoryBean();
        sqlSessionFactory.setDataSource(dataSource);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactory.setMapperLocations(resolver.getResources("classpath:/mapper/**/*.xml"));
        sqlSessionFactory.setTypeHandlers(
                new FlavorTagListTypeHandler(),
                new AlcoholPercentFeelingTypeHandler(AlcoholPercentFeeling.class),
                new StringListTypeHandler()
        );
        return sqlSessionFactory.getObject();
    }

    @Bean(name = "sqlSession")
    public SqlSessionTemplate sqlSession(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}

