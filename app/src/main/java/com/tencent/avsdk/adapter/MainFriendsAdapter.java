package com.tencent.avsdk.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.falcon.Utils.Node.UserNode;
import com.falcon.facekall.R;
import com.tencent.openqq.protocol.im_open.im_common;

public class MainFriendsAdapter extends BaseAdapter {

	/** 上下文 */
	private Context ctx;
	/** 图片Url集合 */
	private  List<UserNode> friends;

	public MainFriendsAdapter(Context ctx, List<UserNode> friends) {
		this.ctx = ctx;
		this.friends = friends;
	}

	@Override
	public int getCount() {
		return friends == null ? 0 : friends.size();
	}

	@Override
	public Object getItem(int position) {
		return friends.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = View.inflate(ctx, R.layout.friend_list_item_gridview, null);
		TextView name = (TextView) view.findViewById(R.id.friend_name);
		ImageView pic = (ImageView) view.findViewById(R.id.pic);	
//		DisplayImageOptions options = new DisplayImageOptions.Builder()//
//				.cacheInMemory(true)//
//				.cacheOnDisc(true)//
//				.bitmapConfig(Config.RGB_565)//
//				.build();
//		ImageLoader.getInstance().displayImage(friends.get(position).category_pic, pic, options);
		if(friends.get(position).getNickname().equals("+")){
			pic.setImageResource(R.drawable.add_friend);
			name.setText("添加好友");
			name.setTextSize(22);
		}
		name.setText((friends.get(position).getNickname()));
		return view;
	}

}
