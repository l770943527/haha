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
import com.task.activity.data.SdInforItem;
import com.task.activity.data.SwInforItem;

public class SwctInforAdapter extends BaseAdapter{

	private List<SwInforItem> guidelist;
	private LayoutInflater inflater;
	
	public SwctInforAdapter(Context context, List<SwInforItem> list) {
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
			convertView = inflater.inflate(R.layout.sw_report_form_item, null);
			//holder.sw1 = (TextView) convertView.findViewById(R.id.sw1);
			holder.sw2 = (TextView) convertView.findViewById(R.id.sw2);
			holder.sw3 = (TextView) convertView.findViewById(R.id.sw3);
			holder.sw4 = (TextView)convertView.findViewById(R.id.sw4);
			
			holder.backView = convertView.findViewById(R.id.swback_image_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		SwInforItem guideItem = guidelist.get(position);
		holder.sw2.setText(""+guideItem.getDstIp());
		holder.sw3.setText(""+guideItem.getSrcIp());
		holder.sw4.setText(""+ guideItem.getSwctTime());
		
		
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
		TextView sw1;
		TextView sw2;
		TextView sw3;
		TextView sw4;
		
		
		View backView;
	}

}
