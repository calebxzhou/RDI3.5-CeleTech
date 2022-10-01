package calebzhou.rdi.core.server.model;

/**
 * Created by calebzhou on 2022-09-18,22:40.
 */
public class RdiUser {

	private final String uuid;
	private final String name;
	private final String pwd;
	private final String type;

	public String getUuid() {
		return uuid;
	}

	public String getName() {
		return name;
	}

	public String getPwd() {
		return pwd;
	}

	public String getType() {
		return type;
	}

	public RdiUser(String uuid, String name, String pwd, String type) {
		this.uuid = uuid;
		this.name = name;
		this.pwd = pwd;
		this.type = type;
	}
	public boolean isGenuine(){
		return "mojang".equals(type);
	}
}
