package com.aprido.cintabogor;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

/**
 * Created by apridosandyasa on 3/20/17.
 */

public class BaseActivity extends AppCompatActivity {

    private Dialog dialog;

    public void showLoadingDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_screen);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();
    }

    public void dismissLoadingDialog() {
        if (null == dialog) return;
        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }

}
