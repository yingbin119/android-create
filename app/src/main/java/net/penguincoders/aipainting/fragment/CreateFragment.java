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

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.activity.AnalyseCvActivity;
import net.penguincoders.aipainting.activity.MainActivity;

import java.util.ArrayList;

public class CreateFragment extends Fragment {

    private View view;
    private Context context;
    private MainActivity activity;



    private SegmentTabLayout createTabLayout;

    private ViewPager CreateViewPager;
    private String[] titles = {"以字生图", "以图画图","上传简历","简历分析"};
    private ArrayList<Fragment> fragments = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_create, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();
        //绑定TabLayout和ViewPager
        initNav();

        return view;
    }



    private void init() {
        createTabLayout = view.findViewById(R.id.createTabLayout);

        CreateViewPager = view.findViewById(R.id.createViewPager);
        fragments.add(new TextFragment());
        fragments.add(new PictureFragment());
        fragments.add(new LoadCvFragment());
        fragments.add(new AnalyseCvFragment());
        createTabLayout.setTabData(titles);

    }

    private void initListener() {

    }

    private void initNav() {
        CreateViewPager.setAdapter(new MyPagerAdapter(activity.getSupportFragmentManager()));

        createTabLayout.setTabData(titles);
        createTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                CreateViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });


        CreateViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        CreateViewPager.setCurrentItem(0);
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
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }
    }
}