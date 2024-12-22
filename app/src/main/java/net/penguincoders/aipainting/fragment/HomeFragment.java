package net.penguincoders.aipainting.fragment;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.activity.MainActivity;
import net.penguincoders.aipainting.activity.SettingActivity;

import java.util.ArrayList;



public class HomeFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private RelativeLayout paintSettingGo;
    private ImageView paintAvatar;

    private ViewPager paintViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();
        //绑定TabLayout和ViewPager
        initNav();

        return view;
    }

    private void init() {
        paintSettingGo = view.findViewById(R.id.paintSettingGo);
        paintAvatar = view.findViewById(R.id.paintAvatar);
        paintViewPager = view.findViewById(R.id.paintViewPager);
        fragments.add(new OptionFragment());
    }

    private void initListener() {
        paintSettingGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, SettingActivity.class);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.left_in, R.anim.right_out);
            }
        });

    }

    private void initNav() {
        paintViewPager.setAdapter(new MyPagerAdapter(activity.getSupportFragmentManager()));


        paintViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }


            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        paintViewPager.setCurrentItem(0);
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}
