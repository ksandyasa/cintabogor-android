package com.aprido.cintabogor.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aprido.cintabogor.R;
import com.aprido.cintabogor.widget.CustomButton;
import com.aprido.cintabogor.widget.CustomTextView;

/**
 * Created by apridosandyasa on 6/29/17.
 */

public class AboutUsFragment extends DialogFragment {

    private Context context;
    private View view;
    private CustomTextView tv_title_about_us;
    private CustomButton btn_close_about_us;

    public AboutUsFragment() {

    }

    @SuppressLint("ValidFragment")
    public AboutUsFragment(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.content_about_us, container, false);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.tv_title_about_us = (CustomTextView) view.findViewById(R.id.tv_title_about_us);
        this.btn_close_about_us = (CustomButton) view.findViewById(R.id.btn_close_about_us);

        this.tv_title_about_us.setText(Html.fromHtml("<p>Cinta Bogor adalah aplikasi gratis mengenai riwayat kota Bogor dari tempoe doeloe hingga modern yang  memberikan paduan, rekomendasi, dan informasi terlengkap tentang destinasi wisata, kuliner, penginapan, properti, budaya. Kesenian dan segala acara di kota Bogor.  Member aplikasi Cinta Bogor akan mendapatkan potongan harga di setiap tempat yang memasang logo Cinta Bogor</p>"));
        this.btn_close_about_us.setOnClickListener(new DismissAboutUs());
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    private class DismissAboutUs implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

}
