//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.device;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class DeviceParser {
  private List<DevicePattern> patterns;

  public DeviceParser(List<DevicePattern> patterns) {
    this.patterns = patterns;
  }

  public static List<DevicePattern> patternsFromList(List<Map<String, String>> configList) {
    List<DevicePattern> configPatterns = Lists.newArrayList();
    Iterator var2 = configList.iterator();

    while(var2.hasNext()) {
      Map<String, String> configMap = (Map)var2.next();
      configPatterns.add(DevicePattern.patternFromMap(configMap));
    }

    return configPatterns;
  }

  public Device parse(String agentString) {
    if (Strings.isNullOrEmpty(agentString)) {
      return Device.DEFAULT_PC_SCREEN;
    } else {
      Iterator var3 = this.patterns.iterator();

      Device device;
      DevicePattern p;
      do {
        if (!var3.hasNext()) {
          String lower = agentString.toLowerCase();
          if (lower.contains("ottsdk")) {
            return Device.DEFAULT_TV;
          }

          if (!lower.contains("android") && !lower.contains("phone") && !lower.contains("mobile")) {
            return Device.DEFAULT_PC_SCREEN;
          }

          return Device.DEFAULT_PHONE_SCREEN;
        }

        p = (DevicePattern)var3.next();
      } while((device = p.match(agentString)) == null);

      return device;
    }
  }
}
