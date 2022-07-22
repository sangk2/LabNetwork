package com.android.labnetwork.lab3;

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
import android.widget.ImageView;
import android.widget.TextView;

import com.android.labnetwork.IPwifi;
import com.android.labnetwork.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class B4_artRetrofitFragment extends Fragment {

    RecyclerView rvProduct;
    FloatingActionButton fBtnB4;
    ArrayList<Product> productList;

    public static B4_artRetrofitFragment newInstance(){
        return new B4_artRetrofitFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_b4_art_retrofit, container, false);

        rvProduct = v.findViewById(R.id.rvProduct);
        fBtnB4 = v.findViewById(R.id.fBtnB4);

        productList = new ArrayList<>();

        fBtnB4.setOnClickListener(view -> {
            getJsonData();
        });
        return v;


    }
    private void getJsonData() {
        String ip = new IPwifi().ip;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl( ip+ "/android-network/lab3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        getDataProduct getDataProduct = retrofit.create(com.android.labnetwork.lab3.getDataProduct.class);

        Call<svResProducts> call = getDataProduct.GetJson();

        call.enqueue(new Callback<svResProducts>() {
            @Override
            public void onResponse(Call<svResProducts> call, Response<svResProducts> response) {
                svResProducts products = response.body();

                productList = new ArrayList<>(Arrays.asList(products.getProducts()));

                RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
                rvProduct.setLayoutManager(mLayoutManager);
                ProductAdapter adapter = new ProductAdapter(productList,getContext());
                rvProduct.setAdapter(adapter);

            }

            @Override
            public void onFailure(Call<svResProducts> call, Throwable t) {
                Log.e("//===ErrorB4: ",t.getMessage());
            }
        });
    }
}

class Product{
    String ten;
    String gia;
    String sl;
    String art;

    public Product() {
    }

    public Product(String ten, String gia, String sl, String art) {
        this.ten = ten;
        this.gia = gia;
        this.sl = sl;
        this.art = art;
    }
}
class svResProducts{
    //GET
    private Product[] products;

    public Product[] getProducts() {
        return products;
    }
}
interface getDataProduct{
    @GET("product_data.json")
    Call<svResProducts> GetJson();
}

class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductVH>{
    List<Product> list;
    Context context;

    public ProductAdapter(List<Product> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductAdapter.ProductVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item, parent, false);
        return new ProductAdapter.ProductVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductVH holder, int position) {
        Product product = list.get(position);
        if (product == null){
            return;
        }
        holder.ten.setText(product.ten);
        holder.gia.setText(product.gia);
        holder.sl.setText(product.sl);
        Picasso.get().load(product.art).into(holder.imgArt);
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ProductVH extends RecyclerView.ViewHolder{
        TextView ten, gia, sl;
        ImageView imgArt;
        CardView detail;

        public ProductVH(View itemView) {
            super(itemView);

            detail = itemView.findViewById(R.id.detail);
            imgArt = itemView.findViewById(R.id.imgArt);
            ten = itemView.findViewById(R.id.ten);
            gia = itemView.findViewById(R.id.gia);
            sl = itemView.findViewById(R.id.sl);
        }
    }
}