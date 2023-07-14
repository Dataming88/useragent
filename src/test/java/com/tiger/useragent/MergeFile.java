package com.tiger.useragent;

import com.tiger.useragent.device.DeviceMap;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class MergeFile {
    public static void main(String[] args) throws IOException {
        String newfileName = "./useragent-parser/output.txt";
        String oldfileName = "./useragent-parser/src/main/resources/DeviceDictionary_Auto.txt";
        String oldDeviceName = "./useragent-parser/src/main/resources/DeviceDictionary.txt";

        HashMap<String, String> newMap = new HashMap<>();
        HashMap<String, String> oldMap = new HashMap<>();
        HashMap<String, String> oldDeviceMap = new HashMap<>();

        readFile(newfileName, newMap);
        readFile(oldfileName, oldMap);
        readFile(oldDeviceName, oldDeviceMap);

//        oldDeviceMap.putAll(oldMap);
        newMap.putAll(oldMap);


        for (Map.Entry<String, String> entry : newMap.entrySet()) {

            System.out.println(entry.getValue());
        }
        System.out.println(newMap.size());

    }


    public static void readFile(String fileName, HashMap<String, String> dataMap) throws IOException {
        FileReader fileReader = new FileReader(fileName);
        BufferedReader reader = new BufferedReader(fileReader);
        String line;
        while ((line = reader.readLine()) != null) {
            if(line.startsWith("#")){
                continue;
            }
            String keyStr = line.split(",,")[0];
            dataMap.put(keyStr, line);
        }
    }
}
