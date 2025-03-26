package com.zombies.cowhealthai;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CowAdapter extends RecyclerView.Adapter<CowAdapter.CowViewHolder> {
    private Context context;
    private List<Cow> cowList;
    private DatabaseHelper dbHelper;

    public CowAdapter(Context context, List<Cow> cowList) {
        this.context = context;
        this.cowList = cowList;
        this.dbHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cow, parent, false);
        return new CowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CowViewHolder holder, int position) {
        Cow cow = cowList.get(position);
        holder.cowName.setText(cow.getName());
        holder.cowDetails.setText("Breed: " + cow.getBreed() + ", Weight: " + cow.getWeight() +
                "kg, Age: " + cow.getAge() + " years, Milk: " + cow.getMilk() + "L");

        holder.menuButton.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(context, holder.menuButton);
            popupMenu.inflate(R.menu.cow_menu);
            popupMenu.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.delete) {
                    showDeleteConfirmationDialog(position);
                    return true;
                } else if (item.getItemId() == R.id.edit) {
                    showEditDialog(cow, position);
                    return true;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    private void showEditDialog(Cow cow, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_edit_cow, null);
        builder.setView(view);

        EditText editCowName = view.findViewById(R.id.edit_cow_name);
        EditText editCowBreed = view.findViewById(R.id.edit_cow_breed);
        EditText editCowWeight = view.findViewById(R.id.edit_cow_weight);
        EditText editCowAge = view.findViewById(R.id.edit_cow_age);
        EditText editCowMilk = view.findViewById(R.id.edit_cow_milk);

        // Set existing values
        editCowName.setText(cow.getName());
        editCowBreed.setText(cow.getBreed());
        editCowWeight.setText(String.valueOf(cow.getWeight()));
        editCowAge.setText(String.valueOf(cow.getAge()));
        editCowMilk.setText(String.valueOf(cow.getMilk()));

        builder.setPositiveButton("Update", (dialog, which) -> {
            // Get updated values
            String updatedName = editCowName.getText().toString();
            String updatedBreed = editCowBreed.getText().toString();
            String updatedWeight = editCowWeight.getText().toString();
            String updatedAge = editCowAge.getText().toString();
            String updatedMilk = editCowMilk.getText().toString();

            // Update in database
            dbHelper.updateCow(cow.getName(), updatedName, updatedBreed, updatedWeight, updatedAge, updatedMilk);

            // Update in local list
            cow.setName(updatedName);
            cow.setBreed(updatedBreed);
            cow.setWeight(updatedWeight);
            cow.setAge(updatedAge);
            cow.setMilk(updatedMilk);
            notifyItemChanged(position);

            // Refresh the activity to reflect changes
            refreshActivity();
        });

        builder.setNegativeButton("Cancel", null);
        builder.show();
    }

    private void showDeleteConfirmationDialog(int position) {
        new AlertDialog.Builder(context)
                .setTitle("Delete Cow")
                .setMessage("Do you really want to delete this cow?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    Cow cow = cowList.get(position);

                    // Delete from database
                    dbHelper.deleteCow(cow.getName());

                    // Remove from list and update RecyclerView
                    cowList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, cowList.size());

                    // Refresh the activity to reflect changes
                    refreshActivity();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void refreshActivity() {
        Intent intent = new Intent(context, ViewCowsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return cowList.size();
    }

    static class CowViewHolder extends RecyclerView.ViewHolder {
        TextView cowName, cowDetails;
        ImageView menuButton;

        public CowViewHolder(@NonNull View itemView) {
            super(itemView);
            cowName = itemView.findViewById(R.id.cow_name);
            cowDetails = itemView.findViewById(R.id.cow_details);
            menuButton = itemView.findViewById(R.id.menu_button);
        }
    }
}
