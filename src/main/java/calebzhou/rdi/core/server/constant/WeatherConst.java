package calebzhou.rdi.core.server.constant;

import calebzhou.rdi.core.server.constant.ColorConst;

public enum WeatherConst {

    CLEAR_DAY("\uD83C\uDF1E", 0),
    CLEAR_NIGHT("\uD83C\uDF1E \uD83C\uDF19 ", 1),
    PARTLY_CLOUDY_DAY(":mostly_sunny: ", 2),
    PARTLY_CLOUDY_NIGHT(":mostly_sunny: :crescent_moon:", 3),
    CLOUDY(":cloud: ", 4),

    LIGHT_HAZE(":fog: 轻霾", 5),
    MODERATE_HAZE(":fog: 中霾", 6),
    HEAVY_HAZE(":fog: 重霾", 7),

    LIGHT_RAIN(":rain_cloud: ", 8),
    MODERATE_RAIN(":rain_cloud: ", 9),
    HEAVY_RAIN(":rain_cloud: ", 10),
    STORM_RAIN(":rain_cloud: ", 11),

    FOG(":fog: 雾", 12),

    LIGHT_SNOW(":snowflake: ", 13),
    MODERATE_SNOW(":snowflake: ", 14),
    HEAVY_SNOW(":snowflake: ", 15),
    STORM_SNOW(":snowflake: ", 16),
    DUST("浮尘", 17),
    SAND("沙尘", 18),
    WIND("\uD83D\uDCA8 ", 19);
    private String name;
    private int index;

    // 构造方法
    private WeatherConst(String name, int index) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }
}
