package com.example.jiaxiaotong.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.jiaxiaotong.R;

public class AskForLeave extends Activity {
	
	private Button btnSure=null;
	private Button btnCancel=null;
	private EditText textReason=null;
	private EditText textTime=null;
	private String studentId=null;
	private TextView studentName=null;
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
        setContentView(R.layout.askforleave_layout);
        initview();
        //btnCancel.setOnClickListener(this);
        //btnSure.setOnClickListener(this);
        Intent intent=getIntent();
        String data=intent.getStringExtra("studentName");
        studentName.setText(data); 
        
    }
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();	
	}
	
	@Override
	protected void onStop(){
		super.onStop();		
	}
	
	private void initview(){
		
		this.btnSure=(Button)findViewById(R.id.btn_sure);
		this.btnCancel=(Button)findViewById(R.id.btn_cancel);
		this.textReason=(EditText)findViewById(R.id.editText_reason);
		this.textTime=(EditText)findViewById(R.id.editText_time);
		this.studentName=(TextView)findViewById(R.id.textView_name);
		
	}

	/*@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_cancel:
			finish();
			break;
		default:
				break;
		}
		
	}*/
	
	
	
}
