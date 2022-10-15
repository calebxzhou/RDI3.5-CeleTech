package calebzhou.rdi.core.server;


import calebzhou.rdi.core.server.utils.ServerUtils;
import net.minecraft.network.chat.Component;

//服务器的流畅程度
public class ServerLaggingStatus {

	//当前tick时间 与下次应该tick的时间 相距多长（流畅的时候是-50）
	private static long msBehind;

	//落后这些ms代表卡顿
	private static final long behindThreshold = 1;

	//更新落后的tick时间
	public static void updateMilliSecondsBehind(long msBehind){
		ServerLaggingStatus.msBehind=msBehind;
		if(msBehind>0)
			RdiCoreServer.LOGGER.info("服务器tick时间 落后{}ms",msBehind);
		/*if(isServerVeryLagging()){
			ServerUtils.broadcastActionBarMessage(Component.literal("正在存档与清理内存"));
		}*/

	}

	public static long getMsBehind() {
		return msBehind;
	}

	public static boolean isServerVeryLagging(){
		return msBehind > 200;
	}
	public static boolean isServerLagging(){
		return msBehind >behindThreshold;
	}
}
