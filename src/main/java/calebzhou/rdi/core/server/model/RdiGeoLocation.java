package calebzhou.rdi.core.server.model;


import org.apache.commons.lang3.builder.EqualsBuilder;

import java.util.Objects;

public class RdiGeoLocation {
    public String nation;
    public String province;
    public String city;
    public String district;
    public String isp;
    public GeoLocation location;

	//两个地理位置 经纬度一样就是相同
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		RdiGeoLocation that = (RdiGeoLocation) o;
		return new EqualsBuilder().append(location.latitude, that.location.latitude).append(location.longitude, that.location.longitude).isEquals();
	}
}
