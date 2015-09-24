package com.falcon.facekall.login;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.falcon.HTTP.FLHttpListener;
import com.falcon.HTTP.FLHttpLogin;
import com.falcon.facekall.FacekallApplication;
import com.falcon.facekall.R;
import com.falcon.facekall.main.MainActivity;
import com.falcon.facekall.model.RegisterModel;
import com.falcon.facekall.model.RegisterModelListener;
import com.tencent.avsdk.Utils.AVUtil;
import com.tencent.avsdk.c2c.HttpProcessor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/7/9 0009.
 */
public class RegisterDoneActivity extends Activity implements RegisterModelListener {
    private EditText etNickName;
    private String mobileNum;
    private String pinCode;
    private RegisterModel mRegisterModel;
    private ChangeHead changeHead;
    private View.OnClickListener itemsOnClick;
    private Uri photoUri;
    private ImageButton icon;
    private View mBackview;

    private String mNickname;
    private String mPassword="123456";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRegisterModel=new RegisterModel(this);
        mobileNum = getIntent().getStringExtra("mobile");
        pinCode = getIntent().getStringExtra("pinCode");
        setContentView(R.layout.activity_registerdone);
        initView();
    }

    private void initView() {
        mBackview = findViewById(R.id.bg_register_done);
        icon = (ImageButton) findViewById(R.id.icPhoto);
        etNickName=(EditText) findViewById(R.id.etRegNickname);
        changeHead = new ChangeHead();
        changeHead.showOnView = mBackview;
        itemsOnClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.popHeadCamera:
                        Intent intent = new Intent(
                                "android.media.action.IMAGE_CAPTURE");
                        /***
                         * 需要说明一下，以下操作使用照相机拍照，拍照后的图片会存放在相册中的
                         * 这里使用的这种方式有一个好处就是获取的图片是拍照后的原图
                         * 如果不实用ContentValues存放照片路径的话，拍照后获取的图片为缩略图不清晰
                         */
                        ContentValues values = new ContentValues();
                        values.put(android.provider.MediaStore.Images.Media.TITLE,
                                getTempFileName());
                        photoUri = getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                                values);
                        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                                photoUri);
                        startActivityForResult(intent, Activity.DEFAULT_KEYS_DIALER);
                        changeHead.dissMissPop();
                        break;

                    case R.id.popHeadGallery:
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(i, Activity.DEFAULT_KEYS_SHORTCUT);
                        changeHead.dissMissPop();
                        break;
                }
            }
        };
    }

    //按钮点击事件
    public void onClick(View v) {
        // TODO Auto-generated method stub
        final Intent intent=new Intent();
        switch (v.getId()) {
            case R.id.tvRegisterDoneBack:
                finish();
                break;
            case R.id.btnDone:
                //现在注册的逻辑，没有设置密码的地方，可以把密码默认为123456.
                //现在需要确定的是是用手机号做用户名还是昵称呢？手机号的话就从其他Activity传过来，我暂时用昵称。
                mNickname = etNickName.getText().toString();
                if(!TextUtils.isEmpty(mNickname)){
                    mRegisterModel.register(mobileNum, mPassword,mNickname,pinCode);
                }else{
                    Toast.makeText(RegisterDoneActivity.this,"昵称不能为空",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.icPhoto:
                changeHead.showPopMenu(this, itemsOnClick);
                break;
            default:
                break;
        }
    }





    public void onRegisterErr(String err){
        Log.d("onRegisterErr", err);
        Toast.makeText(this,err, Toast.LENGTH_LONG).show();
    }


    public void onRegisterDone(JSONObject obj) throws JSONException {
        FacekallApplication.getInstance().setUserInfo(obj);

        final Intent intent=new Intent();
        intent.setClass(RegisterDoneActivity.this, MainActivity.class);
        RegisterDoneActivity.this.startActivity(intent);
        RegisterDoneActivity.this.finish();
    }

    private String attachPath;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        File file = null;
        switch (requestCode) {
            case Activity.DEFAULT_KEYS_DIALER:
                if (resultCode == Activity.RESULT_OK) {
                    String[] pojo = { MediaStore.Images.Media.DATA };
                    Cursor cursor = RegisterDoneActivity.this.managedQuery(photoUri, pojo,
                            null, null, null);

                    if (cursor != null) {
                        int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                        cursor.moveToFirst();
                        attachPath = cursor.getString(columnIndex);
                    }
                    file = new File(attachPath);
                    // photo = ImageUtils.safeDecodeBimtapFile(attachPath, null);
                    startPhotoZoom(Uri.fromFile(file), 1, 1, 150, 150);
                }

                break;
            case Activity.DEFAULT_KEYS_SHORTCUT:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = data.getData();
                    if (uri != null) {
                        attachPath = getContentFilePath(uri);
                        file = new File(attachPath);
                        startPhotoZoom(Uri.fromFile(file), 1, 1, 150, 150);
                        // photo = ImageUtils.safeDecodeBimtapFile(attachPath,
                        // null);
                    }

                }
                break;
            case Activity.DEFAULT_KEYS_SEARCH_LOCAL:
                if (resultCode == Activity.RESULT_OK) {
                    Bundle mBundle = data.getExtras();
                    Bitmap mBitmap = mBundle.getParcelable("data");

                    icon.setImageBitmap(mBitmap);
                }
                break;
        }
    }

    private String getContentFilePath(Uri uri) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        @SuppressWarnings("deprecation")
        Cursor cur = RegisterDoneActivity.this.managedQuery(uri, projection, null, null,
                null);

        if (cur != null) {
            int column_index_data = cur
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cur.moveToFirst();
            return cur.getString(column_index_data);
        } else {
            return uri.getPath();
        }
    }

    public void startPhotoZoom(Uri uri, int aspectX, int aspectY, int outputX,
                               int outputY) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", aspectX);
        intent.putExtra("aspectY", aspectY);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("scale", true);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, Activity.DEFAULT_KEYS_SEARCH_LOCAL);
    }


    //换头像生成名字
    public static String getTempFileName() {
        return "change_head" + System.currentTimeMillis();
    }
}
