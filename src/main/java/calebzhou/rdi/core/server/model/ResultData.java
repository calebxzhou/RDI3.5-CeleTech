package calebzhou.rdi.core.server.model;


public record ResultData(int status, String message, String data) {
	public boolean isSuccess(){
		return status>0;
	}
}
