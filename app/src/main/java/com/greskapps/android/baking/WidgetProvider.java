package com.greskapps.android.baking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int[] appWidgetIds, Recipe recipe) {
        for (int appWidgetId : appWidgetIds) {
            CharSequence widgetText = context.getString(R.string.appwidget_text);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);

            Intent wrvsIntent = new Intent(context, WidgetRVService.class);
            Intent mainIntent = new Intent(context, MainActivity.class);

            if (recipe != null) {
                wrvsIntent.putStringArrayListExtra("bundle", recipe.ingredients);
                wrvsIntent.setData(Uri.fromParts("content", String.valueOf(appWidgetId + Math.random()), null ));
                views.setTextViewText(R.id.appwidget_text, widgetText);
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_heading, pendingIntent);

            views.setRemoteAdapter(R.id.widget_lv, wrvsIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    public static void sendFresh (Context context, Recipe recipe) {
        Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        intent.putExtra("bundle", recipe);
        intent.setComponent(new ComponentName(context, WidgetProvider.class));
        context.sendBroadcast(intent);
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(AppWidgetManager.ACTION_APPWIDGET_UPDATE)) {
            Recipe recipe = intent.getParcelableExtra("bundle");
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName cn = new ComponentName(context, WidgetProvider.class);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(cn), R.id.widget_lv);

            updateAppWidget(context, manager, manager.getAppWidgetIds(cn), recipe);
            onUpdate(context, manager, manager.getAppWidgetIds(cn));
        }
    }
}