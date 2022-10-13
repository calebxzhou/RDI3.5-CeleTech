package calebzhou.rdi.core.server.constant

/**
 * Created by calebzhou on 2022-10-04,18:48.
 */
enum class ResponseCode(var code:Int, var msg:String) {
    Success(200,"成功"),
    NotFound(-404,"路径找不到"),
    AccessDenied(-403,"权限不足"),
    TooManyRequests(-429,"too many requests"),
    InternalError(-500,"服务器内部错误"),
    ParameterNotPresent(-500,"参数错误"),
    TokenInvalid(-401,"登录失效，请重新登录！"),
    //账号系列
    SourceAlreadyRegistered(-10,"请求者已经注册过一个账号了"),
    SourceNotRegistered(-11,"请求者没注册账号"),
    SourceIdNotMatchPassword(-11,"请求者的用户名和密码不匹配"),
    //岛屿系列
    SourceAlreadyOwnIsland (-100,"请求者已经拥有岛屿"),
    SourceAlreadyJoinAnyIsland(-101,"请求者已经加入过了一个岛屿"),
    SourceNotOwnIsland (-102,"请求者未拥有岛屿"),
    SourceNotJoinAnyIsland(-103,"请求者未加入任何一个岛屿"),
    SourceNotInsideOwnIsland(-104,"请求者不在自己的岛屿上"),

    TargetAlreadyOwnIsland(-110,"目标已经拥有岛屿"),
    TargetAlreadyJoinAnyIsland(-111,"目标已经加入过了一个岛屿"),
    TargetNotOwnIsland(-112,"目标未拥有岛屿"),
    TargetNotJoinAnyIsland(-113,"目标未加入任何一个岛屿"),

    SourceEqualsTarget(-120,"请求者和目标相同"),
    SourceNotOwnTarget(-121,"请求者所操作的资源不属于自己"),


}
