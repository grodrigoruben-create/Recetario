package com.example.recetario;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetalleRecetaActivity extends AppCompatActivity {

    DBHelper dbHelper;

    TextView txtNombre;
    TextView txtPreparacion;
    TextView txtIngredientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_receta);

        dbHelper = new DBHelper(this);

        txtNombre = findViewById(R.id.txtNombre);
        txtPreparacion = findViewById(R.id.txtPreparacion);
        txtIngredientes = findViewById(R.id.txtIngredientes);

        int recetaId = getIntent().getIntExtra("receta_id", -1);

        if (recetaId != -1) {
            mostrarReceta(recetaId);
        }
    }

    private void mostrarReceta(int recetaId) {
        Cursor receta = dbHelper.obtenerReceta(recetaId);

        if (receta != null && receta.moveToFirst()) {
            String nombre = receta.getString(
                    receta.getColumnIndexOrThrow("nombre")
            );

            String preparacion = receta.getString(
                    receta.getColumnIndexOrThrow("preparacion")
            );

            txtNombre.setText(nombre);
            txtPreparacion.setText(preparacion);
            receta.close();
        }

        Cursor ingredientes = dbHelper.obtenerIngredientes(recetaId);

        if (ingredientes != null) {
            StringBuilder listaIngredientes = new StringBuilder();

            while (ingredientes.moveToNext()) {
                String ingrediente = ingredientes.getString(
                        ingredientes.getColumnIndexOrThrow("ingrediente")
                );

                String cantidad = ingredientes.getString(
                        ingredientes.getColumnIndexOrThrow("cantidad")
                );

                listaIngredientes
                        .append("• ")
                        .append(cantidad)
                        .append(" - ")
                        .append(ingrediente)
                        .append("\n");
            }

            txtIngredientes.setText(listaIngredientes.toString());
            ingredientes.close();
        }
    }
}
