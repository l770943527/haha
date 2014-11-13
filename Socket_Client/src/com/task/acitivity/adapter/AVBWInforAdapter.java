package com.task.acitivity.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.socket.client.R;
import com.task.activity.data.AVBWInforItem;
import com.task.activity.data.SwInforItem;

public class AVBWInforAdapter extends BaseAdapter{

	private List<AVBWInforItem> guidelist;
	private LayoutInflater inflater;
	
	public AVBWInforAdapter(Context context, List<AVBWInforItem> list) {
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
			convertView = inflater.inflate(R.layout.avbw_report_form_item, null);
			holder.avbw1 = (TextView) convertView.findViewById(R.id.avbw1);
			holder.avbw2 = (TextView) convertView.findViewById(R.id.avbw2);
			
			
			holder.backView = convertView.findViewById(R.id.avbw_back_image_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		AVBWInforItem guideItem = guidelist.get(position);
		holder.avbw1.setText(""+guideItem.getReportTime());
		holder.avbw2.setText(""+guideItem.getAvbw());
		
		
		
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
		TextView avbw1;
		TextView avbw2;
		
		
		
		View backView;
	}

}
