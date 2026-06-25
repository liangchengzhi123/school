package com.shimu.service;

import com.shimu.pojo.Book;

import java.util.List;

/**
 * 图书业务层接口
 * <p>
 * 定义图书管理系统所需的全部业务操作。
 * 实现类需要在此接口基础上加入数据校验与业务规则（例如借书前检查是否可借）。
 * </p>
 */
public interface BookService {

    /**
     * 按 ID 精确查询一本图书
     * @param id 图书主键
     * @return 匹配的图书对象；不存在时返回 null
     */
    Book getById(Integer id);

    /**
     * 按书名模糊查询
     * @param name 书名关键字
     * @return 匹配的图书列表（无结果时为空列表）
     */
    List<Book> getByName(String name);

    /**
     * 按作者模糊查询
     * @param author 作者关键字
     * @return 匹配的图书列表（无结果时为空列表）
     */
    List<Book> getByAuthor(String author);

    /**
     * 按分类标签模糊查询
     * @param tag 标签关键字
     * @return 匹配的图书列表（无结果时为空列表）
     */
    List<Book> getByTag(String tag);

    /**
     * 查询全部图书
     * @return 数据库中所有图书的列表
     */
    List<Book> getAll();

    /**
     * 添加一本新书
     * @param book 要添加的图书对象（id 由数据库自增生成，无需设置）
     */
    void addBook(Book book);

    /**
     * 按 ID 删除一本图书
     * @param id 要删除的图书 ID
     */
    void deleteBook(Integer id);

    /**
     * 更新图书的全部信息（按 ID 定位）
     * @param book 包含更新后数据的图书对象
     */
    void updateBook(Book book);

    /**
     * 借阅图书：将指定图书状态改为"已借出"。
     * 业务规则：仅当图书状态为"可借出"时才允许借阅，否则抛出异常。
     * @param id 要借阅的图书 ID
     */
    void borrowBook(Integer id);

    /**
     * 归还图书：将指定图书状态改为"可借出"。
     * 业务规则：仅当图书状态为"已借出"时才允许归还，否则抛出异常。
     * @param id 要归还的图书 ID
     */
    void returnBook(Integer id);
}
