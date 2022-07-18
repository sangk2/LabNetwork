package com.android.labnetwork.lab3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.android.labnetwork.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Lab3Activity extends AppCompatActivity {

    ViewPager2 viewPager2;
    TabLayout tabLayout;

    BottomNavigationView navBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab3);

//        navBottom = findViewById(R.id.bottom_nav);
//
//        FragmentManager fragmentManager = this.getSupportFragmentManager();
//        B1_ContactFragment b1 = new B1_ContactFragment();
//        fragmentManager.beginTransaction()
//                .replace(R.id.flContent1,b1)
//                .commit();
//
//        navBottom.setOnItemSelectedListener(item -> {
//            FragmentManager manager1 = this.getSupportFragmentManager();
//            switch (item.getItemId()) {
//                case R.id.bai1:
//                    setTitle("Sản phẩm");
//                    B1_ContactFragment b = new B1_ContactFragment();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.flContent1,b)
//                            .commit();
//                    break;
//                case R.id.bai2:
//
//                    break;
//                case R.id.bai3:
//
//                    break;
//                case R.id.bai4:
//
//                    break;
//            }
//            return true;
//        });
//    }
//}

        viewPager2 =  findViewById(R.id.viewPager2);
        tabLayout = findViewById(R.id.tabLayout);

        Lab3ViewPager2Adapter adapter = new Lab3ViewPager2Adapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0){
                    tab.setText("Bài 1");
                }else if (position == 1){
                    tab.setText("Bài 2");
                }else if (position == 2){;
                    tab.setText("Bài 3");
                }else if (position == 3){
                    tab.setText("Bài 4");
                }
            }
        }).attach();

    }
}
class Lab3ViewPager2Adapter extends FragmentStateAdapter {

    public Lab3ViewPager2Adapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        if (position == 0){
            fragment = B1_ContactFragment.newInstance();
        }else if (position == 1){
            fragment = B2_VolleyFragment.newInstance();
        }else if (position == 2){
            fragment = B3_RetrofitFragment.newInstance();
        }else if (position == 3){
            fragment = B4_artRetrofitFragment.newInstance();
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 4;
    }
}

