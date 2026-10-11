package com.example.recetario;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "recetario.db";
    private static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE recetas (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "nombre TEXT," +
                        "preparacion TEXT," +
                        "foto TEXT)"
        );

        db.execSQL(
                "CREATE TABLE ingredientes (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "receta_id INTEGER," +
                        "ingrediente TEXT," +
                        "cantidad TEXT)"
        );
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase db,
            int oldVersion,
            int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS ingredientes");
        db.execSQL("DROP TABLE IF EXISTS recetas");

        onCreate(db);
    }

    public long guardarReceta(
            String nombre,
            String preparacion,
            String foto) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("nombre", nombre);
        values.put("preparacion", preparacion);
        values.put("foto", foto);

        return db.insert(
                "recetas",
                null,
                values
        );
    }

    public long guardarIngrediente(
            int recetaId,
            String ingrediente,
            String cantidad) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("receta_id", recetaId);
        values.put("ingrediente", ingrediente);
        values.put("cantidad", cantidad);

        return db.insert(
                "ingredientes",
                null,
                values
        );
    }

    public Cursor buscarPorNombre(String texto) {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM recetas WHERE nombre LIKE ?",
                new String[]{"%" + texto + "%"}
        );
    }

    public Cursor buscarPorIngrediente(String ingrediente) {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT r.* " +
                        "FROM recetas r " +
                        "INNER JOIN ingredientes i " +
                        "ON r.id = i.receta_id " +
                        "WHERE i.ingrediente LIKE ?",
                new String[]{"%" + ingrediente + "%"}
        );
    }
    public ArrayList<String> obtenerListaDeIngredientes(int recetaId) {
        ArrayList<String> listaIngredientes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT ingrediente, cantidad FROM ingredientes WHERE receta_id = ?",
                new String[]{String.valueOf(recetaId)}
        );

        // Recorremos el cursor para llenar el ArrayList
        if (cursor.moveToFirst()) {
            do {
                // Obtenemos los valores de las columnas (0 = ingrediente, 1 = cantidad)
                String nombreIngrediente = cursor.getString(0);
                String cantidad = cursor.getString(1);

                // Los combinamos y agregamos a la lista
                listaIngredientes.add(nombreIngrediente + " - " + cantidad);

            } while (cursor.moveToNext());
        }

        cursor.close(); // Siempre cierra el cursor para liberar memoria
        return listaIngredientes;
    }
    public ArrayList<Receta> obtenerTodasLasRecetas() {
        ArrayList<Receta> listaRecetas = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM recetas", null);

        if (cursor.moveToFirst()) {
            do {
                // Extraemos cada columna según su posición en la tabla
                int id = cursor.getInt(0);
                String nombre = cursor.getString(1);
                String preparacion = cursor.getString(2);
                String foto = cursor.getString(3);

                // Creamos el objeto y lo metemos a la lista
                Receta receta = new Receta(id, nombre, preparacion, foto);
                listaRecetas.add(receta);

            } while (cursor.moveToNext());
        }

        cursor.close();
        return listaRecetas;
    }
    public Cursor obtenerIngredientes(int recetaId) {

        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(
                "SELECT * FROM ingredientes WHERE receta_id = ?",
                new String[]{String.valueOf(recetaId)}
        );
    }
    public Cursor obtenerReceta(int id) {
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM recetas WHERE id = ?",
                new String[]{String.valueOf(id)}
        );
    }

}