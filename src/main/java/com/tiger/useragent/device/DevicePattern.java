//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.device;

import com.google.common.base.Strings;
import com.tiger.useragent.enums.DeviceType;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;

public class DevicePattern {
  private final Pattern pattern;
  private final String brandReplacement;
  private final String deviceReplacement;
  private final DeviceType deviceTypeReplacement;
  private final boolean isMobileReplacement;
  private static String[] deviceType;
  private static final Pattern screenSizePattern;

  DevicePattern(Pattern pattern, String brandReplacement, String deviceReplacement, DeviceType deviceTypeReplacement, boolean isMobileReplacement) {
    this.pattern = pattern;
    this.brandReplacement = brandReplacement;
    this.deviceReplacement = deviceReplacement;
    this.deviceTypeReplacement = deviceTypeReplacement;
    this.isMobileReplacement = isMobileReplacement;
  }

  public static DevicePattern patternFromMap(Map<String, String> configMap) {
    String regex = (String)configMap.get("regex");
    if (Strings.isNullOrEmpty(regex)) {
      throw new IllegalArgumentException("Device is missing regex" + regex);
    } else {
      String brand = (String)configMap.get("brand_replacement");
      String family = (String)configMap.get("device_replacement");
      String isMobileString = (String)configMap.get("is_mobile_replacement");
      boolean isMobile = true;
      if (isMobileString != null && isMobileString.equalsIgnoreCase("false")) {
        isMobile = false;
      }

      String deviceTypeString = (String)configMap.get("device_type_replacement");
      DeviceType type = DeviceType.parseOf(deviceTypeString);
      ArrayUtils.contains(deviceType, deviceTypeString);
      return new DevicePattern(Pattern.compile(regex), brand, family, type, isMobile);
    }
  }

  public Device match(String agentString) {
    Matcher matcher = this.pattern.matcher(agentString);
    if (!matcher.find()) {
      return null;
    } else {
      int groupCount = matcher.groupCount();
      String brand = null;
      String family = null;
      DeviceType deviceType = this.deviceTypeReplacement;
      boolean isMobile = this.isMobileReplacement;
      if (deviceType == DeviceType.Other && isMobile) {
        deviceType = DeviceType.Phone;
      }

      if (!Strings.isNullOrEmpty(this.brandReplacement)) {
        brand = this.brandReplacement;
      } else if (groupCount > 0) {
        brand = matcher.group(1);
      }

      if (this.deviceReplacement != null) {
        family = this.deviceReplacement;
        if (this.deviceReplacement.contains("$1") && groupCount >= 0 && matcher.group(1) != null) {
          family = family.replaceFirst("\\$1", Matcher.quoteReplacement(matcher.group(1)));
        }

        if (this.deviceReplacement.contains("$2") && groupCount >= 2 && matcher.group(2) != null) {
          family = family.replaceFirst("\\$2", Matcher.quoteReplacement(matcher.group(2)));
        }
      } else if (groupCount >= 2) {
        family = matcher.group(2);
      }

      if (family == null) {
        String var8 = "-";
      }

      if (null == family) {
        family = "";
      }

      return brand == null ? null : new Device(brand, family, deviceType, isMobile, "-");
    }
  }

  private String parseScreenSize(String agentString) {
    Matcher matcher = screenSizePattern.matcher(agentString);
    return matcher.find() ? matcher.group(1) : "-";
  }

  static {
    DeviceType[] deviceTypes = DeviceType.values();
    deviceType = new String[deviceTypes.length];
    int i = 0;

    for(int length = deviceTypes.length; i < length; ++i) {
      deviceType[i] = deviceTypes[i].name();
    }

    screenSizePattern = Pattern.compile("\\W(\\d{3,4}[x\\*]\\d{3,4})\\W*", 2);
  }
}
