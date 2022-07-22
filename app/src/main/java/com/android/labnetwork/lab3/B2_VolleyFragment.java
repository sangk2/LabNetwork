package com.android.labnetwork.lab3;

import android.app.Application;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.labnetwork.IPwifi;
import com.android.labnetwork.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;


public class B2_VolleyFragment extends Fragment {

    String ip = new IPwifi().ip;

    private String urlJsonObject = ip + "/android-network/lab3/jsonObject.json";
    private String urlJsonArray = ip + "/android-network/lab3/jsonArray.json";

    private Button btnJsonObj, btnJsonArray;
    private TextView tvResponse;

    private ProgressDialog dialog;

    public static B2_VolleyFragment newInstance() {
        return new B2_VolleyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_b2__volley, container, false);

        btnJsonObj = v.findViewById(R.id.btnJsonObject);
        btnJsonArray = v.findViewById(R.id.btnJsonArray);
        tvResponse = v.findViewById(R.id.tvResponse);

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("Vui lòng đợi ...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);

        btnJsonObj.setOnClickListener(view -> {
            getJsonObject();
        });

        btnJsonArray.setOnClickListener(view -> {
            getJsonArray();
        });


        return v;
    }

    public void getJsonArray(){
        showDialog();
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(urlJsonArray, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
//                Log.d("//==JsonArray",response.toString());

                String strJson = "";
                for (int i = 0; i< response.length(); i++){
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String address = jsonObject.getString("address");
                        JSONObject lienhe = jsonObject.getJSONObject("lienhe");
                            String email = lienhe.getString("email");
                            String phone = lienhe.getString("phone");

                        strJson += "Tên: " + name + "\n";
                        strJson += "Địa chỉ: " + address + "\n";
                        strJson += "Email: " + email + "\n";
                        strJson += "Số điện thoại: " + phone + "\n\n";

                        tvResponse.setText(strJson);
                    }catch (Exception e){
                        Log.e("Err: ", e.toString());
                    }
                }
                hideDialog();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ErrRes: ", error.getMessage());
                hideDialog();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }

    public void getJsonObject(){
        showDialog();

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,urlJsonObject,
                null, new Response.Listener<JSONObject>() {
             @Override
             public void onResponse(JSONObject response) {
                 String strJson = "";
                 try {
                     String name = response.getString("name");
                     String address = response.getString("address");
                     JSONObject lienhe = response.getJSONObject("lienhe");
                     String email = lienhe.getString("email");
                     String phone = lienhe.getString("phone");

                     strJson += "Tên: " + name + "\n";
                     strJson += "Địa chỉ: " + address + "\n";
                     strJson += "Email: " + email + "\n";
                     strJson += "Số điện thoại: " + phone + "\n\n";

                     tvResponse.setText(strJson);
                 }catch (Exception e){
                     Log.e("Err: ", e.toString());
                 }
                 hideDialog();
             }
        }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 Log.e("ErrRes: ", error.getMessage());
                 hideDialog();
             }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void showDialog(){
        if (!dialog.isShowing()) dialog.show();
    }
    private void hideDialog(){
        if (dialog.isShowing()) dialog.dismiss();
    }
}

