package com.shimu.constant;

public final class BookStatusConstant {
    private BookStatusConstant() {} // 私有构造，防止实例化

    public static final int AVAILABLE = 1;
    public static final int BORROWED = 2;
    public static final int NOT_FOR_LOAN = 3;
    public static final int DAMAGED = 4;
}