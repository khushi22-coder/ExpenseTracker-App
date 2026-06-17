package com.apexplanet.expensetracker.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apexplanet.expensetracker.R;
import com.apexplanet.expensetracker.data.Expense;

import java.util.ArrayList;
import java.util.List;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    private List<Expense> expenseList = new ArrayList<>();
    private OnItemClickListener listener;

    // Interface for click events
    public interface OnItemClickListener {
        void onItemClick(Expense expense);
        void onItemLongClick(Expense expense);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Update list when data changes
    public void setExpenses(List<Expense> expenses) {
        this.expenseList = expenses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.bind(expense);
    }

    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    // ViewHolder - holds each item view
    class ExpenseViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvCategory, tvDate, tvAmount, tvCategoryIcon;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);

            // Connect views
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvAmount = itemView.findViewById(R.id.tvAmount);
            tvCategoryIcon = itemView.findViewById(R.id.tvCategoryIcon);

            // Single click
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_ID) {
                    listener.onItemClick(expenseList.get(position));
                }
            });

            // Long click (for delete)
            itemView.setOnLongClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_ID) {
                    listener.onItemLongClick(expenseList.get(position));
                }
                return true;
            });
        }

        // Fill data into views
        public void bind(Expense expense) {
            tvTitle.setText(expense.getTitle());
            tvCategory.setText(expense.getCategory());
            tvDate.setText(expense.getDate());

            // Set amount with color
            if (expense.getType().equals("INCOME")) {
                tvAmount.setText("+ ₹" + String.format("%.2f", expense.getAmount()));
                tvAmount.setTextColor(
                        itemView.getContext().getResources().getColor(R.color.income_green)
                );
                tvCategoryIcon.setText("💰");
            } else {
                tvAmount.setText("- ₹" + String.format("%.2f", expense.getAmount()));
                tvAmount.setTextColor(
                        itemView.getContext().getResources().getColor(R.color.expense_red)
                );
                tvCategoryIcon.setText("💸");
            }
        }
    }
}