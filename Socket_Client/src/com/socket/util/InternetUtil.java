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

	private static String workStatus;// 当前工作状况，null表示正在处理，success表示处理成功，failure表示处理失败

	private static String currAction; // 标记当前请求头信息，在获取服务器端反馈的数据后，进行验证，以免出现反馈信息和当前请求不一致问题。比如现在发送第二个请求，但服务器端此时才响应第一个请求

	private static Handler handler;
	
	// 超时的时限为8秒 
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
	 * 向服务器发送请求
	 * 
	 * @param action
	 *            :标识当前发送的是哪一个动作
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
	 * 返回当前workStatus的值 / public StringgetWorkStatus() { return workStatus ; }
	 * 
	 * /** 处理服务器端反馈的数据
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
	 * 退出程序时，关闭Socket连接
	 */
	public static void closeConnection() {
		try {
			Log.v("num",
					"============================closeSocket========================");
			sendMessage(EncapSendInforUtil.encapCloseConnectionReq());// 向服务器端发送断开连接请求
			reader.close();
			writer.close();
			socket.close();
			socket = null;
		} catch (Exception ex) {

			ex.printStackTrace();
		}
	}

	/**
	 * 连接服务器
	 */
	private static void connectService() {
		try {
			socket = new Socket();
			SocketAddress socAddress = new InetSocketAddress(ConfigVarieble.SEVERIP,
					ConfigVarieble.PORT);
			socket.connect(socAddress, 3000);// 连接超时为3秒
			reader = new BufferedReader(new InputStreamReader(
					socket.getInputStream(), "GBK"));
			writer = new BufferedWriter(new OutputStreamWriter(
					socket.getOutputStream()));
		} catch (SocketException ex) {
			ex.printStackTrace();
			workStatus = "failure";// 如果是网络连接出错了，则提示网络连接错误
			return;
		} catch (SocketTimeoutException ex) {
			ex.printStackTrace();
			workStatus = "failure";// 如果是网络连接出错了，则提示网络连接错误
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
			workStatus = "failure";// 如果是网络连接出错了，则提示网络连接错误
			return;
		}
	}

	/**
	 * 向服务器发送传入的JSON数据信息
	 * 
	 * @param json
	 */
	public static void sendMessage(final String message) {
		// if (!isNetworkConnected())// 如果当前网络连接不可用,直接提示网络连接不可用，并退出执行。
		// {
		// workStatus = "failure";
		// return;
		// }

		new Thread() {
			@Override
			public void run() {
				// 你要执行的方法
				// 执行完毕后给handler发送一个空消息
				sendMessageInAnotherThread(message);
			}
		}.start();
		// 设定定时器 
       Timer timer = new Timer(); 
        timer.schedule(new TimerTask() { 
            @Override 
            public void run() { 
                sendTimeOutMsg(); 
            } 
        }, TIME_LIMIT); 

	}

	protected static void sendMessageInAnotherThread(String message) {

		if (socket == null)// 如果未连接到服务器，创建连接
		{
			connectService();
		}
		if (!socket.isConnected() || (socket.isClosed())) // isConnected（）返回的是是否曾经连接过，isClosed()返回是否处于关闭状态，只有当isConnected（）返回true，isClosed（）返回false的时候，网络处于连接状态
		{
			for (int i = 0; i < 3 && workStatus == null; i++) {// 如果连接处于关闭状态，重试三次，如果连接正常了，跳出循环
				socket = null;
				connectService();
				if (socket.isConnected() && (!socket.isClosed())) {
					break;
				}
			}
			if (!socket.isConnected() || (socket.isClosed()))// 如果此时连接还是不正常，提示错误，并跳出循环
			{
				workStatus = "failure";
				return;
			}
		}

		if (!socket.isOutputShutdown()) {// 输入输出流是否关闭
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
	 * 处理服务器端传来的消息，并通过action头信息判断，传递给相应处理方法
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
	//向handler发送超时信息 
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
	 * 循环，接收从服务器端传来的数据
	 */
	public void run() {
		try {
			while (true) {
				Thread.sleep(500);// 休眠0.5s
				if (socket != null && !socket.isClosed()) {// 如果socket没有被关闭
					if (socket.isConnected()) {// 判断socket是否连接成功
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
			workStatus = "failure";// 如果出现异常，提示网络连接出现问题。
			ex.printStackTrace();
		}
	}

}