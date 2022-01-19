package calebzhou.rdimc.celestech.model;

import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

public class ApiResponse implements Serializable {
    private String type;
    private String message;
    private Object data;

    public ApiResponse() {
    }

    public ApiResponse(String type, String message, @Nullable Object data) {
        this.type = type;
        this.message = message;
        this.data = data;
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
    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
