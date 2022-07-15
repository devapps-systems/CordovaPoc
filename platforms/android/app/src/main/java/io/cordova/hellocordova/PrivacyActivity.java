package io.cordova.hellocordova;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PrivacyActivity extends AppCompatActivity {
    private static final String PREF_SHOW_PRIVACY_PROMPT = "pref_show_privacy_prompt";
    private static final String PREF_FILENAME = "pref.xml";

    boolean showPrivacyPrompt = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        sharedPreferences = getSharedPreferences(PREF_FILENAME, Context.MODE_PRIVATE);
        showPrivacyPrompt = sharedPreferences.getBoolean(PREF_SHOW_PRIVACY_PROMPT, true);

        if (showPrivacyPrompt) {
            showDialog();
        } else {
            showCordovaApp();
        }
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View alertLayout = getLayoutInflater().inflate(R.layout.dialog_alert, null);
        builder.setView(alertLayout);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);

        CheckBox chkAgree = alertLayout.findViewById(R.id.chkAgree);
        Button agreementLinkButton = alertLayout.findViewById(R.id.btnAgreement);
        agreementLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLinkDetails();
            }
        });
        Button privacyLinkButton = alertLayout.findViewById(R.id.btnPrivacy);
        privacyLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLinkDetails();
            }
        });

        Button btnYes = alertLayout.findViewById(R.id.btnYes);
        Button btnNo = alertLayout.findViewById(R.id.btnNo);

        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                savePreference(!chkAgree.isChecked());
                showCordovaApp();
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dialog.show();

        // Set height of the dialog
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int displayHeight = displayMetrics.heightPixels;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        int dialogWindowHeight = (int) (displayHeight * 0.75f);
        layoutParams.height = dialogWindowHeight;
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void savePreference(boolean shouldShowPrompt) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(PREF_SHOW_PRIVACY_PROMPT, shouldShowPrompt);
        editor.commit();
    }

    private void showCordovaApp() {
        Intent cordovaIntent = new Intent(this, MainActivity.class);
        startActivity(cordovaIntent);
        finish();
    }

    private void showLinkDetails() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final View alertLayout = getLayoutInflater().inflate(R.layout.link_info_alert, null);
        builder.setView(alertLayout);
        AlertDialog dialog = builder.create();

        Button btnClose = alertLayout.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}
