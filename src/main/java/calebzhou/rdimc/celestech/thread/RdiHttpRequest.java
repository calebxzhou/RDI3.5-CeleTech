package calebzhou.rdimc.celestech.thread;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

//封装HTTP请求
public class RdiHttpRequest implements Comparable<RdiHttpRequest>{
    public static final int HIGH_PRIORITY=3;
    public static final int MEDIUM_PRIORITY=2;
    public static final int LOW_PRIORITY=1;
    public static final int NO_PRIORITY=0;

    @Override
    public int compareTo(@NotNull RdiHttpRequest request) {
        return priority-request.priority;

    }

    public RdiHttpRequest(Type type, String url, String... params) {
        this.type = type;
        this.url = url;
        this.params = params;
        this.priority=NO_PRIORITY;
    }

    public RdiHttpRequest(Type type, String url, int priority, String... params) {
        this.type = type;
        this.url = url;
        this.params = params;
        this.priority = priority;
    }

    //http请求类型
    public Type type;
    //请求url
    public String url;
    public String[] params;
    int priority;

    public NameValuePair[] getParamPairs(){
        if(params==null)
            return null;
        List<NameValuePair> paramList = new ObjectArrayList<>();
        for (String param : params) {
            String[] split = param.split("=");
            paramList.add(new BasicNameValuePair(split[0],split[1]));
        }
        return paramList.toArray(new NameValuePair[0]);
    }
    public enum Type{
        get,post,delete,put;
        public String getHttpType(){
            return this.toString().toUpperCase();
        }
    }
}
