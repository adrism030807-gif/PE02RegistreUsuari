package com.example.pe02_registreusuari;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * PANTALLA 1 (Activity origen).
 * El usuario rellena su ficha y pulsa "Mostrar Info" para ir a la pantalla 2.
 * Cuando vuelve, se muestra si ha aceptado o no la política de privacidad.
 */
public class MainActivity extends AppCompatActivity {

    // Claves para identificar los datos dentro del Intent.
    // Son "public static" para poder usarlas también desde DetalleActivity.
    public static final String EXTRA_PERSONA = "persona";
    public static final String EXTRA_ACEPTADO = "aceptado";

    // Referencias a los elementos del layout
    private EditText etNombre;
    private EditText etApellidos;
    private EditText etEdad;
    private RadioGroup rgMatriculado;
    private TextView tvEstadoPrivacidad;

    // "Lanzador" que abre la pantalla 2 y espera a que nos devuelva un resultado.
    // Es la forma actual de hacerlo (sustituye al antiguo startActivityForResult).
    private final ActivityResultLauncher<Intent> lanzadorDetalle =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), resultado -> {
                // Este código se ejecuta cuando volvemos de la pantalla 2.
                // Comprobamos que ha vuelto con el botón "Salir" (RESULT_OK) y con datos.
                if (resultado.getResultCode() == Activity.RESULT_OK && resultado.getData() != null) {
                    // Leemos el true/false que nos ha enviado la pantalla 2
                    boolean aceptado = resultado.getData().getBooleanExtra(EXTRA_ACEPTADO, false);

                    if (aceptado) {
                        tvEstadoPrivacidad.setText(R.string.privacidad_aceptada);
                    } else {
                        tvEstadoPrivacidad.setText(R.string.privacidad_no_aceptada);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Deja espacio para la barra de estado y la de navegación del móvil
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Enlazamos las variables con las vistas del XML
        etNombre = findViewById(R.id.etNombre);
        etApellidos = findViewById(R.id.etApellidos);
        etEdad = findViewById(R.id.etEdad);
        rgMatriculado = findViewById(R.id.rgMatriculado);
        tvEstadoPrivacidad = findViewById(R.id.tvEstadoPrivacidad);
        Button btnMostrarInfo = findViewById(R.id.btnMostrarInfo);

        // Al pulsar el botón, validamos y abrimos la pantalla 2
        btnMostrarInfo.setOnClickListener(v -> mostrarInfo());
    }

    /**
     * Valida los campos y, si todo está bien, crea una Persona
     * y la envía a la pantalla 2 con un Intent explícito.
     */
    private void mostrarInfo() {
        // trim() quita los espacios del principio y del final
        String nombre = etNombre.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String textoEdad = etEdad.getText().toString().trim();

        // ---- VALIDACIONES ----
        // setError muestra un aviso rojo en el propio campo
        if (nombre.isEmpty()) {
            etNombre.setError(getString(R.string.error_nombre_vacio));
            etNombre.requestFocus();
            return; // salimos del método: no seguimos
        }

        if (apellidos.isEmpty()) {
            etApellidos.setError(getString(R.string.error_apellidos_vacio));
            etApellidos.requestFocus();
            return;
        }

        if (textoEdad.isEmpty()) {
            etEdad.setError(getString(R.string.error_edad_vacia));
            etEdad.requestFocus();
            return;
        }

        // Convertimos el texto a número entero (int)
        int edad = Integer.parseInt(textoEdad);
        if (edad < 1 || edad > 120) {
            etEdad.setError(getString(R.string.error_edad_rango));
            etEdad.requestFocus();
            return;
        }

        // getCheckedRadioButtonId() devuelve -1 si no hay ningún RadioButton marcado
        int idMarcado = rgMatriculado.getCheckedRadioButtonId();
        if (idMarcado == -1) {
            // Un RadioGroup no tiene setError, así que usamos un Toast
            Toast.makeText(this, R.string.error_matriculado, Toast.LENGTH_SHORT).show();
            return;
        }
        boolean matriculado = (idMarcado == R.id.rbSi);

        // ---- CREAR EL OBJETO Y ENVIARLO ----
        Persona persona = new Persona(nombre, apellidos, edad, matriculado);

        // Intent explícito: indicamos exactamente qué Activity queremos abrir
        Intent intent = new Intent(this, DetalleActivity.class);

        // Metemos el objeto en un Bundle y el Bundle en los Extras del Intent.
        // Funciona porque Persona implementa Serializable.
        Bundle bundle = new Bundle();
        bundle.putSerializable(EXTRA_PERSONA, persona);
        intent.putExtras(bundle);

        // Abrimos la pantalla 2 esperando un resultado
        lanzadorDetalle.launch(intent);
    }
}
