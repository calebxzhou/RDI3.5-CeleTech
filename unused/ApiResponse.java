package calebzhou.rdi.celestech.model;

import com.google.gson.Gson;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class ApiResponse<T extends Serializable> implements Serializable {
    private String type;//success error info
    private String message;
    private String data;

    public ApiResponse(String type, String message, String data) {
        this.type = type;
        this.message = message;
        this.data = data;
    }

    public ApiResponse() {
    }
    public boolean isSuccess(){
        return type.equalsIgnoreCase("success");
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Nullable
    public T getData(Class<T> modelTypeClz) {
        return new Gson().fromJson((String) data,modelTypeClz);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
