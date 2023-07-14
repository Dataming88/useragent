package com.tiger.useragent;

import org.junit.Test;

import java.io.IOException;

/**
 * @author shengming.gao@yoyi.com.cn
 * @date 2023/07/14
 * description:
 */
public class UserAgentTest {
    @Test
    public void testUserAgentParser() throws IOException {
        String ua = "Mozilla/5.0 (Linux; Android 13; SM-S9180 Build/TP1A.220624.014; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/113.0.5672.162 Mobile Safari/537.36 SamsungLifeService/8.5.00.8";
        UserAgentParser instance = UserAgentParser.getInstance();
        UserAgentInfo userAgentInfo = instance.getUserAgentInfo(ua);
        System.out.println(userAgentInfo.getDeviceBrand());
        System.out.println(userAgentInfo.getDeviceName());

    }

}
