package com.example.dany.stockhome;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dany on 06/05/2016.
 */
public class RecyclerAdapterReceitas extends RecyclerView.Adapter<RecyclerAdapterReceitas.RecyclerViewHolder> {

    ArrayList<Receita> arrayList = new ArrayList<>();
    public RecyclerAdapterReceitas(ArrayList<Receita> arrayList){this.arrayList = arrayList;}

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_receitas, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {

        final Receita fruit = arrayList.get(position);
        holder.Name.setText(fruit.getNome());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),fazer_receita.class);
                intent.putExtra("id",fruit.getId());
                intent.putExtra("nome",fruit.getNome());
                intent.putExtra("prep",fruit.getPreparaçao());
                v.getContext().startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {
                Intent intent = new Intent(v.getContext(),editar_receitas.class);
                intent.putExtra("id",fruit.getId());
                intent.putExtra("nome",fruit.getNome());
                intent.putExtra("prep",fruit.getPreparaçao());
                v.getContext().startActivity(intent);
                return false;
            }
        });
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public  static class RecyclerViewHolder extends RecyclerView.ViewHolder{

        TextView Name;
        public RecyclerViewHolder(View View) {
            super(View);
            Name = (TextView)itemView.findViewById(R.id.NomeReceita);

        }
    }

}
