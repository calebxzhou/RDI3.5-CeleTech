package calebzhou.rdi.core.server.model

import java.sql.Timestamp

/**
 * Created by calebzhou on 2022-10-03,21:11.
 */
/**
 *  岛屿信息
*/

//岛屿位置
data class Island2Loca (var id :Int ,var iid :Int,
var x :Double, var y :Double, var z :Double, var w :Double, var p :Double)
//岛屿成员
data class Island2Crew (var id:Int,var pid:String,var iid:Int,var ts:Timestamp)
//岛屿
data class Island2(var iid:Int, var pid:String, var ts: Timestamp,
                   var loca: Island2Loca, var crews:List<Island2Crew>){
}
data class Island2CrewVo(val player: RdiPlayer,val joinTime:Timestamp?)
data class Island2Vo(val iid:Int,
                     val owner:RdiPlayer,
                     val createTime:Timestamp,
                     val loca:Island2Loca, val crews:List<Island2CrewVo> ) {
}
