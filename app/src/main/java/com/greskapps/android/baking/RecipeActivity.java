package com.greskapps.android.baking;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecipeActivity extends AppCompatActivity implements StepsAdapter.StepsOnClickListener {

    Boolean dualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Recipe recipe = getIntent().getParcelableExtra("parcel_data");
        setTitle(recipe.title);

        if (findViewById(R.id.recipe_activity_ll) != null) {
            dualPane = true;

            if(savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();

                DetailFragment detailFragment = new DetailFragment();
                fragmentManager.beginTransaction().add(R.id.detail_container, detailFragment).commit();
            }
        } else {
            dualPane = false;
        }
    }

    @Override
    public void onStepSelected(int position, Recipe recipe) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelable("parcel_data", recipe);

        if (dualPane) {
            DetailFragment newFragment = new DetailFragment();
            newFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.detail_container, newFragment).commit();

        } else {
            final Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
