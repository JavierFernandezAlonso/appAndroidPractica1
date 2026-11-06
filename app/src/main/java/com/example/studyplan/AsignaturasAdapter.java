package com.example.studyplan;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.widget.TextView;
import java.util.ArrayList;
import android.widget.Button;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog;

public class AsignaturasAdapter extends RecyclerView.Adapter<AsignaturasAdapter.AsignaturaViewHolder> {

    private ArrayList<String> listaAsignaturas;
    private Runnable alEliminarAsignatura;
    private SharedPreferences preferencias;
    public AsignaturasAdapter(ArrayList<String> listaAsignaturas, Runnable alEliminarAsignatura) {
        this.listaAsignaturas = listaAsignaturas;
        this.alEliminarAsignatura = alEliminarAsignatura;
        // El guardado se inicializará desde el contexto de cada tarjeta.
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
        Context context = holder.itemView.getContext();
        preferencias = context.getSharedPreferences("StudyPlan", Context.MODE_PRIVATE);
        holder.textViewNombreAsignatura.setText(listaAsignaturas.get(position));
        String nombreAsignatura = listaAsignaturas.get(position);

        String profesor = preferencias.getString(
                "profesor_" + nombreAsignatura, ""
        );

        if (profesor.isEmpty()) {
            holder.textViewDescripcionAsignatura.setText("Profesor: Sin asignar");
        } else {
            holder.textViewDescripcionAsignatura.setText("Profesor: " + profesor);
        }
        holder.textViewDescripcionAsignatura.setOnClickListener(v -> {

            EditText inputProfesor = new EditText(context);
            inputProfesor.setSingleLine(true);
            inputProfesor.setHint("Nombre del profesor");
            inputProfesor.setText(preferencias.getString(
                    "profesor_" + nombreAsignatura, ""
            ));

            new AlertDialog.Builder(context)
                    .setTitle("Asignar profesor")
                    .setView(inputProfesor)
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Guardar", (dialog, which) -> {

                        String nuevoProfesor = inputProfesor.getText()
                                .toString().trim();

                        preferencias.edit()
                                .putString("profesor_" + nombreAsignatura, nuevoProfesor)
                                .apply();

                        if (nuevoProfesor.isEmpty()) {
                            holder.textViewDescripcionAsignatura
                                    .setText("Profesor: Sin asignar");
                        } else {
                            holder.textViewDescripcionAsignatura
                                    .setText("Profesor: " + nuevoProfesor);
                        }
                    })
                    .show();
        });
        holder.btnEliminarAsignatura.setOnClickListener(v -> {
            int posicion = holder.getBindingAdapterPosition();
            if (posicion != RecyclerView.NO_POSITION) {

                String asignaturaEliminada = listaAsignaturas.get(posicion);

                preferencias.edit()
                        .remove("profesor_" + asignaturaEliminada)
                        .apply();

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