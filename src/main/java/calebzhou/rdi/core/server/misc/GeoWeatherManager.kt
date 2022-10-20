package calebzhou.rdi.core.server.misc

import calebzhou.rdi.core.server.constant.Skycon
import calebzhou.rdi.core.server.model.GeoLocation
import calebzhou.rdi.core.server.model.RdiGeoLocation
import calebzhou.rdi.core.server.model.RdiWeather
import calebzhou.rdi.core.server.utils.PlayerUtils
import calebzhou.rdi.core.server.utils.RdiHttpClient
import it.unimi.dsi.fastutil.Pair
import it.unimi.dsi.fastutil.objects.Object2CharOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap
import net.minecraft.ChatFormatting
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.server.level.ServerPlayer
import kotlin.math.roundToInt

/**
 * Created by calebzhou on 2022-10-16,8:28.
 */
object GeoWeatherManager {
    //pid vs 玩家地理位置
    val pidGeoMap = Object2ObjectOpenHashMap<String, RdiGeoLocation>()
    //pid vs 玩家天气
    val pidWeatherMap = Object2ObjectOpenHashMap<String, RdiWeather>()


    fun getGeoLocation(pid:String,ip:String) :RdiGeoLocation{
        val resp = RdiHttpClient.sendRequest(RdiGeoLocation::class,"get","/v37/public/ip2loca", Pair.of("ip",ip))
        if(resp.isSuccess){
            pidGeoMap[pid] = resp.data
        }
        return resp.data!!
    }
    fun getWeather(pid:String,loca:GeoLocation):RdiWeather{
        val resp = RdiHttpClient.sendRequest(RdiWeather::class,"get","/v37/public/weather", Pair.of("latitude",loca.latitude),Pair.of("longitude",loca.longitude))
        if(resp.isSuccess){
            pidWeatherMap[pid] = resp.data
        }
        return resp.data!!
    }
    fun clearForPlayer(pid:String){
        pidGeoMap.remove(pid)
        pidWeatherMap.remove(pid)
    }
    fun sendToPlayer(player: ServerPlayer){
        val geo = pidGeoMap[player.stringUUID]?:let {
            PlayerUtils.sendChatMessage(player,"无法获取天气预报")
            return
        }
        val weather = pidWeatherMap[player.stringUUID]?:let {
            PlayerUtils.sendChatMessage(player,"无法获取天气预报")
            return
        }
        //实时温度
        val rtw = weather.realTimeWeather
        val line1 = Component.literal(
            if(geo.nation=="中国")
                "${geo.cityShort}-${geo.districtShort}"
            else
                "${geo.city},${geo.province},${geo.nation}"
        )
                //天气状况Emoji
            .append("${Skycon.valueOf(rtw.skycon).desc} ")
            //天气状况描述
            .append("${rtw.skyDesc} ")
                //温度与感受
            .append("\uD83C\uDF21 ${rtw.temp.roundToInt()}℃ ${rtw.feel} ")
            //紫外线
            .append("紫外线${rtw.uv} ")
                //湿度
            .append("\uD83D\uDCA6 ${rtw.humi*100}% ")
                //能见度
            .append("\uD83D\uDC40 ${rtw.visi.roundToInt()}km ")
                //风速风向
            .append("\uD83C\uDF2C ${rtw.windSpd}km/h ${rtw.windDir}°")
                //气压
            .append("\uD83D\uDCD0 ${rtw.pres.roundToInt()}Pa")
                //空气
            .append("\uD83D\uDE24 ${rtw.aqi}(${rtw.aqiDesc})")
                //降水
            .append("\uD83C\uDF27 ${rtw.rainDesc}")

        PlayerUtils.sendChatMessage(player,line1)

    }
    fun getTemperatureDisplayComponent(temp:Int):MutableComponent{
        return Component.literal("${temp}℃").withStyle(
            if (temp >=40)
                ChatFormatting.DARK_RED
            else if (temp in 31..40)
                ChatFormatting.RED
            else if (temp in 21..30)
                ChatFormatting.GOLD
            else if (temp in 11..20)
                ChatFormatting.GREEN
            else if (temp in 0 .. 10)
                ChatFormatting.AQUA
            else if (temp in -10 .. 0)
                ChatFormatting.BLUE
            else
                ChatFormatting.DARK_BLUE
        )
    }
    fun getIspCode(ispName:String):Char{
        return if(ispCodeMap.containsKey(ispName)){
            ispCodeMap.getChar(ispName)
        }else if(ispName.contains("广电")){
            'g'
        }else
            '*'

    }
    fun getProvinceCode(geo:RdiGeoLocation):String{
        val code = provinceCodeMap.getInt(geo.province.subSequence(0,2))
        return if(code==0 && geo.nation!="中国")
            geo.nation.split("|")[1]
        else code.toString()
    }
    private val provinceCodeMap = Object2IntOpenHashMap<String>()
    private val ispCodeMap = Object2CharOpenHashMap<String>()
    init{
        ispCodeMap["联通"]='u'
        ispCodeMap["电信"]='t'
        ispCodeMap["移动"]='m'
        ispCodeMap["铁通"]='m'
        ispCodeMap["鹏博士"]='p'
        ispCodeMap["方正宽带"]='f'
        ispCodeMap["教育网"]='e'

        provinceCodeMap["北京"]=11
        provinceCodeMap["天津"]=12
        provinceCodeMap["河北"]=13
        provinceCodeMap["山西"]=14
        provinceCodeMap["内蒙"]=15

        provinceCodeMap["辽宁"]=21
        provinceCodeMap["吉林"]=22
        provinceCodeMap["黑龙"]=23

        provinceCodeMap["上海"]=31
        provinceCodeMap["江苏"]=32
        provinceCodeMap["浙江"]=33
        provinceCodeMap["安徽"]=34
        provinceCodeMap["福建"]=35
        provinceCodeMap["江西"]=36
        provinceCodeMap["山东"]=37

        provinceCodeMap["河南"]=41
        provinceCodeMap["湖北"]=42
        provinceCodeMap["湖南"]=43
        provinceCodeMap["广东"]=44
        provinceCodeMap["广西"]=45
        provinceCodeMap["海南"]=46

        provinceCodeMap["重庆"]=50
        provinceCodeMap["四川"]=51
        provinceCodeMap["贵州"]=52
        provinceCodeMap["云南"]=53
        provinceCodeMap["西藏"]=54

        provinceCodeMap["陕西"]=61
        provinceCodeMap["甘肃"]=62
        provinceCodeMap["青海"]=63
        provinceCodeMap["宁夏"]=64
        provinceCodeMap["新疆"]=65
        provinceCodeMap["台湾"]=71
        provinceCodeMap["香港"]=81
        provinceCodeMap["澳门"]=82
    }
}
