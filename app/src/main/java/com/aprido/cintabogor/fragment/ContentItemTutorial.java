package com.aprido.cintabogor.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.aprido.cintabogor.MainActivity;
import com.aprido.cintabogor.R;
import com.aprido.cintabogor.TutorialActivity;
import com.aprido.cintabogor.widget.CustomButton;

/**
 * Created by apridosandyasa on 10/14/16.
 */

public class ContentItemTutorial extends Fragment {

    private Context context;
    private View view;
    private ImageView iv_item_tutorial;
    private CustomButton btn_item_tutorial;
    private Bundle data;

    public ContentItemTutorial() {

    }

    @SuppressLint("ValidFragment")
    public ContentItemTutorial(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.content_item_tutorial, container, false);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.data = getArguments();

        this.iv_item_tutorial = (ImageView) view.findViewById(R.id.iv_item_tutorial);
        this.btn_item_tutorial = (CustomButton) view.findViewById(R.id.btn_item_tutorial);

        this.btn_item_tutorial.setOnClickListener(new ActionTutorialClick());

        if (this.data.getInt("position") == 3)
            this.btn_item_tutorial.setVisibility(View.VISIBLE);
        else
            this.btn_item_tutorial.setVisibility(View.INVISIBLE);
    }

    private class ActionTutorialClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent mainIntent = new Intent(context, MainActivity.class);
            startActivity(mainIntent);
            ((AppCompatActivity) context).finish();
        }
    }
}
