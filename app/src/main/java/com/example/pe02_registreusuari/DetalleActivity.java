package com.example.pe02_registreusuari;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * PANTALLA 2.
 * Recibe la Persona de la pantalla 1, muestra sus datos en TextView (no editables)
 * y devuelve a la pantalla 1 si se ha aceptado la política de privacidad (true/false).
 */
public class DetalleActivity extends AppCompatActivity {

    private CheckBox cbPrivacidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);

        // Deja espacio para la barra de estado y la de navegación del móvil
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.detalle), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enlazamos las vistas del XML
        TextView tvNombre = findViewById(R.id.tvNombre);
        TextView tvApellidos = findViewById(R.id.tvApellidos);
        TextView tvEdad = findViewById(R.id.tvEdad);
        TextView tvMatriculado = findViewById(R.id.tvMatriculado);
        cbPrivacidad = findViewById(R.id.cbPrivacidad);
        Button btnSalir = findViewById(R.id.btnSalir);

        // ---- RECOGER LA PERSONA QUE NOS HA ENVIADO LA PANTALLA 1 ----
        Persona persona = obtenerPersona(getIntent());

        if (persona != null) {
            // getString(id, valor) sustituye el %1$s o %1$d del strings.xml por el valor
            tvNombre.setText(getString(R.string.dato_nombre, persona.getNombre()));
            tvApellidos.setText(getString(R.string.dato_apellidos, persona.getApellidos()));
            tvEdad.setText(getString(R.string.dato_edad, persona.getEdad()));

            // Elegimos el texto "Sí" o "No" según el booleano
            String textoMatriculado = persona.isMatriculado()
                    ? getString(R.string.opcion_si)
                    : getString(R.string.opcion_no);
            tvMatriculado.setText(getString(R.string.dato_matriculado, textoMatriculado));
        }

        // Botón "Salir": devuelve el resultado y cierra esta pantalla
        btnSalir.setOnClickListener(v -> devolverResultadoYSalir());

        // El botón "Atrás" del móvil también devuelve si se ha aceptado o no
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                devolverResultadoYSalir();
            }
        });
    }

    /**
     * Saca el objeto Persona del Intent.
     * A partir de Android 13 (API 33) hay una versión nueva del método que pide la clase;
     * en versiones anteriores se usa la antigua y se hace un "cast" a Persona.
     */
    @SuppressWarnings("deprecation")
    private Persona obtenerPersona(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return intent.getSerializableExtra(MainActivity.EXTRA_PERSONA, Persona.class);
        } else {
            return (Persona) intent.getSerializableExtra(MainActivity.EXTRA_PERSONA);
        }
    }

    /**
     * Crea un Intent de respuesta con true/false según el CheckBox,
     * lo devuelve a la pantalla 1 y cierra esta Activity.
     */
    private void devolverResultadoYSalir() {
        Intent respuesta = new Intent();
        respuesta.putExtra(MainActivity.EXTRA_ACEPTADO, cbPrivacidad.isChecked());

        // RESULT_OK indica a la pantalla 1 que todo ha ido bien
        setResult(Activity.RESULT_OK, respuesta);
        finish(); // cierra la pantalla 2 y vuelve a la 1
    }
}
