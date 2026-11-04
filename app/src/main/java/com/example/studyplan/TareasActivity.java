
package com.example.studyplan;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TareasActivity extends AppCompatActivity {

    private ArrayList<String> tareas;
    private ArrayList<String> asignaturas;
    private ArrayList<String> fechas;
    private ArrayAdapter<String> adaptador;

    private ListView listaTareas;

    private static final int REQUEST_NUEVA_TAREA = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tareas);

        ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main), (v, insets) -> {

                    Insets systemBars = insets.getInsets(
                            WindowInsetsCompat.Type.systemBars());

                    v.setPadding(
                            systemBars.left + 24,
                            systemBars.top,
                            systemBars.right + 24,
                            systemBars.bottom);

                    return insets;
                });

        listaTareas = findViewById(R.id.listaTareas);

        ImageButton btnVolverInicio =
                findViewById(R.id.btnVolverInicio);

        Button btnAnadirTarea =
                findViewById(R.id.btnAnadirTarea);

        // Recuperar las tareas guardadas en el dispositivo.
        cargarTareas();

        adaptador = new ArrayAdapter<String>(
                this,
                R.layout.item_tarea,
                R.id.tvTarea,
                tareas
        ) {
            @Override
            public View getView(int position, View convertView,
                                ViewGroup parent) {

                View vista = super.getView(
                        position, convertView, parent);

                TextView tvTarea =
                        vista.findViewById(R.id.tvTarea);

                TextView tvAsignatura =
                        vista.findViewById(R.id.tvAsignatura);

                TextView tvFecha =
                        vista.findViewById(R.id.tvFechaEntrega);

                Button btnEliminar =
                        vista.findViewById(R.id.btnEliminar);

                tvTarea.setText(tareas.get(position));
                tvAsignatura.setText(asignaturas.get(position));
                tvFecha.setText(fechas.get(position));

                btnEliminar.setOnClickListener(v -> {
                    int posicion =
                            listaTareas.getPositionForView(vista);

                    if (posicion != ListView.INVALID_POSITION) {
                        tareas.remove(posicion);
                        asignaturas.remove(posicion);
                        fechas.remove(posicion);

                        guardarTareas();

                        adaptador.notifyDataSetChanged();
                    }
                });

                return vista;
            }
        };

        listaTareas.setAdapter(adaptador);

        btnVolverInicio.setOnClickListener(v -> finish());

        btnAnadirTarea.setOnClickListener(v -> {
            Intent intent = new Intent(
                    TareasActivity.this,
                    FormularioTareaActivity.class
            );

            startActivityForResult(
                    intent, REQUEST_NUEVA_TAREA);
        });
    }

    // Guardar las tareas en el almacenamiento local.
    private void guardarTareas() {

        SharedPreferences preferencias =
                getSharedPreferences("datos_tareas", MODE_PRIVATE);

        JSONArray arrayTareas = new JSONArray();

        for (int i = 0; i < tareas.size(); i++) {
            JSONObject objeto = new JSONObject();

            try {
                objeto.put("tarea", tareas.get(i));
                objeto.put("asignatura", asignaturas.get(i));
                objeto.put("fecha", fechas.get(i));

                arrayTareas.put(objeto);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        preferencias.edit()
                .putString("lista_tareas", arrayTareas.toString())
                .apply();
    }

    // Recuperar las tareas guardadas.
    // Si no hay datos guardados, la lista empieza vacía.
    private void cargarTareas() {

        SharedPreferences preferencias =
                getSharedPreferences("datos_tareas", MODE_PRIVATE);

        String datos =
                preferencias.getString("lista_tareas", null);

        tareas = new ArrayList<>();
        asignaturas = new ArrayList<>();
        fechas = new ArrayList<>();

        if (datos == null) {
            return;
        }

        try {
            JSONArray arrayTareas = new JSONArray(datos);

            for (int i = 0; i < arrayTareas.length(); i++) {
                JSONObject objeto =
                        arrayTareas.getJSONObject(i);

                tareas.add(objeto.getString("tarea"));
                asignaturas.add(objeto.getString("asignatura"));
                fechas.add(objeto.getString("fecha"));
            }

        } catch (JSONException e) {
            e.printStackTrace();

            tareas.clear();
            asignaturas.clear();
            fechas.clear();
        }
    }

    // Recibir una tarea nueva desde el formulario.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_NUEVA_TAREA
                && resultCode == RESULT_OK
                && data != null) {

            String tarea = data.getStringExtra(
                    FormularioTareaActivity.EXTRA_TAREA);

            String asignatura = data.getStringExtra(
                    FormularioTareaActivity.EXTRA_ASIGNATURA);

            String fecha = data.getStringExtra(
                    FormularioTareaActivity.EXTRA_FECHA);

            if (tarea != null
                    && asignatura != null
                    && fecha != null) {

                tareas.add(tarea);
                asignaturas.add(asignatura);
                fechas.add("Entrega: " + fecha);

                guardarTareas();

                adaptador.notifyDataSetChanged();
            }
        }
    }
}
