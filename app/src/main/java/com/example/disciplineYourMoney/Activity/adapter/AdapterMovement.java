
package com.example.disciplineYourMoney.Activity.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.disciplineYourMoney.Activity.model.Movement;
import com.example.disciplineYourMoney.R;
import java.util.List;



public class AdapterMovement extends RecyclerView.Adapter<AdapterMovement.MyViewHolder> {

    List< Movement > movements;
    Context context;

    public AdapterMovement(List< Movement > movements, Context context) {
        this.movements = movements;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemList = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_movements, parent, false);
        return new MyViewHolder( itemList );
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterMovement.MyViewHolder holder, int position) {
        Movement movement = movements.get(position);

        holder.description.setText(movement.getDescription());
        holder.value.setText(String.valueOf(movement.getValue()));
        holder.category.setText(movement.getCategory());
        holder.value.setTextColor(context.getResources().getColor(R.color.colorAcentRecipe));

        if (movement.getType().equals("d")) {
            holder.value.setTextColor(context.getResources().getColor(R.color.colorAccent));
            holder.value.setText("-" + movement.getValue());
        }

    }

    @Override
    public int getItemCount() {
        return movements.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView description, value, category;

        public MyViewHolder(View itemView) {
            super(itemView);

            description = itemView.findViewById(R.id.textAdapterTitle);
            value = itemView.findViewById(R.id.textAdapterValue);
            category = itemView.findViewById(R.id.textAdapterCategory);

        }

    }




}
