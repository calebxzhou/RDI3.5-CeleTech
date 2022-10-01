package calebzhou.rdi.core.server.model;


import calebzhou.rdi.core.server.utils.RdiSerializer;

public class RdiWeather {
	public String alert;
	public double temperature;
	public double humidity;
	public String skycon;
	public double visibility;
	public double windSpeed;
	public double windDirection;
	public int aqiValue;
	public String aqiDescription;
	public double rainProba;
	public String minuteRainDescription;
	public String hourlyDescr;
	public String sunRiseTime;
	public String sunSetTime;
	public double lowTemp;
	public double highTemp;

	@Override
	public String toString() {
		return RdiSerializer.GSON.toJson(this);
	}
}
