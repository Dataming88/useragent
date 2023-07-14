package com.tiger.useragent.browser;

public class Browser {
  public static final Browser DEFAULT_BROWSER = new Browser("-", "-", (String)null, (String)null);
  final String brand;
  final String family;
  final String major;
  final String minor;

  public Browser(String brand, String family, String major, String minor) {
    this.brand = brand;
    this.family = family;
    this.major = major;
    this.minor = minor;
  }

  public static Browser getDefaultBrowser() {
    return DEFAULT_BROWSER;
  }

  public String getBrand() {
    return this.brand;
  }

  public String getFamily() {
    return this.family;
  }

  public String getMajor() {
    return this.major;
  }

  public String getMinor() {
    return this.minor;
  }

  public String toString() {
    return "Browser{brand='" + this.brand + '\'' + ", family='" + this.family + '\'' + ", major='" + this.major + '\'' + ", minor='" + this.minor + '\'' + '}';
  }
}
