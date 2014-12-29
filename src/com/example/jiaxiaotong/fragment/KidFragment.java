package com.example.jiaxiaotong.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.jiaxiaotong.R;
import com.example.jiaxiaotong.bar.DataElement;
import com.example.jiaxiaotong.bar.DataSeries;
import com.example.jiaxiaotong.bar.PerformancePanel;
import com.example.jiaxiaotong.bean.ChildBean;
import com.example.jiaxiaotong.dao.ChildDB;
import com.example.jiaxiaotong.utils.Logger;
import com.example.jiaxiaotong.utils.SharePreferencesUtil;
import com.example.jiaxiaotong.utils.Util;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;



/**
 * Created by John on 12/15/2014.
 */
public class KidFragment extends Fragment implements OnClickListener{
	private View view = null;
	private LinearLayout kidLinearLayout = null;
	private ChildDB cdb = null;
	private ArrayList<CheckBox> childChecks = null;
	private ArrayList<ChildBean> children = null;
	private PerformancePanel panel = null;
	public final static int[] platterTable = new int[]{Color.RED, Color.BLUE, Color.GREEN, Color.YELLOW, Color.CYAN}; 
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_kid_fragment, container, false);
		cdb = new ChildDB(getActivity());
		childChecks = new ArrayList<CheckBox>();
		test();
		children = cdb.getChildren();
		initView();
		return view;
	}
	
	private void initView() {
		kidLinearLayout = (LinearLayout) view.findViewById(R.id.kid_linear_layout);
		panel = new PerformancePanel(getActivity());
		initDataSerial();
		for(int i = 0; i < children.size(); i++) {
			addChildItem(i);
		}
		childChecks.get(0).setChecked(true);
		childChecks.get(0).setEnabled(false);
		addPerformance();
		kidLinearLayout.addView(panel);
	}
	
	public void addChildItem(int childNum) {
		RelativeLayout rLayout = new RelativeLayout(getActivity());
		RelativeLayout.LayoutParams rParams = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams rParams_iv = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams rParams_tv = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		RelativeLayout.LayoutParams rParams_cb = new RelativeLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		//rParams_tv.addRule(RelativeLayout.RIGHT_OF, 100 + childNum);
		//rParams_tv.leftMargin = 51;
		rParams_tv.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		rParams_tv.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		rParams_cb.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		rParams_cb.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
		ImageView userIcon = new ImageView(getActivity());
		userIcon.setId(100 + childNum);
		userIcon.setLayoutParams(rParams_iv);
		//userIcon.setBackground(Util.getBackground("ic_launcher.png"));
		TextView userName = new TextView(getActivity());
		userName.setText(children.get(childNum).getChildName());
		userName.setLayoutParams(rParams_tv);
		CheckBox checkBox = new CheckBox(getActivity());
		checkBox.setId(childNum);
		checkBox.setOnClickListener(this);
		childChecks.add(checkBox);
		checkBox.setLayoutParams(rParams_cb);
		rLayout.addView(checkBox);
		rLayout.addView(userName);
		//rLayout.addView(userIcon);
		kidLinearLayout.addView(rLayout);
	}
	
	public void addPerformance() {
		LinearLayout.LayoutParams lParmas = new LinearLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		TextView performance = new TextView(getActivity());
		TextView padding = new TextView(getActivity());
		performance.setLayoutParams(lParmas);
		performance.setText("近期表现");
		performance.setTextSize(18);
		padding.setLayoutParams(lParmas);
		padding.setText(" ");
		//kidLinearLayout.addView(padding);
		kidLinearLayout.addView(performance);
	}
	
	public void changePerformancePanel() {
		kidLinearLayout.removeView(panel);
		panel = new PerformancePanel(getActivity());
		DataSeries series = new DataSeries();  
        List<DataElement> itemListOne = new ArrayList<DataElement>();  
        itemListOne.add(new DataElement("shoes",100, platterTable[0]));   
        series.addSeries("正常", itemListOne);  
          
        List<DataElement> itemListTwo = new ArrayList<DataElement>();  
        itemListTwo.add(new DataElement("shoes",170, platterTable[1]));   
        series.addSeries("迟到", itemListTwo);  
          
        List<DataElement> itemListThree = new ArrayList<DataElement>();  
        itemListThree.add(new DataElement("shoes",50, platterTable[2]));   
        series.addSeries("早退", itemListThree);  
        
        panel.setSeries(series);
		kidLinearLayout.addView(panel);
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		CheckBox checkBox = (CheckBox) v;
		if(checkBox.isChecked()) {
			SharePreferencesUtil.writeCurrentChild(getActivity(), 
					children.get(checkBox.getId()).getChildName());
			checkBox.setEnabled(false);
			changePerformancePanel();
			for(int j = 0; j < children.size(); j++) {
				if(j == checkBox.getId()) continue;
				else {
					childChecks.get(j).setChecked(false);
					childChecks.get(j).setEnabled(true);
				}
			}
			
		}
	}
	
    public void initDataSerial() {
    	DataSeries series = new DataSeries();  
        List<DataElement> itemListOne = new ArrayList<DataElement>();  
        itemListOne.add(new DataElement("shoes",120, platterTable[0]));   
        series.addSeries("正常", itemListOne);  
          
        List<DataElement> itemListTwo = new ArrayList<DataElement>();  
        itemListTwo.add(new DataElement("shoes",110, platterTable[1]));   
        series.addSeries("迟到", itemListTwo);  
          
        List<DataElement> itemListThree = new ArrayList<DataElement>();  
        itemListThree.add(new DataElement("shoes",100, platterTable[2]));   
        series.addSeries("早退", itemListThree);  
        
        panel.setSeries(series);
    }
    
	public void test() {
		cdb.save("刘沛涵", "迟到", "上午10点到校", "二年级一班");
		cdb.save("刘沛涵", "迟到", "下午2点到校", "二年级一班");
		cdb.save("蒲毛毛", "早退", "下午2点请假", "二年级二班");
		cdb.save("刘大毛", "正常", "上午8点到校", "二年级三班");
	}
	
}
