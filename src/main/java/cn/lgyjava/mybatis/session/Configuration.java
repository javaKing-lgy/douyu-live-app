package cn.lgyjava.mybatis.session;

import cn.lgyjava.mybatis.binding.MapperRegistry;
import cn.lgyjava.mybatis.datasource.druid.DruidDataSourceFactory;
import cn.lgyjava.mybatis.datasource.pooled.PooledDataSourceFactory;
import cn.lgyjava.mybatis.datasource.unpooled.UnpooledDataSourceFactory;
import cn.lgyjava.mybatis.executor.Executor;
import cn.lgyjava.mybatis.executor.SimpleExecutor;
import cn.lgyjava.mybatis.executor.resultset.DefaultResultSetHandler;
import cn.lgyjava.mybatis.executor.resultset.ResultSetHandler;
import cn.lgyjava.mybatis.executor.statement.PreparedStatementHandler;
import cn.lgyjava.mybatis.executor.statement.StatementHandler;
import cn.lgyjava.mybatis.mapping.BoundSql;
import cn.lgyjava.mybatis.mapping.Environment;
import cn.lgyjava.mybatis.mapping.MappedStatement;
import cn.lgyjava.mybatis.transaction.Transaction;
import cn.lgyjava.mybatis.transaction.jdbc.JdbcTransactionFactory;
import cn.lgyjava.mybatis.type.TypeAliasRegistry;

import java.util.HashMap;
import java.util.Map;

/** 配置项
 * 在配置类中添加映射器注册机 和 映射语句 的存放
 *
 * @author liuguanyi
 * * @date 2025/1/29
 */
public class Configuration {

    protected Environment environment;

    /**
     * 映射注册机
     */
    protected MapperRegistry mapperRegistry = new MapperRegistry(this);

    /**
     * 映射的语句，存在Map里
     */
    protected final Map<String, MappedStatement> mappedStatements = new HashMap<>();

    /**
     * 类型别名注册机
     */
    protected final TypeAliasRegistry typeAliasRegistry = new TypeAliasRegistry();

    public Configuration() {
        typeAliasRegistry.registerAlias("JDBC", JdbcTransactionFactory.class);
        typeAliasRegistry.registerAlias("DRUID", DruidDataSourceFactory.class);
        typeAliasRegistry.registerAlias("UNPOOLED", UnpooledDataSourceFactory.class);
        typeAliasRegistry.registerAlias("POOLED", PooledDataSourceFactory.class);
    }

    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }

    public void addMappedStatement(MappedStatement ms) {
        mappedStatements.put(ms.getId(), ms);
    }

    public MappedStatement getMappedStatement(String id) {
        return mappedStatements.get(id);
    }

    public TypeAliasRegistry getTypeAliasRegistry() {
        return typeAliasRegistry;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * 创建结果集处理器
     */
    public ResultSetHandler newResultSetHandler(Executor executor, MappedStatement mappedStatement, BoundSql boundSql) {
        return new DefaultResultSetHandler(executor, mappedStatement, boundSql);
    }

    /**
     * 生产执行器
     */
    public Executor newExecutor(Transaction transaction) {
        return new SimpleExecutor(this, transaction);
    }

    /**
     * 创建语句处理器
     */
    public StatementHandler newStatementHandler(Executor executor, MappedStatement mappedStatement, Object parameter, ResultHandler resultHandler, BoundSql boundSql) {
        return new PreparedStatementHandler(executor, mappedStatement, parameter, resultHandler, boundSql);
    }


}
