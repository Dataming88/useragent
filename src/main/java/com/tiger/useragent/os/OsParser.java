//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent.os;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OsParser {
  private final List<OsPattern> patterns;

  public OsParser(List<OsPattern> patterns) {
    this.patterns = patterns;
  }

  public static OsParser fromList(List<Map<String, String>> configList) {
    List<OsPattern> configPatterns = Lists.newArrayList();
    Iterator var2 = configList.iterator();

    while(var2.hasNext()) {
      Map<String, String> configMap = (Map)var2.next();
      configPatterns.add(OsPattern.patternFromMap(configMap));
    }

    return new OsParser(configPatterns);
  }

  public Os parse(String agentString) {
    if (Strings.isNullOrEmpty(agentString)) {
      return Os.DEFAULT_OS;
    } else {
      Iterator var3 = this.patterns.iterator();

      Os os;
      OsPattern p;
      do {
        if (!var3.hasNext()) {
          return Os.DEFAULT_OS;
        }

        p = (OsPattern)var3.next();
      } while((os = p.match(agentString)) == null);

      return os;
    }
  }
}
