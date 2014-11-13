package com.task.acitivity.adapter;

import java.util.List;

import com.socket.client.R;
import com.task.activity.data.TaskInforItem;

import common.TaskStatusAdapter;
import common.TaskTypeAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TaskInforAdapter extends BaseAdapter{

	private List<TaskInforItem> guidelist;
	private LayoutInflater inflater;
	
	public TaskInforAdapter(Context context, List<TaskInforItem> list) {
		this.guidelist = list;
		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		return guidelist.size();
	}

	@Override
	public Object getItem(int position) {
		return guidelist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.task_report_form_item, null);
			holder.item1 = (TextView) convertView.findViewById(R.id.item1);
			holder.item2 = (TextView) convertView.findViewById(R.id.item2);
			holder.item3 = (TextView) convertView.findViewById(R.id.item3);
			holder.item4 = (TextView)convertView.findViewById(R.id.item4);
			holder.item5 = (TextView)convertView.findViewById(R.id.item5);
			holder.item6 = (TextView)convertView.findViewById(R.id.item6);
			holder.item7 = (TextView)convertView.findViewById(R.id.item7);
			holder.item8 = (TextView)convertView.findViewById(R.id.item8);
			holder.backView = convertView.findViewById(R.id.back_image_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		TaskInforItem guideItem = guidelist.get(position);
		holder.item1.setText(""+TaskTypeAdapter.getTaskTypeName(guideItem.getTaskType()));
		holder.item2.setText(""+guideItem.getUserId());
		holder.item3.setText(""+guideItem.getTaskId());
		holder.item4.setText(guideItem.getSrcIp());
		holder.item5.setText(guideItem.getDstIp());
		holder.item6.setText(""+guideItem.getStartTime());
		holder.item7.setText(""+guideItem.getDuration()/1000/60 + "∑÷÷”");
		holder.item8.setText(String.valueOf(TaskStatusAdapter.getTaskStatusName(guideItem.getStatus())));
		if(position % 2 == 0)
		{ 
			holder.backView.setBackgroundColor(Color.rgb(240, 240, 240));
		}
		else
		{
			holder.backView.setBackgroundColor(Color.WHITE);
		}
		return convertView;
	}

	class ViewHolder {
		TextView item1;
		TextView item2;
		TextView item3;
		TextView item4;
		TextView item5;
		TextView item6;
		TextView item7;
		TextView item8;
		View backView;
	}

}
