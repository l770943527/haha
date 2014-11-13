package com.chart;
import java.util.Date;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;

public class DemoChart{
    
    private XYMultipleSeriesDataset dataset;
    private XYMultipleSeriesRenderer renderer;

    //TimeSeries
    TimeSeries series1;
    TimeSeries series2;
    
    public XYMultipleSeriesRenderer getRenderer() {
		return renderer;
	}

	public DemoChart(){
        super();
        dataset=new XYMultipleSeriesDataset();
        renderer=new XYMultipleSeriesRenderer();
    }
    
    //����ͼ��
    public GraphicalView getChartGraphicalView(Context context){

    	GraphicalView tempView = ChartFactory.getTimeChartView(context, dataset, renderer,null);
        return tempView;
    }
    
    public XYMultipleSeriesDataset bulidBasicDataset(){
        return dataset;
    }
    
    public XYMultipleSeriesRenderer buildRenderer(){
        return renderer;
    }
    
    /*
     * ��������Ϊ��ͼ�εı��⣬X����⣬Y����⣬�ᱳ����ɫ����������ɫ
     * */
    public void setRandererBasicProperty(String title,String xTitle,String yTitle,int axeColor,int labelColor){    
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
       
        //���ø�����ɫ
        renderer.setAxesColor(axeColor);
        renderer.setLabelsColor(labelColor);
        renderer.setMarginsColor(Color.WHITE);//�հ���ɫ����ɫ��
        renderer.setGridColor(Color.GRAY);//������ɫ
       
        //�������ֵ�����
        renderer.setXLabels(5);
        renderer.setYLabels(5);
        renderer.setXLabelsAlign(Align.RIGHT);
        renderer.setYLabelsAlign(Align.LEFT);
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        
        //����ͼ��λ�ã����ţ���̬
        renderer.setPointSize(0);
        
        renderer.setMargins(new int[] { 35, 35, 0, 20 });//���� �ϣ����£���
        renderer.setLegendHeight(41);//���õײ����ֵĸ߶�
        renderer.setShowGrid(true);
        renderer.setZoomEnabled(true, false);
    }
    
    public void addNewSRPair(TimeSeries Series,XYSeriesRenderer xyRenderer){
        if(dataset==null||renderer==null){
            return ;
        }else{
            dataset.addSeries(Series);
            renderer.addSeriesRenderer(xyRenderer);
        }
    }
    
    public XYMultipleSeriesDataset getLastestDateset(){
        return dataset;
    }
    
    public XYMultipleSeriesRenderer getLastestRenderer(){
        return renderer;
    }
    public GraphicalView getDemoChartGraphicalView(Context context){
        setRandererBasicProperty("RTTChart", "Time", "RTT",Color.GRAY,Color.GRAY);

        XYSeriesRenderer xyRenderer=new XYSeriesRenderer();
        xyRenderer.setColor(Color.parseColor("#00B2EE"));
        xyRenderer.setPointStyle(PointStyle.CIRCLE);
        xyRenderer.setLineWidth(6);
        
        XYSeriesRenderer xyRenderer2=new XYSeriesRenderer();
        xyRenderer2.setColor(Color.YELLOW);
        xyRenderer2.setPointStyle(PointStyle.CIRCLE);
        xyRenderer2.setLineWidth(2);
        
        series1=new TimeSeries("RTT1");
        series2=new TimeSeries("RTT2");
        
        addNewSRPair(series1, xyRenderer);
        addNewSRPair(series2, xyRenderer2);
        
        return getChartGraphicalView(context);
    }
    
    /*�������ݣ���series�Ĳ���*/
    public void updateData(Date date,double rate){
        series1.add(date, rate);
    }
    
    public void clearData(){
    	series1.clear();
    }
}