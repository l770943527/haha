package common;

public class TaskTypeAdapter {

	public static String getTaskTypeName(int taskType){
		switch(taskType){
			case 20:
				return "IPTVֱ��";
			case 21:
				return "��·��������";
			case 22:
				return "����������";
			case 23:
				return "Ƶ���л�ʱ��";
			case 29:
				return "IPTV�㲥";
			case 101:
				return "ʱ�Ӷ���";
			case  5:
				return "ƿ������";
			case  6:
				return "���ô���";
		}
		return "��������";
	}
}
