package com.greskapps.android.baking;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class WidgetRVFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private ArrayList<String> factoryArrayList = new ArrayList<>();

    public WidgetRVFactory(Context appContext, Intent intent, ArrayList<String> recipe) {
        context = appContext;

        if(recipe != null)
            factoryArrayList = recipe;
    }

    @Override
    public void onCreate() { }

    @Override
    public void onDataSetChanged() {
        final long idToken = Binder.clearCallingIdentity();
        Binder.restoreCallingIdentity(idToken);
    }

    @Override
    public void onDestroy() { }

    @Override
    public int getCount() {
        return factoryArrayList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (position == AdapterView.INVALID_POSITION) {
            return null;
        }

        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.list_item);
        rv.setTextViewText(R.id.ingredient_item, factoryArrayList.get(position));
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
