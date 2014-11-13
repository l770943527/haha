package common;

public class TaskTypeAdapter {

	public static String getTaskTypeName(int taskType){
		switch(taskType){
			case 20:
				return "IPTV直播";
			case 21:
				return "链路质量评估";
			case 22:
				return "主动流侦听";
			case 23:
				return "频道切换时间";
			case 29:
				return "IPTV点播";
			case 101:
				return "时延丢包";
			case  5:
				return "瓶颈带宽";
			case  6:
				return "可用带宽";
		}
		return "错误类型";
	}
}
