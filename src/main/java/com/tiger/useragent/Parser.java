//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent;

import com.google.common.base.Strings;
import com.tiger.useragent.browser.Browser;
import com.tiger.useragent.browser.BrowserParser;
import com.tiger.useragent.device.Device;
import com.tiger.useragent.device.DeviceMap;
import com.tiger.useragent.device.DeviceParser;
import com.tiger.useragent.device.DevicePattern;
import com.tiger.useragent.enums.DeviceType;
import com.tiger.useragent.enums.NetType;
import com.tiger.useragent.enums.OsType;
import com.tiger.useragent.os.Os;
import com.tiger.useragent.os.OsParser;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public class Parser {
  private OsParser osParser;
  private BrowserParser browserParser;
  private DeviceParser deviceParser;
  private DeviceMap deviceMap;
  protected static final Object LOCK = new Object();
  private static final ConcurrentMap<String, DeviceType> dMap = new ConcurrentHashMap();
  private static final ConcurrentMap<String, NetType> netMap = new ConcurrentHashMap();
  private static final ConcurrentMap<String, OsType> osMap = new ConcurrentHashMap();
  private static final Pattern pattern = Pattern.compile("\\.net( clr | client )?(?<ver>\\d(\\.\\d)?)(\\.\\d+)*[ce;$) ]", 2);
  private static final Pattern netTypePattern = Pattern.compile("\\W(WIFI|5G|4G|3G|2G)\\W*", 2);
  private static final Pattern deviceIdPattern = Pattern.compile("[\\s&;\"](deviceid|deviceId|sdk_guid|UTDID|GUID|guid|Id|ID|id|udid|UDID|MZ)[\" /:=]+([\\w-]+)", 2);

  public Parser() throws IOException {
    this.readConfigs();
  }

  private void readConfigs() throws IOException {
    Yaml yaml = new Yaml(new SafeConstructor());
    InputStream dictionaryStream = Parser.class.getResourceAsStream("/OSConfig.yaml");
    Throwable var3 = null;

    List deviceParserConfigs;
    try {
      deviceParserConfigs = (List)yaml.load(dictionaryStream);
      if (deviceParserConfigs == null) {
        throw new IllegalArgumentException("OSConfig.yaml loading failed.");
      }

      this.osParser = OsParser.fromList(deviceParserConfigs);
    } catch (Throwable var81) {
      var3 = var81;
      throw var81;
    } finally {
      if (dictionaryStream != null) {
        if (var3 != null) {
          try {
            dictionaryStream.close();
          } catch (Throwable var71) {
            var3.addSuppressed(var71);
          }
        } else {
          dictionaryStream.close();
        }
      }

    }

    dictionaryStream = Parser.class.getResourceAsStream("/BrowserConfig.yaml");
    var3 = null;

    try {
      deviceParserConfigs = (List)yaml.load(dictionaryStream);
      if (deviceParserConfigs == null) {
        throw new IllegalArgumentException("BrowserConfig.yaml loading failed.");
      }

      this.browserParser = BrowserParser.fromList(deviceParserConfigs);
    } catch (Throwable var79) {
      var3 = var79;
      throw var79;
    } finally {
      if (dictionaryStream != null) {
        if (var3 != null) {
          try {
            dictionaryStream.close();
          } catch (Throwable var73) {
            var3.addSuppressed(var73);
          }
        } else {
          dictionaryStream.close();
        }
      }

    }

    dictionaryStream = Parser.class.getResourceAsStream("/DeviceConfig.yaml");
    var3 = null;

    try {
      deviceParserConfigs = (List)yaml.load(dictionaryStream);
      if (deviceParserConfigs == null) {
        throw new IllegalArgumentException("DeviceConfig.yaml loading failed.");
      }

      List<DevicePattern> patterns = DeviceParser.patternsFromList(deviceParserConfigs);
      this.deviceParser = new DeviceParser(patterns);
    } catch (Throwable var77) {
      var3 = var77;
      throw var77;
    } finally {
      if (dictionaryStream != null) {
        if (var3 != null) {
          try {
            dictionaryStream.close();
          } catch (Throwable var72) {
            var3.addSuppressed(var72);
          }
        } else {
          dictionaryStream.close();
        }
      }

    }

    dictionaryStream = Parser.class.getResourceAsStream("/DeviceDictionary.txt");
    var3 = null;

    try {
      this.deviceMap = DeviceMap.mapFromFile(dictionaryStream);
    } catch (Throwable var75) {
      var3 = var75;
      throw var75;
    } finally {
      if (dictionaryStream != null) {
        if (var3 != null) {
          try {
            dictionaryStream.close();
          } catch (Throwable var74) {
            var3.addSuppressed(var74);
          }
        } else {
          dictionaryStream.close();
        }
      }

    }

  }

  public UserAgentInfo parse(String agentString) {
    if (agentString == null) {
      return null;
    } else {
      Device device = this.parseDevice(agentString);
      if (device.getDeviceType().equals(DeviceType.Spider)) {
        return this.buildUserAgentInfo(Os.DEFAULT_OS, Browser.DEFAULT_BROWSER, device, Pair.of("-", NetType.Other), "-");
      } else {
        Os os = this.parseOS(agentString);
        Browser browser = this.parseBrowser(agentString);
        Pair<String, NetType> pair = this.parseNetType(agentString);
        if (os == null) {
          os = Os.DEFAULT_OS;
        } else if (os.isTv()) {
          device = Device.DEFAULT_TV;
        } else if (os.isMobile() && !device.isMobile() && device.getDeviceType() != DeviceType.TV) {
          device = Device.DEFAULT_PHONE_SCREEN;
        }

        return this.buildUserAgentInfo(os, browser, device, pair, "-");
      }
    }
  }

  public Pair<String, NetType> parseNetType(String agentString) {
    Matcher matcher = netTypePattern.matcher(agentString.toUpperCase());
    String result = "";
    if (matcher.find()) {
      result = matcher.group(1);
    }

    String key = Strings.isNullOrEmpty(result) ? "-" : result;
    NetType value = this.getNetType(key);
    return Pair.of(key, value);
  }

  public String parseDeviceId(String agentString) {
    Matcher matcher = deviceIdPattern.matcher(agentString.toLowerCase());
    String result = "";
    if (matcher.find()) {
      result = matcher.group(2);
    }

    return !Strings.isNullOrEmpty(result) && result.length() >= 8 ? result : "-";
  }

  public Device parseDevice(String agentString) {
    Device device = this.deviceParser.parse(agentString);
    return this.deviceMap.parseDevice(device);
  }

  public Browser parseBrowser(String agentString) {
    return this.browserParser.parse(agentString);
  }

  public Os parseOS(String agentString) {
    return this.osParser.parse(agentString);
  }

  private DeviceType getDeviceType(String deviceType) {
    if (Strings.isNullOrEmpty(deviceType)) {
      return DeviceType.Other;
    } else {
      String key = deviceType.trim();
      if (!dMap.containsKey(key)) {
        synchronized(LOCK) {
          if (!dMap.containsKey(key)) {
            DeviceType item = DeviceType.parseOf(key);
            dMap.put(key, item);
          }
        }
      }

      return (DeviceType)dMap.get(key);
    }
  }

  private NetType getNetType(String netType) {
    if (Strings.isNullOrEmpty(netType)) {
      return NetType.Other;
    } else {
      String key = netType.trim();
      if (!netMap.containsKey(key)) {
        synchronized(LOCK) {
          if (!netMap.containsKey(key)) {
            NetType item = NetType.parseOf(key);
            netMap.put(key, item);
          }
        }
      }

      return (NetType)netMap.get(key);
    }
  }

  private OsType getOsType(String os) {
    if (Strings.isNullOrEmpty(os)) {
      return OsType.Other;
    } else {
      String key = os.trim();
      if (!osMap.containsKey(key)) {
        synchronized(LOCK) {
          if (!osMap.containsKey(key)) {
            OsType item = OsType.parseOf(key);
            osMap.put(key, item);
          }
        }
      }

      return (OsType)osMap.get(key);
    }
  }

  private UserAgentInfo buildUserAgentInfo(Os os, Browser browser, Device device, Pair<String, NetType> netTypePair, String deviceId) {
    UserAgentInfo userAgentInfo = new UserAgentInfo();
    String detail;
    if (!"-".equalsIgnoreCase(os.getFamily()) && !StringUtils.isEmpty(os.getMajor())) {
      detail = StringUtils.isEmpty(os.getMinor()) ? os.getFamily() + " " + os.getMajor() : os.getFamily() + " " + os.getMajor() + "." + os.getMinor();
    } else {
      detail = os.getFamily();
    }

    String osVersion = StringUtils.replace(os.getFamily(), os.getBrand(), "").trim();
    userAgentInfo.setOsName(os.getBrand());
    userAgentInfo.setOsDetail(detail);
    userAgentInfo.setOsType(this.getOsType(os.getBrand()).getValue());
    userAgentInfo.setOsVersion(osVersion);
    if (StringUtils.isEmpty(browser.getMajor())) {
      detail = browser.getFamily();
    } else {
      detail = StringUtils.isEmpty(browser.getMinor()) ? browser.getFamily() + " " + browser.getMajor() : browser.getFamily() + " " + browser.getMajor() + "." + browser.getMinor();
    }

    String browserVersion = StringUtils.replace(detail, browser.getBrand(), "").trim();
    userAgentInfo.setBrowserName(browser.getBrand());
    userAgentInfo.setBrowserDetail(detail);
    userAgentInfo.setBrowserVersion(browserVersion);
    String deviceType = device.getDeviceType().toString();
    userAgentInfo.setDeviceBrand(device.getBrand());
    userAgentInfo.setDeviceName(device.getFamily());
    userAgentInfo.setDeviceType(deviceType);
    userAgentInfo.setIsMobile(device.isMobile());
    userAgentInfo.setIntDeviceType(this.getDeviceType(deviceType).getValue());
    userAgentInfo.setNetType((CharSequence)netTypePair.getKey());
    userAgentInfo.setIntNetType(((NetType)netTypePair.getValue()).getValue());
    return userAgentInfo;
  }
}
