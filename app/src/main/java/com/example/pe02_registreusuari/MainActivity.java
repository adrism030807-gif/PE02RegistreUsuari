package com.example.pe02_registreusuari;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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

    // Variables para los Views del layout
    EditText edtNombre, edtApellidos, edtEdad;
    RadioButton rbSi, rbNo;
    TextView txtPrivacidad;
    Button btnMostrarInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Código que pone Android Studio al crear la Activity:
        // deja espacio para la barra de estado de arriba del móvil
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referenciamos los Views del layout con findViewById
        edtNombre = findViewById(R.id.edtNombre);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtEdad = findViewById(R.id.edtEdad);
        rbSi = findViewById(R.id.rbSi);
        rbNo = findViewById(R.id.rbNo);
        txtPrivacidad = findViewById(R.id.txtPrivacidad);
        btnMostrarInfo = findViewById(R.id.btnMostrarInfo);

        // Listener del botón "Mostrar Info"
        btnMostrarInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Leemos lo que ha escrito el usuario
                String nombre = edtNombre.getText().toString();
                String apellidos = edtApellidos.getText().toString();
                String textoEdad = edtEdad.getText().toString();

                // ---- VALIDACIONES ----
                // Si falta algún dato mostramos un Toast y salimos con return
                if (nombre.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.error_nombre, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (apellidos.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.error_apellidos, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (textoEdad.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.error_edad, Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!rbSi.isChecked() && !rbNo.isChecked()) {
                    Toast.makeText(MainActivity.this, R.string.error_matriculado, Toast.LENGTH_SHORT).show();
                    return;
                }

                // Pasamos la edad de texto a número entero
                int edad = Integer.parseInt(textoEdad);
                // Si está marcado "Sí", matriculado será true; si no, false
                boolean matriculado = rbSi.isChecked();

                // ---- CREAMOS LA PERSONA Y LA ENVIAMOS ----
                Persona p = new Persona(nombre, apellidos, edad, matriculado);

                // Intent explícito: decimos qué Activity queremos abrir
                Intent i = new Intent(MainActivity.this, DetalleActivity.class);
                // Metemos la Persona en los extras (funciona porque es Serializable)
                i.putExtra("persona", p);

                // Abrimos la pantalla 2 y esperamos que nos devuelva un resultado.
                // El 123 es el código para saber desde qué llamada volvemos.
                startActivityForResult(i, 123);
            }
        });
    }

    // Se ejecuta automáticamente cuando volvemos de la pantalla 2
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Comprobamos que volvemos de la llamada 123 y con el botón "Salir" (RESULT_OK)
        if (requestCode == 123 && resultCode == RESULT_OK) {
            // Leemos el true/false que nos envía la pantalla 2
            boolean aceptado = data.getBooleanExtra("aceptado", false);

            if (aceptado) {
                txtPrivacidad.setText(R.string.privacidad_aceptada);
            } else {
                txtPrivacidad.setText(R.string.privacidad_no_aceptada);
            }
        }
    }
}
