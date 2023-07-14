//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.enums;

import org.apache.commons.lang3.StringUtils;

public enum NetType {
  Other(0, "-"),
  Wifi(1, "wifi"),
  _2G(2, "2g"),
  _3G(3, "3g"),
  _4G(4, "4g"),
  _5G(5, "5g");

  private final int value;
  private final String name;

  private NetType(int value, String name) {
    this.value = value;
    this.name = name;
  }

  public static NetType parseOf(String name) {
    if (StringUtils.isEmpty(name)) {
      return Other;
    } else {
      NetType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
        NetType item = var1[var3];
        if (name.equalsIgnoreCase(item.name)) {
          return item;
        }
      }

      return Other;
    }
  }

  public static NetType parseOf(int value) {
    NetType[] var1 = values();
    int var2 = var1.length;

    for(int var3 = 0; var3 < var2; ++var3) {
      NetType item = var1[var3];
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
