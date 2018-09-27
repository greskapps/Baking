package com.greskapps.android.baking;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestRecipeInfo();
    }

    private boolean isOnline () {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void requestRecipeInfo () {
        String recipeUrl = "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";
        final ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.clear();

        if (isOnline()) {
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(recipeUrl).build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) {
                        throw new IOException("That's not right..." + response);
                    }
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());

                        for (int j = 0; j <jsonArray.length(); j++) {
                            int id;
                            String name;
                            ArrayList<String> ingredient = new ArrayList<>();
                            ArrayList<String> shortDescription = new ArrayList<>();
                            ArrayList<String> description = new ArrayList<>();
                            ArrayList<String> media = new ArrayList<>();
                            JSONObject pullObject = jsonArray.getJSONObject(j);
                            id = pullObject.getInt("id");
                            name = pullObject.getString("name");

                            JSONArray ingredients = pullObject.getJSONArray("ingredients");
                            for (int i = 0; i < ingredients.length(); i++) {
                                JSONObject ingredientsObject = ingredients.getJSONObject(i);
                                ingredient.add(ingredientsObject.getInt("quantity") + " " + ingredientsObject.getString("measure") + " " + ingredientsObject.getString("ingredient"));
                            }

                            JSONArray steps = pullObject.getJSONArray("steps");
                            for (int i = 0; i < steps.length(); i++) {
                                JSONObject step = steps.getJSONObject(i);
                                shortDescription.add(step.getString("shortDescription"));
                            }
                            for (int i = 0; i < steps.length(); i++) {
                                JSONObject step = steps.getJSONObject(i);
                                description.add(step.getString("description"));
                            }
                            for (int i = 0; i < steps.length(); i++) {
                                JSONObject step = steps.getJSONObject(i);
                                String tempUrl = step.getString("videoURL");
                                if (tempUrl.equals(""))
                                    media.add(step.getString("thumbnailURL"));
                                else
                                    media.add(step.getString("videoURL"));
                            }

                            Recipe recipe = new Recipe(id, name, ingredient, shortDescription, description, media);
                            recipes.add(recipe);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                    MainActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RecipeAdapter recipeAdapter = new RecipeAdapter(getApplicationContext(), recipes);

                            GridView gridView = findViewById(R.id.recipes_gv);
                            //had trouble with LinearLayout here, so switched to 1-column GV
                            gridView.setAdapter(recipeAdapter);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Recipe clickedRecipe = recipes.get(position);
                                    WidgetProvider.sendFresh(getApplicationContext(), clickedRecipe);
                                    Intent intent = new Intent(getApplicationContext(), RecipeActivity.class);
                                    intent.putExtra("parcel_data", clickedRecipe);
                                    startActivity(intent);
                                }
                            });

                        }
                    });
                }
            });
        } else {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
        }
    }

}

