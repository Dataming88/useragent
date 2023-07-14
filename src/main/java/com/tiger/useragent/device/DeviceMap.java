//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.device;

import com.google.common.base.Strings;
import com.tiger.useragent.Parser;
import com.tiger.useragent.enums.DeviceType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class DeviceMap {
  private Map<String, Device> map;
  public static final int DEVICE_TYPE_LENGTH = 5;
  public static final int DEVICE_FAMILY = 0;
  public static final int DEVICE_BRAND = 1;
  public static final int RE_DEVICE_FAMILY = 2;
  public static final int DEVICE_TYPE = 3;
  public static final int DEVICE_SCREEN_SIZE = 4;

  DeviceMap(Map<String, Device> map) {
    this.map = map;
  }

  public static DeviceMap mapFromFile(InputStream stream) throws IOException {
    Map<String, Device> map = new HashMap();
    InputStream inputStream = Parser.class.getResourceAsStream("/DeviceDictionary_Auto.txt");
    Throwable var3 = null;

    try {
      fillMap(inputStream, map);
    } catch (Throwable var12) {
      var3 = var12;
      throw var12;
    } finally {
      if (inputStream != null) {
        if (var3 != null) {
          try {
            inputStream.close();
          } catch (Throwable var11) {
            var3.addSuppressed(var11);
          }
        } else {
          inputStream.close();
        }
      }

    }

    fillMap(stream, map);
    return new DeviceMap(map);
  }

  private static void fillMap(InputStream stream, Map<String, Device> map) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "utf-8"));
    Throwable var3 = null;

    try {
      String line;
      try {
        while((line = reader.readLine()) != null) {
          if (!isCommentOrBlank(line)) {
            String[] items = line.split(",,");
            if (items.length == 5) {
              String key = items[0];
              DeviceType deviceType = DeviceType.parseOf(items[3]);
              map.put(key, new Device(items[1], items[2], deviceType, true, items[4]));
            }
          }
        }
      } catch (Throwable var15) {
        var3 = var15;
        throw var15;
      }
    } finally {
      if (reader != null) {
        if (var3 != null) {
          try {
            reader.close();
          } catch (Throwable var14) {
            var3.addSuppressed(var14);
          }
        } else {
          reader.close();
        }
      }

    }

  }

  private static boolean isCommentOrBlank(String line) {
    return line.startsWith("#") || StringUtils.isBlank(line) || Strings.isNullOrEmpty(line);
  }

  public Device parseDevice(Device device) {
    if (!device.deviceType.equals(DeviceType.PC) && !device.deviceType.equals(DeviceType.Spider) && device.family != null && !"-".equals(device.family)) {
      String replaceFamily = StringUtils.trimToEmpty(StringUtils.replaceEach(device.family, new String[]{"　"}, new String[]{""}));
      if ("".equals(replaceFamily)) {
        return new Device(device.brand, "-", device.deviceType, device.isMobile, "-");
      } else if ("".equals(replaceFamily.replaceAll("/", ""))) {
        return device;
      } else {
        String family = device.family.split("/")[0].replace('_', ' ').toUpperCase();
        return this.map.containsKey(family) ? (Device)this.map.get(family) : device;
      }
    } else {
      return device;
    }
  }
}
