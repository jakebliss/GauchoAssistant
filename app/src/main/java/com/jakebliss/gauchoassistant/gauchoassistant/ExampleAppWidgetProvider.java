package com.jakebliss.gauchoassistant.gauchoassistant;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

public class ExampleAppWidgetProvider extends AppWidgetProvider {
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Log.d("Log", "linkes");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
    /*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            remoteViews.setImageViewResource(R.id.image4, R.drawable.ic_settings_black_24dp);

        } else {
            Drawable d = ContextCompat.getDrawable(context, R.drawable.ic_settings_black_24dp);
            Bitmap b = Bitmap.createBitmap(d.getIntrinsicWidth(),
                    d.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            d.setBounds(0, 0, c.getWidth(), c.getHeight());
            d.draw(c);
            remoteViews.setImageViewBitmap(R.id.image4, b);
        }
        */
        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];

            // Create an Intent to launch ExampleActivity
            Intent intent = new Intent(context, WidgetActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            views.setOnClickPendingIntent(R.id.image4, pendingIntent);

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged (Context context,
                                           AppWidgetManager appWidgetManager,
                                           int appWidgetId,
                                           Bundle newOptions){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Log.d("Log", "linkes");
        int maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
        Log.d("Log", Integer.toString(maxHeight));
        if(maxHeight > 90 && maxHeight < 350){
            views.setViewVisibility(R.id.class2, View.VISIBLE);
            views.setViewVisibility(R.id.class3, View.VISIBLE);
            views.setViewVisibility(R.id.class4, View.VISIBLE);
            views.setViewVisibility(R.id.divider, View.VISIBLE);
            views.setViewVisibility(R.id.divider1, View.GONE);
            views.setViewVisibility(R.id.class5, View.GONE);
            views.setViewVisibility(R.id.class6, View.GONE);
            views.setViewVisibility(R.id.class7, View.GONE);
            views.setViewVisibility(R.id.class8, View.GONE);
            views.setViewVisibility(R.id.class9, View.GONE);
            views.setViewVisibility(R.id.class10, View.GONE);
        }
        else if(maxHeight >350 && maxHeight< 450){
            views.setViewVisibility(R.id.divider1, View.VISIBLE);
            views.setViewVisibility(R.id.class5, View.VISIBLE);
            views.setViewVisibility(R.id.class6, View.VISIBLE);
            views.setViewVisibility(R.id.class7, View.VISIBLE);
            views.setViewVisibility(R.id.class8, View.VISIBLE);
            views.setViewVisibility(R.id.class9, View.VISIBLE);
            views.setViewVisibility(R.id.class10, View.VISIBLE);
            views.setViewVisibility(R.id.pass1, View.GONE);
            views.setViewVisibility(R.id.pass2, View.GONE);
            views.setViewVisibility(R.id.pass3, View.GONE);
        }
        else if(maxHeight > 450){
            views.setViewVisibility(R.id.pass1, View.VISIBLE);
            views.setViewVisibility(R.id.pass2, View.VISIBLE);
            views.setViewVisibility(R.id.pass3, View.VISIBLE);
            views.setViewVisibility(R.id.divider2, View.VISIBLE);
        }
        else{
            views.setViewVisibility(R.id.class2, View.GONE);
            views.setViewVisibility(R.id.class3, View.GONE);
            views.setViewVisibility(R.id.class4, View.GONE);
            views.setViewVisibility(R.id.divider, View.GONE);
            views.setViewVisibility(R.id.divider1, View.GONE);
            views.setViewVisibility(R.id.class5, View.GONE);
            views.setViewVisibility(R.id.class6, View.GONE);
            views.setViewVisibility(R.id.class7, View.GONE);
            views.setViewVisibility(R.id.class8, View.GONE);
            views.setViewVisibility(R.id.class9, View.GONE);
            views.setViewVisibility(R.id.class10, View.GONE);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
}
