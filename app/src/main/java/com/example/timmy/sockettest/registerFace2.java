package com.example.timmy.sockettest;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.timmy.Uitils.FaceUtil;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Request;

import static android.content.ContentValues.TAG;

public class registerFace2 extends Activity implements View.OnClickListener{

    private Button take_photo;
    private Button getImage;
    private Button submit;
    private TextView tip;
    private ImageView imageView;
    private Toast mToast;
    private final int REQUEST_PICTURE_CHOOSE = 1;//照片选择请求码
    private Bitmap mImage = null;//处理后的位图,也是处理图片的开始文件
    private final int REQUEST_CAMERA_IMAGE = 2;//拍照请求码
    private byte[] mImageData = null;
    private File mPictureFile;
    private  TextView textView;
    private String mBaseUrl="http://192.168.1.109:8080/AMS/fileupload";
    private String username="";
    private String ID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_face2);
        init();

    }

    private void init() {
        take_photo= (Button) findViewById(R.id.take_photo2);
        getImage= (Button) findViewById(R.id.btn_getImage2);
        submit= (Button) findViewById(R.id.btn_register2);
        tip= (TextView) findViewById(R.id.tip2);
        imageView= (ImageView) findViewById(R.id.image_pic2);
    //    textView= (TextView) findViewById(R.id.textview2);
        take_photo.setOnClickListener(this);
        getImage.setOnClickListener(this);
        submit.setOnClickListener(this);
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        Intent intent = getIntent();
        ID=intent.getStringExtra("Id");
        username=intent.getStringExtra("username");
        //showTip();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_getImage2:
                getImage();
                break;
            case R.id.take_photo2:
                take_photo();
                break;
            case R.id.btn_register2:
                break;
            default:
                break;

        }
    }
    private void take_photo() {
        // 设置相机拍照后照片保存路径
        mPictureFile = new File(Environment.getExternalStorageDirectory(),
                "picture" + System.currentTimeMillis()/1000 + ".jpg");
        // 启动拍照,并保存到临时文件
        Intent mIntent = new Intent();
        mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
        mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
        startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);
    }
    private void getImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        String fileSrc = null;
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICTURE_CHOOSE) {
            if ("file".equals(data.getData().getScheme())) {
                // 有些低版本机型返回的Uri模式为file
                fileSrc = data.getData().getPath();
            } else {
                // Uri模型为content
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), proj,
                        null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                fileSrc = cursor.getString(idx);
                cursor.close();
            }
            // 跳转到图片裁剪页面
            FaceUtil.cropPicture(this, Uri.fromFile(new File(fileSrc)));
        } else if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (null == mPictureFile) {
                showTip("拍照失败，请重试");
                return;
            }

            fileSrc = mPictureFile.getAbsolutePath();
            updateGallery(fileSrc);
            // 跳转到图片裁剪页面
            FaceUtil.cropPicture(this,Uri.fromFile(new File(fileSrc)));
        } else if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {
            // 获取返回数据
            Bitmap bmp = data.getParcelableExtra("data");
            // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
            if(null != bmp){
                FaceUtil.saveBitmapToFile(registerFace2.this, bmp);
            }
            // 获取图片保存路径
            fileSrc = FaceUtil.getImagePath(registerFace2.this);
            //       showTip(fileSrc);
            // 获取图片的宽和高
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            mImage = BitmapFactory.decodeFile(fileSrc, options);

            // 压缩图片
//            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
//                    (double) options.outWidth / 1024f,
//                    (double) options.outHeight / 1024f)));
//            options.inJustDecodeBounds = false;
            //       mImage = BitmapFactory.decodeFile(fileSrc, options);


            // 若mImageBitmap为空则图片信息不能正常获取
            if(null == mImage) {
                showTip("图片信息无法正常获取！");
                return;
            }

            // 部分手机会对图片做旋转，这里检测旋转角度
            //  int degree = FaceUtil.readPictureDegree(fileSrc);
            //    if (degree != 0) {
            // 把图片旋转为正的方向
            //       mImage = FaceUtil.rotateImage(degree, mImage);
            //    }

            //  ByteArrayOutputStream baos = new ByteArrayOutputStream();

            //可根据流量及网络状况对图片进行压缩
            // mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            //   mImageData = baos.toByteArray();
            uploadFile(fileSrc,ID,username);
            imageView.setImageBitmap(mImage);
        }






    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    private void updateGallery(String filename) {
        MediaScannerConnection.scanFile(this, new String[] {filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }

    public void uploadFile(String uri,String ID,String username)
    {

        File file = new File(uri);
        if (!file.exists())
        {
            Toast.makeText(registerFace2.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("ID", ID);
        params.put("username",username);

        Map<String, String> headers = new HashMap<>();
        headers.put("APP-Key", "APP-Secret222");
        headers.put("APP-Secret", "APP-Secret111");


        String url = mBaseUrl;

        OkHttpUtils.post()//
                .addFile("mFile", ID+"_02"+".png", file)//
                .url(url)//
                .params(params)//
                .headers(headers)//
                .build()//
                .execute(new registerFace2.MyStringCallback());
    }

    public class MyStringCallback extends StringCallback
    {
        @Override
        public void onBefore(Request request, int id)
        {
            setTitle("loading...");
        }

        @Override
        public void onAfter(int id)
        {
            setTitle("Sample-okHttp");
        }

        @Override
        public void onError(Call call, Exception e, int id)
        {
            e.printStackTrace();
            showTip("onError:" + e.getMessage());
        }

        @Override
        public void onResponse(String response, int id)
        {
            Log.e(TAG, "onResponse：complete");
            showTip("onResponse:" + response);

//            switch (id)
//            {
//                case 100:
//                    Toast.makeText(registerFace2.this, "http", Toast.LENGTH_SHORT).show();
//                    break;
//                case 101:
//                    Toast.makeText(registerFace2.this, "https", Toast.LENGTH_SHORT).show();
//                    break;
//            }
            Gson gson=new Gson();
            Code code=gson.fromJson(response,Code.class);
            if(code.getCode()==2)
            {
                Toast.makeText(registerFace2.this, "人脸注册成功", Toast.LENGTH_SHORT).show();
                direct();

            }else
            {
                Toast.makeText(registerFace2.this, "人脸注册失败请重新上传照片", Toast.LENGTH_SHORT).show();

            }
          //  direct();
        }

        @Override
        public void inProgress(float progress, long total, int id)
        {
            Log.e(TAG, "inProgress:" + progress);
            //  mProgressBar.setProgress((int) (100 * progress));
        }
    }
    private void direct() {
        Intent intent=new Intent(this,registerFace3.class);
        intent.putExtra("Id",ID);
        intent.putExtra("username",username);
        startActivity(intent);
        finish();
    }
}
