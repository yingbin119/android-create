package net.penguincoders.aipainting.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.activity.AnalyseCvActivity;
import net.penguincoders.aipainting.activity.LoadCvActivity;
import net.penguincoders.aipainting.activity.MainActivity;
import net.penguincoders.aipainting.activity.Picture_PaintActivty;
import net.penguincoders.aipainting.activity.Text_PaintActivity;

import java.util.ArrayList;



public class OptionFragment extends Fragment {
    private View view;
    private Context context;
    private MainActivity activity;

    private LinearLayout SelectBtn1;
    private LinearLayout SelectBtn2;
    private LinearLayout SelectBtn3;
    private LinearLayout SelectBtn4;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_homeoption, container, false);
        context = getActivity();
        activity = (MainActivity) getActivity();

        init();
        initListener();
        return view;
    }



    private void init() {
        SelectBtn1 = view.findViewById(R.id.SelectBtn1);
        SelectBtn2 = view.findViewById(R.id.SelectBtn2);
        SelectBtn3 = view.findViewById(R.id.SelectBtn3);
        SelectBtn4 = view.findViewById(R.id.SelectBtn4);

    }

    private void initListener() {
        SelectBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Text_PaintActivity.class);
                context.startActivity(intent);
            }
        });
        SelectBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Picture_PaintActivty.class);
                context.startActivity(intent);
            }
        });
        SelectBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoadCvActivity.class);
                context.startActivity(intent);
            }
        });
        SelectBtn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnalyseCvActivity.class);
                context.startActivity(intent);
            }
        });

    }


}
