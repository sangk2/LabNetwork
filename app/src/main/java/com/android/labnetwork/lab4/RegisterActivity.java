package com.android.labnetwork.lab4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.labnetwork.R;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RegisterActivity extends AppCompatActivity {

    EditText edName, edEmail, edPass;
    Button btnDangKi, btnDangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        edName = findViewById(R.id.edName);
        edEmail = findViewById(R.id.edEmail);
        edPass = findViewById(R.id.edPass);
        btnDangKi = findViewById(R.id.btnDangKi);
        btnDangNhap = findViewById(R.id.btnDangNhap);

        btnDangNhap.setOnClickListener(view -> {
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        });
        btnDangKi.setOnClickListener(view -> {
            String name = edName.getText().toString();
            String email = edEmail.getText().toString();
            String pass = edPass.getText().toString();
            if(!name.isEmpty() && !email.isEmpty() && !pass.isEmpty()){
                registerProcess(name, email, pass);
            }
            else{
                Toast.makeText(getApplicationContext(), "Không được để trống", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void registerProcess(String name, String email, String pass){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.name = name;
        user.email = email;
        user.password = pass;

        ServerRequest serverRequest = new ServerRequest();
        serverRequest.setOperation(Constants.REGISTER_OPERATION);
        serverRequest.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(serverRequest);
        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                ServerResponse resp = response.body();
                if (resp.getResult().equals(Constants.SUCCESS)){
                    Toast.makeText(getApplicationContext(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    finish();
                }else {
                    Toast.makeText(getApplicationContext(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Log.e(Constants.TAG, "Failed");
            }
        });
    }
}