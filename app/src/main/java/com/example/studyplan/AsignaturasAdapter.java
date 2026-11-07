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
import android.widget.LinearLayout;

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
        holder.itemView.setOnClickListener(v -> {

            int posicion = holder.getBindingAdapterPosition();

            if (posicion == RecyclerView.NO_POSITION) {
                return;
            }

            String nombreActual = listaAsignaturas.get(posicion);

            EditText inputNombre = new EditText(context);
            inputNombre.setHint("Nombre de la asignatura");
            inputNombre.setSingleLine(true);
            inputNombre.setText(nombreActual);

            EditText inputProfesor = new EditText(context);
            inputProfesor.setHint("Nombre del profesor");
            inputProfesor.setSingleLine(true);
            inputProfesor.setText(preferencias.getString(
                    "profesor_" + nombreActual, ""
            ));

            LinearLayout contenedor = new LinearLayout(context);
            contenedor.setOrientation(LinearLayout.VERTICAL);
            contenedor.setPadding(50, 20, 50, 10);

            contenedor.addView(inputNombre);
            contenedor.addView(inputProfesor);

            new AlertDialog.Builder(context)
                    .setTitle("Editar asignatura")
                    .setView(contenedor)
                    .setNegativeButton("Cancelar", null)
                    .setPositiveButton("Guardar cambios", (dialog, which) -> {

                        String nuevoNombre = inputNombre.getText().toString().trim();
                        String nuevoProfesor = inputProfesor.getText().toString().trim();

                        if (!nuevoNombre.isEmpty()) {

                            preferencias.edit()
                                    .remove("profesor_" + nombreActual)
                                    .putString("profesor_" + nuevoNombre, nuevoProfesor)
                                    .apply();

                            listaAsignaturas.set(posicion, nuevoNombre);

                            alEliminarAsignatura.run();
                            notifyItemChanged(posicion);
                        }
                    })
                    .show();
        });
        String nombreAsignatura = listaAsignaturas.get(position);

        String profesor = preferencias.getString(
                "profesor_" + nombreAsignatura, ""
        );

        if (profesor.isEmpty()) {
            holder.textViewDescripcionAsignatura.setText("Profesor: Sin asignar");
        } else {
            holder.textViewDescripcionAsignatura.setText("Profesor: " + profesor);
        }

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