package com.android.labnetwork.lab1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.labnetwork.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Lab1Activity extends AppCompatActivity implements Listener{

    private ImageView imgLoad1, imgLoad2, imgLoad3;
    private Button btnLoad1, btnLoad2, btnLoad3, btnRun;
    private TextView tvMsg1, tvMsg2, tvMsg3, tvMsg4;
    private EditText edText;

    public String Image_Url = "https://i-vnexpress.vnecdn.net/2019/07/30/anh-thien-nhien-dep-thang-7-1564483719_680x0.jpg";
    public String Image_Url1 = "http://truongvutansang.xtgem.com/anh/gif/Rain.gif";

    Bitmap bitmap = null;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.android.labnetwork.R.layout.activity_lab1);

        bai1();
        bai2();
        bai3();
        bai4();
    }

    // bài 1
    Bitmap loadImageFromNetWork (String link){
        URL url;
        Bitmap bitmap = null;
        try {
            url = new URL(link);
            bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }
    void bai1(){
        imgLoad1 = findViewById(R.id.imgLoad1);
        btnLoad1 = findViewById(R.id.btnLoad1);
        tvMsg1 = findViewById(R.id.tvMsg1);

        btnLoad1.setOnClickListener(view -> {
            final Thread myThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap = loadImageFromNetWork(Image_Url);
                    imgLoad1.post(new Runnable() {
                        @Override
                        public void run() {
                            tvMsg1.setText("Tải ảnh thành công");
                            imgLoad1.setImageBitmap(bitmap);
                        }
                    });
                }
            });
            myThread.start();
        });
    }

    // bài 2
    private Handler msgHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            String messg = bundle.getString("msg");
            tvMsg2.setText(messg);
            imgLoad2.setImageBitmap(bitmap);
            progressDialog.dismiss();
        }
    };
    Bitmap downloadBitmap (String link){
        try {
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream inputStream = connection.getInputStream();
            Bitmap bitmap1 = BitmapFactory.decodeStream(inputStream);
            return bitmap1;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    };
    void bai2(){
        imgLoad2 = findViewById(R.id.imgLoad2);
        btnLoad2 = findViewById(R.id.btnLoad2);
        tvMsg2 = findViewById(R.id.tvMsg2);

        btnLoad2.setOnClickListener(view -> {
            progressDialog = ProgressDialog.show(this, "Thông báo", "Đang tải...");

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    bitmap = downloadBitmap(Image_Url1);
                    Message message = msgHandler.obtainMessage();
                    Bundle bundle = new Bundle();
                    String threMsg = "Ảnh đã được tải";
                    bundle.putString("msg", threMsg);
                    message.setData(bundle);
                    msgHandler.sendMessage(message);
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        });
    }

    // bai3
    void bai3(){
        tvMsg3 = findViewById(R.id.tvMsg3);
        btnLoad3 = findViewById(R.id.btnLoad3);
        imgLoad3 = findViewById(R.id.imgLoad3);

        btnLoad3.setOnClickListener(view -> {
            switch (view.getId()){
                case R.id.btnLoad3:
                    new LoadImageTask(this, this).execute(Image_Url);
                    break;
            }
        });
    }
    //bai3
    @Override
    public void onImageLoaded(Bitmap bitmap) {
        imgLoad3.setImageBitmap(bitmap);
        tvMsg3.setText("Tải ảnh thành công");
    }
    //bai3
    @Override
    public void onError() {
        tvMsg3.setText("Tải ảnh lỗi");
    }

    //bai4
    void bai4(){
        edText = findViewById(R.id.edText);
        btnRun = findViewById(R.id.btnLoad4);
        tvMsg4 = findViewById(R.id.tvMsg4);

        btnRun.setOnClickListener(view -> {
            switch (view.getId()){
                case R.id.btnLoad4:
                    AysncTaskRun aysncTaskRun = new AysncTaskRun(this, tvMsg4, edText);
                    String sleepTime = edText.getText().toString();
                    aysncTaskRun.execute(sleepTime);
                    break;
            }
        });
    }
}
//bai3
interface Listener {
    void onImageLoaded(Bitmap bitmap);
    void onError();
}
//bai3
class LoadImageTask extends AsyncTask<String, Void, Bitmap> {

    private Listener mListener;
    private ProgressDialog progressDialog;

    public LoadImageTask (Listener listener, Context context){
        mListener = listener;
        progressDialog = new ProgressDialog(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Đang tải ảnh...");
        progressDialog.show();
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        try{
            return BitmapFactory.decodeStream((InputStream) new URL(strings[0]).getContent());
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(progressDialog.isShowing()){
            progressDialog.dismiss();
        }
        if (bitmap != null){
            mListener.onImageLoaded(bitmap);
        }else {
            mListener.onError();
        }
    }
}
// bai4
class AysncTaskRun extends AsyncTask<String, String, String>{
    private String slep;
    ProgressDialog dialog;
    TextView tvSlept;
    EditText time;
    Context context;

    public AysncTaskRun(Context context, TextView tvSlept, EditText time){
        this.tvSlept = tvSlept;
        this.context = context;
        this.time = time;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = ProgressDialog.show(context, "Thông báo","Đợi "+
                time.getText().toString() + " giây");

    }

    @Override
    protected String doInBackground(String... strings) {
        publishProgress("Đợi...");
        try {
            int time = Integer.parseInt(strings[0]) * 1000;
            Thread.sleep(time);
            slep = "Đợi trong " + strings[0] + " giây";
        }catch (Exception e){
            e.printStackTrace();
            slep = e.getMessage();
        }

        return slep;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (dialog.isShowing()){
            dialog.dismiss();
        }
        tvSlept.setText(slep);
    }
}