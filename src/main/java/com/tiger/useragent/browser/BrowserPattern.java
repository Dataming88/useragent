//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.browser;

import com.google.common.base.Strings;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class BrowserPattern {
  private final Pattern pattern;
  private final String brandReplacement;
  private final String familyReplacement;
  private final String majorReplacement;
  private final String minorReplacement;

  BrowserPattern(Pattern pattern, String brandReplacement, String familyReplacement, String majorReplacement, String minorReplacement) {
    this.pattern = pattern;
    this.brandReplacement = brandReplacement;
    this.familyReplacement = familyReplacement;
    this.majorReplacement = majorReplacement;
    this.minorReplacement = minorReplacement;
  }

  public static BrowserPattern patternFromMap(Map<String, String> configMap) {
    String regex = (String)configMap.get("regex");
    if (regex == null) {
      throw new IllegalArgumentException("browser's regex is lose");
    } else {
      String brand = (String)configMap.get("brand_replacement");
      String family = (String)configMap.get("family_replacement");
      String major = (String)configMap.get("v1_replacement");
      String minor = (String)configMap.get("v2_replacement");
      return new BrowserPattern(Pattern.compile(regex), brand, family, major, minor);
    }
  }

  public Browser match(String uaString) {
    String family = null;
    String major = null;
    String minor = null;
    Matcher matcher = this.pattern.matcher(uaString.toLowerCase());
    if (!matcher.find()) {
      return null;
    } else {
      int groupCount = matcher.groupCount();
      if (this.familyReplacement != null) {
        family = this.familyReplacement;
      } else if (groupCount > 0) {
        family = matcher.group(1);
      }

      String brand = Strings.isNullOrEmpty(this.brandReplacement) ? family : this.brandReplacement;
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

      return Strings.isNullOrEmpty(family) ? null : new Browser(brand, family, major, minor);
    }
  }
}
