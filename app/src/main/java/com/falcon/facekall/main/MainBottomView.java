package com.falcon.facekall.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.falcon.HTTP.FLHttp;
import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.FLHttpLogin;
import com.falcon.Utils.Node.UserNode;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.falcon.facekall.av.AddFriendActivity;
import com.falcon.facekall.av.ChatNewActivity;
import com.tencent.TIMConversation;
import com.tencent.TIMConversationType;
import com.tencent.TIMElemType;
import com.tencent.TIMGroupBaseInfo;
import com.tencent.TIMGroupManager;
import com.tencent.TIMGroupTipsElem;
import com.tencent.TIMGroupTipsType;
import com.tencent.TIMManager;
import com.tencent.TIMMessage;
import com.tencent.TIMMessageListener;
import com.tencent.TIMValueCallBack;
import com.tencent.avsdk.Utils.ChnToSpell;
import com.tencent.avsdk.Utils.Constant;
import com.tencent.avsdk.Utils.RecentEntity;
import com.tencent.avsdk.adapter.MainFriendsAdapter;
import com.tencent.avsdk.adapter.RecentGridAdapter;
import com.tencent.avsdk.c2c.UserInfo;
import com.tencent.avsdk.c2c.UserInfoManager;
import com.tencent.avsdk.control.QavsdkControl;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/10 0010.
 */
public class MainBottomView extends LinearLayout implements  ViewPager.OnPageChangeListener{
    private GridView mgridview1 , mgridview2;
    public ViewPager viewPager;
    public PagerAdapter vpAdapter;
    private RecentGridAdapter recentAdapter;
    private MainFriendsAdapter friendsAdapter;
    private List<View> views;
    // 底部小点图片
    private ImageView[] dots;
    // 记录当前选中位置
    private int currentIndex;

    private List<RecentEntity> entitys;
    private List<UserInfo> contactList;
    private static long conversation_num = 0;//会话数

    private Context context;
    private QavsdkControl mQavsdkControl= FacekallApplication.getInstance().getQavsdkControl();

    private MainCenterView centerView;
    FLHttpLogin mHttp=new FLHttpLogin();


    public MainBottomView(Context context) {
        super(context);
    }

    public MainBottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_mainbottom, this);
//        contactList = new ArrayList<UserInfo>();
//        SetMessageListener();
//        initViewPager();
    }

    private void initViewPager() {
        View main_bottom1 = LayoutInflater.from(context).inflate(R.layout.act_main_bottom1, null);
        View  main_bottom2 = LayoutInflater.from(context).inflate(R.layout.act_main_bottom2, null);
        viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        mgridview1 = (GridView) main_bottom1.findViewById(R.id.gridview_main_friends);
        mgridview1.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mgridview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

//                if (mQavsdkControl.hasAVContext()) {
//                    if (position == 0) {
//                        Intent intent = new Intent(context, AddFriendActivity.class);
//                        context.startActivity(intent);
//                    } else {
//                        invite(true);
//                        MainActivity.isSender = true;
//                    }
//
//                }
            }
        });

        mgridview2 = (GridView) main_bottom2.findViewById(R.id.gridview_main_message);
        mgridview2.setSelector(new ColorDrawable(Color.TRANSPARENT));
        mgridview2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                final RecentEntity entity = (RecentEntity) recentAdapter.getItem(position);
                Intent intent = new Intent(context, ChatNewActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(entity.getIsGroupMsg()){
                    intent.putExtra("chatType", ChatNewActivity.CHATTYPE_GROUP);
                    intent.putExtra("groupID", entity.getName());
                    intent.putExtra("groupName", entity.getNick());
                }else{
                    intent.putExtra("chatType", ChatNewActivity.CHATTYPE_C2C);
                    intent.putExtra("userName", entity.getName());
                }
                context.startActivity(intent);
            }
        });

        views = new ArrayList<View>();
        views.add(main_bottom1);
        views.add(main_bottom2);
        vpAdapter = new PagerAdapter() {

            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return (arg0 == arg1);
            }

            @Override
            public int getCount() {
                if (views != null) {
                    return views.size();
                }
                return 0;
            }
            @Override
            public Object instantiateItem(View arg0, int arg1) {
                ((ViewPager) arg0).addView(views.get(arg1), 0);
                return views.get(arg1);
            }
            @Override
            public void destroyItem(View arg0, int arg1, Object arg2) {
                ((ViewPager) arg0).removeView(views.get(arg1));
            }
        };
        viewPager.setAdapter(vpAdapter);
        // 初始化底部小点
        initDots();
        initBottom();
    }

    private void initBottom(){
        final ArrayList<UserNode> friends = new ArrayList<UserNode>();
//        friendsAdapter = new MainFriendsAdapter(context, friends);
//        final AddressBook user = new AddressBook();
//        user.setNickname("+");
//        friends.add(user);
//        String uid = FacekallApplication.getInstance().userID();
//        mHttp.queryFriendList(uid, "1", "16", new FLHttpListener() {
//            @Override
//            public void onHttpDone(JSONObject result)  {
//                try {
//                    if (result.getString("r").equals(FLHttp.RESULTSUCCESS)) {
//                        JSONObject c = result.getJSONObject("c");
//                        JSONArray list = c.getJSONArray("friendList");
//                        for (int i = 0; i < list.length(); i++) {
//                            AddressBook addressBook = new AddressBook();
//
//                            //原生的解析很多问题啊，注意数据类型
//                            JSONObject object = (JSONObject) list.get(i);
//                            addressBook.setNickname(object.getString("nickname").substring(4));
//                            addressBook.setId(object.getString("id"));
//                            addressBook.setUid(object.getString("uid"));
//                            addressBook.setStatus(object.getInt("status")+"");
//                            friends.add(addressBook);
//                        }
//                        friendsAdapter.notifyDataSetChanged();
//
//                    } else {
//                        return;
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        mgridview1.setAdapter(friendsAdapter);
//        entitys = new ArrayList<RecentEntity>();
//        recentAdapter = new RecentGridAdapter(context, entitys);
//        mgridview2.setAdapter(recentAdapter);
//        // 绑定回调
//        viewPager.setOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout ll = (LinearLayout) findViewById(R.id.ll_main_bottom_dot);
        dots = new ImageView[views.size()];

        // 循环取得小点图片
        for (int i = 0; i < views.size(); i++) {
            dots[i] = (ImageView) ll.getChildAt(i);
            dots[i].setEnabled(true);// 都设为灰色
        }

        currentIndex = 0;
        dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void setCenterView(MainCenterView centerView){
        this.centerView = centerView;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int i) {
        // 设置底部小点选中状态
        setCurrentDot(i);
        if(i==0){
            centerView.changeArrow(true);
        }else{
            centerView.changeArrow(false);
        }
        loadContactsContent();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void setCurrentDot(int position) {
        if (position < 0 || position > views.size() - 1
                || currentIndex == position) {
            return;
        }

        dots[position].setEnabled(false);
        dots[currentIndex].setEnabled(true);
        currentIndex = position;
    }

    //邀请某人 进行视频
    private void invite(boolean isVideo) {

    }

    //ImSDK 登录成功的 话就注册  消息监听事件
    private void SetMessageListener(){
        //设置消息监听器，收到新消息时，通过此监听器回调
        TIMManager.getInstance().addMessageListener(msgListener);
    }

    //IMSDK遍历未读消息的方法
    public void loadContactsContent()
    {
        contactList.clear();
        Map<String, UserInfo> users = UserInfoManager.getInstance().getContactsList();
        if(users == null){
            return;
        }
        for(Map.Entry<String,UserInfo> entry: users.entrySet())	{
            contactList.add(entry.getValue());
        }

        Collections.sort(contactList, new Comparator<UserInfo>() {
            @Override
            public int compare(final UserInfo user1, final UserInfo user2) {
                String str1, str2;
                str1 = (user1.getNick() == null ? user1.getName() : user1.getNick());
                str2 = (user2.getNick() == null ? user2.getName() : user2.getNick());
                //  PingYinUtil.getPingYin(str1).toLowerCase().compareTo(  PingYinUtil.getPingYin(str2).toLowerCase() );
                return ChnToSpell.MakeSpellCode(str1, ChnToSpell.TRANS_MODE_PINYIN_INITIAL).toLowerCase()
                        .compareTo(ChnToSpell.MakeSpellCode(str2, ChnToSpell.TRANS_MODE_PINYIN_INITIAL).toLowerCase());
            }
        });

        UserInfo groupUsers = new UserInfo();
        groupUsers.setName(Constant.GROUP_USERNAME);
        groupUsers.setNick(Constant.GROUP_NICK);
        contactList.add(0, groupUsers);
    }

    public void loadRecentContent(){
        conversation_num = TIMManager.getInstance().getConversationCount();
        entitys.clear();
        //遍历会话列表
        if(conversation_num > Constant.RECENT_MSG_NUM){
            conversation_num = Constant.RECENT_MSG_NUM;
        }
        for(long i = 0; i < conversation_num; ++i) 	{
            final TIMConversation conversation = TIMManager.getInstance().getConversationByIndex(i);
            conversation.getMessage(1, null, new TIMValueCallBack<List<TIMMessage>>() {
                @Override
                public void onError(int code, String desc) {
                }

                @Override
                public void onSuccess(List<TIMMessage> msgs) {
                    if(msgs.size()<1){
                        return;
                    }
                    if(conversation.getType() == TIMConversationType.Group){
                        //	Log.d(TAG,"loadRecentContent:group");
                        ProcessGroupMsg(msgs.get(0));
                    }else{
                        //	Log.d(TAG,"loadRecentContent:c2c");
                        ProcessC2CMsg(msgs.get(0));
                    }
                }
            });
        }
    }

    private TIMMessageListener msgListener = new TIMMessageListener() {
        @Override
        public boolean onNewMessages(List<TIMMessage> arg0) {
            //改群名，加人等操作更新下群名称
            for(TIMMessage msg : arg0){
                for(int i=0;i<msg.getElementCount();i++){
                    if(msg.getElement(i).getType() == TIMElemType.GroupTips){
                        TIMGroupTipsElem tipsElem = (TIMGroupTipsElem)msg.getElement(i);
                        Map<String,String> groupID2Name = UserInfoManager.getInstance().getGroupID2Name();
                        if( tipsElem.getTipsType() == TIMGroupTipsType.Join || tipsElem.getTipsType() == TIMGroupTipsType.ModifyGroupName){
                            groupID2Name.put(msg.getConversation().getPeer(),tipsElem.getGroupName());
                        }else if(tipsElem.getTipsType() == TIMGroupTipsType.Quit){
                            groupID2Name.remove(msg.getConversation().getPeer());
                        }

                    }
                }

            }
            loadRecentContent();
            return true;
        }
    };

    private void ProcessC2CMsg(TIMMessage msg){
        final TIMMessage tmpMsg = msg;
        final TIMConversation conversation = msg.getConversation();
        String strUserName = conversation.getPeer();
        RecentEntity entity = new RecentEntity();
        entity.setMessage(tmpMsg);
        entity.setName(strUserName);
        entity.setIsGroupMsg(false);
        UserInfo userInfo = UserInfoManager.getInstance().getContactsList().get(strUserName);
        if(userInfo != null){
            entity.setNick(userInfo.getNick());
        }
        entity.setCount(conversation.getUnreadMessageNum());
        //activity初始化或者新消息通知都有可能调用到这里，且顺序不确定。 数据可能重复，需要去重
        for(int i=0;i<entitys.size();i++){
            if(entitys.get(i).getName().equals(strUserName)){
                if( entitys.get(i).getMessage().timestamp() < tmpMsg.timestamp()){
                    entitys.remove(i);
                    break;
                }else{
                    return;
                }
            }
        }
        entitys.add(entity);
        recentAdapter.notifyDataSetChanged();
    }

    private void ProcessGroupMsg(TIMMessage msg){
        final TIMMessage tmpMsg =msg;
        final TIMConversation conversation = msg.getConversation();
        final String strGroupID = conversation.getPeer();
        //	Log.d(TAG,"grp msg:" + strGroupID );

        for(int i=0;i<entitys.size();i++){
            if(entitys.get(i).getName().equals(strGroupID)){
                if( entitys.get(i).getMessage().timestamp() < tmpMsg.timestamp()){
                    entitys.remove(i);
                    break;
                }else{
                    return;
                }
            }
        }

        if(!UserInfoManager.getInstance().getGroupID2Name().containsKey(strGroupID)){
            //群组关系可能发生了变化，比如被加入了新群后发送的消息。更新一下
            TIMGroupManager.getInstance().getGroupList(new TIMValueCallBack<List<TIMGroupBaseInfo>>() {
                @Override
                public void onError(int code, String desc) {
                }

                @Override
                public void onSuccess(List<TIMGroupBaseInfo> arg0) {
                    // TODO Auto-generated method stub
                    //缓存群列表。生成群id和群名称的对应关系
                    Map<String,String> mGroup = new HashMap<String,String>();
                    for(TIMGroupBaseInfo baseInfo :arg0){
                        mGroup.put(baseInfo.getGroupId(),baseInfo.getGroupName());
                    }
                    UserInfoManager.getInstance().setGroupID2Name(mGroup);

                    RecentEntity entity = new RecentEntity();
                    entity.setMessage(tmpMsg);
                    entity.setName(strGroupID);
                    entity.setIsGroupMsg(true);
                    if(UserInfoManager.getInstance().getGroupID2Name().containsKey(strGroupID)){
                        entity.setNick(UserInfoManager.getInstance().getGroupID2Name().get(strGroupID));
                    }else{
                        entity.setNick("");
                    }
                    entity.setCount(conversation.getUnreadMessageNum());
                    entitys.add(entity);
                    recentAdapter.notifyDataSetChanged();
                }
            });
            return;
        }

        RecentEntity entity = new RecentEntity();
        entity.setMessage(tmpMsg);
        entity.setName(strGroupID);
        entity.setIsGroupMsg(true);
        entity.setNick(UserInfoManager.getInstance().getGroupID2Name().get(strGroupID));
        entity.setCount(conversation.getUnreadMessageNum());
        entitys.add(entity);
        recentAdapter.notifyDataSetChanged();
    }

}
