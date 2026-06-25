package com.shimu;

import com.shimu.mapper.BookMapper;
import com.shimu.pojo.Book;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // 1. 加载 MyBatis 配置，创建 SqlSessionFactory
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        // 2. 打开 SqlSession（自动提交，方便插入后直接查询）
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            BookMapper mapper = session.getMapper(BookMapper.class);

            // ---------- 插入假数据 ----------
            System.out.println("===== 插入假数据 =====");

            Book book1 = new Book();
            book1.setName("深入理解Java虚拟机");
            book1.setAuthor("周志明");
            book1.setPublishTime(LocalDateTime.of(2019, 12, 1, 0, 0));
            book1.setStatus(1);   // 可借出
            book1.setPosition(101);
            book1.setTag("计算机");
            mapper.insertBook(book1);

            Book book2 = new Book();
            book2.setName("Java编程思想");
            book2.setAuthor("Bruce Eckel");
            book2.setPublishTime(LocalDateTime.of(2005, 6, 1, 0, 0));
            book2.setStatus(2);   // 已借出
            book2.setPosition(202);
            book2.setTag("编程");
            mapper.insertBook(book2);

            Book book3 = new Book();
            book3.setName("百年孤独");
            book3.setAuthor("马尔克斯");
            book3.setPublishTime(LocalDateTime.of(1967, 5, 30, 0, 0));
            book3.setStatus(1);
            book3.setPosition(303);
            book3.setTag("文学");
            mapper.insertBook(book3);

            Book book4 = new Book();
            book4.setName("Head First设计模式");
            book4.setAuthor("Eric Freeman");
            book4.setPublishTime(LocalDateTime.of(2004, 10, 1, 0, 0));
            book4.setStatus(1);
            book4.setPosition(404);
            book4.setTag("设计模式");
            mapper.insertBook(book4);

            System.out.println("插入了 4 本测试书。\n");

            // ---------- 测试查询方法 ----------

            // 1. 根据 ID 查询
            System.out.println("===== 根据 ID 查询（假设 ID=1） =====");
            Book found = mapper.getById(1);
            if (found != null) {
                System.out.println(found);
            } else {
                System.out.println("未找到 ID=1 的书（可能 ID 不是从1开始，请修改）");
            }
            System.out.println();

            // 2. 根据书名模糊查询
            System.out.println("===== 根据书名模糊查询（关键字: Java） =====");
            List<Book> booksByName = mapper.getByName("Java");
            booksByName.forEach(System.out::println);
            System.out.println();

            // 3. 根据作者模糊查询
            System.out.println("===== 根据作者模糊查询（关键字: 马） =====");
            List<Book> booksByAuthor = mapper.getByAuthor("马");
            booksByAuthor.forEach(System.out::println);
            System.out.println();

            // 4. 根据分类标签模糊查询
            System.out.println("===== 根据分类模糊查询（关键字: 设计） =====");
            List<Book> booksByTag = mapper.getByTag("设计");
            booksByTag.forEach(System.out::println);
            System.out.println();

            // 5. 测试删除
            System.out.println("===== 测试删除（删除 ID=1 的书） =====");
            mapper.delById(1);
            Book deleted = mapper.getById(1);
            if (deleted == null) {
                System.out.println("ID=1 的书已成功删除。");
            } else {
                System.out.println("删除失败，仍然存在。");
            }
            System.out.println();

            // 再次查询所有，看看剩余的数据（可选）
            System.out.println("===== 再次查询所有剩余数据（使用书名模糊查询空字符串可以列出所有） =====");
            List<Book> all = mapper.getByName("");
            all.forEach(System.out::println);
        }
    }
}