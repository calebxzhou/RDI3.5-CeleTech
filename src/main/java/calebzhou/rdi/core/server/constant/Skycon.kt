package calebzhou.rdi.core.server.constant;


public enum WeatherConst {

    CLEAR_DAY("\uD83C\uDF1E", 0),
    CLEAR_NIGHT("\uD83C\uDF1E \uD83C\uDF19 ", 1),
    PARTLY_CLOUDY_DAY("\uD83C\uDF24️", 2),
    PARTLY_CLOUDY_NIGHT("\uD83C\uDF24️\uD83C\uDF19", 3),
    CLOUDY("☁️", 4),

    LIGHT_HAZE("\uD83C\uDF2B 轻霾", 5),
    MODERATE_HAZE("\uD83C\uDF2B 中霾", 6),
    HEAVY_HAZE("\uD83C\uDF2B重霾", 7),

    LIGHT_RAIN("🌧️ ", 8),
    MODERATE_RAIN("🌧️ ", 9),
    HEAVY_RAIN("🌧️ ", 10),
    STORM_RAIN("🌧️ ", 11),

    FOG("\uD83C\uDF2B 雾", 12),

    LIGHT_SNOW("❄️ ", 13),
    MODERATE_SNOW("❄️ ", 14),
    HEAVY_SNOW("❄️ ", 15),
    STORM_SNOW("❄️ ", 16),
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
