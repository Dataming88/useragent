//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.enums;

import org.apache.commons.lang3.StringUtils;

public enum OsType {
  Other(0, "-"),
  iOS(1, "ios"),
  Android(2, "android"),
  WinPhone(3, "winphone"),
  Windows(4, "windows"),
  Linux(5, "linux"),
  Chrome(6, "chrome os");

  private final int value;
  private final String name;

  private OsType(int value, String name) {
    this.value = value;
    this.name = name;
  }

  public static OsType parseOf(String name) {
    if (StringUtils.isEmpty(name)) {
      return Other;
    } else {
      OsType[] var1 = values();
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
        OsType item = var1[var3];
        if (name.equalsIgnoreCase(item.name)) {
          return item;
        }
      }

      return Other;
    }
  }

  public static OsType parseOf(int value) {
    OsType[] var1 = values();
    int var2 = var1.length;

    for(int var3 = 0; var3 < var2; ++var3) {
      OsType item = var1[var3];
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
