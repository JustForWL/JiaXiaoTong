package com.example.jiaxiaotong.fragment;

import java.util.ArrayList;

import com.example.jiaxiaotong.R;
import com.example.jiaxiaotong.activity.ChatActivity;
import com.example.jiaxiaotong.bean.TeacherUserBean;
import com.example.jiaxiaotong.dao.TeacherUserDB;
import com.example.jiaxiaotong.utils.Logger;
import com.example.jiaxiaotong.utils.Util;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;



/**
 * Created by John on 12/15/2014.
 */
public class TchFragment extends Fragment{
	private ListView teachersView = null;
	private View view = null;
	private TeacherAdapter teacherAdapter = null;
	private ArrayList<TeacherUserBean> teachers = null;
	private TeacherUserDB tdb = null;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			 ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.activity_teacher_fragment, container, false);
		teachersView = (ListView) view.findViewById(R.id.listview_teacher);
		tdb = new TeacherUserDB(getActivity());
		//test();
		initData();
		return view;
	}
	
	private void initData() {
//		String currentChild = SharePreferencesUtil.readCurrentChild(getActivity());
//		teachers = tdb.getUsersByStudent(currentChild);
		teachers = tdb.getUsersByStudent("cc");
		teacherAdapter = new TeacherAdapter(this.getActivity(), teachers);
		teachersView.setAdapter(teacherAdapter);
	}
    
	private void test() {
		TeacherUserBean t1 = new TeacherUserBean();
		t1.setUserAccount("aa");
		t1.setUserName("刘沛涵");
		t1.setIconAddr("ic_launcher.png");
		t1.setRole("班主任");
		t1.setStudent("cc");
		TeacherUserBean b2 = new TeacherUserBean();
		b2.setUserAccount("bb");
		b2.setUserName("刘沛涵");
		b2.setIconAddr("ic_launcher.png");
		b2.setRole("语文老师");
		b2.setStudent("cc");
		tdb.saveUser(t1);
		tdb.saveUser(b2);
	}
	
	class TeacherAdapter extends BaseAdapter {
		private ArrayList<TeacherUserBean> teachers = null;
	    private LayoutInflater layoutInflater = null;
	    private static final String TALK = "talkTo";
        private static final String ACCOUNT = "account";
        private Context context;
        
		public TeacherAdapter(Context context, ArrayList<TeacherUserBean> teachers) {
			this.teachers = teachers;
			layoutInflater = LayoutInflater.from(context);
			this.context = context;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return this.teachers.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return this.teachers.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			final TeacherUserBean teacher = (TeacherUserBean) this.teachers.get(position);
			ViewHolder viewHolder = null;
			if(null == convertView) {
				convertView = layoutInflater.inflate(R.layout.teacher_item, null);
				viewHolder = new ViewHolder();
				viewHolder.icon = (ImageView) convertView.findViewById(R.id.teacher_icon);
				viewHolder.role = (TextView) convertView.findViewById(R.id.teacher_role);
				viewHolder.name = (TextView) convertView.findViewById(R.id.teacher_name);
				viewHolder.layout = (RelativeLayout) convertView.findViewById(R.id.teacher_layout);
				viewHolder.layout.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Bundle bundle = new Bundle();
						bundle.putString(TALK, teacher.getUserName());
						bundle.putString(ACCOUNT, teacher.getUserAccount());
						Intent intent = new Intent(context, ChatActivity.class);
						intent.putExtras(bundle);
						startActivity(intent);
					}
				});
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.icon.setBackground(Util.getBackground(teacher.getIconAddr()));
			viewHolder.name.setText(teacher.getUserName());
			viewHolder.role.setText(teacher.getRole());
			return convertView;
		}
	}
	static class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView role;
		public RelativeLayout layout;
	}
}
