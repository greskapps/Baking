package com.greskapps.android.baking;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<String> steps;
    private Recipe recipe;
    private StepsOnClickListener mCall;

    public interface StepsOnClickListener {
        void onStepSelected (int position, Recipe recipe);
    }

    public StepsAdapter (Context context, ArrayList<String> steps, Recipe recipe) {
        this.context = context;
        this.steps = steps;
        this.recipe = recipe;
        mCall = (StepsOnClickListener) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.list_steps, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        String step = steps.get(position);
        holder.textView.setText(step);
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCall.onStepSelected(holder.getAdapterPosition(), recipe);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.steps.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder {
        private TextView textView;
        private View parentView;

        public ViewHolder(@NonNull View view) {
            super(view);
            this.parentView = view;
            this.textView = view.findViewById(R.id.step_item);
        }
    }

}
