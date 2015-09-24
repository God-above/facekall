package com.tencent.avsdk.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.falcon.facekall.R;
import com.tencent.avsdk.Utils.RecentEntity;
import com.tencent.avsdk.view.BadgeView;

import java.util.List;

public class RecentGridAdapter extends BaseAdapter {

	private static final String TAG = RecentGridAdapter.class.getSimpleName();

	private List<RecentEntity> listRecentMsg;
	private LayoutInflater inflater;
	private Context context;

	public RecentGridAdapter(Context context, List<RecentEntity> list) {
		this.listRecentMsg = list;
		inflater = LayoutInflater.from(context);
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return listRecentMsg.size();
	}

	@Override
	public Object getItem(int position) {
		return listRecentMsg.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void refresh(){
		this.notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {	
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.recent_list_item_gridview, null);
			holder = new ViewHolder();		
			holder.userName = (TextView) convertView.findViewById(R.id.recent_name);
			holder.avatar = (ImageView)convertView.findViewById(R.id.recent_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final RecentEntity entity = listRecentMsg.get(position);	
		holder.userName.setText(entity.getNick()!=null? entity.getNick():entity.getName());
 		if (entity.getCount() > 0) {
			BadgeView badge = new BadgeView(context, holder.avatar);
			badge.setBadgePosition(BadgeView.POSITION_BOTTOM_RIGHT);
 			if(entity.getCount() > 99){
				badge.setText("99");
				badge.show();
 			}else{
				badge.setText(entity.getCount()+"");
				badge.show();
 			}
		}

		if(entity.getIsGroupMsg()){	
			holder.avatar.setImageResource(R.drawable.icon_group);
		}else{
			holder.avatar.setImageResource(R.drawable.chat_default_avatar);
		}
	    return convertView;
	}
	
	
	private static class ViewHolder {
		TextView userName;
		ImageView avatar;
	}

}


