package com.task.acitivity.adapter;

import java.util.List;

import com.socket.client.R;
import com.task.activity.data.DAInforItem;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DAInforAdapter extends BaseAdapter{

	private List<DAInforItem> guidelist;
	private LayoutInflater inflater;
	
	public DAInforAdapter(Context context, List<DAInforItem> list) {
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
			convertView = inflater.inflate(R.layout.data_analyst_report_form_item, null);
			holder.item1 = (TextView) convertView.findViewById(R.id.item1);
			holder.item2 = (TextView) convertView.findViewById(R.id.item2);
			holder.item3 = (TextView) convertView.findViewById(R.id.item3);
			holder.item4 = (TextView)convertView.findViewById(R.id.item4);
			holder.item5 = (TextView)convertView.findViewById(R.id.item5);
			holder.item6 = (TextView)convertView.findViewById(R.id.item6);
			holder.backView = convertView.findViewById(R.id.back_image_view);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DAInforItem guideItem = guidelist.get(position);
		holder.item1.setText(""+guideItem.getId());
		holder.item2.setText(""+guideItem.getIp());
		holder.item3.setText(""+ (guideItem.getStatus()==0?"‘À––÷–":"Õ£÷π"));
		holder.item4.setText(String.format("%.2f", guideItem.getSlotsused()*100)+"%");
		holder.item5.setText(String.format("%.2f",guideItem.getCpuusage()*100)+"%");
		holder.item6.setText(String.format("%.2f",guideItem.getMemusage()*100)+"%");
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
		View backView;
	}

}
