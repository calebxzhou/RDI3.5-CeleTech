package calebzhou.rdimc.celestech.utils;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class JsonUtils {
    private static Gson gson=null;
    public static Gson getGson(){
        if(gson==null)
            gson = new Gson();
        return gson;
    }
    public static <T> List<T> stringToArray(String s, Class<T[]> clazz) {
        T[] arr = new Gson().fromJson(s, clazz);
        return Arrays.asList(arr); //or return Arrays.asList(new Gson().fromJson(s, clazz)); for a one-liner
    }
}
