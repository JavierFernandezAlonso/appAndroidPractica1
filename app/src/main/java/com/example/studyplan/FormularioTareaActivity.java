package com.example.studyplan;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class FormularioTareaActivity extends AppCompatActivity {

    public static final String EXTRA_TAREA = "tarea";
    public static final String EXTRA_ASIGNATURA = "asignatura";
    public static final String EXTRA_FECHA = "fecha";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_formulario_tarea);

            EditText etNombreTarea = findViewById(R.id.etNombreTarea);
            EditText etAsignatura = findViewById(R.id.etAsignatura);
            EditText etFechaEntrega = findViewById(R.id.etFechaEntrega);

            Button btnGuardar = findViewById(R.id.btnGuardarTarea);
            Button btnCancelar = findViewById(R.id.btnCancelar);

            btnGuardar.setOnClickListener(v -> {
                String tarea = etNombreTarea.getText().toString().trim();
                String asignatura = etAsignatura.getText().toString().trim();
                String fecha = etFechaEntrega.getText().toString().trim();

                if (tarea.isEmpty() || asignatura.isEmpty()
                        || fecha.isEmpty()) {
                    Toast.makeText(
                            this,
                            "Completa todos los campos",
                            Toast.LENGTH_SHORT
                    ).show();
                    return;
                }

                if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    etFechaEntrega.setError("Usa el formato DD/MM/AAAA");
                    return;
                }

                android.content.Intent resultado =
                        new android.content.Intent();

                resultado.putExtra(EXTRA_TAREA, tarea);
                resultado.putExtra(EXTRA_ASIGNATURA, asignatura);
                resultado.putExtra(EXTRA_FECHA, fecha);

                setResult(RESULT_OK, resultado);
                finish();
            });

            btnCancelar.setOnClickListener(v -> {
                setResult(RESULT_CANCELED);
                finish();
            });
        }
    }