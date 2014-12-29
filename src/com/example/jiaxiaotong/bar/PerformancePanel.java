package com.example.jiaxiaotong.bar;

import java.text.NumberFormat;  
import java.util.ArrayList;  
import java.util.List;  
import java.util.Map;

import com.example.jiaxiaotong.R;
import com.example.jiaxiaotong.dao.ChildDB;
import com.example.jiaxiaotong.utils.Util;
  
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;  
import android.content.DialogInterface;
import android.graphics.Canvas;  
import android.graphics.Color;  
import android.graphics.DashPathEffect;  
import android.graphics.Paint;  
import android.graphics.Paint.Style;  
import android.view.Display;  
import android.view.MotionEvent;
import android.view.View;  
import android.view.WindowManager;  
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
  
public class PerformancePanel extends  View{  
    private ArrayList<Position>  positions = null;
    private String plotTitle;  
    private DataSeries series;  
    public final static int[] platterTable = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN};  
    public PerformancePanel(Context context, String plotTitle) {  
        this(context);  
        this.plotTitle = plotTitle;  
        this.positions = new ArrayList<Position>();
    }  
      
    public PerformancePanel(Context context) {  
        super(context);  
        this.positions = new ArrayList<Position>();
    }  
      
    public void setSeries(DataSeries series) {  
        this.series = series;  
    }  
      
    @Override    
    public void onDraw(Canvas canvas) {    
        // get default screen size from system service  
        WindowManager wm = (WindowManager) this.getContext().getSystemService(Context.WINDOW_SERVICE);  
        Display display = wm.getDefaultDisplay();  
        int width = display.getWidth();  
          
        // remove application title height  
        //int height = display.getHeight() - 80; 
        int height = display.getHeight() / 3;
        
        // draw background  
        Paint myPaint = new Paint();  
        myPaint.setColor(Color.WHITE);  
        myPaint.setStrokeWidth(2);  
        canvas.drawRect(0, 0, width, height, myPaint);  
       
        // draw XY Axis  
        int xOffset = (int)(width * 0.05);  
        int yOffset = (int)(height * 0.05);  
    
        if(series == null) {  
            getMockUpSeries();  
        }  
        
        myPaint.setColor(Color.BLACK);
        myPaint.setStrokeWidth(1);
        int xPadding = 10;
        if(series != null) {  
	        int count = series.getSeriesCount();  
	        int xUnit = (width - 2 - xOffset)/count;  
        	String[] seriesNames = series.getSeriesKeys();  
                  
            // Y Axis markers  
            float min = 0, max = 0;  
            for(int i=0; i<seriesNames.length; i++) {  
                List<DataElement> itemList = series.getItems(seriesNames[i]);  
                if(itemList != null && itemList.size() > 0) {  
                    for(DataElement item : itemList) {  
                        if(item.getValue() > max) {  
                            max = item.getValue();  
                        }  
                        if(item.getValue() < min) {  
                            min = item.getValue();  
                        }  
                    }  
                }  
            }  
            int offset = 20;  
            int yUnit = 22;   
            int unitValue = (height-2-yOffset)/yUnit;  
            float ymarkers = (max-min+offset)/yUnit;  
              
            // draw bar chart now  
            myPaint.setStyle(Style.FILL);  
            myPaint.setStrokeWidth(0);  
            String maxItemsKey = null;  
            int maxItem = 0;  
            for(int i=0; i<seriesNames.length; i++) {  
                List<DataElement> itemList = series.getItems(seriesNames[i]);  
                int barWidth = (int)(xUnit/Math.pow(itemList.size(),2));  
                int startPos = xOffset + 2 + xUnit*i;   
                if(itemList.size() > maxItem) {  
                    maxItemsKey = seriesNames[i];  
                    maxItem = itemList.size();  
                }  
                for(DataElement item : itemList) {  
                    myPaint.setColor(item.getColor());  
                    int barHeight = (int)((item.getValue()/ymarkers) * unitValue); 
                    canvas.drawRect(startPos , height-2-yOffset-barHeight,   
                            startPos + barWidth / 2, height-2-yOffset, myPaint); 
                    canvas.drawText(item.getValue()+"", xOffset+barWidth/6+xUnit*i, 
                    		height-yOffset-5-barHeight, myPaint);
                    Position position = new Position(startPos, startPos + barWidth / 2, 
                    					height-2-yOffset-barHeight, height-2-yOffset);
                    this.positions.add(position);
                }  
                canvas.drawText(seriesNames[i], xOffset+barWidth/6+xUnit*i, height-yOffset+10, myPaint);  
                
            }   
        }  
    }  
      
    @Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
    	ChildDB cdb = new ChildDB(this.getContext());
    	float x = event.getX();
    	float y = event.getY();
    	for(int i = 0; i < series.getSeriesCount(); i++) {
    		Position p = positions.get(i);
    		if((p.startX <= x && x <= p.endX) && (p.startY <= y && y <= p.endY)) {
    			if(0 == i) {
    				showDetailDialog("刘沛涵", cdb.getLogsByState("刘沛涵", "早退"));
    			}else if(1 == i) {
    				showDetailDialog("刘沛涵", cdb.getLogsByState("刘沛涵", "正常"));
    			}else {
    				showDetailDialog("刘沛涵", cdb.getLogsByState("刘沛涵", "迟到"));
    			}
    		}
    	}
		return super.onTouchEvent(event);
	}

	public DataSeries getMockUpSeries() {  
        series = new DataSeries();  
        List<DataElement> itemListOne = new ArrayList<DataElement>();  
        itemListOne.add(new DataElement("shoes",120, platterTable[0]));   
        series.addSeries("正常", itemListOne);  
          
        List<DataElement> itemListTwo = new ArrayList<DataElement>();  
        itemListTwo.add(new DataElement("shoes",110, platterTable[1]));   
        series.addSeries("迟到", itemListTwo);  
          
        List<DataElement> itemListThree = new ArrayList<DataElement>();  
        itemListThree.add(new DataElement("shoes",100, platterTable[2]));   
        series.addSeries("早退", itemListThree);  
     
        return series;  
    }  
	
	public void showDetailDialog(String childName, ArrayList<Map<String, String>> logs) {
		LinearLayout linearLayoutMain = new LinearLayout(this.getContext());//自定义一个布局文件  
		linearLayoutMain.setLayoutParams(new LinearLayout.LayoutParams(  
				LinearLayout.LayoutParams.MATCH_PARENT, 
				LinearLayout.LayoutParams.WRAP_CONTENT));  
		ListView listView = new ListView(this.getContext());//this为获取当前的上下文  
		listView.setFadingEdgeLength(0);  
		  
		List<Map<String,String>> nameList = logs;//建立一个数组存储listview上显示的数据  
		 
		  
		SimpleAdapter adapter = new SimpleAdapter(this.getContext(),  
		        nameList, R.layout.dialog_item,  
		        new String[] { "刘沛涵" },  
		        new int[] { R.id.log});  
		listView.setAdapter(adapter);  
		  
		linearLayoutMain.addView(listView);//往这个布局中加入listview  
		  
		final AlertDialog dialog = new AlertDialog.Builder(this.getContext())  
		        .setTitle("出入记录").setView(linearLayoutMain)//在这里把写好的这个listview的布局加载dialog中  
		        .setNegativeButton("取消", new DialogInterface.OnClickListener() {  
		  
		            @Override  
		            public void onClick(DialogInterface dialog, int which) {  
		                // TODO Auto-generated method stub  
		                dialog.cancel();  
		            }  
		        }).create();  
		dialog.setCanceledOnTouchOutside(false);//使除了dialog以外的地方不能被点击  
		dialog.show();  
	}
	class Position {
		public float startX;
		public float endX;
		public float startY;
		public float endY;
		
		public Position(float startX, float endX, float startY, float endY) {
			this.startX = startX;
			this.endX = endX;
			this.startY = startY;
			this.endY = endY;
		}
	}
}  
