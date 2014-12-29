package com.example.jiaxiaotong.fragment;

import java.io.File;
import java.util.ArrayList;

import com.example.jiaxiaotong.R;
import com.example.jiaxiaotong.activity.AskForLeave;
import com.example.jiaxiaotong.activity.ChatActivity;
import com.example.jiaxiaotong.bean.ParentUserBean;
import com.example.jiaxiaotong.constants.App;
import com.example.jiaxiaotong.dao.ParentUserDB;
import com.example.jiaxiaotong.utils.Logger;
import com.example.jiaxiaotong.utils.SharePreferencesUtil;
import com.example.jiaxiaotong.utils.Util;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * Created by John on 12/15/2014.
 */
public class StuFragment extends Fragment {
	private ListView students = null;
	private View view = null;
	private UserAdapter userAdapter = null;
	private ArrayList<ParentUserBean> parents = null;
	private ParentUserDB pdb = null;
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container,  Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_student_fragment, container, false);
		students = (ListView) view.findViewById(R.id.listview_student);
		pdb = new ParentUserDB(getActivity());
		//test();
		initData();
		return view;
	}
	
	private void initData() {
//		String currentChild = SharePreferencesUtil.readCurrentChild(getActivity());
//		parents = pdb.getUsersByClassmate(currentChild);
		parents = pdb.getUsersByClassmate("cc");
		userAdapter = new UserAdapter(this.getActivity(), parents);
		students.setAdapter(userAdapter);
	}
	
	private void test() {
		ParentUserBean b1 = new ParentUserBean();
		b1.setUserAccount("aa");
		b1.setUserName("刘沛涵");
		b1.setIconAddr("ic_launcher.png");
		b1.setClassmate("cc");
		ParentUserBean b2 = new ParentUserBean();
		b2.setUserAccount("bb");
		b2.setUserName("刘沛涵");
		b2.setIconAddr("ic_launcher.png");
		b2.setClassmate("cc");
		pdb.saveUser(b1);
		pdb.saveUser(b2);
	}
	
	class UserAdapter extends BaseAdapter{
        private ArrayList<ParentUserBean> parents = null;
        private LayoutInflater layoutInflater = null;
        private Context context = null;
        public UserAdapter(Context context, ArrayList<ParentUserBean> parents) {
        	this.context = context;
        	this.parents = parents;
        	this.layoutInflater = LayoutInflater.from(context);
        }
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.parents.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.parents.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final ParentUserBean mParent = (ParentUserBean) this.parents.get(position);
			ViewHolder viewHolder = null;
			if(null == convertView) {
				convertView = layoutInflater.inflate(R.layout.student_item, null);
				viewHolder = new ViewHolder();
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.userIcon);
				viewHolder.name = (TextView) convertView.findViewById(R.id.userName);
				viewHolder.layout = (LinearLayout) convertView.findViewById(R.id.student_layout);
				viewHolder.layout.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Bundle bundle = new Bundle();
						bundle.putString(App.TALK, mParent.getUserName());
						bundle.putString(App.ACCOUNT, mParent.getUserAccount());
						Intent intent = new Intent(context, ChatActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
						
					}
				});
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.icon.setBackground(Util.getBackground(mParent.getIconAddr()));
			viewHolder.name.setText(mParent.getUserName());
			viewHolder.account = mParent.getUserAccount();
			return convertView;
		}	
		
	}
	
	static class ViewHolder {
		public ImageView icon;
		public TextView name;
		public LinearLayout layout;
		public String account;
	}
	
}
