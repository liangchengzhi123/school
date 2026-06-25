package com.shimu.mapper;

import com.shimu.pojo.Book;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface BookMapper {

    // 根据ID精确查询
    @Select("SELECT * FROM book WHERE id = #{id}")
    Book getById(Integer id);

    // 根据书名模糊查询（like）
    @Select("SELECT * FROM book WHERE name LIKE CONCAT('%', #{name}, '%')")
    List<Book> getByName(String name);

    // 根据作者模糊查询
    @Select("SELECT * FROM book WHERE author LIKE CONCAT('%', #{author}, '%')")
    List<Book> getByAuthor(String author);

    // 根据分类标签模糊查询
    @Select("SELECT * FROM book WHERE tag LIKE CONCAT('%', #{tag}, '%')")
    List<Book> getByTag(String tag);

    // 插入新书（id为自增，不需要插入）
    @Insert("INSERT INTO book (name, author, publish_time, status, position, tag) " +
            "VALUES (#{name}, #{author}, #{publishTime}, #{status}, #{position}, #{tag})")
    void insertBook(Book book);

    // 根据ID删除
    @Delete("DELETE FROM book WHERE id = #{id}")
    void delById(Integer id);

    /**
     * 更新图书的全部字段（根据 id 定位记录）
     * 参数 book 的 id 用于 WHERE 条件，其余字段写入 SET 子句
     */
    @Update("UPDATE book SET name=#{name}, author=#{author}, publish_time=#{publishTime}, " +
            "status=#{status}, position=#{position}, tag=#{tag} WHERE id=#{id}")
    void updateBook(Book book);

    /**
     * 仅更新图书的借阅状态 —— 用于借书 / 还书操作
     * @param id     目标图书的 ID
     * @param status 新状态值（参考 BookStatusConstant 中的常量）
     */
    @Update("UPDATE book SET status=#{status} WHERE id=#{id}")
    void updateStatus(@Param("id") Integer id, @Param("status") Integer status);

    /**
     * 查询全部图书，不做任何过滤
     * @return 数据库中所有图书的列表（无结果时返回空列表）
     */
    @Select("SELECT * FROM book")
    List<Book> getAll();
}
