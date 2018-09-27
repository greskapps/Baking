package com.greskapps.android.baking;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class IngredientAdapter extends BaseExpandableListAdapter {

    private Context context;
    private String expandableListTitle;
    private ArrayList<String> expandableListDetail;

    public IngredientAdapter (Context context, String expandableListTitle, ArrayList<String> expandableListDetail) {
        this.context = context;
        this.expandableListDetail = expandableListDetail;
        this.expandableListTitle = expandableListTitle;
    }

    @Override
    public Object getChild (int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(expandedListPosition);
    }

    @Override
    public long getChildId (int listPosition, int expandedListPosition) {
        return  expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition, boolean isRecentChild, View changeView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (changeView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            changeView = layoutInflater.inflate(R.layout.list_item, null);
        }

        TextView expandedListTextView = (TextView) changeView.findViewById(R.id.ingredient_item);
        expandedListTextView.setText(expandedListText);

        return changeView;
    }

    @Override
    public int getChildrenCount(int i) {
        return this.expandableListDetail.size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded, View changeView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);

        if (changeView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            changeView = layoutInflater.inflate(R.layout.list_group, null);
        }

        TextView expandedListTitleTextView = (TextView) changeView.findViewById(R.id.list_title);
        expandedListTitleTextView.setTypeface(null, Typeface.BOLD_ITALIC);
        expandedListTitleTextView.setText(listTitle);

        return changeView;
        }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

}
