package com.resuadam2.frgtelefonos;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {
    final static String DB_NAME="telefonos";
    final static int DB_VERSION=1;
    final static String TABLE_NAME="llamadas";
    final static String COL_ID="_id";
    final static String COL_NUM_ORIGEN="numOrigen";
    final static String COL_NUM_DESTINO="numDestino";
    final static String COL_TIPO="tipo"; // entrante, saliente, perdida

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="CREATE TABLE "+TABLE_NAME+" ("+
                COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COL_NUM_ORIGEN+" INTEGER NOT NULL, "+
                COL_NUM_DESTINO+" INTEGER NOT NULL, "+
                COL_TIPO+" string NOT NULL)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP "+TABLE_NAME);
        onCreate(db);
    }

    public DBManager(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void insertarLlamada(int numOrigen, int numDestino, String tipo) {
        SQLiteDatabase db=getWritableDatabase();
        String sql="INSERT INTO "+TABLE_NAME+" ("+
                COL_NUM_ORIGEN+", "+COL_NUM_DESTINO+", "+COL_TIPO+") VALUES ("+
                numOrigen+", "+numDestino+", '"+tipo+"')";
        db.execSQL(sql);
    }

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
}
