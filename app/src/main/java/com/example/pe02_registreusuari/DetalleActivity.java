package com.example.pe02_registreusuari;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * PANTALLA 2.
 * Recibe la Persona de la pantalla 1, muestra sus datos en TextView (no editables)
 * y al pulsar "Salir" devuelve si se ha aceptado la política de privacidad (true/false).
 */
public class DetalleActivity extends AppCompatActivity {

    // Variables para los Views del layout
    TextView txtNombre, txtApellidos, txtEdad, txtMatriculado;
    CheckBox chkPrivacidad;
    Button btnSalir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Código que pone Android Studio al crear la Activity:
        // deja espacio para la barra de estado de arriba del móvil
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referenciamos los Views del layout
        txtNombre = findViewById(R.id.txtNombre);
        txtApellidos = findViewById(R.id.txtApellidos);
        txtEdad = findViewById(R.id.txtEdad);
        txtMatriculado = findViewById(R.id.txtMatriculado);
        chkPrivacidad = findViewById(R.id.chkPrivacidad);
        btnSalir = findViewById(R.id.btnSalir);

        // ---- RECOGEMOS LA PERSONA QUE NOS ENVÍA LA PANTALLA 1 ----
        // Usamos la misma clave "persona" con la que se envió.
        // Hay que hacer un cast (Persona) porque getSerializableExtra devuelve un objeto genérico.
        Persona p = (Persona) getIntent().getSerializableExtra("persona");

        // Mostramos los datos en los TextView
        txtNombre.setText(p.getNombre());
        txtApellidos.setText(p.getApellidos());
        // setText necesita un texto, así que convertimos la edad (int) a String
        txtEdad.setText(String.valueOf(p.getEdad()));

        if (p.isMatriculado()) {
            txtMatriculado.setText(R.string.opcion_si);
        } else {
            txtMatriculado.setText(R.string.opcion_no);
        }

        // Listener del botón "Salir"
        btnSalir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Creamos un Intent vacío solo para llevar la respuesta
                Intent i = new Intent();
                // isChecked() devuelve true si el CheckBox está marcado y false si no
                i.putExtra("aceptado", chkPrivacidad.isChecked());
                // Devolvemos el resultado a la pantalla 1 y cerramos esta pantalla
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }
}
