package com.example.quanlychitieuapp.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.quanlychitieuapp.R;
import com.example.quanlychitieuapp.sign_up;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link caiDatFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class caiDatFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public caiDatFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment caiDatFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static caiDatFragment newInstance(String param1, String param2) {
        caiDatFragment fragment = new caiDatFragment();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cai_dat, container, false);
        // Initialize the button and set the onClick listener
        CardView cardTaiKhoan = view.findViewById(R.id.card_taikhoan);
        CardView cardGiaodich = view.findViewById(R.id.card_giaodich);
        CardView cardVi = view.findViewById(R.id.card_vi);
        cardTaiKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Action when button is clicked
                Intent intent = new Intent(getActivity(), com.example.quanlychitieuapp.hosocanhan.class);
                startActivity(intent);
            }
        });
//        cardGiaodich.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1=new Intent(getActivity(),com.example.quanlychitieuapp.activity_barchart.class);
//                startActivity(intent1);
//            }
//        });
//        cardVi.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent2=new Intent(getActivity(),com.example.quanlychitieuapp.activity_wallet_barchart.class);
//                startActivity(intent2);
//            }
//        });

        return view;
    }
}