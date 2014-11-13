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
import com.task.activity.data.BOBWInforItem;

public class BOBWInforAdapter extends BaseAdapter{

	private List<BOBWInforItem> guidelist;
	private LayoutInflater inflater;
	
	public BOBWInforAdapter(Context context, List<BOBWInforItem> list) {
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
			convertView = inflater.inflate(R.layout.bobw_report_form_item, null);
			holder.bobw1 = (TextView) convertView.findViewById(R.id.bobw1);
			holder.bobw2 = (TextView) convertView.findViewById(R.id.bobw2);
			
			
			holder.backView = convertView.findViewById(R.id.bobw_back_image_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BOBWInforItem guideItem = guidelist.get(position);
		holder.bobw1.setText(""+guideItem.getReportTime());
		holder.bobw2.setText(""+guideItem.getBobw());
		
		
		
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
		TextView bobw1;
		TextView bobw2;
		
		
		
		View backView;
	}

}
