package com.glasspad.notes;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import com.getcapacitor.BridgeActivity;

public class MainActivity extends BridgeActivity {
    @Override
    public void onPause() {
        super.onPause();
        try {
            AppWidgetManager m = AppWidgetManager.getInstance(this);
            int[] ids = m.getAppWidgetIds(new ComponentName(this, NotesWidget.class));
            if (ids.length > 0) {
                Intent i = new Intent(this, NotesWidget.class);
                i.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                i.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                sendBroadcast(i);
            }
        } catch (Exception e) {}
    }
}
