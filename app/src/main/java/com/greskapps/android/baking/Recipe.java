package com.greskapps.android.baking;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Recipe implements Parcelable {
    final int id;
    final String title;
    ArrayList<String> ingredients;
    ArrayList<String> steps;
    ArrayList<String> instructions;
    ArrayList<String> media;

    public Recipe (int id, String title, ArrayList<String> ingredients, ArrayList<String> shortDescription, ArrayList<String> description, ArrayList<String> media) {
        this.id = id;
        this.title = title;
        this.ingredients = ingredients;
        this.steps = shortDescription;
        this.instructions = description;
        this.media = media;
    }

    private Recipe (Parcel input) {
        id = input.readInt();
        title = input.readString();
        ingredients = new ArrayList<String>();
        steps = new ArrayList<String>();
        instructions = new ArrayList<String>();
        media = new ArrayList<String>();
        input.readList(ingredients, getClass().getClassLoader());
        input.readList(steps, getClass().getClassLoader());
        input.readList(instructions, getClass().getClassLoader());
        input.readList(media, getClass().getClassLoader());
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel input) {
            return new Recipe(input);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeList(ingredients);
        parcel.writeList(steps);
        parcel.writeList(instructions);
        parcel.writeList(media);

    }

}