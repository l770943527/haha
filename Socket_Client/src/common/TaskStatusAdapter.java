package common;

public class TaskStatusAdapter {

	public static String getTaskStatusName(int taskStatus){
		switch(taskStatus){
			case 1:
				return "�Ѵ���";
			case 2:
				return "�Ѵ���";
			case 3:
				return "ִ����";
			case 4:
				return "��ͣ";
			case 5:
				return "����ֹͣ";
			case 6:
				return "���";
			case 7:
				return "��ɾ��";
			case 10:
				return "����ʧ��";
			case 11:
				return "����ʧ��";
			case 12:
				return "�·���ʱ";
			case 13:
				return "ִ�г�ʱ";
			case 14:
				return "ִ���쳣";
		}
		return "��Ч����";
	}
}
