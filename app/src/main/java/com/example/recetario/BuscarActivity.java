package com.example.recetario;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class BuscarActivity extends AppCompatActivity {

    DBHelper dbHelper;
    EditText txtBuscar;
    ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar);
        dbHelper = new DBHelper(this);
        txtBuscar = findViewById(R.id.txtBuscar);
        lista = findViewById(R.id.lista);
        findViewById(R.id.btnNombre)
                .setOnClickListener(v -> buscarNombre());
        findViewById(R.id.btnIngrediente)
                .setOnClickListener(v -> buscarIngrediente());
    }

    private void buscarNombre() {

        Cursor c = dbHelper.buscarPorNombre(
                txtBuscar.getText().toString()
        );

        mostrar(c);
    }

    private void buscarIngrediente() {

        Cursor c = dbHelper.buscarPorIngrediente(
                txtBuscar.getText().toString()
        );

        mostrar(c);
    }

    private void mostrar(Cursor c) {

        ArrayList<String> datos = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        while (c.moveToNext()) {

            ids.add(
                    c.getInt(
                            c.getColumnIndexOrThrow("id")
                    )
            );

            datos.add(
                    c.getString(
                            c.getColumnIndexOrThrow("nombre")
                    )
            );
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_list_item_1,
                        datos
                );

        lista.setAdapter(adapter);

        lista.setOnItemClickListener((parent, view, position, id) -> {

            int recetaId = ids.get(position);

            Intent intent = new Intent(
                    BuscarActivity.this,
                    DetalleRecetaActivity.class
            );

            intent.putExtra("receta_id", recetaId);

            startActivity(intent);
        });
    }
}