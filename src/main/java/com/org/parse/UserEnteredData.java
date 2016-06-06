package com.org.parse;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@JsonDeserialize(using = UserDataDeserializer.class)
public class UserEnteredData {
    private Map<String, String> dataMap = new LinkedHashMap<String, String>();

    private Map<String, List<Map<String, String>>> dataListMap = new LinkedHashMap<String, List<Map<String, String>>>();

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }

    public Map<String, List<Map<String, String>>> getDataListMap() {
        return dataListMap;
    }

    public void setDataListMap(Map<String, List<Map<String, String>>> dataListMap) {
        this.dataListMap = dataListMap;
    }
}
