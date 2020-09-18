package com.sikiedu.message.utils;


/**
 * @author jianggang
 * @version 1.0
 * @date 2020/9/18 14:50
 *
 *
 * Twitter - snowflake 算法 JAVA 实现
 *
 *
 *
 */
public class SnowflakeIdWorker {

    /**   开始时间戳 时间起点 一般取当前时间 一旦确定  不能更改  **/
    private final long twepoch = 1559443636204L;

    /**   机器ID所占的位数   **/
    private final long workerIdBits = 5L;

    /**   数据标识ID所占位数  **/
    private final long datacenterIdBits = 5L;

    /**   支持最大的机器ID  结果为31 这个位移算法能很计算出几位二进制数能表示的最大十进制数  **/
    private final long maxWorkId = -1L ^ ( -1L << workerIdBits);

    /**   支持最大的数据标识ID 结果31  **/
    private final long maxDatacenterId = -1L ^ (-1l << datacenterIdBits);

    /**  序列在ID中占的位数  **/
    private final long sequenceBits = 12L;

    /**  机器ID向左移12位  **/
    private final long workerIdShift = sequenceBits;

    /**  数据标识ID向左移17位 **/
    private final long datacenterIdShift = sequenceBits + workerIdBits;

    /**  时间戳向左移22位  **/
    private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

    /**  生成序列的掩码 这里为4095**/
    private final long sequenceMask = -1L ^ (-1L << sequenceBits);

    /**  工作机器ID **/
    private long workerId;

    /**  数据中心ID  **/
    private long datacenterId;

    /**  毫秒内序列 0-4095 **/
    private long sequence = 0L;

    /**  上次生成ID的时间戳  **/
    private long lastTimestamp = -1L;


    public SnowflakeIdWorker(long workerId, long datacenterId){
        if(workerId > maxWorkId || workerId < 0){
            throw new IllegalArgumentException(String.format("worker Id can't be greater that %d or less that 0", maxWorkId));
        }
        if(datacenterId > maxDatacenterId || datacenterId < 0){
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater that %d or less that 0", maxDatacenterId));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
    }

    /**
     * 或得下一个ID
     * @return
     */
    public synchronized long nextId(){
        long timestamp = timeGen();

        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过，这个时候生成序列会乱
        if(timestamp < lastTimestamp){
            throw new RuntimeException(String.format("Clock moved backwards, Refusing to generate id for %d milliseconds",lastTimestamp-timestamp));
        }

        //如果是同一时间生成，则进行毫秒内序列
        if(lastTimestamp == timestamp){
            sequence = ( sequence + 1 ) & sequenceMask;
            //毫秒内序列溢出
            if(sequence == 0){
                // 阻塞到下一毫秒，或得新的时间戳
                timestamp = tilNextMillis(lastTimestamp);
            }
        // 时间戳改变  重置序列
        }else{
            sequence = 0L;
        }

        lastTimestamp = timestamp;

        return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;

    }

    /**
     * 阻塞到下一毫秒 知道获得新的时间戳
     * @param lastTimestamp
     * @return
     */
    protected long tilNextMillis(long lastTimestamp){
        long timestamp = timeGen();
        while (timestamp <= lastTimestamp){
            timestamp = timeGen();
        }
        return timestamp;
    }

    /**
     * 返回以毫秒为单位的当前时间
     * @return
     */
    protected long timeGen(){
        return System.currentTimeMillis();
    }


    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        SnowflakeIdWorker worker = new SnowflakeIdWorker(31,31);
        for(int i =0 ; i< 100 ; i++){
            long id = worker.nextId();
            System.out.println(id);
        }
    }
}
