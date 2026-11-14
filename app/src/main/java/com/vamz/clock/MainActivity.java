package com.vamz.clock;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Default result = CANCELED
        setResult(RESULT_CANCELED);

        // Cek apakah dipanggil dari widget
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, 
                AppWidgetManager.INVALID_APPWIDGET_ID
            );
        }

        setContentView(R.layout.activity_main);

        EditText editColor = findViewById(R.id.editColor);
        EditText editSize = findViewById(R.id.editSize);
        Button btnSave = findViewById(R.id.btnSave);

        SharedPreferences prefs = getSharedPreferences("VamzClockPrefs", Context.MODE_PRIVATE);
        editColor.setText(prefs.getString("color", "#FFFFFF"));
        editSize.setText(String.valueOf(prefs.getInt("size", 120)));

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("color", editColor.getText().toString());
                try {
                    editor.putInt("size", Integer.parseInt(editSize.getText().toString()));
                } catch (Exception e) {
                    editor.putInt("size", 120);
                }
                editor.apply();

                // Update semua widget yang aktif
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
                ComponentName widget = new ComponentName(MainActivity.this, VamzClockProvider.class);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);
                Intent updateIntent = new Intent(MainActivity.this, VamzClockProvider.class);
                updateIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                updateIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
                sendBroadcast(updateIntent);

                // Kalau dari widget, balikin RESULT_OK
                if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                    Intent resultValue = new Intent();
                    resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
                    setResult(RESULT_OK, resultValue);
                }
                
                finish();
            }
        });
    }
}
