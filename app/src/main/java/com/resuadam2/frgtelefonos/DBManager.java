package com.resuadam2.frgtelefonos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
    final static String DB_NAME="telefonos"; // nombre de la BD
    final static int DB_VERSION=1; // versión de la BD
    final static String TABLE_NAME="llamadas"; // nombre de la tabla
    final static String COL_ID="_id"; // nombre de la columna que es la clave primaria
    final static String COL_NUM_ORIGEN="numOrigen"; // nombre de la columna que guarda el nº de teléfono origen
    final static String COL_NUM_DESTINO="numDestino"; // nombre de la columna que guarda el nº de teléfono destino
    final static String COL_TIPO="tipo"; // (entrante)*, saliente, perdida (*no se usa)

    /**
     * Método que se invoca cuando se crea la BD
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE "+TABLE_NAME+" ("+
                COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_NUM_ORIGEN+" INTEGER NOT NULL, "+
                COL_NUM_DESTINO+" INTEGER NOT NULL, "+
                COL_TIPO+" string NOT NULL)";
        db.execSQL(sql);
    }

    /**
     * Método que se invoca cuando se actualiza la BD
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP "+TABLE_NAME);
        onCreate(db);
    }

    /*+
        * Constructor
     */
    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Método que inserta una llamada en la BD
     * @param numOrigen número de teléfono origen
     * @param numDestino número de teléfono destino
     * @param tipo tipo de llamada (entrante*, saliente, perdida) (*no se usa)
     */
    public void insertarLlamada(int numOrigen, int numDestino, String tipo) {
        SQLiteDatabase db=getWritableDatabase();
        String sql="INSERT INTO "+TABLE_NAME+" ("+
                COL_NUM_ORIGEN+", "+COL_NUM_DESTINO+", "+COL_TIPO+") VALUES ("+
                numOrigen+", "+numDestino+", '"+tipo+"')";
        db.execSQL(sql);
    }

    /**
     * Método que devuelve todas las llamadas de la BD en un cursor
     * @return el cursor
     */
    public Cursor getLlamadas() {
        SQLiteDatabase db=getReadableDatabase();
        String sql="SELECT * FROM "+TABLE_NAME;
        return db.rawQuery(sql,null);
    }

    /**
     * Metodo que devuelve todas las llamadas en las que está involucrado un número de teléfono
     * ya sean como origen o como destino, además del tipo de llamada
     * @param numTelefono
     */
    public Cursor getLlamadas(int numTelefono) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " +
                COL_NUM_ORIGEN + "=" + numTelefono + " OR " +
                COL_NUM_DESTINO + "=" + numTelefono;
        return db.rawQuery(sql, null);
    }

    /**
     * Método que borra todas las llamadas de la BD
     */
    public void borrarLlamadas() {
        SQLiteDatabase db=getWritableDatabase();
        String sql="DELETE FROM "+TABLE_NAME;
        db.execSQL(sql);
    }

    /**
     * Método que devuelve el número de llamadas salientes de un número de teléfono
     * @param numTelefono el número de teléfono
     * @return el número de llamadas salientes
     */
    public int getCantSalientes(int numTelefono) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " +
                COL_NUM_ORIGEN + "=" + numTelefono;
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c.getInt(0);
    }


    /**
     * Método que devuelve el número de llamadas entrantes de un número de teléfono
     * @param numTelefono el número de teléfono
     * @return el número de llamadas entrantes
     */
    public int getCantEntrantes(int numTelefono) {
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE " +
                COL_NUM_DESTINO + "=" + numTelefono;
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        return c.getInt(0);
    }
}
