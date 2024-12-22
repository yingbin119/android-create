package net.penguincoders.aipainting.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.core.widget.TintableImageSourceView;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.penguincoders.aipainting.R;
import net.penguincoders.aipainting.activity.AnalyseCvActivity;
import net.penguincoders.aipainting.activity.MainActivity;
import net.penguincoders.aipainting.activity.Picture_PaintActivty;
import net.penguincoders.aipainting.activity.Show_PictureActivity;
import net.penguincoders.aipainting.activity.Text_PaintActivity;
import net.penguincoders.aipainting.reponse.Text2ImgResponse;
import net.penguincoders.aipainting.reponse.UserResultResponse;
import net.penguincoders.aipainting.util.ApiService;
import net.penguincoders.aipainting.util.RetrofitClient;
import net.penguincoders.aipainting.util.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;

import okhttp3.RequestBody;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyseCvFragment extends Fragment {

    private View view;
    private Context context;

    Button toast;
    String TextDescription;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analyse_cv, container, false);
        context = getActivity();
        init();
        initListener();
        return view;
    }



    private void init() {
        toast = view.findViewById(R.id.toast);


    }

    private void initListener() {
        toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AnalyseCvActivity.class);
                context.startActivity(intent);

            }
        });

    }
}