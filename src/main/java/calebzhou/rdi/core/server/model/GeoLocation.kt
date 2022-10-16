package calebzhou.rdi.core.server.model

/**
 * Created by calebzhou on 2022-10-03,21:09.
 */

/**
 *  地理天气
 */
//经度纬度
data class GeoLocation(val latitude:Double,val longitude:Double)
//RDI地址位置
data class RdiGeoLocation(val nation: String, val province: String, val city: String,
                     val district: String, val isp: String, val location: GeoLocation
){
    val cityShort:String get() = city.replace("市","").replace("特别行政区","")
    val districtShort:String get() = district.replace("区", "").replace("市", "").replace("县","")
}
