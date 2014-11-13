package com.task.reportform.activity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import com.task.activity.data.DAInforItem;
import com.task.activity.data.TimeValue;

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

public class IPTVReqReportFormActivity extends Activity {

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
	private double xMin,xMax,xMinAll=0,xMaxAll=0;
	private double yMin,yMax;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);// 设置横屏
		setContentView(R.layout.iptvreq_result_infor_activity);

		Bundle bundle = this.getIntent().getExtras();
		headers = bundle.getStringArrayList("headers");
		records = bundle.getStringArrayList("records");

		initialTitles();
		initialTab();
		initialChart();
		initialNetWork();
	}

	private void initialChart() {
		LinearLayout linearView = (LinearLayout) findViewById(R.id.chart_show);
		demo = new DemoChart();
		r = new Random();
		chartView = demo.getDemoChartGraphicalView(this);
		chartView.addZoomListener(new ZoomListener() {
			public void zoomApplied(ZoomEvent e) {
			}

			public void zoomReset() {
				Log.v("Zoom", "Reset");
			}
		}, true, true);
		// 设置拖动图表时后台打印出图表坐标的最大最小值.
		chartView.addPanListener(new PanListener() {
			public void panApplied() {
				xMin = demo.getRenderer().getXAxisMin();
				xMax = demo.getRenderer().getXAxisMax();
				//	yMin = demo.getRenderer().getYAxisMin();
				//	yMax = demo.getRenderer().getYAxisMax();
				if(xMax-xMin < xMinAll-xMaxAll/2){
					InternetUtil.sendMessage(EncapSendInforUtil.encapIPTVReqResultImforReq(
							Integer.parseInt(records.get(2)), (long)xMin, (long)xMax, 1));// taskid,xMin,xMax,type
					Log.v("QLQ", "New X range=[" + demo.getRenderer().getXAxisMin()
							+ ", " + demo.getRenderer().getXAxisMax()
							+ "], Y range=[" + demo.getRenderer().getYAxisMax()
							+ ", " + demo.getRenderer().getYAxisMax() + "]");
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
				IPTVReqReportFormActivity.this.finish();
			}
		});
	}

	private void initialNetWork() {
		Log.v("QLQ", " "
				+ "****************************InternetUtil.sendMessage");
		InternetUtil.sendMessage(EncapSendInforUtil.encapIPTVReqResultImforReq(
				Integer.parseInt(records.get(2)), 0, 15, 1));// taskid,startpos,endpos

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
						if (messageType.equals("13101")) {
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

									demo.updateData(date, guideList.get(i)
											.getValue());
								}
							} catch (ParseException e) {
								e.printStackTrace();
							}
							chartView.postInvalidate();
							xMinAll = demo.getRenderer().getXAxisMin();
							xMaxAll = demo.getRenderer().getXAxisMax();

						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
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
					Btn1.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_left_pressed));
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapIPTVReqResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 15, 1));// taskid,startpos,endpos
					break;

				case R.id.btn2:
					Btn2.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_mid_pressed));
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapIPTVReqResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 15, 2));// taskid,startpos,endpos
					break;

				case R.id.btn3:
					Btn3.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.common_threegroupbtn_mid_pressed));
					InternetUtil
							.sendMessage(EncapSendInforUtil
									.encapIPTVReqResultImforReq(
											Integer.parseInt(records.get(2)),
											0, 15, 3));// taskid,startpos,endpos
					break;

				}
			}
		};
		Btn1.setOnClickListener(onClickListener);
		Btn2.setOnClickListener(onClickListener);
		Btn3.setOnClickListener(onClickListener);
	}

}
