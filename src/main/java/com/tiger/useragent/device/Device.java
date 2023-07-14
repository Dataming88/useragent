//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.device;

import com.tiger.useragent.enums.DeviceType;

public class Device {
  public static final Device DEFAULT_PC_SCREEN;
  public static final Device DEFAULT_PHONE_SCREEN;
  public static final Device DEFAULT_TV;
  final String brand;
  final String family;
  final DeviceType deviceType;
  final boolean isMobile;
  final String screenSize;

  public Device(String brand, String family, DeviceType deviceType, boolean isMobile) {
    this.brand = brand;
    this.family = family;
    this.deviceType = deviceType;
    this.isMobile = isMobile;
    this.screenSize = "";
  }

  public Device(String brand, String family, DeviceType deviceType, boolean isMobile, String screenSize) {
    this.brand = brand;
    this.family = family;
    this.deviceType = deviceType;
    this.isMobile = isMobile;
    this.screenSize = screenSize;
  }

  public String getBrand() {
    return this.brand;
  }

  public String getFamily() {
    return this.family;
  }

  public DeviceType getDeviceType() {
    return this.deviceType;
  }

  public boolean isMobile() {
    return this.isMobile;
  }

  public String getScreenSize() {
    return this.screenSize;
  }

  static {
    DEFAULT_PC_SCREEN = new Device("PC", "-", DeviceType.PC, false, "-");
    DEFAULT_PHONE_SCREEN = new Device("-", "-", DeviceType.Phone, true, "-");
    DEFAULT_TV = new Device("-", "-", DeviceType.TV, false, "-");
  }
}
