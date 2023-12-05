package com.resuadam2.frgtelefonos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements FrgTelefono.OnFrgTelefono {

    /*
     Solución para un nº estático de teléfonos (fragmentos)
     Se puede mejorar leyendo teléfonos de la BD
     en ese caso habría que crear una estructura dinámica (p.e. HashMap)
     y acceder por su nº de teléfono no necesariamente consecutivo
     */
    int ids[]={R.id.frgT1,R.id.frgT2,R.id.frgT3,R.id.frgT4}; // los ids de los fragmentos
    FrgTelefono[] listaFrgTelefonos; // la lista de fragmentos
    ListView lvTelefonos; // el ListView que muestra el historial de llamadas
    ArrayList<String> listaLlamadas; // la lista de llamadas

    ArrayAdapter<String> adapter; // el adaptador del ListView

    Button btnBorrarHistorial, btnDetails; // los botones de borrar historial y detalles
    Spinner spnTelefonos; // el Spinner que muestra los teléfonos disponibles

    DBManager dbManager; // el gestor de la BD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbManager=new DBManager(this);

        listaFrgTelefonos =new FrgTelefono[ids.length];
        lvTelefonos=findViewById(R.id.lvTelefonos);
        FragmentManager fm=getSupportFragmentManager();
        int i=0;
        for(int id:ids) {
            FrgTelefono frgTelefono=(FrgTelefono)fm.findFragmentById(id);
            listaFrgTelefonos[i++]=frgTelefono;
            frgTelefono.setOnFrgTelefono(new Telefono(i),this);
        }
        btnDetails = findViewById(R.id.btnTelfDetail);

        spnTelefonos = findViewById(R.id.spnTelfs);

        spnTelefonos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, getNumsTelfs()));
        spnTelefonos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                btnDetails.setEnabled(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                btnDetails.setEnabled(false);
            }
        });

        btnDetails.setOnClickListener(v -> {
            Intent intent = new Intent(this, TelfDetailActivity.class);
            intent.putExtra("telefono", Integer.parseInt(spnTelefonos.getSelectedItem().toString()));
            startActivity(intent);
        });

        listaLlamadas = llamadas();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listaLlamadas);
        lvTelefonos.setAdapter(adapter);

        btnBorrarHistorial = findViewById(R.id.btnDeleteHistory);
        btnBorrarHistorial.setOnClickListener(v -> {
            dbManager.borrarLlamadas();
            listaLlamadas.clear();
            listaLlamadas.addAll(llamadas());
            adapter.notifyDataSetChanged();
        });

    }

    /**
     * Método que devuelve una lista de cadenas con el formato: Telf: <nº teléfono> E: <nº entrantes> S: <nº salientes>
     * @return la lista de cadenas
     */
    @SuppressLint("Range")
    private ArrayList<String> llamadas() {
        ArrayList<String> lista=new ArrayList<>();
        for(int i = 0; i < listaFrgTelefonos.length; i++) {
            int telefono = listaFrgTelefonos[i].getTelefono().getNumero();
            lista.add("Telf: " + telefono + " E: " +
                    dbManager.getCantEntrantes(telefono) + " S: " + dbManager.getCantSalientes(telefono));
        }
        return lista;
    }

    /**
     * Método que devuelve una lista de cadenas con el formato: <nº teléfono>
     * @return la lista de cadenas
     */
    private ArrayList<String> getNumsTelfs() {
        ArrayList<String> lista=new ArrayList<>();
        for(int i = 0; i < listaFrgTelefonos.length; i++) {
            int telefono = listaFrgTelefonos[i].getTelefono().getNumero();
            lista.add(String.valueOf(telefono));
        }
        return lista;
    }

    /**
     * Método que se invoca cuando se pulsa el botón y no se está hablando
     * @param telefonoOrigen el teléfono que llama
     * @param numeroDestino el número al que se llama
     * @return true si se puede llamar, false en caso contrario
     */
    @Override
    public boolean llamar(Telefono telefonoOrigen, int numeroDestino) {
        boolean destinoDisponible=true;
        FrgTelefono frgTelefonoDestino;
        if((frgTelefonoDestino = getFrgTelefono(numeroDestino)) != null){
            destinoDisponible = frgTelefonoDestino.llaman(telefonoOrigen);
            if(destinoDisponible) {
                dbManager.insertarLlamada(telefonoOrigen.getNumero(), numeroDestino, "saliente");
            } else {
                dbManager.insertarLlamada(telefonoOrigen.getNumero(), numeroDestino, "perdida");
            }
        } else {
            dbManager.insertarLlamada(telefonoOrigen.getNumero(), numeroDestino, "saliente");
        }
        String mensaje= telefonoOrigen.getNumero() +" >>> "+numeroDestino;
        mensaje+=" >>> "+getString(destinoDisponible?R.string.aceptaLlamada:R.string.comunicando);
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
        listaLlamadas.clear();
        listaLlamadas.addAll(llamadas());
        adapter.notifyDataSetChanged();
        return destinoDisponible;
    }

    /**
     * Método que se invoca cuando se pulsa el botón y se está hablando y se quiere colgar
     * @param telefonoOrigen el teléfono que llama
     * @param numeroDestino el número al que se llama
     */
    @Override
    public void colgar(Telefono telefonoOrigen, int numeroDestino) {
        FrgTelefono frgTelefono;
        if((frgTelefono=getFrgTelefono(numeroDestino))!=null)
            frgTelefono.cuelgan();
        String mensaje=telefonoOrigen.getNumero()+" corta a "+numeroDestino;
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }

    /**
     * Método que devuelve el fragmento que corresponde a un número de teléfono
     * @param numero el número de teléfono
     * @return el fragmento correspondiente o null si no existe
     */
    public FrgTelefono getFrgTelefono(int numero) {
        if (numero<1 || numero>ids.length) return null;
        return listaFrgTelefonos[numero-1];
    }
}