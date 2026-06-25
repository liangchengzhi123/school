package com.shimu.config;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

/**
 * MyBatis 工具配置类
 * <p>
 * 职责：在类加载时读取 mybatis-config.xml，构建全局唯一的 SqlSessionFactory 实例。
 * 整个应用通过 {@link #getSqlSessionFactory()} 获取同一个工厂，避免重复解析配置文件。
 * </p>
 */
public class config {

    /** 全局唯一的 SqlSessionFactory，类加载时初始化一次 */
    private static SqlSessionFactory sqlSessionFactory;

    /*
     * 静态初始化块：JVM 加载本类时自动执行。
     * 从 classpath 读取 MyBatis 主配置文件，构建 SqlSessionFactory。
     * 若配置文件缺失或 XML 格式错误，会打印异常信息并保留 sqlSessionFactory 为 null。
     */
    static {
        try {
            // 指定 MyBatis 配置文件路径（相对于 classpath 根目录）
            String resource = "mybatis-config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            // 通过建造者模式构建 SqlSessionFactory
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            System.out.println("[config] MyBatis SqlSessionFactory 初始化成功");
        } catch (Exception e) {
            // 初始化失败时输出错误信息，方便排查（如数据库连接不上、XML 格式错误等）
            System.err.println("[config] MyBatis 初始化失败，请检查 mybatis-config.xml 及数据库连接: "
                    + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 获取全局唯一的 SqlSessionFactory
     * @return SqlSessionFactory 实例；初始化失败时返回 null
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }
}
