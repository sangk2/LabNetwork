package com.android.labnetwork.lab3;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class B1_ContactFragment extends Fragment {

    ProgressDialog dialog;
    RecyclerView rvContact;
    String urlLink = "http://172.16.16.202/android-network/lab3/contact.php";

    ArrayList<Contact> contactArrayList;

    public static B1_ContactFragment newInstance(){
        return new B1_ContactFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_b1__contact, container, false);

        rvContact = v.findViewById(R.id.rvContact);
        contactArrayList = new ArrayList<Contact>();

        ContactAsyncTask task = new ContactAsyncTask();
        task.execute(urlLink);

        return v;
    }

    private class ContactAsyncTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Vui lòng đợi ...");
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            String strJson = readJsonOnline(strings[0]);
            try {
                JSONObject object = new JSONObject(strJson);
                JSONArray jsonArray = object.getJSONArray("contacts");
                Log.d("//===size===",jsonArray.length()+"");

                for ( int i=0; i<jsonArray.length(); i++ ){
                    JSONObject jsonObject= jsonArray.getJSONObject(i);
                    String name = jsonObject.getString("name");
                    String email = jsonObject.getString("email");
                    String address = jsonObject.getString("address");
                    String phone = jsonObject.getString("phone");

//                    Log.i("//","=="+name);
                    Contact contact = new Contact();
                    contact.name = name;
                    contact.email = email;
                    contact.address = address;
                    contact.phone = phone;

                    contactArrayList.add(contact);


                }
            }catch (Exception ex){
                Log.d("//======Error",ex.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (dialog.isShowing()) dialog.dismiss();

            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);
            rvContact.setLayoutManager(mLayoutManager);
            ContactAdapter adapter = new ContactAdapter(contactArrayList,getContext());
            rvContact.setAdapter(adapter);
        }
    }
    public String readJsonOnline(String linkURL){
        String res = null;
        try {
            URL url = new URL(linkURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // read the response
            InputStream in = new BufferedInputStream(connection.getInputStream());

            res = StreamToString(in);
        } catch (Exception ex) {
            Log.d("//===Error: ", ex.toString());

        }
        return res;
    }
    private String StreamToString(InputStream in){
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                in.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}

class Contact{
    String name, email, address, phone;

    public Contact() {
    }

    public Contact(String name, String email, String address, String phone) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.phone = phone;
    }
}

class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactVH> {

    List<Contact> list;
    Context context;

    public ContactAdapter(List<Contact> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactAdapter.ContactVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        return new ContactAdapter.ContactVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ContactVH holder, int position) {
        Contact contact = list.get(position);

        if (contact == null){
            return;
        }
        holder.tv_name.setText(contact.name);
        holder.tv_email.setText(contact.email);
        holder.tv_phone.setText(contact.phone);
//            holder.tv_address.setText(user.address);

        holder.detail.setOnClickListener(view -> {
            Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.contact_detail);
            dialog.setCanceledOnTouchOutside(true);

            TextView name, email, phone, address;
                name = dialog.findViewById(R.id.name);
                email = dialog.findViewById(R.id.email);
                phone = dialog.findViewById(R.id.phone);
                address = dialog.findViewById(R.id.address);

            name.setText("Họ Tên: " + contact.name);
            email.setText("Email: " + contact.email);
            phone.setText("Số điện thoại: " + contact.phone);
            address.setText("Địa chỉ: " + contact.address);

            dialog.show();
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        }
        return 0;
    }

    public class ContactVH extends RecyclerView.ViewHolder{
        TextView tv_name, tv_email, tv_phone;
        CardView detail;

        public ContactVH(View itemView) {
            super(itemView);

            detail = itemView.findViewById(R.id.detail1);
            tv_name= itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_phone = itemView.findViewById(R.id.tv_phone);
//                tv_address = itemView.findViewById(R.id.address);
        }
    }
}


