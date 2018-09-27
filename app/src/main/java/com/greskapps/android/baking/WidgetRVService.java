package com.greskapps.android.baking;

import android.content.Intent;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class WidgetRVService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {

        ArrayList<String> recipe = intent.getStringArrayListExtra("bundle");
        return new WidgetRVFactory(this.getApplicationContext(), intent, recipe);

    }
}
