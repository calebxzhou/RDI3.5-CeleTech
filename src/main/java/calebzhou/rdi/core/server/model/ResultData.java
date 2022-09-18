package calebzhou.rdi.core.server.model;


public class ResultData{
	private int status;
	private String message;
	private Object data ;

	public ResultData(int status, String message, String data) {
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

	public Object getData() {
		return data;
	}

	public boolean isSuccess(){
		return status>0;
	}
}
