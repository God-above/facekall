package com.tencent.avsdk.adapter;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.text.SpannableString;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.falcon.facekall.av.DisplayOrgPicActivity;
import com.tencent.TIMElem;
import com.tencent.TIMFileElem;
import com.tencent.TIMConversationType;
import com.tencent.TIMImage;
import com.tencent.TIMImageType;
import com.tencent.TIMGroupTipsElem;
import com.tencent.TIMGroupTipsType;
import com.tencent.TIMImageElem;
import com.tencent.TIMMessageStatus;
import com.tencent.TIMSoundElem;
import com.tencent.TIMTextElem;
import com.tencent.TIMValueCallBack;
import com.tencent.TIMElemType;

import com.tencent.avsdk.Utils.ChatEntity;
import com.tencent.avsdk.Utils.Constant;
import com.tencent.avsdk.Utils.DateHelper;
import com.tencent.avsdk.Utils.EmojiUtil;

public class ChatMsgListAdapter extends BaseAdapter {

	private static String TAG = ChatMsgListAdapter.class.getSimpleName();
	private static final int ITEMCOUNT = 9;
	private List<ChatEntity> listMessage = null;
	private LayoutInflater inflater;
	public static final int TYPE_TEXT_SEND = 0;
	public static final int TYPE_TEXT_RECV = 1;
	public static final int TYPE_IMAGE_SEND = 2;
	public static final int TYPE_IMAGE_RECV = 3;
	public static final int TYPE_FILE_SEND = 4;
	public static final int TYPE_FILE_RECV = 5;
	public static final int TYPE_SOUND_SEND = 6;
	public static final int TYPE_SOUND_RECV = 7;
	public static final int TYPE_GROUP_TIPS = 8;
	private  Context context;
	private  Activity activity;
	public static MediaPlayer   mPlayer = null;  
	private  boolean mIsVoicePalying = false;
	private ImageView currentPalyingIV ;
	private AnimationDrawable currentAnimation;
	private ProgressDialog progressDialog;
	
	
	public ChatMsgListAdapter(Context context, List<ChatEntity> messages) {
		this.listMessage = messages;	
		this.context = context;
		this.activity = (Activity)context;	
		inflater = LayoutInflater.from(context);

	}

	public int getCount() {
		if(listMessage!=null){			
			return listMessage.size();
		}
		return 0;
	}

	public Object getItem(int position) {
		if(listMessage!=null){		
			return listMessage.get(position);
		}
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	
	public int getItemViewType(int position) {		
		ChatEntity entity = listMessage.get(position);		
		if(entity.getElem().getType()==TIMElemType.Text){
			return entity.getIsSelf() ? TYPE_TEXT_SEND : TYPE_TEXT_RECV;
		} else if(entity.getElem().getType()==TIMElemType.Image){
			return entity.getIsSelf() ? TYPE_IMAGE_SEND : TYPE_IMAGE_RECV;
//		}else if(entity.getElem().getType()==TIMElemType.File){
//			return entity.getIsSelf()? TYPE_FILE_SEND : TYPE_FILE_RECV;
		}else if(entity.getElem().getType()==TIMElemType.Sound){
			return entity.getIsSelf() ? TYPE_SOUND_SEND : TYPE_SOUND_RECV;
		}else if(entity.getElem().getType() == TIMElemType.GroupTips){
			return TYPE_GROUP_TIPS;
		}
		return -1;			
	}


	public int getViewTypeCount() {
		return ITEMCOUNT;
	}
	
	private View dynamicCreateView(int position) {
		int type = getItemViewType(position); 
		switch(type){
		case TYPE_TEXT_SEND:
			return inflater.inflate(R.layout.chat_item_text_right, null);
		case TYPE_TEXT_RECV:
			return inflater.inflate(R.layout.chat_item_text_left, null);
		case TYPE_IMAGE_SEND:
			return inflater.inflate(R.layout.chat_item_pic_right, null) ;
		case TYPE_IMAGE_RECV:
			return inflater.inflate(R.layout.chat_item_pic_left, null);
		case TYPE_FILE_SEND:
			return inflater.inflate(R.layout.chat_item_file_right, null);
		case TYPE_FILE_RECV:
			return inflater.inflate(R.layout.chat_item_file_left, null);	
		case TYPE_SOUND_SEND:
			return inflater.inflate(R.layout.chat_item_ptt_right, null);
		case TYPE_SOUND_RECV:	
			return  inflater.inflate(R.layout.chat_item_ptt_left, null);	
		case TYPE_GROUP_TIPS:
			return  inflater.inflate(R.layout.chat_item_group_tips, null);	
		default:
			return inflater.inflate(R.layout.chat_item_text_right, null);			
		}
	
	}
	public View getView(int position, View convertView, ViewGroup parent) {
		ChatEntity entity = listMessage.get(position);
		TIMElem elem = entity.getElem();
		ViewHolder viewHolder = null;
		if (convertView == null) {			
			convertView = dynamicCreateView(position);			
			viewHolder = new ViewHolder();
			viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
			if(elem.getType() == TIMElemType.GroupTips){
				viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_msg_content);
			}else{
				viewHolder.avatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
				viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tv_username);
				
				if(entity.getIsSelf()){
					viewHolder.ivMsgStatus = (ImageView)convertView.findViewById(R.id.iv_msg_status);
				}	
				
				if(elem.getType()== TIMElemType.Text){		
					viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);			
				}
				else if(elem.getType()== TIMElemType.Image){
					viewHolder.ivContent = (ImageView)convertView.findViewById(R.id.iv_chat_item_content);	
					viewHolder.pbSending = (ProgressBar)convertView.findViewById(R.id.pb_pic);
				}
//				else if(elem.getType()== TIMElemType.File){
//					viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tv_file_name);
//					viewHolder.tvFileSize = (TextView)convertView.findViewById(R.id.tv_file_size);
//				}	
				else if(elem.getType()== TIMElemType.Sound){				
					viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tv_total_time);
					viewHolder.ivContent = (ImageView)convertView.findViewById(R.id.iv_chat_item_content);
					viewHolder.rlPttContent = (RelativeLayout)convertView.findViewById(R.id.rl_chat_item_content);
				}
			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(position==0){
			viewHolder.tvSendTime.setText(DateHelper.GetStringFormat(entity.getTime()));
		}else {		
			if (DateHelper.LongInterval(entity.getTime(), listMessage.get(position - 1).getTime())) {
				viewHolder.tvSendTime.setText(DateHelper.GetStringFormat(entity.getTime()));
				viewHolder.tvSendTime.setVisibility(View.VISIBLE);
			} else {
				viewHolder.tvSendTime.setVisibility(View.GONE);
			}
		}	
		if(elem.getType() != TIMElemType.GroupTips){
			if(entity.getType() == TIMConversationType.Group){
				viewHolder.tvUserName.setText(entity.getSenderName());
			}else{
				viewHolder.tvUserName.setVisibility(View.GONE);
			}
			viewHolder.avatar.setImageResource(R.drawable.chat_default_avatar);
		}
		Log.d(TAG, "msg status:" + entity.getStatus());
		if((viewHolder.ivMsgStatus != null) && (entity.getStatus() == TIMMessageStatus.SendFail)){
			
			viewHolder.ivMsgStatus.setVisibility(View.VISIBLE);
		}		
		else if (viewHolder.ivMsgStatus != null){
			viewHolder.ivMsgStatus.setVisibility(View.GONE);
		}
		if(elem.getType()== TIMElemType.Text){
			DisplayTextMsg(elem,viewHolder,position);
		}else if(elem.getType()== TIMElemType.Image){			
			DisplayPicMsg(elem,entity.getIsSelf(),entity.getStatus(),viewHolder,position);
//		}else if(elem.getType()== TIMElemType.File){
//			DisplayFileMsg(elem,entity.getIsSelf(),viewHolder,position);
		}else if(elem.getType()== TIMElemType.Sound){
			DisplayPttMsg(elem,entity.getIsSelf(),viewHolder,position);
		}		
		else if(elem.getType() == TIMElemType.GroupTips){
			DisplayGroupTips(elem,viewHolder,position);
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView tvSendTime;
		public TextView tvUserName;
		public TextView tvContent;
		public ImageView ivContent;
		public ImageView avatar;
		public ImageView ivMsgStatus;
		public TextView tvFileSize;	
		public RelativeLayout rlPttContent;
		public ProgressBar pbSending;
	}

	private void DisplayTextMsg(TIMElem elem,ViewHolder viewHolder,int position){
		TIMTextElem textElem = (TIMTextElem)elem;
		SpannableString spannableString = EmojiUtil.getInstace().getSpannableString(context, textElem.getText());
		viewHolder.tvContent.setText(spannableString);		
	}
	
	private void DisplayGroupTips(TIMElem elem,ViewHolder viewHolder,int position){
		TIMGroupTipsElem tipsElem = (TIMGroupTipsElem)elem;
		String strTmp = new String();
		Log.d(TAG,"DisplayGroupTips:" + tipsElem.getOpUser() + ":" + tipsElem.getTipsType() );
		if(tipsElem.getTipsType() == TIMGroupTipsType.Join){
			strTmp = tipsElem.getOpUser() + "邀请";
			for(int i=0;i<tipsElem.getUserList().size();i++){
				strTmp += tipsElem.getUserList().get(i);
				if(i!= (tipsElem.getUserList().size()-1)){
					strTmp += ",";	
				}
			}
			strTmp += "加入了群聊";			
		}else if(tipsElem.getTipsType() == TIMGroupTipsType.ModifyGroupName){
			strTmp = tipsElem.getOpUser() + "修改群名称为" + tipsElem.getGroupName();
			
		}else if(tipsElem.getTipsType() == TIMGroupTipsType.Quit){			
			strTmp = tipsElem.getOpUser() + "退出群聊";
		}else if(tipsElem.getTipsType() == TIMGroupTipsType.Kick){	
			for(int i=0;i<tipsElem.getUserList().size();i++){
				strTmp += tipsElem.getUserList().get(i);
				if(i!= (tipsElem.getUserList().size()-1)){
					strTmp += ",";	
				}
			}			
			strTmp += "被踢出群";			
		}
		
		viewHolder.tvContent.setText(strTmp);	
	}
	private void SaveMap(String filePath,byte[] bytes){
		try {  
		    File file = new File(filePath);
		    if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
	        FileOutputStream fops = new FileOutputStream(file);
	        fops.write(bytes);
	        fops.flush();
	        fops.close();        
	           
	        
         } catch (IOException e) {  
             e.printStackTrace();  
             Log.e(TAG,e.toString());
          
         }
				
	}
	public static Bitmap GetRightOritationNew(String filePath){
		Bitmap bitmap  = BitmapFactory.decodeFile(filePath);		
		ExifInterface exif = null;	
		try{
			exif = new ExifInterface(filePath);  
		}catch (IOException e) {  
            e.printStackTrace();  
            Log.e(TAG,e.toString());
            exif = null;  
            return bitmap;
        }			
		int degree=0;
		if (exif != null) { 
	         int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,  
	                 ExifInterface.ORIENTATION_UNDEFINED);  
	
	         switch (ori) {  
	         case ExifInterface.ORIENTATION_ROTATE_90:  
	        	 degree = 90;  
	             break;  
	         case ExifInterface.ORIENTATION_ROTATE_180:  
	        	 degree = 180;  
	             break;  
	         case ExifInterface.ORIENTATION_ROTATE_270:  
	        	 degree = 270;  
	             break;  
	         default:  
	        	 degree = 0;  
	             break;  
	         }		    
		}		
		if (degree != 0) {  
             // 旋转图片  
             Matrix m = new Matrix();  
             m.postRotate(degree);  
             bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),  
                     bitmap.getHeight(), m, true);   
         }		
		return bitmap;		
	}
	
	private void DisplayPicMsg(TIMElem e,boolean isSelf,TIMMessageStatus status,ViewHolder holder,int position){
        final TIMImageElem elem = (TIMImageElem)e; 
        final ViewHolder viewHolder = holder;
        Log.d(TAG,"DisplayPicMsg:" + status + ":" + elem.getImageSize() + ":" + elem.getImageWidth() + ":" + elem.getImageHeight());


    	viewHolder.ivContent.setVisibility(View.GONE);
    	if(status == TIMMessageStatus.Sending){
        	viewHolder.ivContent.setVisibility(View.VISIBLE);
			viewHolder.ivContent.setImageResource(R.drawable.defaultpic);
			viewHolder.pbSending.setVisibility(View.VISIBLE);
    		return;
    	}else{
    		viewHolder.pbSending.setVisibility(View.GONE);
    	}
    	
    	for(TIMImage image : elem.getImageList()){
    	    Log.d(TAG, "image type: " + image.getType()  +
                    " image size: " + image.getSize()  +
                    " image height: " + image.getHeight() + 
                    " image width: " + image.getWidth());
    	    //先展示缩略图
    	    if(image.getType() == TIMImageType.Thumb){
    	    	final String filePath = Constant.TH_IMG_CACHE_DIR  + image.getUuid() + ".jgp";
    	    	File thumbFile = new File(filePath);
    	    	if(thumbFile.exists()){
    	    		viewHolder.ivContent.setVisibility(View.VISIBLE);
					viewHolder.ivContent.setImageBitmap(GetRightOritationNew(filePath));
    	    	}else{
		            image.getImage(new TIMValueCallBack<byte[]>() {
		                @Override
		                public void onError(int code, String desc) {//获取图片失败
		                    //服务器返回了错误码code和错误描述desc，可用于定位请求失败原因
		                    //错误码code列表请参见附录
		                    Log.e(TAG, "getThumbPic failed. code: " + code + " errmsg: " + desc);		
		              //      Toast.makeText(activity, "获取缩略图失败. code: " + code + " errmsg: " + desc, Toast.LENGTH_SHORT).show();
	      	              	viewHolder.ivContent.setVisibility(View.VISIBLE);
	      					viewHolder.ivContent.setImageResource(R.drawable.defaultpic);          					
		                }
		
		                @Override
		                public void onSuccess(byte[] data) {
		                    //doSomething
		                    Log.d(TAG, "getThumbPic success. data size: " + data.length);
                        	SaveMap(filePath,data);
                        	viewHolder.ivContent.setVisibility(View.VISIBLE);
    						viewHolder.ivContent.setImageBitmap(GetRightOritationNew(filePath));
		                }
		            });
	    	    }
	            break;
    	    }
    	    
    	}

    	holder.ivContent.setClickable(true); 
    	holder.ivContent.setOnClickListener(new OnClickListener(){  
			@Override
			public void onClick(View v) {				
				  progressDialog = ProgressDialog.show(activity, "加载中...", "请稍后...", true, false);  
				  progressDialog.setCancelable(true);
				// TODO Auto-generated method stub
				  
			    	for(TIMImage image : elem.getImageList()){
			    	    Log.d(TAG, "image type: " + image.getType() +
			                    " image size " + image.getSize() +
			                    " image height " + image.getHeight() +
			                    " image width " + image.getWidth());
			    	    //先展示缩略图
			    	    if(image.getType() == TIMImageType.Original){			    	    	
			    	    	final String filePath = Constant.ORG_IMG_CACHE_DIR  + image.getUuid() + ".jpg";
			    	    	File orgFile = new File(filePath);
			    	    	if(orgFile.exists()){	
			    	    		Intent intent = new Intent(activity,DisplayOrgPicActivity.class);
	                        	intent.putExtra("filePath", filePath);
	                        	activity.startActivity(intent);
	                        	progressDialog.dismiss();		
			    	    	}else{
					            image.getImage(new TIMValueCallBack<byte[]>() {
					                @Override
					                public void onError(int code, String desc) {
					                	//获取图片失败
					                    //服务器返回了错误码code和错误描述desc，可用于定位请求失败原因
					                    //错误码code列表请参见附录
					                    Log.e(TAG, "getOriginPic failed. code: " + code + " errmsg: " + desc);
					                    Toast.makeText(activity, "获取原图失败。 code: " + code + " errmsg: " + desc, Toast.LENGTH_SHORT).show();
			          					progressDialog.dismiss();
					                }
					
					                @Override
					                public void onSuccess(byte[] data) {//成功，参数为图片数据
					                    //doSomething
					                    Log.d(TAG, "getOriginPic success. data size: " + data.length);
					                 //   final byte[] info = data;     
//					            		activity.runOnUiThread(new Runnable() {
//					    					public void run(){      	               
					                        //	
			                        	SaveMap(filePath,data);				                        
			                        	Intent intent = new Intent(activity,DisplayOrgPicActivity.class);
			                        	intent.putExtra("filePath", filePath);
			                        	activity.startActivity(intent);
			                        	progressDialog.dismiss();			                       		
					    						
//					    					}
//					            		});	        	                    
					                }
					            });
				    	    }
				            break;
			    	    }			    	    
			    	}	
			}
    		
    	});   
    
	}
	
	private void DisplayFileMsg(TIMElem e,boolean isSelf,ViewHolder holder,int position){
		final TIMFileElem elem = (TIMFileElem)e;		
        final ViewHolder viewHolder = holder;
        	elem.getFile(new TIMValueCallBack<byte []>(){
            @Override
            public void onError(int code, String desc) {
                Log.d(TAG, "getFile failed. code: " + code + " errmsg: " + desc);
          		activity.runOnUiThread(new Runnable() {
    					public void run(){   
    						viewHolder.tvFileSize.setText("未知"); 
    						viewHolder.tvContent.setText("未知");
    					}
            		});	                 
            }

            @Override
            public void onSuccess(byte [] info) {
        		activity.runOnUiThread(new Runnable() {
					public void run(){   
						viewHolder.tvFileSize.setText(String.valueOf(elem.getFileSize()/1024 + "k")); 
						viewHolder.tvContent.setText(elem.getFileName());
					}
        		});	         	
            }
        });   
	}		
	
	private void stopCurrentPttMedia(boolean bSelf){
		if(mPlayer!=null){
	    	mPlayer.stop();
	    	mPlayer.release();
	    	mPlayer =null;
		}
    	currentAnimation.stop();
		if(bSelf){
			currentPalyingIV.setImageResource(R.drawable.skin_aio_ptt_record_user_nor);
		}else{
			currentPalyingIV.setImageResource(R.drawable.skin_aio_ptt_record_friend_nor);
		}	
		mIsVoicePalying = false;
	}
	private void DisplayPttMsg(TIMElem e,boolean isSelf,ViewHolder holder,int position){
		final TIMSoundElem elem = (TIMSoundElem)e;
		final boolean bSelf=isSelf;
		final int tmpPs = position;
		//临时显示下时间，后期sdk加上				
		holder.tvContent.setText(elem.getDuration() + "'");
		final ImageView im = holder.ivContent;		
	  	holder.rlPttContent.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {	
				//点击正在播放的ptt，停止
				if(currentPalyingIV == im && mIsVoicePalying){
					stopCurrentPttMedia(bSelf);					
					return;
				}				
				   elem.getSound(new TIMValueCallBack<byte []>(){
		            @Override
		            public void onError(int code, String desc) {
		                Log.e(TAG, "getSound failed. code: " + code + " errmsg: " + desc);		             
		            }

		            @Override
		            public void onSuccess(byte[] info) {
		            	final byte[] bytes = info;
		            	
		        		activity.runOnUiThread(new Runnable() {
							public void run(){  
							   	String filePath = FacekallApplication.getInstance().getFilesDir().getAbsolutePath() + "/ptt/tmp_ptt.amr";
							    
					    		try {	    			
					    			File file = new File(filePath);
					    			if (!file.getParentFile().exists()) {
					    				file.getParentFile().mkdirs();
					    			}
					    	        FileOutputStream fops = new FileOutputStream(file);
					    	        fops.write(bytes);
					    	        fops.flush();
					    	        fops.close();
					    	        Log.d(TAG,"voice status:" + mIsVoicePalying);					    	        
									    	        
					    	        if(mIsVoicePalying){
					    	        	stopCurrentPttMedia(bSelf);
					    	        }
					    	        mPlayer = new MediaPlayer();  	    	     
				    	            mPlayer.setDataSource(filePath);  
				    	            mPlayer.prepare(); 
				    	            mPlayer.start();   
				    	            mIsVoicePalying =true;
				
									int animationResId ;
									if(bSelf){
										animationResId= R.anim.mystop;					
									}else{
										animationResId= R.anim.stop;
									}
									final AnimationDrawable animateDrawable = (AnimationDrawable) context.getResources().getDrawable(animationResId);
					    	        	currentPalyingIV = im;
					    	        	currentAnimation = animateDrawable;
					    	        	
									im.setImageDrawable(animateDrawable);
									Log.d(TAG,"anmination status:" + tmpPs + ":" +animateDrawable.isRunning());
									animateDrawable.start();								
									
									mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
										@Override
										public void onCompletion(MediaPlayer mp) {										
											// TODO Auto-generated method stub
											mIsVoicePalying =false;
											if(mPlayer != null){
												mPlayer.release();
												mPlayer = null;
											}
											animateDrawable.stop(); 
											if(bSelf){
												im.setImageResource(R.drawable.skin_aio_ptt_record_user_nor);
											}else{
												im.setImageResource(R.drawable.skin_aio_ptt_record_friend_nor);
											}
										}
									});					
								   	            
				    	        }catch(IllegalArgumentException e){
				    	        	Log.e(TAG, "ptt paly  failed" + e.toString());
				    	        } catch (IOException e) {  
				    	            Log.e(TAG, "ptt paly  failed" + e.toString());  
				    	        }  
							}	
								
						});		        		      	
		            }
		        });   
    	}
		
	});
	}
}
