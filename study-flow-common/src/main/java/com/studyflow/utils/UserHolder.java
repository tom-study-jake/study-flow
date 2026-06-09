package com.studyflow.utils;

/**
 * 用户信息持有者（ThreadLocal）
 *
 * ThreadLocal = 线程局部变量
 * 意思是在一个请求的整个处理过程中（Interceptor → Controller → Service），
 * 都可以从 UserHolder 拿到当前用户的信息，而不需要每个方法都传参。
 *
 * 原理：
 * - 每个请求由 Tomcat 的一个线程处理
 * - ThreadLocal 在当前线程中存储数据
 * - 请求结束后要清理，否则内存泄漏
 */
public class UserHolder {

    private static final ThreadLocal<Long> tl = new ThreadLocal<>();

    /**
     * 保存当前登录用户ID
     */
    public static void setUserId(Long userId) {
        tl.set(userId);
    }

    /**
     * 获取当前登录用户ID
     */
    public static Long getUserId() {
        return tl.get();
    }

    /**
     * 清理（请求结束后调用）
     */
    public static void remove() {
        tl.remove();
    }
}
