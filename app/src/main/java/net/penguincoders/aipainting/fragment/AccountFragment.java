package net.penguincoders.aipainting.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.activity.MainActivity;
import net.penguincoders.aipainting.activity.SettingActivity;
import net.penguincoders.aipainting.activity.Setting_AccountActivity;
import net.penguincoders.aipainting.activity.Setting_SetupActivity;
import net.penguincoders.aipainting.activity.Show_PictureActivity;


public class AccountFragment extends Fragment {

    private View view;
    private Context context;
    private MainActivity activity;

    private ImageView settingBackBtn;
    private ImageView settingSetupBtn1;
    private ImageView settingAvatar;
    private TextView settingNickname;


    private RelativeLayout settingAccountBtn;
    private RelativeLayout settingLoveBtn;
    private RelativeLayout settingGitBtn;
    private RelativeLayout settingSetupBtn2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_account, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();
        return view;
    }



    private void init() {

        settingBackBtn = view.findViewById(R.id.settingBackBtn);
        settingSetupBtn1 = view.findViewById(R.id.settingSetupBtn1);
        settingAvatar = view.findViewById(R.id.settingAvatar);
        settingNickname = view.findViewById(R.id.settingNickname);
        settingAccountBtn = view.findViewById(R.id.settingAccountBtn);
        settingLoveBtn = view.findViewById(R.id.settingLoveBtn);
        settingGitBtn = view.findViewById(R.id.settingGitBtn);
        settingSetupBtn2 = view.findViewById(R.id.settingSetupBtn2);

    }

    private void initListener() {
    //    settingBackBtn.setOnClickListener(new View.OnClickListener() {
    //        @Override
    //        public void onClick(View v) {
     //           finish();
    //        }
    //    });
        settingSetupBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Setting_SetupActivity.class);
                context.startActivity(intent);
            }
        });
        settingAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Setting_AccountActivity.class);
                startActivity(intent);
            }
        });

        settingAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Setting_AccountActivity.class);
                startActivity(intent);
            }
        });
        settingLoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        settingGitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        settingSetupBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Setting_SetupActivity.class);
                startActivity(intent);
            }
        });
    }


}