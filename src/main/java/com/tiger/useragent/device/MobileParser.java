//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.device;

import com.google.common.collect.Maps;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MobileParser {
    public MobileParser() {
    }

    public static Map<String, Map<String, String>> mapForFile(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        Throwable var2 = null;

        try {
            HashMap mapForfile = Maps.newHashMap();

            String line;
            while((line = bufferedReader.readLine()) != null) {
                String[] split = line.split(",");
                Object stringStringMap;
                if (mapForfile.containsKey(split[0])) {
                    stringStringMap = (Map)mapForfile.get(split[0]);
                } else {
                    stringStringMap = Maps.newHashMap();
                }

                ((Map)stringStringMap).put(split[1], split[2]);
                mapForfile.put(split[0], stringStringMap);
            }

            HashMap var16 = mapForfile;
            return var16;
        } catch (Throwable var14) {
            var2 = var14;
            throw var14;
        } finally {
            if (bufferedReader != null) {
                if (var2 != null) {
                    try {
                        bufferedReader.close();
                    } catch (Throwable var13) {
                        var2.addSuppressed(var13);
                    }
                } else {
                    bufferedReader.close();
                }
            }

        }
    }

    public static float getScreenSize(String brand, String family, Map<String, Map<String, String>> mobileParser) {
        if (family.equalsIgnoreCase("iPhone")) {
            return 0.0F;
        } else {
            if (mobileParser.containsKey(brand.toUpperCase())) {
                Map<String, String> stringStringMap = (Map)mobileParser.get(brand.toUpperCase());
                if (stringStringMap.containsKey(family.toUpperCase())) {
                    return Float.parseFloat((String)stringStringMap.get(family.toUpperCase()));
                }
            }

            return 0.0F;
        }
    }
}
