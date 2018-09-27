package com.greskapps.android.baking;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Recipe recipe = getIntent().getParcelableExtra("parcel_data");
        setTitle(recipe.title);
        int position = getIntent().getIntExtra("position", 0);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putParcelable("parcel_data", recipe);

        if (savedInstanceState == null) {
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(bundle);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().add(R.id.detail_container, detailFragment).commit();

        }
    }
}
