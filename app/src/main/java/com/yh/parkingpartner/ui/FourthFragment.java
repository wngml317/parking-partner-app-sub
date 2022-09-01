package com.yh.parkingpartner.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.google.android.gms.common.api.Api;
import com.yh.parkingpartner.R;
import com.yh.parkingpartner.adapter.AdapterMypageList;
import com.yh.parkingpartner.api.ApiFourthFragment;
import com.yh.parkingpartner.api.ApiMypageActivity;
import com.yh.parkingpartner.api.NetworkClient;
import com.yh.parkingpartner.config.Config;
import com.yh.parkingpartner.model.Data;
import com.yh.parkingpartner.model.DataListRes;
import com.yh.parkingpartner.model.ReviewListRes;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FourthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FourthFragment extends Fragment {


    MainActivity mainActivity;

    ArrayList<Data> dataList = new ArrayList<>();

    TextView prkNm;
    TextView prkStart;
    TextView prkLct;
    TextView prkLct2;
    TextView prkTime;
    TextView prkTime2;
    TextView prkPay;
    TextView prkPay2;
    Button btnCheckPay;
    Button btnEndParking;
    int prk_id;
    int count = 0;




    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FourthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SecondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SecondFragment newInstance(String param1, String param2) {
        SecondFragment fragment = new SecondFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mainActivity = (MainActivity) getActivity();
        mainActivity.getSupportActionBar().setTitle("요금 확인");
        ReadSharedPreferences();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_fourth,container,false);


        prkNm = (TextView) rootView.findViewById(R.id.prkNm);
        prkStart = (TextView) rootView.findViewById(R.id.prkStart);
        prkLct = (TextView) rootView.findViewById(R.id.prkLct);
        prkLct2 = (TextView) rootView.findViewById(R.id.prkLct2);
        prkTime = (TextView) rootView.findViewById(R.id.prkTime);
        prkTime2 = (TextView) rootView.findViewById(R.id.prkTime2);
        prkPay = (TextView) rootView.findViewById(R.id.prkPay);
        prkPay2 = (TextView) rootView.findViewById(R.id.prkPay2);
        btnCheckPay = (Button) rootView.findViewById(R.id.btnCheckPay);
        btnEndParking = (Button) rootView.findViewById(R.id.btnEndParking);
        // Inflate the layout for this fragment


        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity(),Config.PP_BASE_URL);
        ApiFourthFragment api = retrofit.create(ApiFourthFragment.class);
        Call<DataListRes> call = api.getPay(prk_id);
        call.enqueue(new Callback<DataListRes>() {
            @Override
            public void onResponse(Call<DataListRes> call, Response<DataListRes> response) {
                Log.i("로그", "결과 : "+response.isSuccessful());
                if(response.isSuccessful()){
                    dataList = response.body().getItems();
                    prkNm.setText(dataList.get(0).getPrk_plce_nm());
                    prkStart.setText(dataList.get(0).getStart_prk_at());
                    prkLct2.setText(dataList.get(0).getPrk_area());

                }
            }

            @Override
            public void onFailure(Call<DataListRes> call, Throwable t) {
                t.printStackTrace();
                Log.i("로그", "결과 : 실패");

            }
        });

        btnCheckPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                btnCheckPay.setEnabled(false);
                btnEndParking.setEnabled(true);
                getNetworkData();
            }
        });




        return rootView;
        }


        void ReadSharedPreferences(){
        //SharedPref소erences 를 이용해서, 앱 내의 저장에 영구저장된 데이터를 읽어오는 방법
        SharedPreferences sp = getActivity().getSharedPreferences(Config.SP_NAME, getActivity().MODE_PRIVATE);
        prk_id = sp.getInt(Config.SP_KEY_PRK_ID,0);
        Log.i("로그", "prk_id : " + prk_id);

    }


    @Override
    public void onResume() {
        super.onResume();
        getNetworkData();
    }

    // 데이터를 처음 가져올때만 실행하는 함수
    // 데이터의 초기화도 필요하다.
    private void getNetworkData() {
        dataList.clear();
        count = 1;
        Retrofit retrofit = NetworkClient.getRetrofitClient(getActivity(),Config.PP_BASE_URL);
        ApiFourthFragment api = retrofit.create(ApiFourthFragment.class);
        Call<DataListRes> call = api.getPay(prk_id);
        call.enqueue(new Callback<DataListRes>() {
            @Override
            public void onResponse(Call<DataListRes> call, Response<DataListRes> response) {
                if (response.isSuccessful()) {
                    dataList = response.body().getItems();
                    prkTime2.setText(dataList.get(0).getUse_prk_at());
                    prkPay2.setText(String.valueOf(dataList.get(0).getEnd_pay()));


                }
            }

            @Override
            public void onFailure(Call<DataListRes> call, Throwable t) {

            }
        });
    }



}