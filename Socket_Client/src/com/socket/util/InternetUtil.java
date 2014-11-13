package com.socket.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Timer;
import java.util.TimerTask;

import Config.ConfigVarieble;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.json.util.EncapSendInforUtil;

public class InternetUtil implements Runnable {

	private static Socket socket;

	private static BufferedReader reader;//

	private static BufferedWriter writer;//

	private static String workStatus;// ��ǰ����״����null��ʾ���ڴ���success��ʾ����ɹ���failure��ʾ����ʧ��

	private static String currAction; // ��ǵ�ǰ����ͷ��Ϣ���ڻ�ȡ�������˷��������ݺ󣬽�����֤��������ַ�����Ϣ�͵�ǰ����һ�����⡣�������ڷ��͵ڶ������󣬵��������˴�ʱ����Ӧ��һ������

	private static Handler handler;
	
	// ��ʱ��ʱ��Ϊ8�� 
    private static final int TIME_LIMIT = 8000; 
    
    private static final int TIME_OUT = 0;

	public InternetUtil(Handler handler) {
		this.handler = handler;
	}

	public InternetUtil() {
		// TODO Auto-generated constructor stub
	}

	public static void setHandler(Handler handler) {
		InternetUtil.handler = handler;
	}

	/**
	 * ���������������
	 * 
	 * @param action
	 *            :��ʶ��ǰ���͵�����һ������
	 * 
	 */
	// public void sendRequest(String message) {
	// try {
	// workStatus = null;
	// sendMessage(message);
	// } catch (Exception ex) {
	// workStatus = "failure";
	// ex.printStackTrace();
	// }
	// }
	//
	/**
	 * ���ص�ǰworkStatus��ֵ / public StringgetWorkStatus() { return workStatus ; }
	 * 
	 * /** ����������˷���������
	 * 
	 * @param json
	 * 
	 */
	// private void dealUploadSuperviseTask(JSONObject json)
	// {
	// try{
	// workStatus=json.getString("result");
	// }catch(Exception ex)
	// {
	// ex.printStackTrace();
	// workStatus="failure";
	// }
	// }

	/**
	 * �˳�����ʱ���ر�Socket����
	 */
	public static void closeConnection() {
		try {
			Log.v("num",
					"============================closeSocket========================");
			sendMessage(EncapSendInforUtil.encapCloseConnectionReq());// ��������˷��ͶϿ���������
			reader.close();
			writer.close();
			socket.close();
			socket = null;
		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

	/**
	 * ���ӷ�����
	 */
	private static void connectService() {
		try {
			socket = new Socket();
			SocketAddress socAddress = new InetSocketAddress(ConfigVarieble.SEVERIP,
					ConfigVarieble.PORT);
			socket.connect(socAddress, 3000);// ���ӳ�ʱΪ3��
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "GBK"));
			writer = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (SocketException ex) {
			ex.printStackTrace();
			workStatus = "failure";// ������������ӳ����ˣ�����ʾ�������Ӵ���
			return;
		} catch (SocketTimeoutException ex) {
			ex.printStackTrace();
			workStatus = "failure";// ������������ӳ����ˣ�����ʾ�������Ӵ���
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
			workStatus = "failure";// ������������ӳ����ˣ�����ʾ�������Ӵ���
			return;
		}
	}

	/**
	 * ����������ʹ����JSON������Ϣ
	 * 
	 * @param json
	 */
	public static void sendMessage(final String message) {
		// if (!isNetworkConnected())// �����ǰ�������Ӳ�����,ֱ����ʾ�������Ӳ����ã����˳�ִ�С�
		// {
		// workStatus = "failure";
		// return;
		// }

		new Thread() {
			@Override
			public void run() {
				// ��Ҫִ�еķ���
				// ִ����Ϻ��handler����һ������Ϣ
				sendMessageInAnotherThread(message);
			}
		}.start();
		// �趨��ʱ�� 
       Timer timer = new Timer(); 
        timer.schedule(new TimerTask() { 
            @Override 
            public void run() { 
                sendTimeOutMsg(); 
            } 
        }, TIME_LIMIT); 

	}

	protected static void sendMessageInAnotherThread(String message) {

		if (socket == null)// ���δ���ӵ�����������������
		{
			connectService();
		}
		if (!socket.isConnected() || (socket.isClosed())) // isConnected�������ص����Ƿ��������ӹ���isClosed()�����Ƿ��ڹر�״̬��ֻ�е�isConnected��������true��isClosed��������false��ʱ�����紦������״̬
		{
			for (int i = 0; i < 3 && workStatus == null; i++) {// ������Ӵ��ڹر�״̬���������Σ�������������ˣ�����ѭ��
				socket = null;
				connectService();
				if (socket.isConnected() && (!socket.isClosed())) {
					break;
				}
			}
			if (!socket.isConnected() || (socket.isClosed()))// �����ʱ���ӻ��ǲ���������ʾ���󣬲�����ѭ��
			{
				workStatus = "failure";
				return;
			}
		}

		if (!socket.isOutputShutdown()) {// ����������Ƿ�ر�
			try {
				writer.write(message + "\r\n\r\n");
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
				workStatus = "failure";
			}
		} else {
			workStatus = "failure";
		}
	}

	/**
	 * ����������˴�������Ϣ����ͨ��actionͷ��Ϣ�жϣ����ݸ���Ӧ������
	 * 
	 * @param str
	 */
	private void getMessage(String str) {
		try {
			handle(str);
		} catch (Exception ex) {
			ex.printStackTrace();
			workStatus = "failure";
		}
	}
	//��handler���ͳ�ʱ��Ϣ 
    private static void sendTimeOutMsg() { 
    	Log.v("timeout","in send timeout msg...");
        Message timeOutMsg = new Message(); 
        timeOutMsg.what = TIME_OUT; 
        handler.sendMessage(timeOutMsg); 
    } 

	private void handle(String message) {
		// send the msg by handler named "title"
		Message msg = new Message();
		msg.what = 1;
		Bundle bundle = new Bundle();
		bundle.putString("title", message);
		msg.setData(bundle);
		handler.sendMessage(msg);
	}

	/**
	 * ѭ�������մӷ������˴���������
	 */
	public void run() {
		try {
			while (true) {
				Thread.sleep(500);// ����0.5s
				if (socket != null && !socket.isClosed()) {// ���socketû�б��ر�
					if (socket.isConnected()) {// �ж�socket�Ƿ����ӳɹ�
						if (!socket.isInputShutdown()) {
							String content;
							if ((content = reader.readLine()) != null) {
								getMessage(content);
								
							}
						}
					}
				}
			}

		} catch (Exception ex) {
			try {
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			workStatus = "failure";// ��������쳣����ʾ�������ӳ������⡣
			ex.printStackTrace();
		}
	}

}