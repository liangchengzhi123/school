package com.shimu.control;

import com.shimu.config.config;
import com.shimu.mapper.BookMapper;
import com.shimu.pojo.Book;
import com.shimu.service.BookService;
import com.shimu.service.Impl.BookServiceImpl;
import org.apache.ibatis.session.SqlSession;

import java.util.List;

/**
 * 图书控制层
 * <p>
 * 在表示层（Test.java）与业务层（BookService）之间起桥梁作用。
 * 核心职责：
 * <ul>
 *   <li>从 {@link config} 获取 SqlSessionFactory，管理数据库会话的创建与关闭</li>
 *   <li>在会话中获取 Mapper，注入到 Service 实现类</li>
 *   <li>将表示层的请求委派给 Service，并确保会话通过 try-with-resources 正确释放</li>
 * </ul>
 * 每个公开方法都是一次独立的数据库会话（auto-commit 模式）。
 * </p>
 */
public class BookControl {

    /**
     * 工厂方法：在当前 SqlSession 中创建 BookService 实例。
     * Mapper 的生命周期绑定到 Session，因此 Service 不应在 Session 关闭后继续使用。
     */
    private BookService createService(SqlSession session) {
        BookMapper mapper = session.getMapper(BookMapper.class);
        return new BookServiceImpl(mapper);
    }

    // ==================== 查询 ====================

    /**
     * 按 ID 查询图书
     * @param id 图书主键
     * @return 匹配的图书对象；不存在返回 null
     */
    public Book getById(Integer id) {
        // try-with-resources 保证 SqlSession 在操作结束后自动关闭
        try (SqlSession session = config.getSqlSessionFactory().openSession(true)) {
            BookService service = createService(session);
            return service.getById(id);
        }
    }

    /**
     * 按书名模糊查询
     * @param name 书名关键字
     * @return 匹配的图书列表
     */
    public List<Book> getByName(String name) {
        try (SqlSession session = config.getSqlSessionFactory().openSession(true)) {
            BookService service = createService(session);
            return service.getByName(name);
        }
    }

    /**
     * 按作者模糊查询
     * @param author 作者关键字
     * @return 匹配的图书列表
     */
    public List<Book> getByAuthor(String author) {
        try (SqlSession session = config.getSqlSessionFactory().openSession(true)) {
            BookService service = createService(session);
            return service.getByAuthor(author);
        }
    }

    /**
     * 按分类标签模糊查询
     * @param tag 标签关键字
     * @return 匹配的图书列表
     */
    public List<Book> getByTag(String tag) {
        try (SqlSession session = config.getSqlSessionFactory().openSession(true)) {
            BookService service = createService(session);
            return service.getByTag(tag);
        }
    }

    /**
     * 查询全部图书
     * @return 所有图书列表
     */
    public List<Book> getAll() {
        try (SqlSession session = config.getSqlSessionFactory().openSession(true)) {
            BookService service = createService(session);
            return service.getAll();
        }
    }

    // ==================== 增删改 ====================

    /**
     * 添加新书
     * @param book 待添加的图书对象
     */
    public void addBook(Book book) {
        try (SqlSession session = config.getSqlSessionFactory().openSession(true)) {
            BookService service = createService(session);
            service.addBook(book);
        }
    }

    /**
     * 删除图书
     * @param id 要删除的图书 ID
     */
    public void deleteBook(Integer id) {
        try (SqlSession session = config.getSqlSessionFactory().openSession(true)) {
            BookService service = createService(session);
            service.deleteBook(id);
        }
    }

    /**
     * 更新图书全部信息
     * @param book 包含更新后数据的图书对象（id 用于定位）
     */
    public void updateBook(Book book) {
        try (SqlSession session = config.getSqlSessionFactory().openSession(true)) {
            BookService service = createService(session);
            service.updateBook(book);
        }
    }

    // ==================== 借阅 / 归还 ====================

    /**
     * 借阅图书：校验状态后改为"已借出"。
     * 在同一会话内完成"查询 + 改状态"，保证操作连贯性。
     * @param id 要借阅的图书 ID
     */
    public void borrowBook(Integer id) {
        try (SqlSession session = config.getSqlSessionFactory().openSession(true)) {
            BookService service = createService(session);
            service.borrowBook(id);
        }
    }

    /**
     * 归还图书：校验状态后改为"可借出"。
     * @param id 要归还的图书 ID
     */
    public void returnBook(Integer id) {
        try (SqlSession session = config.getSqlSessionFactory().openSession(true)) {
            BookService service = createService(session);
            service.returnBook(id);
        }
    }
}
