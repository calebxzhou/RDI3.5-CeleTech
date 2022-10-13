package calebzhou.rdi.core.server.model

import calebzhou.rdi.core.server.constant.ResponseCode
import calebzhou.rdi.core.server.utils.RdiSerializer

/**
 * Created by calebzhou on 2022-10-04,18:42.
 */
class ResponseData<T>(val stat:Int, val msg:String,var data:T?){
    constructor(responseCode: ResponseCode,data: T?) : this(responseCode.code,responseCode.msg,data)
    constructor(responseCode: ResponseCode) : this(responseCode,null)

    override fun toString(): String {
        return RdiSerializer.gson.toJson(this)
    }
    companion object{

    }
}
