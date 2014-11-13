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
import com.task.activity.data.LogInforItem;
import com.task.activity.data.SdInforItem;

public class SdInforAdapter extends BaseAdapter{

	private List<SdInforItem> guidelist;
	private LayoutInflater inflater;
	
	public SdInforAdapter(Context context, List<SdInforItem> list) {
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
			convertView = inflater.inflate(R.layout.sd_report_form_item, null);
			holder.sd1 = (TextView) convertView.findViewById(R.id.sd1);
			holder.sd2 = (TextView) convertView.findViewById(R.id.sd2);
			holder.sd3 = (TextView) convertView.findViewById(R.id.sd3);
			holder.sd4 = (TextView)convertView.findViewById(R.id.sd4);
			holder.sd5 = (TextView)convertView.findViewById(R.id.sd5);
			holder.sd6 = (TextView)convertView.findViewById(R.id.sd6);
			
			holder.backView = convertView.findViewById(R.id.sdback_image_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SdInforItem guideItem = guidelist.get(position);
		holder.sd1.setText(""+guideItem.getSdTime());
		holder.sd2.setText(""+guideItem.getSrcIp());
		holder.sd3.setText(""+ guideItem.getDstIp());
		holder.sd4.setText(""+guideItem.getSrcPort());
		holder.sd5.setText(""+guideItem.getDstPort());
		holder.sd6.setText(""+guideItem.getVideoType());
		
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
		TextView sd1;
		TextView sd2;
		TextView sd3;
		TextView sd4;
		TextView sd5;
		TextView sd6;
		
		View backView;
	}

}
