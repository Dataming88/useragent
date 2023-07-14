//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.browser;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BrowserParser {
  private final List<BrowserPattern> patterns;

  public BrowserParser(List<BrowserPattern> patterns) {
    this.patterns = patterns;
  }

  public static BrowserParser fromList(List<Map<String, String>> configList) {
    List<BrowserPattern> configPatterns = Lists.newArrayList();
    Iterator var2 = configList.iterator();

    while(var2.hasNext()) {
      Map<String, String> configMap = (Map)var2.next();
      configPatterns.add(BrowserPattern.patternFromMap(configMap));
    }

    return new BrowserParser(configPatterns);
  }

  public Browser parse(String uaString) {
    if (Strings.isNullOrEmpty(uaString)) {
      return Browser.DEFAULT_BROWSER;
    } else {
      Iterator var3 = this.patterns.iterator();

      Browser browser;
      BrowserPattern p;
      do {
        if (!var3.hasNext()) {
          return Browser.DEFAULT_BROWSER;
        }

        p = (BrowserPattern)var3.next();
      } while((browser = p.match(uaString)) == null);

      return browser;
    }
  }
}
