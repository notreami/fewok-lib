package com.fewok.lib.process.util;

/**
 * twitter的snowflake算法 -- java实现<br>
 * From: https://github.com/twitter/snowflake <br>
 * SnowFlake的结构如下(每部分用-分开):<br>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000 <br>
 * 协议格式： 0 - 41位时间戳 - 5位数据中心标识 - 5位机器标识 - 12位序列号 <br>
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0<br>
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序的originEpoch属性）。41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据标识Id和机器标识Id作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 */
public class IdWorker {
    /**
     * 起始的时间戳，可以修改为服务第一次启动的时间
     * 一旦服务已经开始使用，起始时间戳就不应该改变
     */
    private static final long ORIGIN_EPOCH = 1522719856603L;

    /**
     * 每一部分占用的位数
     */
    private static final long DATACENTER_ID_BITS = 5L;  //数据标识id所占的位数
    private static final long WORKER_ID_BITS = 5L;      //机器标识id所占的位数
    private static final long SEQUENCE_BITS = 12L;     //序列号所占的位数

    /**
     * 每一部分的最大值
     */
    private static final long MAX_DATACENTER_ID = -1L ^ (-1L << DATACENTER_ID_BITS);   //支持的最大数据标识id，结果是31
    private static final long MAX_WORKER_ID = -1L ^ (-1L << WORKER_ID_BITS);           //支持的最大机器标识id，结果是31
    private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);          //支持的最大序列号，结果是4095

    /**
     * 每一部分向左的位移
     */
    private static final long TIMESTAMP_LEFT_SHIFT = DATACENTER_ID_BITS + WORKER_ID_BITS + SEQUENCE_BITS;     //时间截向左移22位(5+5+12)
    private static final long DATACENTER_ID_SHIFT = WORKER_ID_BITS + SEQUENCE_BITS;                         //数据标识id向左移17位(12+5)
    private static final long WORKER_ID_SHIFT = SEQUENCE_BITS;                                            //机器标识id向左移12位

    private long lastTimestamp = -1L;   //上一次时间戳
    private long dataCenterId;          //数据标识id(0~31)
    private long workerId;              //机器标识id(0~31)
    private long sequence = 0L;         //序列号(0~4095)

    private static IdWorker idWorker;

    /**
     * @param dataCenterId
     * @param workerId
     * @return
     */
    public synchronized static IdWorker getInstance(long dataCenterId, long workerId) {
        if (idWorker == null) {
            idWorker = new IdWorker(dataCenterId, workerId);
        }
        return idWorker;
    }

    public static IdWorker getIdWorker() {
        return idWorker;
    }

    /**
     * 通过单例模式来获取实例
     * 分布式部署服务时，数据标识id和机器标识id作为联合键必须唯一
     *
     * @param dataCenterId 数据标识id(0~31)
     * @param workerId     机器标识id(0~31)
     */
    private IdWorker(long dataCenterId, long workerId) {
        if (dataCenterId > MAX_DATACENTER_ID || dataCenterId < 0) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", MAX_DATACENTER_ID));
        }
        if (workerId > MAX_WORKER_ID || workerId < 0) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", MAX_WORKER_ID));
        }
        this.dataCenterId = dataCenterId;
        this.workerId = workerId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long timestamp = timeGen();

        if (timestamp < lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
        }

        if (lastTimestamp == timestamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & SEQUENCE_MASK;
            if (sequence == 0) {
                //同一毫秒的序列号已经达到最大
                timestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - ORIGIN_EPOCH) << TIMESTAMP_LEFT_SHIFT)  //时间戳部分
                | (dataCenterId << DATACENTER_ID_SHIFT)             //数据标识id部分
                | (workerId << WORKER_ID_SHIFT)                     //机器标识id部分
                | sequence;                                       //序列号部分
    }

    private long tilNextMillis(long lastTimestamp) {
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp) {
            timestamp = timeGen();
        }
        return timestamp;
    }

    private long timeGen() {
        return System.currentTimeMillis();
    }
}
