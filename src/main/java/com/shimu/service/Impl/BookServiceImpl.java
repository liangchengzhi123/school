package com.shimu.service.Impl;

import com.shimu.constant.BookStatusConstant;
import com.shimu.mapper.BookMapper;
import com.shimu.pojo.Book;
import com.shimu.service.BookService;

import java.util.List;

/**
 * 图书业务层实现
 * <p>
 * 持有 {@link BookMapper} 实例，在数据访问的基础上加入业务规则校验：
 * <ul>
 *   <li>借书前检查状态是否为"可借出"</li>
 *   <li>还书前检查状态是否为"已借出"</li>
 *   <li>添加书籍时校验书名非空</li>
 *   <li>删除 / 更新前校验图书是否存在</li>
 * </ul>
 * 构造函数接收外部传入的 Mapper（由控制层在 Session 内创建并注入）。
 * </p>
 */
public class BookServiceImpl implements BookService {

    /** 数据访问对象 —— 由控制层在 SqlSession 中获取后注入 */
    private final BookMapper bookMapper;

    /**
     * 构造注入 Mapper
     * @param bookMapper MyBatis 生成的 BookMapper 代理对象
     */
    public BookServiceImpl(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    // ==================== 查询操作（直接委托 Mapper，无额外逻辑） ====================

    @Override
    public Book getById(Integer id) {
        return bookMapper.getById(id);
    }

    @Override
    public List<Book> getByName(String name) {
        return bookMapper.getByName(name);
    }

    @Override
    public List<Book> getByAuthor(String author) {
        return bookMapper.getByAuthor(author);
    }

    @Override
    public List<Book> getByTag(String tag) {
        return bookMapper.getByTag(tag);
    }

    @Override
    public List<Book> getAll() {
        return bookMapper.getAll();
    }

    // ==================== 增删改操作（含基础校验） ====================

    @Override
    public void addBook(Book book) {
        // 业务校验：书名不能为空
        if (book.getName() == null || book.getName().trim().isEmpty()) {
            throw new RuntimeException("添加失败：书名不能为空");
        }
        // 如果前端未设置状态，默认为"可借出"
        if (book.getStatus() == null) {
            book.setStatus(BookStatusConstant.AVAILABLE);
        }
        bookMapper.insertBook(book);
    }

    @Override
    public void deleteBook(Integer id) {
        // 先检查图书是否存在，避免误删提示不明确
        Book book = bookMapper.getById(id);
        if (book == null) {
            throw new RuntimeException("删除失败：ID=" + id + " 的图书不存在");
        }
        bookMapper.delById(id);
    }

    @Override
    public void updateBook(Book book) {
        // 先检查图书是否存在（id 是 Long 类型，需转为 int）
        Book existing = bookMapper.getById(Math.toIntExact(book.getId()));
        if (existing == null) {
            throw new RuntimeException("更新失败：ID=" + book.getId() + " 的图书不存在");
        }
        bookMapper.updateBook(book);
    }

    // ==================== 借阅 / 归还（核心业务逻辑） ====================

    @Override
    public void borrowBook(Integer id) {
        // 1. 查询图书是否存在
        Book book = bookMapper.getById(id);
        if (book == null) {
            throw new RuntimeException("借阅失败：ID=" + id + " 的图书不存在");
        }
        // 2. 检查状态是否允许借阅 —— 只有"可借出(1)"才能借
        if (book.getStatus() != BookStatusConstant.AVAILABLE) {
            String statusDesc = switch (book.getStatus()) {
                case BookStatusConstant.BORROWED -> "已借出";
                case BookStatusConstant.NOT_FOR_LOAN -> "不可借出";
                case BookStatusConstant.DAMAGED -> "损坏";
                default -> "未知状态";
            };
            throw new RuntimeException("借阅失败：《" + book.getName() + "》当前状态为【"
                    + statusDesc + "】，无法借阅");
        }
        // 3. 更新状态为"已借出"
        bookMapper.updateStatus(id, BookStatusConstant.BORROWED);
    }

    @Override
    public void returnBook(Integer id) {
        // 1. 查询图书是否存在
        Book book = bookMapper.getById(id);
        if (book == null) {
            throw new RuntimeException("归还失败：ID=" + id + " 的图书不存在");
        }
        // 2. 只有"已借出(2)"状态的图书才需要归还
        if (book.getStatus() != BookStatusConstant.BORROWED) {
            String statusDesc = switch (book.getStatus()) {
                case BookStatusConstant.AVAILABLE -> "可借出（未被借阅）";
                case BookStatusConstant.NOT_FOR_LOAN -> "不可借出";
                case BookStatusConstant.DAMAGED -> "损坏";
                default -> "未知状态";
            };
            throw new RuntimeException("归还失败：《" + book.getName() + "》当前状态为【"
                    + statusDesc + "】，无需归还");
        }
        // 3. 更新状态为"可借出"
        bookMapper.updateStatus(id, BookStatusConstant.AVAILABLE);
    }
}
