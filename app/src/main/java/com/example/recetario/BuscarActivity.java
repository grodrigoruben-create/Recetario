package com.example.recetario;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.content.Intent;
import android.widget.Toast;

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

        listacompleta();

        findViewById(R.id.btnNombre).setOnClickListener(v -> buscarNombre());
        findViewById(R.id.btnIngrediente).setOnClickListener(v -> buscarIngrediente());
    }

    private void listacompleta() {

        ArrayList<Receta> datos = dbHelper.obtenerTodasLasRecetas();

        if (datos == null || datos.isEmpty()) {
            Toast.makeText(this, "No se encontraron recetas", Toast.LENGTH_SHORT).show();
        } else {
            ArrayList<String> nombres = new ArrayList<>();
            ArrayList<Integer> ids = new ArrayList<>();

            for (Receta r : datos) {
                nombres.add(r.getNombre());
                ids.add(r.getId());
            }

            // Ahora sí creamos el adapter
            ArrayAdapter<String> adapter = new ArrayAdapter<>(
                    this,
                    android.R.layout.simple_list_item_1,
                    nombres
            );
            lista.setAdapter(adapter);

            // Replicamos el clic para que también funcione al mostrar la lista completa
            lista.setOnItemClickListener((parent, view, position, id) -> {
                int recetaId = ids.get(position);
                Intent intent = new Intent(BuscarActivity.this, DetalleRecetaActivity.class);
                intent.putExtra("receta_id", recetaId);
                startActivity(intent);
            });
        }
    }

    private void buscarNombre() {
        Cursor c = dbHelper.buscarPorNombre(txtBuscar.getText().toString());
        mostrar(c);
    }

    private void buscarIngrediente() {
        Cursor c = dbHelper.buscarPorIngrediente(txtBuscar.getText().toString());
        // 3. Corregido: Simplemente llamamos a mostrar(c), igual que en buscarNombre
        mostrar(c);
    }

    private void mostrar(Cursor c) {
        ArrayList<String> datos = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        if (c.getCount() == 0) {
            Toast.makeText(this, "No se encontraron recetas", Toast.LENGTH_SHORT).show();
            lista.setAdapter(null);
            return;
        }

        while (c.moveToNext()) {
            ids.add(c.getInt(c.getColumnIndexOrThrow("id")));
            datos.add(c.getString(c.getColumnIndexOrThrow("nombre")));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                datos
        );

        lista.setAdapter(adapter);

        lista.setOnItemClickListener((parent, view, position, id) -> {
            int recetaId = ids.get(position);
            Intent intent = new Intent(BuscarActivity.this, DetalleRecetaActivity.class);
            intent.putExtra("receta_id", recetaId);
            startActivity(intent);
        });
    }
}