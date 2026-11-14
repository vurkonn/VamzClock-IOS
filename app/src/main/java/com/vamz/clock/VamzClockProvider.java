package com.vamz.clock;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.TypedValue;
import android.widget.RemoteViews;

public class VamzClockProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences prefs = context.getSharedPreferences("VamzClockPrefs", Context.MODE_PRIVATE);
        String colorHex = prefs.getString("color", "#FFFFFF");
        int textSize = prefs.getInt("size", 120);

        int color;
        try {
            color = Color.parseColor(colorHex);
        } catch (Exception e) {
            color = Color.WHITE;
        }

        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            
            views.setTextColor(R.id.clock_text, color);
            views.setTextColor(R.id.date_text, color);
            views.setTextViewTextSize(R.id.clock_text, TypedValue.COMPLEX_UNIT_SP, textSize);
            
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
