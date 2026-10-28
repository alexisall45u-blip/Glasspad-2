package com.glasspad.notes;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.View;
import android.widget.RemoteViews;
import org.json.JSONArray;
import org.json.JSONObject;

public class NotesWidget extends AppWidgetProvider {
    static final int[] BOX = {R.id.b1, R.id.b2, R.id.b3, R.id.b4};
    static final int[] TT = {R.id.t1, R.id.t2, R.id.t3, R.id.t4};
    static final int[] SS = {R.id.s1, R.id.s2, R.id.s3, R.id.s4};

    @Override
    public void onUpdate(Context c, AppWidgetManager m, int[] ids) {
        for (int id : ids) m.updateAppWidget(id, build(c));
    }

    static PendingIntent open(Context c, String uri, int code) {
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        i.setClass(c, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(c, code, i,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
    }

    static RemoteViews build(Context c) {
        RemoteViews v = new RemoteViews(c.getPackageName(), R.layout.widget_notes);
        JSONArray a = new JSONArray();
        try {
            SharedPreferences p = c.getSharedPreferences("CapacitorStorage", Context.MODE_PRIVATE);
            String raw = p.getString("widget", null);
            if (raw != null) a = new JSONArray(raw);
        } catch (Exception e) {}
        v.setViewVisibility(R.id.empty, a.length() == 0 ? View.VISIBLE : View.GONE);
        for (int i = 0; i < 4; i++) {
            if (i < a.length()) {
                try {
                    JSONObject o = a.getJSONObject(i);
                    String b = o.optString("b");
                    v.setViewVisibility(BOX[i], View.VISIBLE);
                    v.setTextViewText(TT[i], o.optString("t"));
                    v.setTextViewText(SS[i], b);
                    v.setViewVisibility(SS[i], b.isEmpty() ? View.GONE : View.VISIBLE);
                    v.setOnClickPendingIntent(BOX[i], open(c, "glasspad://note/" + o.optString("i"), 10 + i));
                    continue;
                } catch (Exception e) {}
            }
            v.setViewVisibility(BOX[i], View.GONE);
        }
        v.setOnClickPendingIntent(R.id.add, open(c, "glasspad://new", 1));
        v.setOnClickPendingIntent(R.id.hd, open(c, "glasspad://home", 2));
        return v;
    }
}
