package baseball.baseballDiary.auth.utils;

import baseball.baseballDiary.common.utils.LocalDateAdapter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class JsonUtil {

    static ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();

    public static Object jsonToObject(String str, Object obj) throws Exception {
        Object result = mapper.readValue(str, obj.getClass());
        return result;
    }

    public static String objectToJson(Object obj) throws Exception {
        String result = mapper.writeValueAsString(obj);
        return result;
    }

    public static <T> T mapToObject(Map map, Class<T> valueType) throws Exception{
        return mapper.readValue(mapper.writeValueAsString(map), valueType);
    }

    public static Map<String,Object> objectToMap(Object obj) throws Exception{
        mapper.registerModule(new JavaTimeModule());
        return mapper.readValue(mapper.writeValueAsString(obj), Map.class);
    }

    public static Map<String, String> jsonToMap(String json) throws Exception{
        if (StringUtils.isEmpty(json)) {
            return null;
        }
        return  mapper.readValue(json, Map.class);
    }

    public static Map<String,Object> checkParams(Map<String,Object> oldParams){
        Map<String,Object> params = new HashMap<>();
        for (String key : oldParams.keySet()){
            if (null == oldParams.get(key)){
                continue;
            }else {
                params.put(key,oldParams.get(key));
            }
        }
        return params;
    }

    public static String beanToJson(Object obj){
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class,new LocalDateAdapter()).create();
        String s = gson.toJson(obj);
        return s;
    }

    public static <T> T jsonToObjectNoException(String content, Class<T> valueType) {
        try {
            if(content != null) {
                return mapper.readValue(content, valueType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String objectToJsonNoException(Object obj) {
        try {
            if(obj != null) {
                return mapper.writeValueAsString(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static <T> T ObjectMutualCovertMap(Object object, Class<T> valueType) {
        return mapper.convertValue(object, valueType);
    }

}
