package com.example.studyplan;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;
import android.widget.EditText;
import android.content.SharedPreferences;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;

public class AsignaturasActivity extends AppCompatActivity {
    private RecyclerView recyclerViewAsignaturas;
    private ArrayList<String> listaAsignaturas;
    private Button btnAnadirAsignatura;
    private SharedPreferences preferencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_asignaturas);
        preferencias = getSharedPreferences("StudyPlan", MODE_PRIVATE);
        btnAnadirAsignatura = findViewById(R.id.btnAñadirAsignaturas);
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
        recyclerViewAsignaturas = findViewById(R.id.recyclerViewAsignaturas);
        recyclerViewAsignaturas.setLayoutManager(new LinearLayoutManager(this));
        listaAsignaturas = new ArrayList<>();
        String asignaturasGuardadas = preferencias.getString("asignaturas", "");
        if (!asignaturasGuardadas.isEmpty()) {
            String[] asignaturas = asignaturasGuardadas.split(";");
            Collections.addAll(listaAsignaturas, asignaturas);
        }
        AsignaturasAdapter adapter = new AsignaturasAdapter(listaAsignaturas, () -> guardarAsignaturas());
        recyclerViewAsignaturas.setAdapter(adapter);
        btnAnadirAsignatura.setOnClickListener(v -> {
            LinearLayout contenedor = new LinearLayout(this);
            contenedor.setOrientation(LinearLayout.VERTICAL);
            contenedor.setPadding(50, 20, 50, 10);

            EditText inputAsignatura = new EditText(this);
            inputAsignatura.setHint("Nombre de la asignatura");

            EditText inputProfesor = new EditText(this);
            inputProfesor.setHint("Nombre del profesor");

            contenedor.addView(inputAsignatura);
            contenedor.addView(inputProfesor);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Nueva asignatura");
            builder.setView(contenedor);
            builder.setNegativeButton("Cancelar", (dialog, which) -> {
                dialog.dismiss();
            });

            builder.setPositiveButton("Añadir", (dialog, which) -> {
                String nombre = inputAsignatura.getText().toString().trim();
                if (!nombre.isEmpty()) {

                    String profesor = inputProfesor.getText().toString().trim();

                    listaAsignaturas.add(nombre);

                    preferencias.edit()
                            .putString("profesor_" + nombre, profesor)
                            .apply();

                    adapter.notifyItemInserted(listaAsignaturas.size() - 1);

                    guardarAsignaturas();
                }
            });
            builder.show();
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void guardarAsignaturas() {
        SharedPreferences.Editor editor = preferencias.edit();
        String asignaturasGuardadas = String.join(";", listaAsignaturas);
        editor.putString("asignaturas", asignaturasGuardadas);
        editor.apply();

    }
}