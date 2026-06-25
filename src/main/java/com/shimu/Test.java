package com.shimu;

import com.shimu.control.BookControl;
import com.shimu.pojo.Book;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * 图书管理系统 —— 控制台交互入口（表示层）
 * <p>
 * 提供命令行菜单，用户通过输入数字选择操作。
 * 所有业务逻辑委托给 {@link BookControl} 处理，本类只负责 I/O 和异常展示。
 * </p>
 *
 * <h3>菜单结构</h3>
 * <pre>
 * 主菜单:
 *   1. 查询书籍  →  子菜单: 按ID / 书名 / 作者 / 标签 / 返回
 *   2. 添加书籍
 *   3. 借阅书籍
 *   4. 归还书籍
 *   5. 修改书籍信息
 *   6. 删除书籍
 *   7. 显示全部书籍
 *   0. 退出系统
 * </pre>
 */
public class Test {

    /** 日期格式化器，用于控制台输入输出出版时间 */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /** 控制层对象：所有操作都通过它完成 */
    private static final BookControl bookControl = new BookControl();

    /** 全局 Scanner，整个应用共用一个 */
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * 程序入口：初始化完成后进入主循环，反复显示菜单直到用户选择退出。
     */
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("      欢迎使用图书管理系统 v1.0");
        System.out.println("========================================");

        // 主循环
        while (true) {
            showMainMenu();
            int choice = readInt("请选择操作: ");

            switch (choice) {
                case 1 -> queryMenu();      // 进入查询子菜单
                case 2 -> addBook();        // 添加书籍
                case 3 -> borrowBook();     // 借阅书籍
                case 4 -> returnBook();     // 归还书籍
                case 5 -> updateBook();     // 修改书籍信息
                case 6 -> deleteBook();     // 删除书籍
                case 7 -> listAllBooks();   // 显示全部书籍
                case 0 -> {
                    System.out.println("感谢使用图书管理系统，再见！");
                    scanner.close();
                    return;                 // 退出 main 方法，结束程序
                }
                default ->
                    System.out.println("[错误] 无效选项，请输入 0-7 之间的数字。");
            }
        }
    }

    // ==================== 菜单显示 ====================

    /** 打印主菜单 */
    private static void showMainMenu() {
        System.out.println("\n┌──────────────────────────────────────┐");
        System.out.println("│           主  菜  单                  │");
        System.out.println("├──────────────────────────────────────┤");
        System.out.println("│  1. 查询书籍                         │");
        System.out.println("│  2. 添加书籍                         │");
        System.out.println("│  3. 借阅书籍                         │");
        System.out.println("│  4. 归还书籍                         │");
        System.out.println("│  5. 修改书籍信息                      │");
        System.out.println("│  6. 删除书籍                         │");
        System.out.println("│  7. 显示全部书籍                      │");
        System.out.println("│  0. 退出系统                         │");
        System.out.println("└──────────────────────────────────────┘");
    }

    /** 打印查询子菜单 */
    private static void showQueryMenu() {
        System.out.println("\n--- 查询书籍 ---");
        System.out.println("  1. 按 ID 查询");
        System.out.println("  2. 按书名查询");
        System.out.println("  3. 按作者查询");
        System.out.println("  4. 按分类标签查询");
        System.out.println("  5. 返回主菜单");
    }

    // ==================== 查询子菜单 ====================

    /** 查询子菜单入口：循环展示直到用户选择返回 */
    private static void queryMenu() {
        while (true) {
            showQueryMenu();
            int choice = readInt("请选择查询方式: ");

            switch (choice) {
                case 1 -> queryById();
                case 2 -> queryByName();
                case 3 -> queryByAuthor();
                case 4 -> queryByTag();
                case 5 -> { return; }  // 返回主菜单
                default ->
                    System.out.println("[错误] 无效选项，请输入 1-5 之间的数字。");
            }
        }
    }

    private static void queryById() {
        int id = readInt("请输入图书 ID: ");
        try {
            Book book = bookControl.getById(id);
            if (book == null) {
                System.out.println("[提示] 未找到 ID=" + id + " 的图书。");
            } else {
                printBook(book);
            }
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
    }

    private static void queryByName() {
        System.out.print("请输入书名关键字: ");
        String keyword = scanner.nextLine().trim();
        try {
            List<Book> books = bookControl.getByName(keyword);
            printBookList(books, keyword);
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
    }

    private static void queryByAuthor() {
        System.out.print("请输入作者关键字: ");
        String keyword = scanner.nextLine().trim();
        try {
            List<Book> books = bookControl.getByAuthor(keyword);
            printBookList(books, keyword);
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
    }

    private static void queryByTag() {
        System.out.print("请输入分类标签关键字: ");
        String keyword = scanner.nextLine().trim();
        try {
            List<Book> books = bookControl.getByTag(keyword);
            printBookList(books, keyword);
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
    }

    // ==================== 添加书籍 ====================

    /** 逐字段收集用户输入，构造 Book 对象后提交给控制层 */
    private static void addBook() {
        System.out.println("\n--- 添加新书 ---");
        try {
            System.out.print("请输入书名: ");
            String name = scanner.nextLine().trim();
            if (name.isEmpty()) {
                System.out.println("[错误] 书名不能为空，添加取消。");
                return;
            }

            System.out.print("请输入作者: ");
            String author = scanner.nextLine().trim();

            System.out.print("请输入出版时间（yyyy-MM-dd HH:mm，回车跳过）: ");
            String timeStr = scanner.nextLine().trim();
            LocalDateTime publishTime = null;
            if (!timeStr.isEmpty()) {
                try {
                    publishTime = LocalDateTime.parse(timeStr, DATE_FORMATTER);
                } catch (DateTimeParseException e) {
                    System.out.println("[警告] 日期格式错误，出版时间设为 null。");
                }
            }

            System.out.print("请输入位置编号（回车跳过）: ");
            String posStr = scanner.nextLine().trim();
            Integer position = null;
            if (!posStr.isEmpty()) {
                position = Integer.parseInt(posStr);
            }

            System.out.print("请输入分类标签: ");
            String tag = scanner.nextLine().trim();

            // 组装 Book 对象 —— id 自增，status 默认在 Service 中设为"可借出"
            Book book = new Book();
            book.setName(name);
            book.setAuthor(author);
            book.setPublishTime(publishTime);
            book.setPosition(position);
            book.setTag(tag);

            bookControl.addBook(book);
            System.out.println("[成功] 图书《" + name + "》已添加！");
        } catch (NumberFormatException e) {
            System.out.println("[错误] 位置编号格式不正确。");
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
    }

    // ==================== 借阅书籍 ====================

    /** 输入 ID，控制层会校验状态是否允许借阅 */
    private static void borrowBook() {
        System.out.println("\n--- 借阅书籍 ---");
        int id = readInt("请输入要借阅的图书 ID: ");
        try {
            bookControl.borrowBook(id);
            System.out.println("[成功] 图书 ID=" + id + " 已借出！");
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
    }

    // ==================== 归还书籍 ====================

    /** 输入 ID，控制层会校验是否为"已借出"状态 */
    private static void returnBook() {
        System.out.println("\n--- 归还书籍 ---");
        int id = readInt("请输入要归还的图书 ID: ");
        try {
            bookControl.returnBook(id);
            System.out.println("[成功] 图书 ID=" + id + " 已归还！");
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
    }

    // ==================== 修改书籍 ====================

    /** 先按 ID 查出原信息，再逐字段修改（回车保留原值） */
    private static void updateBook() {
        System.out.println("\n--- 修改书籍信息 ---");
        int id = readInt("请输入要修改的图书 ID: ");
        try {
            // 先查出当前信息供用户参考
            Book book = bookControl.getById(id);
            if (book == null) {
                System.out.println("[错误] 未找到 ID=" + id + " 的图书。");
                return;
            }
            System.out.println("当前信息:");
            printBook(book);
            System.out.println("（直接回车保留原值）");

            System.out.print("新书名 [" + book.getName() + "]: ");
            String name = scanner.nextLine().trim();
            if (!name.isEmpty()) book.setName(name);

            System.out.print("新作者 [" + book.getAuthor() + "]: ");
            String author = scanner.nextLine().trim();
            if (!author.isEmpty()) book.setAuthor(author);

            System.out.print("新出版时间 (yyyy-MM-dd HH:mm) ["
                    + (book.getPublishTime() != null
                        ? book.getPublishTime().format(DATE_FORMATTER) : "无")
                    + "]: ");
            String timeStr = scanner.nextLine().trim();
            if (!timeStr.isEmpty()) {
                book.setPublishTime(LocalDateTime.parse(timeStr, DATE_FORMATTER));
            }

            System.out.print("新位置编号 [" + book.getPosition() + "]: ");
            String posStr = scanner.nextLine().trim();
            if (!posStr.isEmpty()) book.setPosition(Integer.parseInt(posStr));

            System.out.print("新分类标签 ["
                    + (book.getTag() != null ? book.getTag() : "无") + "]: ");
            String tag = scanner.nextLine().trim();
            if (!tag.isEmpty()) book.setTag(tag);

            bookControl.updateBook(book);
            System.out.println("[成功] 图书 ID=" + id + " 信息已更新！");
        } catch (NumberFormatException e) {
            System.out.println("[错误] 输入格式不正确。");
        } catch (DateTimeParseException e) {
            System.out.println("[错误] 日期格式错误（请使用 yyyy-MM-dd HH:mm）。");
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
    }

    // ==================== 删除书籍 ====================

    /** 按 ID 删除，删除前让用户二次确认 */
    private static void deleteBook() {
        System.out.println("\n--- 删除书籍 ---");
        int id = readInt("请输入要删除的图书 ID: ");
        try {
            Book book = bookControl.getById(id);
            if (book == null) {
                System.out.println("[错误] 未找到 ID=" + id + " 的图书。");
                return;
            }
            System.out.println("将要删除以下图书:");
            printBook(book);
            System.out.print("确认删除？(y/n): ");
            String confirm = scanner.nextLine().trim();
            if ("y".equalsIgnoreCase(confirm) || "yes".equalsIgnoreCase(confirm)) {
                bookControl.deleteBook(id);
                System.out.println("[成功] 图书 ID=" + id + " 已删除！");
            } else {
                System.out.println("已取消删除。");
            }
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
    }

    // ==================== 显示全部 ====================

    /** 列出数据库中所有图书 */
    private static void listAllBooks() {
        System.out.println("\n--- 全部图书 ---");
        try {
            List<Book> books = bookControl.getAll();
            if (books.isEmpty()) {
                System.out.println("（暂无图书）");
            } else {
                System.out.println("共 " + books.size() + " 本:\n");
                for (Book b : books) {
                    printBook(b);
                }
            }
        } catch (Exception e) {
            System.out.println("[错误] " + e.getMessage());
        }
    }

    // ==================== 工具方法 ====================

    /**
     * 安全读取整数输入 —— 使用 nextLine 避免 nextInt 遗留换行符的问题。
     * @param prompt 提示文字
     * @return 用户输入的整数；解析失败时返回 -1
     */
    private static int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /** 格式化输出单本图书的详细信息 */
    private static void printBook(Book b) {
        System.out.println("  ┌─────────────────────────────────────");
        System.out.printf("  │ ID: %-6d  位置: %d\n", b.getId(), b.getPosition());
        System.out.println("  │ 书名: " + b.getName());
        System.out.println("  │ 作者: " + b.getAuthor());
        System.out.println("  │ 出版: " + (b.getPublishTime() != null
                ? b.getPublishTime().format(DATE_FORMATTER) : "未知"));
        System.out.println("  │ 状态: " + statusToString(b.getStatus()));
        System.out.println("  │ 标签: " + b.getTag());
        System.out.println("  └─────────────────────────────────────");
    }

    /** 打印查询结果列表 */
    private static void printBookList(List<Book> books, String keyword) {
        if (books.isEmpty()) {
            System.out.println("[提示] 未找到与 \"" + keyword + "\" 匹配的图书。");
        } else {
            System.out.println("共找到 " + books.size() + " 本匹配 \"" + keyword + "\":\n");
            for (Book b : books) {
                printBook(b);
            }
        }
    }

    /** 将数字状态码转为中文说明，方便控制台阅读 */
    private static String statusToString(Integer status) {
        if (status == null) return "未知";
        return switch (status) {
            case 1 -> "可借出";
            case 2 -> "已借出";
            case 3 -> "不可借出";
            case 4 -> "损坏";
            default -> "未知(" + status + ")";
        };
    }
}
