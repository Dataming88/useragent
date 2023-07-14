//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.enums;

import org.apache.commons.lang3.StringUtils;

public enum DeviceType {
  Other(0, "-"),
  Phone(1, "phone"),
  Pad(2, "pad"),
  TV(3, "tv"),
  PC(4, "pc"),
  Spider(5, "spider");

  private final int value;
  private final String name;

  private DeviceType(int value, String name) {
    this.value = value;
    this.name = name;
  }

  public static DeviceType parseOf(String name) {
    if (StringUtils.isEmpty(name)) {
      return Other;
    } else {
      DeviceType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
        DeviceType item = var1[var3];
        if (name.equalsIgnoreCase(item.name)) {
          return item;
        }
      }

      return Other;
    }
  }

  public static DeviceType parseOf(int value) {
    DeviceType[] var1 = values();
    int var2 = var1.length;

    for(int var3 = 0; var3 < var2; ++var3) {
      DeviceType item = var1[var3];
      if (value == item.getValue()) {
        return item;
      }
    }

    return Other;
  }

  public int getValue() {
    return this.value;
  }
}
