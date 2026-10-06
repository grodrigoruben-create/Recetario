package com.example.recetario;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText txtNombre;
    EditText txtPreparacion;
    EditText txtIngrediente;
    EditText txtCantidad;

    ImageView imgFoto;

    DBHelper dbHelper;

    String rutaFoto = "";

    ArrayList<String[]> ingredientes =
            new ArrayList<>();

    ActivityResultLauncher<Intent> launcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        dbHelper = new DBHelper(this);

        txtNombre = findViewById(R.id.txtNombre);
        txtPreparacion = findViewById(R.id.txtPreparacion);
        txtIngrediente = findViewById(R.id.txtIngrediente);
        txtCantidad = findViewById(R.id.txtCantidad);

        imgFoto = findViewById(R.id.imgFoto);

        Button btnFoto =
                findViewById(R.id.btnFoto);

        Button btnGuardar =
                findViewById(R.id.btnGuardar);

        Button btnAgregar =
                findViewById(R.id.btnAgregarIngrediente);

        Button btnBuscar =
                findViewById(R.id.btnBuscar);

        launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == RESULT_OK) {

                        Intent data = result.getData();

                        if (data != null) {

                            Uri uri = data.getData();

                            if (uri != null) {

                                imgFoto.setImageURI(uri);

                                rutaFoto =
                                        uri.toString();
                            }
                        }
                    }
                }
        );

        btnFoto.setOnClickListener(
                v -> seleccionarFoto()
        );

        btnAgregar.setOnClickListener(v -> {

            String ingrediente =
                    txtIngrediente
                            .getText()
                            .toString();

            String cantidad =
                    txtCantidad
                            .getText()
                            .toString();

            ingredientes.add(
                    new String[]{
                            ingrediente,
                            cantidad
                    }
            );

            txtIngrediente.setText("");
            txtCantidad.setText("");

            Toast.makeText(
                    this,
                    "Ingrediente agregado",
                    Toast.LENGTH_SHORT
            ).show();
        });

        btnGuardar.setOnClickListener(
                v -> guardarReceta()
        );

        btnBuscar.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    BuscarActivity.class
            );

            startActivity(intent);
        });
    }

    private void seleccionarFoto() {

        Intent intent =
                new Intent(Intent.ACTION_PICK);

        intent.setType("image/*");

        launcher.launch(intent);
    }

    private void guardarReceta() {

        long recetaId =
                dbHelper.guardarReceta(
                        txtNombre.getText().toString(),
                        txtPreparacion.getText().toString(),
                        rutaFoto
                );

        for (String[] item : ingredientes) {

            dbHelper.guardarIngrediente(
                    (int) recetaId,
                    item[0],
                    item[1]
            );
        }

        Toast.makeText(
                this,
                "Receta guardada",
                Toast.LENGTH_LONG
        ).show();
    }
}