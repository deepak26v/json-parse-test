package com.org.parse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by deepakvalechha on 6/5/16.
 */
public class JsonParser {
    public static void main(String[] args) throws IOException {
        InputStream jsonData = new FileInputStream(new File("/Users/deepakvalechha/IdeaProjects/json-parse-test/src/main/resources/data.json"));

        UserEnteredData userEnteredData = new ObjectMapper().readValue(jsonData, UserEnteredData.class);

        String val = getFromUserEnteredData(userEnteredData, "channel");
        System.out.println(val + "," + val.length());
    }

    private static String getFromUserEnteredData(UserEnteredData userEnteredData, String key) {
        String value = "";
        //First check happens on one-to-one map
        Map<String, String> dataMap = userEnteredData.getDataMap();
        if(dataMap != null && dataMap.containsKey(key)) {
            value = dataMap.get(key);
        } else {
            Map<String, List<Map<String, String>>> dataListMap = userEnteredData.getDataListMap();
            if(dataListMap != null) {
                Iterator<String> iterator = dataListMap.keySet().iterator();
                while(iterator.hasNext()) {
                    String mapKey = iterator.next();
                    List<Map<String, String>> dataMapList = dataListMap.get(mapKey);

                    for(Map<String, String> map : dataMapList) {
                        if(map != null && map.containsKey(key)) {
                            value = map.get(key);
                            break;
                        }
                    }
                }
            }
        }
        return value;
    }
}
