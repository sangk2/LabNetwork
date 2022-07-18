package com.android.labnetwork.lab3;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.labnetwork.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class B3_RetrofitFragment extends Fragment {

    RecyclerView rvUser;
//    TextView tvRs;
    FloatingActionButton fbtnLoad;

    ArrayList<User> listUser;


    public static B3_RetrofitFragment newInstance() {
        return new B3_RetrofitFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_b3__retrofit, container, false);
//        tvRs = v.findViewById(R.id.tvRs);
        rvUser = v.findViewById(R.id.rvUser);
        fbtnLoad = v.findViewById(R.id.fbtnLoad);

        listUser = new ArrayList<>();

        fbtnLoad.setOnClickListener(view -> {
            getJsonData();
        });
        return v;
    }

    // Lấy dữ liệu từ Server = Retrofit
//    http://192.168.1.5/android-network/lab3/user_data.json
    public void getJsonData(){
        // 1.Tạo đối tượng Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.16.16.202/android-network/lab3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        // 2.Lấy request
        getDataUser getDataUser = retrofit.create(com.android.labnetwork.lab3.getDataUser.class);

        Call<ServerResponseUsers> call = getDataUser.GetJson();
        // 3.Thực thi request
        call.enqueue(new Callback<ServerResponseUsers>() {
            // nếu thành công
            @Override
            public void onResponse(Call<ServerResponseUsers> call, Response<ServerResponseUsers> response) {
                ServerResponseUsers users = response.body();
                //chuyển kết quả sang List
                listUser = new ArrayList<>(Arrays.asList(users.getUser()));
                // chuyển kết quả sang chuỗi
//                String strKQ = "";
//                for(User u : listUser){
//                    strKQ += "Tên: "+u.name+"\n";
//                    strKQ += "Email: "+u.email+"\n";
//                    strKQ += "Số điện thoại: "+u.phone+"\n\n";
//                }
//                // hiển thị kết quả lên client
//                tvRs.setText(strKQ);
                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                rvUser.setLayoutManager(mLayoutManager);
                UserAdapter adapter = new UserAdapter(listUser,getContext());
                rvUser.setAdapter(adapter);
            }
            // nếu thất bại
            @Override
            public void onFailure(Call<ServerResponseUsers> call, Throwable t) {
//                tvRs.setText(t.getMessage());
                Log.e("//===ErroB3: ",t.getMessage());
            }
        });
    }
}

class User {
    String name;
    String email;
    String phone;

    public User() {
    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }
}
// class trung gian lấy dữ liệu từ class User truyền vào interface
class ServerResponseUsers {
    // GET
    private User[] user; // user không đc đổi tên (để tên giống dữ liệu trên API)

    public User[] getUser() {
        return user;
    }
}
interface getDataUser{
    @GET("user_data.json")
    Call<ServerResponseUsers> GetJson();
}
class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserVH> {

    List<User> list;
    Context context;

    public UserAdapter(List<User> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.UserVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserAdapter.UserVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserVH holder, int position) {
        User user = list.get(position);

        if (user == null){
            return;
        }
        holder.tv_name3.setText(user.name);
        holder.tv_email3.setText(user.email);
        holder.tv_phone3.setText(user.phone);


    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class UserVH extends RecyclerView.ViewHolder{
        TextView tv_name3, tv_email3, tv_phone3;
        CardView detail3;

        public UserVH(View itemView) {
            super(itemView);

            detail3 = itemView.findViewById(R.id.detail3);
            tv_name3= itemView.findViewById(R.id.tv_ten3);
            tv_email3 = itemView.findViewById(R.id.tv_email3);
            tv_phone3 = itemView.findViewById(R.id.tv_phone3);
        }
    }
}