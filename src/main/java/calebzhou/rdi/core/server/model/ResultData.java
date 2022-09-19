package calebzhou.rdi.core.server.model;


public class ResultData<T>{
	private int status;
	private String message;
	private T data ;

	public ResultData(int status, String message, T data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public int getStatus() {
		return status;
	}

	public String getMessage() {
		return message;
	}

	public T getData() {
		return data;
	}

	public boolean isSuccess(){
		return status>0;
	}
}
