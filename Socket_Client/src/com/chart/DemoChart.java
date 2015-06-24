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
    TimeSeries series1;//hahah test！
    TimeSeries series2;
    
    public XYMultipleSeriesRenderer getRenderer() {
		return renderer;
	}

	public DemoChart(){
        super();
        dataset=new XYMultipleSeriesDataset();
        renderer=new XYMultipleSeriesRenderer();
    }
    
    //生成图形
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
     * 参数依次为：图形的标题，X轴标题，Y轴标题，轴背景颜色，轴坐标颜色
     * */
    public void setRandererBasicProperty(String title,String xTitle,String yTitle,int axeColor,int labelColor){    
        renderer.setChartTitle(title);
        renderer.setXTitle(xTitle);
        renderer.setYTitle(yTitle);
       
        //设置各种颜色
        renderer.setAxesColor(axeColor);
        renderer.setLabelsColor(labelColor);
        renderer.setMarginsColor(Color.WHITE);//空白颜色（底色）
        renderer.setGridColor(Color.GRAY);//网格颜色
       
        //设置文字的属性
        renderer.setXLabels(5);
        renderer.setYLabels(5);
        renderer.setXLabelsAlign(Align.RIGHT);
        renderer.setYLabelsAlign(Align.LEFT);
        renderer.setAxisTitleTextSize(16);
        renderer.setChartTitleTextSize(20);
        renderer.setLabelsTextSize(15);
        renderer.setLegendTextSize(15);
        
        //设置图形位置，缩放，形态
        renderer.setPointSize(0);
        
        renderer.setMargins(new int[] { 35, 35, 0, 20 });//留白 上，左，下，右
        renderer.setLegendHeight(41);//设置底部文字的高度
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
    
    /*更新数据，对series的操作*/
    public void updateData(Date date,double rate){
        series1.add(date, rate);
    }
    
    public void clearData(){
    	series1.clear();
    }
}
