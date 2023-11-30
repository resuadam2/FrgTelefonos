package com.resuadam2.frgtelefonos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements FrgTelefono.OnFrgTelefono {

    /*
     Solución para un nº estático de teléfonos (fragmentos)
     TODO: mejorar leyendo teléfonos de la BD
     en ese caso habría que crear una estructura dinámica (p.e. HashMap)
     y acceder por su nº de teléfono no necesariamente consecutivo
     */
    int ids[]={R.id.frgT1,R.id.frgT2,R.id.frgT3,R.id.frgT4};
    FrgTelefono[] listaFrgTelefonos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm=getSupportFragmentManager();
        listaFrgTelefonos =new FrgTelefono[ids.length];
        int i=0;
        for(int id:ids) {
            FrgTelefono frgTelefono=(FrgTelefono)fm.findFragmentById(id);
            listaFrgTelefonos[i++]=frgTelefono;
            frgTelefono.setOnFrgTelefono(new Telefono(i),this);
        }
    }
    @Override
    public boolean llamar(Telefono telefonoOrigen, int numeroDestino) {
        boolean destinoDisponible=true;
        FrgTelefono frgTelefonoDestino;
        if((frgTelefonoDestino=getFrgTelefono(numeroDestino))!=null)
            destinoDisponible=frgTelefonoDestino.llaman(telefonoOrigen);
        // TODO: else if(numeroDestino (externo) está comunicando) destinoDisponible=false
        String mensaje= telefonoOrigen.getNumero() +" >>> "+numeroDestino;
        mensaje+=" >>> "+getString(destinoDisponible?R.string.aceptaLlamada:R.string.comunicando);
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
        return destinoDisponible;
    }
    @Override
    public void colgar(Telefono telefonoOrigen, int numeroDestino) {
        FrgTelefono frgTelefono;
        if((frgTelefono=getFrgTelefono(numeroDestino))!=null)
            frgTelefono.cuelgan();
        String mensaje=telefonoOrigen.getNumero()+" corta a "+numeroDestino;
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
    }
    public FrgTelefono getFrgTelefono(int numero) {
        if (numero<1 || numero>ids.length) return null;
        return listaFrgTelefonos[numero-1];
    }
}