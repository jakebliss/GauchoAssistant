package com.jakebliss.gauchoassistant.gauchoassistant;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ExampleAppWidgetProvider extends AppWidgetProvider {

    private String quarter_calendar = new String();
    private String menu = new String();
    private String quarter_calendar_data = new String("quarter_calendar");
    private String menu_data = new String("menu");
    private List<String> menu_items;
    private List<String> quarter_info;

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
        Log.d("Log", "linkes");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = mdformat.format(calendar.getTime());
        strDate = "https://api.ucsb.edu/dining/menu/v1/"+strDate+"T00%3A00%3A00-08%3A00/carrillo/dinner";
        Log.d("pap", strDate);
        getResponseData("https://api.ucsb.edu/academics/quartercalendar/v1/quarters/current", quarter_calendar_data, remoteViews, context, appWidgetManager, appWidgetIds[0]);
        getResponseData(strDate, menu_data, remoteViews, context, appWidgetManager, appWidgetIds[0]);

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
        getResponseData("https://api.ucsb.edu/dining/menu/v1/2018-12-09T00%3A00%3A00-08%3A00/de-la-guerra/brunch", menu_data, views, context, appWidgetManager, appWidgetId);
        getResponseData("https://api.ucsb.edu/academics/quartercalendar/v1/quarters/current", quarter_calendar_data, views, context, appWidgetManager, appWidgetId);
        Log.d("Log", "linkes");
        int maxHeight = newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
        Log.d("Log", Integer.toString(maxHeight));
        if(maxHeight > 90 && maxHeight < 350){
            views.setViewVisibility(R.id.class2, View.VISIBLE);
            views.setViewVisibility(R.id.class3, View.VISIBLE);
            views.setViewVisibility(R.id.class4, View.VISIBLE);
            views.setViewVisibility(R.id.divider, View.VISIBLE);

            views.setViewVisibility(R.id.class5, View.GONE);
            views.setViewVisibility(R.id.class6, View.GONE);
        }
        else if(maxHeight >350 && maxHeight< 450){
            views.setViewVisibility(R.id.class5, View.VISIBLE);
            views.setViewVisibility(R.id.class6, View.VISIBLE);
            views.setViewVisibility(R.id.pass1, View.GONE);
            views.setViewVisibility(R.id.pass2, View.GONE);
            views.setViewVisibility(R.id.pass3, View.GONE);
        }
        else if(maxHeight > 450){
            views.setViewVisibility(R.id.pass1, View.VISIBLE);
            views.setViewVisibility(R.id.pass2, View.VISIBLE);
            views.setViewVisibility(R.id.pass3, View.VISIBLE);
        }
        else{
            views.setViewVisibility(R.id.class2, View.GONE);
            views.setViewVisibility(R.id.class3, View.GONE);
            views.setViewVisibility(R.id.class4, View.GONE);
            views.setViewVisibility(R.id.divider, View.GONE);
            views.setViewVisibility(R.id.class5, View.GONE);
            views.setViewVisibility(R.id.class6, View.GONE);
        }
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private String getResponseData(final String endpoint, final String data_type, final RemoteViews rv, final Context context, final AppWidgetManager appWidgetManager, final int appWidgetId){
        String data = new String("null");
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                URL githubEndpoint = null;
                try {
                    githubEndpoint = new URL(endpoint);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    HttpsURLConnection myConnection =
                            (HttpsURLConnection) githubEndpoint.openConnection();
                    myConnection.setRequestProperty("User-Agent", "gaucho-assistant");
                    myConnection.setRequestProperty("accept",
                            "application/json");
                    myConnection.setRequestProperty("ucsb-api-version",
                            "1.0");
                    myConnection.setRequestProperty("ucsb-api-key",
                            "8IQgXsvmA6OuIjuGkVT3t9oYf2MDZbbS");
                    if (myConnection.getResponseCode() == 200) {
                        Log.d("api", "gotit");
                        InputStream responseBody = myConnection.getInputStream();
                        InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                        JsonReader jsonReader = new JsonReader(responseBodyReader);
                        List<Object> obj = new ArrayList<Object>();

                        if(data_type.equals("menu")){
                            jsonReader.beginArray();
                            while(jsonReader.hasNext()){
                                jsonReader.beginObject(); // Start processing the JSON object
                                while (jsonReader.hasNext()) {
                                    String key = jsonReader.nextName(); // Fetch the next key
                                    if (jsonReader.peek() != JsonToken.NULL) {
                                        menu_data += "," + key + ":" + jsonReader.nextString();
                                        Log.d("widget", menu_data);
                                    } else {
                                        jsonReader.nextNull(); // note on this
                                    }
                                }
                                jsonReader.endObject();
                            }
                            jsonReader.endArray();
                        }
                        else {
                            jsonReader.beginObject(); // Start processing the JSON object
                            while (jsonReader.hasNext()) { // Loop through all keys
                                String key = jsonReader.nextName(); // Fetch the next key
                                // Check if desired key
                                if (jsonReader.peek() != JsonToken.NULL) {
                                    quarter_calendar += "," + key + ":" + jsonReader.nextString();
                                    Log.d("widget", quarter_calendar);
                                } else {
                                    jsonReader.nextNull(); // note on this
                                }
                            }
                        }
                        if(data_type.equals("menu")){
                            menu_items = parseMenu(menu_data);
                            UpdateWidgetMenuViews(menu_items, rv, context, appWidgetManager, appWidgetId);
                        }
                        else{
                            quarter_info = parseQuarter(quarter_calendar);
                            UpdateWidgetPasstimeViews(quarter_info, rv, context, appWidgetManager, appWidgetId);
                            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

                        }
                        jsonReader.close();
                        myConnection.disconnect();

                    } else {
                        // Error handling code goes here
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        return data;
    }

    private List<String> parseMenu(String raw_data){
        List<String> ret = new ArrayList<>();
        List<String> items = Arrays.asList(raw_data.split("\\s*,\\s*"));
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).contains("name")){
                items.set(i, items.get(i).substring(5));
                ret.add(items.get(i));
                Log.d("food", items.get(i));
            }
        }
        return ret;
    }

    private List<String> parseQuarter(String raw_data){
        List<String> items = Arrays.asList(raw_data.split("\\s*,\\s*"));
        List<String> ret = new ArrayList<>();
        for(int i = 0; i < items.size(); i++){
            if(items.get(i).contains("pass1")){
                items.set(i, items.get(i).substring(10));
                String pass1 = new String("Pass 1: " + items.get(i).toString());
                ret.add(pass1);
                Log.d("food", items.get(i));
            }
            if(items.get(i).contains("pass2")){
                items.set(i, items.get(i).substring(10));
                String pass2 = new String("Pass 2: " + items.get(i).toString());
                ret.add(pass2);
                Log.d("food", items.get(i));
            }
            if(items.get(i).contains("pass3")){
                items.set(i, items.get(i).substring(10));
                String pass3 = new String("Pass 3: " + items.get(i).toString());
                ret.add(pass3);
                Log.d("food", items.get(i));
            }
        }
        return ret;
    }

    private void UpdateWidgetMenuViews(List<String> data, RemoteViews rv, Context context, AppWidgetManager appWidgetManager, int appWidgetid){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        String menu = data.get(0) + ", " + data.get(1) + ", " + data.get(2) + '\n' + data.get(3) + ", " + data.get(4) + ", " + data.get(5) + '\n' + data.get(6) + ", " + data.get(7) + ", " + data.get(8) + " ...";
        views.setTextViewText(R.id.class6, menu);
        appWidgetManager.updateAppWidget(appWidgetid, views);
    }
    private void UpdateWidgetPasstimeViews(List<String> data, RemoteViews rv, Context context, AppWidgetManager appWidgetManager, int appWidgetid){
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

        Log.d("poop", "data:" + data.get(0));
        views.setTextViewText(R.id.pass1, data.get(0));
        views.setTextViewText(R.id.pass2, data.get(1));
        views.setTextViewText(R.id.pass3, data.get(2));
        appWidgetManager.updateAppWidget(appWidgetid, views);

    }
}
