package com.task.reportform.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.achartengine.GraphicalView;
import org.achartengine.chart.TimeChart;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import com.chart.DemoChart;
import com.json.util.EncapSendInforUtil;
import com.json.util.ParseJSONStringUtil;
import com.socket.client.R;
import com.socket.util.InternetUtil;
import com.task.activity.data.TimeValue;

import Config.ConfigVarieble;
import android.R.integer;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class IPTVMDIReportFormActivity extends Activity {

	private TextView title;
	private TextView infor;
	private Handler handler;
	private Button backBtn;
	private Button Btn1, Btn2, Btn3;
	private List<Date> timeArr = new ArrayList<Date>();
	private List<integer> valueArr = new ArrayList<integer>();
	private Map<String, Integer> map = new LinkedHashMap<String, Integer>();
	private DemoChart demo;
	private Random r;
	private GraphicalView chartView;
	private List<TimeValue> guideList = new ArrayList<TimeValue>();
	private ArrayList<String> records = new ArrayList<String>();
	private ArrayList<String> headers = new ArrayList<String>();
	private double zoomRate;
	private int type = 1;
	private double xMin,xMax,xMinAll=0,xMaxAll=0;
	private double yMin,yMax;
	private int symbol = 0;
	/**********************************************
	 * level
	 * preXMin
	 * preXMax
	*/
	private int level=1;
	private double preXMin,preXMax;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置横屏
		setContentView(R.layout.iptvmdi_result_infor_activity);

		Bundle bundle = this.getIntent().getExtras();
		headers = bundle.getStringArrayList("headers");
		records = bundle.getStringArrayList("records");

		initialTitles();
		initialTab();
		initialChart();
		initialNetWork();
		
		
		
	}
	
//	protected void onPause(){
//		super.onPause();
//		InternetUtil.closeConnection();
//	}

	/*
	 * 根据当前屏幕的最左和最右坐标获取当前的数据组合等级
	 * */
	int getLevel(double xMaxAll,double xMinAll, double xMax, double xMin){
		double times = (xMaxAll-xMinAll)/(xMax-xMin);
		int level=0,i=1;
		while(i<=times && level<10){
			i *= ConfigVarieble.FLOOR;
			level++;
			//Log.v("num","level="+level + "times="+times + "i="+i);
		}
		level = level<1?1:level;
		return level;
	}
	
	double getOccupancy(double preXMin, double preXMax, double xMin, double xMax){
		if(xMin>=preXMax){
			return 0;
		}
		if(xMax<=preXMin){
			return 0;
		}
		double min = preXMin>xMin?preXMin:xMin;
		double max = preXMax<xMax?preXMax:xMax;
		double result = (max-min)/(preXMax-preXMin);
		return result>0?result:0;
	}
	//CST:central standard time
	public Date getCST(String strGMT) throws ParseException { 
		   DateFormat df = new SimpleDateFormat("EEE, d-MMM-yyyy HH:mm:ss z", Locale.ENGLISH); 
		   return df.parse(strGMT); 
		} 
	
	private void initialChart() {
		LinearLayout linearView = (LinearLayout) findViewById(R.id.chart_show);
		demo = new DemoChart();
		r = new Random();
		chartView = demo.getDemoChartGraphicalView(this);
		// stretch horizontally
		chartView.addZoomListener(new ZoomListener() {
			public void zoomApplied(ZoomEvent e) {
				if(symbol==0){
					xMinAll = demo.getRenderer().getXAxisMin();
					xMaxAll = demo.getRenderer().getXAxisMax();
					preXMin = xMinAll;
					preXMax = xMaxAll;
					level = 1;
					symbol = -1;
				}
				else{
					xMin = demo.getRenderer().getXAxisMin();
					xMax = demo.getRenderer().getXAxisMax();
					
					
					int nowLevel = getLevel(xMaxAll,xMinAll,xMax,xMin);
					Log.v("num","level="+level);
					if(level != nowLevel){
						//数据缩放，局部数据需要更新，更高精度，或者，更低精度
						Log.v("num","==========================level change===prelevel="+level);
						InternetUtil.sendMessage(EncapSendInforUtil.encapIPTVMDIResultImforReq(
								Integer.parseInt(records.get(2)), (long)xMin, (long)xMax, type,nowLevel));// taskid,startpos,endpos,type,level
						
						level = nowLevel;
						preXMin = xMin;
						preXMax = xMax;
					}
					
				}
				
			}

			public void zoomReset() {
				Log.v("Zoom", "Reset");
			}
		}, true, true);
		
		chartView.addPanListener(new PanListener() {
			public void panApplied() {
				if(symbol==0){
					xMinAll = demo.getRenderer().getXAxisMin();
					xMaxAll = demo.getRenderer().getXAxisMax();
					preXMin = xMinAll;
					preXMax = xMaxAll;
					level = 1;
					symbol = -1;
				}
				else{
					xMin = demo.getRenderer().getXAxisMin();
					xMax = demo.getRenderer().getXAxisMax();
					
					
					int nowLevel = getLevel(xMaxAll,xMinAll,xMax,xMin);
					Log.v("num","level="+level);
					
					if(level==nowLevel && getOccupancy(preXMin,preXMax,xMin,xMax)<0.5){
						//数据缩放，局部数据需要更新，更高精度，或者，更低精度
						Log.v("num","===================occupancy change=====prelevel="+level+" occupancy<0.5");
						InternetUtil.sendMessage(EncapSendInforUtil.encapIPTVMDIResultImforReq(
								Integer.parseInt(records.get(2)), (long)xMin, (long)xMax, type,nowLevel));// taskid,startpos,endpos,type,level
						
						Date temp = new Date((long) xMin);Log.v("num","xMin="+temp.toString());
						Date temp2 = new Date((long) xMax);Log.v("num","xMax="+temp2.toString());
						Log.v("num","xMin="+xMin);
						Log.v("num","xMax="+xMax);
						
						level = nowLevel;
						preXMin = xMin;
						preXMax = xMax;
					}
					
				}
				
			}
		});

		linearView.addView(chartView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	
	
	
	
	private void initialTitles() {
		title = (TextView) findViewById(R.id.tasktitlepanel_titleTextView);
		infor = (TextView) findViewById(R.id.infor);
		infor.setVisibility(8);
		backBtn = (Button) findViewById(R.id.tasktitlepanel_leftBtn);
		title.setText("结果信息");
		backBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				IPTVMDIReportFormActivity.this.finish();
			}
		});
	}

	private void initialNetWork() {
		Log.v("QLQ", " "
				+ "****************************InternetUtil.sendMessage");
		InternetUtil.sendMessage(EncapSendInforUtil.encapIPTVMDIResultImforReq(
				//records.get(2)-->taskid
				Integer.parseInt(records.get(2)), 0, 0, type,1));// 初次发送数据请求，level为1，标识，这是第一个发送的数据请求
                   //encapIPTVMDIResultImforReq(int taskid, long timeMin, long timeMax, int type,int level)
		// handler类接收数据
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					// 动态更新UI界面
					String temp1 = "11";
					// t.setText("11");
					String str = msg.getData().getString("title");
					infor.setText("let's have a test3");
					guideList.clear();
					JSONTokener jsonParser = new JSONTokener(str);
					try {
						JSONObject inforOj = (JSONObject) jsonParser
								.nextValue();
						JSONObject headOj = inforOj.getJSONObject("head");
						String messageType = headOj.getString("message_type");
						if (messageType.equals("13201")) {
							timeArr.clear();
							valueArr.clear();
							map.clear();
							JSONObject dataOj = inforOj.getJSONObject("data");
							JSONArray resultArr = dataOj.getJSONArray("Result");
							infor.setText(resultArr.toString());

							for (int i = 0; i < resultArr.length(); i++) {
								JSONObject temp = (JSONObject) resultArr.opt(i);
								TimeValue item = new TimeValue(
										temp.getString("time"),
										temp.getInt("value"));
								guideList.add(item);
							}

							SimpleDateFormat sdf = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							demo.clearData();
							try {
								for (int i = 0; i < guideList.size(); i++) {

									Date date = sdf.parse(guideList.get(i)
											.getTime());

									Log.v("QLQ", date.toString());
									demo.updateData(date, guideList.get(i)
											.getValue());
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
							chartView.postInvalidate();
							Log.v("num",""+guideList.size());


						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}
			};
		};

		Thread t = new Thread(new InternetUtil(handler));
		t.start();

	}

	private void initialTab() {
		Btn1 = (Button) findViewById(R.id.btn1);
		Btn2 = (Button) findViewById(R.id.btn2);
		Btn3 = (Button) findViewById(R.id.btn3);

		Button.OnClickListener onClickListener = new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Btn1.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.common_threegroupbtn_left_unpress));
				Btn2.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.common_threegroupbtn_mid_unpress));
				Btn3.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.common_threegroupbtn_mid_unpress));
				switch (v.getId()) {
				case R.id.btn1:
					// drawable cheart 
					Btn1.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_left_pressed));
					type = 1;
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapIPTVMDIResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 0, type,0));// taskid,startpos,endpos
					break;

				case R.id.btn2:
					Btn2.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_mid_pressed));
					type  = 2;
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapIPTVMDIResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 0, type,0));// taskid,startpos,endpos
					break;

				case R.id.btn3:
					Btn3.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_mid_pressed));
					type = 3;
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapIPTVMDIResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 0, type,0));// taskid,startpos,endpos
					break;
				}
			}
		};
		Btn1.setOnClickListener(onClickListener);
		Btn2.setOnClickListener(onClickListener);
		Btn3.setOnClickListener(onClickListener);
	}
	
	
	
}
