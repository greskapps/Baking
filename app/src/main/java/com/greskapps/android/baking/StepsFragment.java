package com.greskapps.android.baking;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

public class StepsFragment extends Fragment {

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
    }

    public StepsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final Recipe recipe = getActivity().getIntent().getParcelableExtra("parcel_data");
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_steps, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.steps_view);
        ExpandableListView expandableListView = rootView.findViewById(R.id.recipe_view);

        StepsAdapter stepsAdapter = new StepsAdapter(getContext(), recipe.steps, recipe);
        IngredientAdapter ingredientAdapter = new IngredientAdapter(getContext(), "Ingredients", recipe.ingredients);

        recyclerView.setAdapter(stepsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        expandableListView.setAdapter(ingredientAdapter);

        return rootView;
    }

}