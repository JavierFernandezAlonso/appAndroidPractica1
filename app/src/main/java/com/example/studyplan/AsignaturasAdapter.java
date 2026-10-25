package com.example.studyplan;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.ArrayList;
import android.widget.Button;

public class AsignaturasAdapter extends RecyclerView.Adapter<AsignaturasAdapter.AsignaturaViewHolder> {

    private ArrayList<String> listaAsignaturas;
    private Runnable alEliminarAsignatura;
    public AsignaturasAdapter(ArrayList<String> listaAsignaturas, Runnable alEliminarAsignatura) {
        this.listaAsignaturas = listaAsignaturas;
        this.alEliminarAsignatura = alEliminarAsignatura;
    }

    @NonNull
    @Override
    public AsignaturaViewHolder onCreateViewHolder(
            ViewGroup parent, int viewType) {

        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_asignatura, parent, false);

        return new AsignaturaViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull AsignaturaViewHolder holder, int position) {
        holder.textViewNombreAsignatura.setText(listaAsignaturas.get(position));
        holder.btnEliminarAsignatura.setOnClickListener(v -> {
            int posicion = holder.getBindingAdapterPosition();
            if (posicion != RecyclerView.NO_POSITION) {
                listaAsignaturas.remove(posicion);
                notifyItemRemoved(posicion);
                alEliminarAsignatura.run();

            }

        });
    }

    @Override
    public int getItemCount() {
        return listaAsignaturas.size();
    }

    public static class AsignaturaViewHolder extends RecyclerView.ViewHolder {

        TextView textViewNombreAsignatura;
        TextView textViewDescripcionAsignatura;
        Button btnEliminarAsignatura;

        public AsignaturaViewHolder(View itemView) {
            super(itemView);

            textViewNombreAsignatura =
                    itemView.findViewById(R.id.textViewNombreAsignatura);

            textViewDescripcionAsignatura =
                    itemView.findViewById(R.id.textViewDescripcionAsignatura);
            btnEliminarAsignatura = itemView.findViewById(R.id.btnEliminarAsignatura);
        }
    }

}