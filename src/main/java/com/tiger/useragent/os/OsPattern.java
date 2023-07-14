//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.os;

import com.google.common.base.Strings;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class OsPattern {
  private final Pattern pattern;
  private final String brandReplacement;
  private final String osReplacement;
  private final String majorReplacement;
  private final String minorReplacement;
  private final boolean isMobileReplacement;
  private final boolean isTVReplacement;

  OsPattern(Pattern pattern, String brandReplacement, String osReplacement, String majorReplacement, String minorReplacement, boolean isMobileReplacement, boolean isTVReplacement) {
    this.pattern = pattern;
    this.brandReplacement = brandReplacement;
    this.osReplacement = osReplacement;
    this.majorReplacement = majorReplacement;
    this.minorReplacement = minorReplacement;
    this.isMobileReplacement = isMobileReplacement;
    this.isTVReplacement = isTVReplacement;
  }

  public static OsPattern patternFromMap(Map<String, String> configMap) {
    String regex = (String)configMap.get("regex");
    if (Strings.isNullOrEmpty(regex)) {
      throw new IllegalArgumentException("OS is missing regex");
    } else {
      String brand = (String)configMap.get("brand_replacement");
      String os = (String)configMap.get("os_replacement");
      String major = (String)configMap.get("v1_replacement");
      String minor = (String)configMap.get("v2_replacement");
      String isMobileExpr = (String)configMap.get("is_mobile_replacement");
      String isTVExpr = (String)configMap.get("is_tv_replacement");
      boolean isMobile = false;
      if (!Strings.isNullOrEmpty(isMobileExpr) && "true".equals(isMobileExpr.toLowerCase())) {
        isMobile = true;
      }

      boolean isTv = false;
      if (!Strings.isNullOrEmpty(isTVExpr) && "true".equals(isTVExpr.toLowerCase())) {
        isTv = true;
      }

      return new OsPattern(Pattern.compile(regex), brand, os, major, minor, isMobile, isTv);
    }
  }

  public Os match(String useragent) {
    String family = null;
    String major = null;
    String minor = null;
    Matcher matcher = this.pattern.matcher(useragent);
    if (!matcher.find()) {
      return null;
    } else {
      int groupCount = matcher.groupCount();
      if (!Strings.isNullOrEmpty(this.osReplacement)) {
        family = this.osReplacement;
      } else if (groupCount > 0) {
        family = matcher.group(1);
      }

      String brand;
      if (!Strings.isNullOrEmpty(this.brandReplacement)) {
        brand = this.brandReplacement;
      } else {
        brand = family;
      }

      if (!Strings.isNullOrEmpty(this.majorReplacement)) {
        major = this.majorReplacement;
      } else if (groupCount > 1) {
        major = matcher.group(2);
      }

      if (!Strings.isNullOrEmpty(this.minorReplacement)) {
        minor = this.minorReplacement;
      } else if (groupCount > 2) {
        minor = matcher.group(3);
      }

      return Strings.isNullOrEmpty(family) ? null : new Os(brand, family, major, minor, this.isMobileReplacement, this.isTVReplacement);
    }
  }
}
