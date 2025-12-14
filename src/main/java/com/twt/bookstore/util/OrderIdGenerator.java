package com.twt.bookstore.util;

/**
 * 单一雪花算法订单号生成器。
 * 实现了Twitter的Snowflake算法，用于生成全局唯一的、趋势递增的64位长整型ID。
 *
 * 订单号结构（64位）：
 * 1位：未使用（符号位，总是0）
 * 41位：时间戳（毫秒级，可支持约69年）
 * 10位：机器ID (5位数据中心ID + 5位机器ID，最多支持1024个节点)
 * 12位：序列号（每毫秒内最多支持4096个ID）
 * * 默认实现中，机器ID和数据中心ID被简化为常量，适用于单体应用或简单集群。
 * 
 * @author gemini
 */
public class OrderIdGenerator {

    // --- 算法核心配置参数 ---
    
    // 初始时间戳 (2025-01-01 00:00:00.000) - 这是一个起始点，用于减少时间戳的位数
    private final static long START_TIMESTAMP = 1735689600000L;

    // 机器ID占用的位数
    private final static long WORKER_ID_BITS = 5L;
    // 数据中心ID占用的位数
    private final static long DATACENTER_ID_BITS = 5L;
    // 序列号占用的位数
    private final static long SEQUENCE_BITS = 12L;

    // 最大机器ID和数据中心ID (31)
    private final static long MAX_WORKER_ID = ~(-1L << WORKER_ID_BITS);
    private final static long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);

    // --- 位移量计算 ---
    
    // 机器ID向左偏移12位 (序列号位数)
    private final static long WORKER_ID_SHIFT = SEQUENCE_BITS;
    // 数据中心ID向左偏移17位 (序列号位数 + 机器ID位数)
    private final static long DATACENTER_ID_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS;
    // 时间戳向左偏移22位 (序列号位数 + 机器ID位数 + 数据中心ID位数)
    private final static long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + WORKER_ID_BITS + DATACENTER_ID_BITS;

    // 序列号掩码 (4095)
    private final static long SEQUENCE_MASK = ~(-1L << SEQUENCE_BITS);

    // --- 实例变量 ---
    
    private final long workerId;       // 机器ID (0-31)
    private final long datacenterId;   // 数据中心ID (0-31)
    
    private long sequence = 0L;        // 毫秒内的序列号
    private long lastTimestamp = -1L;  // 上次生成ID的时间戳

    /**
     * 构造函数。
     * 在生产环境中，这两个参数应通过配置或外部服务获取。
     *
     * @param datacenterId 数据中心ID
     * @param workerId 机器ID
     */
    public OrderIdGenerator(long datacenterId, long workerId) {
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(
                String.format("Worker ID must be between 0 and %d", MAX_WORKER_ID));
        }
        if (datacenterId > MAX_DATACENTER_ID || datacenterId < 0) {
            throw new IllegalArgumentException(
                String.format("Datacenter ID must be between 0 and %d", MAX_DATACENTER_ID));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 获取下一个唯一的订单号（ID）。
     * 使用 synchronized 确保线程安全。
     * * @return 64位的长整型唯一ID
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        // 如果当前时间小于上次生成ID的时间戳，说明时钟回拨，抛出异常。
        if (timestamp < lastTimestamp) {
            throw new RuntimeException(
                String.format("Clock moved backwards. Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        // 如果是同一毫秒内生成的，进行序列号递增
        if (lastTimestamp == timestamp) {
            sequence = (sequence + 1) & SEQUENCE_MASK;
            // 序列号溢出 (超过4095)，等待下一毫秒
            if (sequence == 0) {
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 新的毫秒，序列号归零
            sequence = 0L;
        }

        // 更新上次生成ID的时间戳
        lastTimestamp = timestamp;

        // 核心组合：时间戳 + 数据中心ID + 机器ID + 序列号
        return ((timestamp - START_TIMESTAMP) << TIMESTAMP_LEFT_SHIFT) // 41位时间戳
             | (datacenterId << DATACENTER_ID_SHIFT)                  // 5位数据中心ID
             | (workerId << WORKER_ID_SHIFT)                          // 5位机器ID
             | sequence;                                              // 12位序列号
    }

    /**
     * 阻塞到下一毫秒，直到获得新的时间戳。
     */
    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 获取当前系统时间（毫秒）。
     */
    private long timeGen() {
        return System.currentTimeMillis();
    }
}