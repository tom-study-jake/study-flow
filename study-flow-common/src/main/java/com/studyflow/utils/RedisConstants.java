package com.studyflow.utils;

// Redis常量
public class RedisConstants {
    // value = userId（谁占的座）
    public static final String SEAT_OCCUPY = "seat:occupy:%d:%s:%d";

    // 该时段剩余座位数 key：seat:stock:{roomId}:{date}:{periodId}
    public static final String SEAT_STOCK = "seat:stock:%d:%s:%d";

    // 用户某日某时段已占座 key：user:reserve:{userId}:{date}:{periodId}
    public static final String USER_RESERVE = "user:reserve:%d:%s:%d";

    // 预约缓存过期时间（秒），15分钟
    public static final long RESERVE_TTL = 900;

}
