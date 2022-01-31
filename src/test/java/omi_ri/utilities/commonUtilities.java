package omi_ri.utilities;

import java.util.*;

public class commonUtilities {

    public static boolean testHasFailed;

    public static HashMap<String,String> buildTestDataHeader(Object data){
        HashMap<String,String> dataMap = new HashMap<>();
        List<?> dataArray = convertObjectToList(data);
        for(int i=0;i<dataArray.size();i++){
            dataMap.put(String.valueOf(dataArray.get(i)), String.valueOf(i));
        }
        return dataMap;
    }

    public static HashMap<String,String> buildScenarioTestData(HashMap<String,String> scenarioHeadersMap,Object data){
        HashMap<String,String> dataMap = new HashMap<>();
        List<?> dataArray = convertObjectToList(data);
        int i = 0;
        for (String key : scenarioHeadersMap.keySet()) {
            int keyIndexData = Integer.parseInt(scenarioHeadersMap.get(key));
            dataMap.put(key, String.valueOf(dataArray.get(keyIndexData)));
        }
        return dataMap;
    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[])obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>)obj);
        }
        return list;
    }



}
