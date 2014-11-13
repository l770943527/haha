package common;

public class TaskStatusAdapter {

	public static String getTaskStatusName(int taskStatus){
		switch(taskStatus){
			case 1:
				return "已创建";
			case 2:
				return "已创建";
			case 3:
				return "执行中";
			case 4:
				return "暂停";
			case 5:
				return "任务停止";
			case 6:
				return "完成";
			case 7:
				return "已删除";
			case 10:
				return "任务失败";
			case 11:
				return "任务失败";
			case 12:
				return "下发超时";
			case 13:
				return "执行超时";
			case 14:
				return "执行异常";
		}
		return "无效类型";
	}
}
