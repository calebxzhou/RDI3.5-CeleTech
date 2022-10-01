package calebzhou.rdi.core.server.model;

import calebzhou.rdi.core.server.utils.RdiSerializer;

import java.io.Serializable;

/**
 * Created by calebzhou on 2022-09-20,16:16.
 */
public class GeoLocation implements Serializable {
	public double latitude;
	public double longitude;

	public GeoLocation(double latitude, double longitude) {
		this.latitude = latitude;
		this.longitude = longitude;
	}

	@Override
	public String toString() {
		return RdiSerializer.GSON.toJson(this);
	}
}
