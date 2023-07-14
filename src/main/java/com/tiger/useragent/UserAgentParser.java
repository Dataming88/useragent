//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.tiger.useragent;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.apache.avro.Schema;
import org.apache.avro.file.DataFileReader;
import org.apache.avro.file.DataFileWriter;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericData.StringType;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAgentParser extends Parser {
  private static final Logger logger = LoggerFactory.getLogger(UserAgentParser.class);
  private static final String CACHE_FILE = "./UserAgentCache.dat";
  private static final String CACHE_VERSION_KEY = "cache.version";
  private static final long CACHE_VERSION_VALUE = 1L;
  private static final Schema CACHE_SCHEMA = Schema.createMap(UserAgentInfo.getClassSchema());
  private static UserAgentParser instance;
  private final LoadingCache<String, UserAgentInfo> cache;

  public UserAgentParser(LoadingCache<String, UserAgentInfo> cache) throws IOException {
    this.cache = cache;
    this.loadCache();
  }

  public UserAgentParser() throws IOException {
    CacheLoader<String, UserAgentInfo> loader = new CacheLoader<String, UserAgentInfo>() {
      public UserAgentInfo load(String key) {
        return UserAgentParser.this.parse(key);
      }
    };
    this.cache = CacheBuilder.newBuilder().expireAfterAccess(10L, TimeUnit.MINUTES).build(loader);
    this.loadCache();
  }

  public static UserAgentParser getInstance() throws IOException {
    if (instance == null) {
      synchronized(LOCK) {
        if (instance == null) {
          instance = new UserAgentParser();
        }
      }
    }

    return instance;
  }

  public static UserAgentParser newInstance() throws IOException {
    return new UserAgentParser();
  }

  public UserAgentInfo getUserAgentInfo(String agentString) {
    return (UserAgentInfo)this.cache.getUnchecked(agentString);
  }

  public UserAgentInfo parseUserAgent(String agentString) {
    return this.parse(agentString);
  }

  public void loadCache() {
    this.loadCache("./UserAgentCache.dat");
  }

  public void loadCache(String filePath) {
    DatumReader<Map<String, UserAgentInfo>> datumReader = new SpecificDatumReader(CACHE_SCHEMA);
    DataFileReader reader = null;

    try {
      reader = new DataFileReader(new File(filePath), datumReader);
      if (reader.getMetaLong("cache.version") != 1L) {
        logger.warn("UserAgentCache version NOT matched, Expected {}, Actual {}.", 1L, reader.getMetaLong("cache.version"));
        return;
      }

      while(reader.hasNext()) {
        this.cache.putAll((Map)reader.next());
      }
    } catch (IOException var16) {
      logger.warn("Deserialize UserAgentInfo FAILED when opening: {}", filePath);
    } catch (Exception var17) {
      logger.warn("Deserialize UserAgentInfo FAILED: ", var17);
    } finally {
      try {
        if (null != reader) {
          reader.close();
        }
      } catch (IOException var15) {
        logger.warn("close DataFileReader FAILED: ", var15);
      }

    }

  }

  public void saveCache() {
    this.saveCache("./UserAgentCache.dat");
  }

  public void saveCache(String filePath) {
    DatumWriter<Map<String, UserAgentInfo>> datumWriter = new SpecificDatumWriter(CACHE_SCHEMA);
    DataFileWriter writer = null;

    try {
      writer = new DataFileWriter(datumWriter);
      writer.setMeta("cache.version", 1L);
      writer.create(CACHE_SCHEMA, new File(filePath));
      writer.append(this.cache.asMap());
    } catch (IOException var13) {
      logger.warn("Serialize UserAgentInfo FAILED: ", var13);
    } finally {
      try {
        if (null != writer) {
          writer.close();
        }
      } catch (IOException var12) {
        logger.warn("close DataFileWriter FAILED: ", var12);
      }

    }

  }

  static {
    GenericData.setStringType(CACHE_SCHEMA, StringType.String);
  }
}
