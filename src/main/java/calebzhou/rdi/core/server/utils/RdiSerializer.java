package calebzhou.rdi.core.server.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class RdiSerializer {
    public static final Gson GSON=new GsonBuilder().serializeNulls().create();

}
