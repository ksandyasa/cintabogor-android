package com.aprido.cintabogor.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aprido.cintabogor.R;
import com.aprido.cintabogor.widget.CustomButton;
import com.aprido.cintabogor.widget.CustomTextView;

/**
 * Created by apridosandyasa on 6/29/17.
 */

public class ContactUsFragment extends DialogFragment {

    private Context context;
    private View view;
    private CustomTextView tv_phone1_contact_us;
    private CustomTextView tv_phone2_contact_us;
    private CustomButton btn_close_contact_us;

    public ContactUsFragment() {

    }

    @SuppressLint("ValidFragment")
    public ContactUsFragment(Context context) {
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

        this.view = inflater.inflate(R.layout.content_contact_us, container, false);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.tv_phone1_contact_us = (CustomTextView) view.findViewById(R.id.tv_phone1_contact_us);
        this.tv_phone2_contact_us = (CustomTextView) view.findViewById(R.id.tv_phone2_contact_us);
        this.btn_close_contact_us = (CustomButton) view.findViewById(R.id.btn_close_contact_us);

        this.tv_phone1_contact_us.setTextColor(Color.BLUE);
        this.tv_phone2_contact_us.setTextColor(Color.BLUE);
        this.tv_phone1_contact_us.setPaintFlags(this.tv_phone1_contact_us.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        this.tv_phone2_contact_us.setPaintFlags(this.tv_phone2_contact_us.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        this.tv_phone1_contact_us.setText("0251-7545654");
        this.tv_phone2_contact_us.setText("0812-9778-4652");

        this.tv_phone1_contact_us.setOnClickListener(new ActionCallPhone("0251-7545654"));
        this.tv_phone2_contact_us.setOnClickListener(new ActionCallPhone("0812-9778-4652"));
        this.btn_close_contact_us.setOnClickListener(new DismissContactUs());
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT;
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private class ActionCallPhone implements View.OnClickListener {

        private String mPhone;

        public ActionCallPhone(String phone) {
            this.mPhone = phone;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + mPhone));
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ((AppCompatActivity)context).requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 0);
            }
            context.startActivity(intent);
        }
    }

    private class DismissContactUs implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            dismiss();
        }
    }

}
