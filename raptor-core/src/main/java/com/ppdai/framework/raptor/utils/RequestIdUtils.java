package com.ppdai.framework.raptor.utils;

import java.net.InetAddress;

public class RequestIdUtils {

    private static CommonSelfIdGenerator ID_GENERATOR = new CommonSelfIdGenerator();

    static {
        /*
         * 根据机器IP获取工作进程Id,如果线上机器的IP二进制表示的最后10位不重复,建议使用此种方式
         * ,列如机器的IP为192.168.1.108,二进制表示:11000000 10101000 00000001 01101100
         * ,截取最后10位 01 01101100,转为十进制364,设置workerId为364.
         */
        InetAddress address = NetUtils.getLocalAddress();
        byte[] ipAddressByteArray = address.getAddress();
        ID_GENERATOR.setWorkerId((long) (((ipAddressByteArray[ipAddressByteArray.length - 2] & 0B11) << Byte.SIZE) + (ipAddressByteArray[ipAddressByteArray.length - 1] & 0xFF)));
    }

    /**
     * 获取 requestId
     *
     * @return
     */
    public static String getRequestId() {
        return String.valueOf(ID_GENERATOR.generateId());
    }
}
