package com.android.labnetwork.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.labnetwork.IPwifi;
import com.android.labnetwork.MainActivity;
import com.android.labnetwork.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Lab2Activity extends AppCompatActivity {

    private EditText edName, edScore;
    private Button btnKQ1;
    private TextView tvKQ1;
    public static final String urlServer1 = new IPwifi().ip + "/android-network/lab2/student_Get.php";

    private EditText edCD, edCR;
    private Button btnKQ2;
    private TextView tvKQ2;
    public static final String urlServer2 = new IPwifi().ip + "/android-network/lab2/cv_dt_Hcn_POST.php";

    private EditText edCanh;
    private Button btnKQ3;
    private TextView tvKQ3;
    public static final String urlServer3 = new IPwifi().ip + "/android-network/lab2/thetich_KLP_POST.php";

    private EditText eda, edb, edc;
    private Button btnKQ4;
    private TextView tvKQ4;
    public static final String urlServer4 = new IPwifi().ip + "/android-network/lab2/giaiPTbac2.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab2);

        edName = findViewById(R.id.edName);
        edScore = findViewById(R.id.edScore);
        btnKQ1 = findViewById(R.id.btnKQ1);
        tvKQ1 = findViewById(R.id.tvKQ1);

        edCD = findViewById(R.id.edCD);
        edCR = findViewById(R.id.edCR);
        btnKQ2 = findViewById(R.id.btnKQ2);
        tvKQ2 = findViewById(R.id.tvKQ2);

        edCanh = findViewById(R.id.edCanh);
        btnKQ3 = findViewById(R.id.btnKQ3);
        tvKQ3 = findViewById(R.id.tvKQ3);

        eda = findViewById(R.id.eda);
        edb = findViewById(R.id.edb);
        edc = findViewById(R.id.edc);
        btnKQ4 = findViewById(R.id.btnKQ4);
        tvKQ4 = findViewById(R.id.tvKQ4);


        bai1();
        bai2();
        bai3();
        bai4();
    }

    void bai1(){
        btnKQ1.setOnClickListener(view -> {
            String name = edName.getText().toString();
            String score = edScore.getText().toString();

            taskSV_GET taskSV_get = new taskSV_GET(tvKQ1, name, score, this);
            taskSV_get.execute();

        });
    }

    void bai2(){
        btnKQ2.setOnClickListener(view -> {
            String cd = edCD.getText().toString();
            String cr = edCR.getText().toString();

            taskHCN_Post taskHCN_post = new taskHCN_Post(this, cd, cr, tvKQ2);
            taskHCN_post.execute();
        });
    }

    void bai3(){
        btnKQ3.setOnClickListener(view -> {
            String canh = edCanh.getText().toString();

            taskKLP_Post KLP = new taskKLP_Post(this, tvKQ3);
            KLP.execute(canh);
        });
    }

    void bai4(){
        btnKQ4.setOnClickListener(view -> {
            String a = eda.getText().toString();
            String b = edb.getText().toString();
            String c = edc.getText().toString();

            taskPTbac2_Post ptbac2 = new taskPTbac2_Post(this, tvKQ4);
            ptbac2.execute(a,b,c);
        });
    }
}

// bài 1
class taskSV_GET extends AsyncTask<Void, Void, Void>{
    String duongdan = Lab2Activity.urlServer1;
    TextView tvRS;
    String strName, strScore;
    String str;
    ProgressDialog dialog;
    Context context;

    public taskSV_GET(TextView tvRS, String strName, String strScore, Context context) {
        this.tvRS = tvRS;
        this.strName = strName;
        this.strScore = strScore;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Đợi tí...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected Void doInBackground(Void... voids) {
        duongdan += "?name=" +this.strName+ "&score=" +this.strScore;
        try{
            URL url = new URL(duongdan);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            BufferedReader bfr = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuffer sb = new StringBuffer();
            while ((line = bfr.readLine()) != null){
                sb.append(line);
            }
            str = sb.toString();
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        tvRS.setText(str);
    }
}

//bai2
class taskHCN_Post extends AsyncTask<String, Void, String>{
    String duongdan = Lab2Activity.urlServer2;
    Context context;
    String strCD, strCR;
    TextView tvRS;
    String strRS;
    ProgressDialog dialog;

    public taskHCN_Post(Context context, String strCD, String strCR, TextView tvRS) {
        this.context = context;
        this.strCD = strCD;
        this.strCR = strCR;
        this.tvRS = tvRS;
    }

    @Override
    protected String doInBackground(String... strings) {
        try{
            URL url = new URL(duongdan);
            String param = "chieudai=" + URLEncoder.encode(strCD, "utf-8")
                    + "&chieurong=" + URLEncoder.encode(strCR, "utf-8");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setFixedLengthStreamingMode(param.getBytes().length);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
            printWriter.print(param);
            printWriter.close();

            String line = "";
            BufferedReader bft = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            while ((line = bft.readLine()) != null){
                sb.append(line);
            }
            strRS = sb.toString();
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Đang tính ...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (dialog.isShowing()){
            dialog.dismiss();
        }
        tvRS.setText(strRS);
    }
}

// bai 3
class taskKLP_Post extends AsyncTask<String, Void, String>{
    String duongdan = Lab2Activity.urlServer3;
    Context context;
    TextView tvRS;
    String strRS;
    ProgressDialog dialog;

    public taskKLP_Post(Context context, TextView tvRS) {
        this.context = context;
        this.tvRS = tvRS;
    }

    @Override
    protected String doInBackground(String... strings) {
        try{
            URL url = new URL(duongdan);
            String param = "canh=" + URLEncoder.encode(strings[0].toString(), "utf-8");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setFixedLengthStreamingMode(param.getBytes().length);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
            printWriter.print(param);
            printWriter.close();

            String line = "";
            BufferedReader bft = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            while ((line = bft.readLine()) != null){
                sb.append(line);
            }
            strRS = sb.toString();
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Đang tính ...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (dialog.isShowing()){
            dialog.dismiss();
        }
        tvRS.setText(strRS);
    }
}

// bai 4
class taskPTbac2_Post extends AsyncTask<String, Void, String>{
    String duongdan = Lab2Activity.urlServer4;
    Context context;

    TextView tvRS;
    String strRS;
    ProgressDialog dialog;

    public taskPTbac2_Post(Context context, TextView tvRS) {
        this.context = context;
        this.tvRS = tvRS;
    }


    @Override
    protected String doInBackground(String... strings) {
        try{
            URL url = new URL(duongdan);
            String param = "a=" + URLEncoder.encode(strings[0].toString(), "utf-8")
                    + "&b=" + URLEncoder.encode(strings[1].toString(), "utf-8")
                    + "&c=" + URLEncoder.encode(strings[2].toString(), "utf-8");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setFixedLengthStreamingMode(param.getBytes().length);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            PrintWriter printWriter = new PrintWriter(connection.getOutputStream());
            printWriter.print(param);
            printWriter.close();

            String line = "";
            BufferedReader bft = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer sb = new StringBuffer();
            while ((line = bft.readLine()) != null){
                sb.append(line);
            }
            strRS = sb.toString();
            connection.disconnect();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(context);
        dialog.setMessage("Đang tính ...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (dialog.isShowing()){
            dialog.dismiss();
        }
        tvRS.setText(strRS);
    }
}