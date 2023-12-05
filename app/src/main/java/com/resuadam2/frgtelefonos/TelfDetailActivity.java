package com.resuadam2.frgtelefonos;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activity que muestra el detalle de un teléfono (nº de teléfono y llamadas)
 * Se invoca desde el MainActivity
 */
public class TelfDetailActivity extends AppCompatActivity {

        TextView telfDetail;
        ListView lvDetails;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_telf_detail);
            telfDetail = findViewById(R.id.tvTelf);
            lvDetails = findViewById(R.id.lvLlamadas);

            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                int numTelf = extras.getInt("telefono");
                telfDetail.setText("Teléfono: " + numTelf);
               cargarLlamadas(numTelf);
            } else {
                Log.e("TelfDetailActivity", "No se ha recibido el nº de teléfono");
                finish();
            }
        }

    /**
     * Metodo que carga las llamadas de un número de teléfono en el ListView
     * Las llamadas salientes se muestran en verde
     * Las llamadas entrantes se muestran en naranja
     * Las llamadas entrantes se corresponden con las entrantes que tienen como destino el nº de teléfono
     * Las llamadas perdidas se muestran en rojo
     * @param numTelf nº de teléfono del que se quieren cargar las llamadas
     */
    private void cargarLlamadas(int numTelf) {
        DBManager dbManager = new DBManager(this);
        Cursor cursor = dbManager.getLlamadas(numTelf);
        if (cursor.getCount() == 0) {
            lvDetails.setAdapter(null);
        } else {
            lvDetails.setAdapter(new CursorAdapter(this, cursor, 0)  {
                @Override
                public View newView(Context context, Cursor cursor, ViewGroup parent) {
                    return getLayoutInflater().inflate(android.R.layout.simple_list_item_1, parent, false);
                }

                @Override
                public void bindView(View view, Context context, Cursor cursor) {
                    TextView tv = (TextView) view;
                    @SuppressLint("Range") String tipo = cursor.getString(cursor.getColumnIndex(DBManager.COL_TIPO));
                    @SuppressLint("Range") int numOrigen = cursor.getInt(cursor.getColumnIndex(DBManager.COL_NUM_ORIGEN));
                    @SuppressLint("Range") int numDestino = cursor.getInt(cursor.getColumnIndex(DBManager.COL_NUM_DESTINO));
                    String texto = "";
                    if(tipo.equals("saliente") && numDestino == numTelf) {
                        tipo = "entrante";
                        tv.setTextColor(getResources().getColor(R.color.entrante));
                    } else if(tipo.equals("perdida")) {
                        tv.setTextColor(getResources().getColor(R.color.perdida));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.saliente));
                    }
                    texto = tipo + " " + numOrigen + " >>> " + numDestino;
                    tv.setText(texto);
                }
            });
        }
    }
}
