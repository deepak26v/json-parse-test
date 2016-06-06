package com.org.parse;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.*;

public class UserDataDeserializer extends JsonDeserializer<UserEnteredData> {

    public static final String APP_DATA = "appData";
    public static final String META_DATA = "metaData";

    @Override
    public UserEnteredData deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode jsonNode = jsonParser.getCodec().readTree(jsonParser);

        Object object1 = ((ObjectNode)jsonNode.get(APP_DATA));
        Object object2 = jsonNode.get(META_DATA);

        UserEnteredData userEnteredData = new UserEnteredData();
        Iterator<Map.Entry<String, JsonNode>> nodeIterator = ((ObjectNode)object1).fields();
        iterateOverTopLevelElement(userEnteredData, nodeIterator);
        nodeIterator = ((ObjectNode)object2).fields();
        iterateOverTopLevelElement(userEnteredData, nodeIterator);

        return userEnteredData;
    }

    private void iterateOverTopLevelElement(UserEnteredData userEnteredData, Iterator<Map.Entry<String, JsonNode>> nodeIterator) {
        while (nodeIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodeIterator.next();

            String key = entry.getKey();
            Object value = entry.getValue();

            if(value instanceof TextNode) {
                userEnteredData.getDataMap().put(key, ((TextNode) value).textValue());
            } else if(value instanceof ObjectNode){
                //Being a single object node, we will store this as single item in the list
                List<Map<String, String>> userDataList = new ArrayList<Map<String, String>>();
                userEnteredData.getDataListMap().put(key, userDataList);
                recurseOverObject(key, value, userEnteredData, 0);
            } else if(value instanceof ArrayNode) {
                List<Map<String, String>> userDataList = new ArrayList<Map<String, String>>();
                userEnteredData.getDataListMap().put(key, userDataList);
                recurseOverArray(key, value, userEnteredData);
            }

        }
    }

    private void recurseOverArray(String key, Object object, UserEnteredData userEnteredData) {
        Iterator<JsonNode> nodeIterator = ((ArrayNode)object).iterator();

        int arrayIndex = -1;
        while (nodeIterator.hasNext()) {
            ObjectNode childObject = (ObjectNode) nodeIterator.next();
            Iterator<Map.Entry<String, JsonNode>> childNodeIterator = ((ObjectNode) childObject).fields();
            recurseOverObject(key, childObject, userEnteredData, ++arrayIndex);
        }
    }

    private void recurseOverObject(String key, Object object, UserEnteredData userEnteredData, int index) {
        Iterator<Map.Entry<String, JsonNode>> nodeIterator = ((ObjectNode)object).fields();

        List<Map<String, String>> dataMapList = userEnteredData.getDataListMap().get(key);
        //Each JSON object (block of elements) would be a new Map in the same list
        //For an Object, there would be a single map
        //For an array of object, each object would be in its own mapß
        Map<String, String> dataMap = new LinkedHashMap<String, String>();;
        dataMapList.add(dataMap);

        while (nodeIterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodeIterator.next();

            String childKey = entry.getKey();
            Object value = entry.getValue();

            if(value instanceof TextNode) {
                //Get the Map specific to the current list index
                dataMap = dataMapList.get(index);
                dataMap.put(childKey, ((TextNode) value).textValue());
            }
        }
    }
}