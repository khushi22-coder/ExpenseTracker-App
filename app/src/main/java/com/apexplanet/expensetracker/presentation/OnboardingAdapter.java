package com.apexplanet.expensetracker.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apexplanet.expensetracker.R;

public class OnboardingAdapter extends
        RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    // Slide data
    private String[] emojis = {"💰", "📊", "🎯"};
    private String[] titles = {
            "Track Expenses",
            "See Statistics",
            "Reach Your Goals"
    };
    private String[] descriptions = {
            "Easily track all your daily income and expenses in one place!",
            "View beautiful charts and understand where your money goes!",
            "Set budget goals and achieve financial freedom!"
    };

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onboarding, parent, false);
        return new OnboardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull OnboardingViewHolder holder, int position) {
        holder.tvEmoji.setText(emojis[position]);
        holder.tvTitle.setText(titles[position]);
        holder.tvDescription.setText(descriptions[position]);
    }

    @Override
    public int getItemCount() {
        return emojis.length;
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {
        TextView tvEmoji, tvTitle, tvDescription;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEmoji = itemView.findViewById(R.id.tvEmoji);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDescription = itemView.findViewById(R.id.tvDescription);
        }
    }
}