package com.resuadam2.frgtelefonos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/*+
    * Fragmento que representa un teléfono
    * Como un componente aislado, no tiene acceso a la BD y es reutilizable
    */
public class FrgTelefono extends Fragment {
    Telefono telefono; // el teléfono que representa este fragmento
    private OnFrgTelefono listener=null; // el listener que se invocará cuando se pulse el botón
    private TextView tvNumeroPropio, tvInfo; // el TextView que muestra el nº de teléfono
    private EditText etNumeroDestino; // el EditText que permite introducir el nº de teléfono destino
    private Button bLlamarColgar; // el botón que permite llamar o colgar

    public Telefono getTelefono() {
        return telefono;
    }

    /**
     * Interfaz que debe implementar la actividad que contiene el fragmento
     */
    public interface OnFrgTelefono {
        boolean llamar(Telefono telefonoOrigen, int numTelefonoDestino);
        void colgar(Telefono telefonoOrigen, int numeroTelefonoDestino);
    }

    /**
     * Método que se invoca cuando se crea el fragmento
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI, or null.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v=inflater.inflate(R.layout.frg_telefono, container, false);
        tvNumeroPropio=v.findViewById(R.id.tvNumeroPropio);
        etNumeroDestino=v.findViewById(R.id.etNumeroDestino);
        bLlamarColgar=v.findViewById(R.id.bLlamarColgar);
        bLlamarColgar.setOnClickListener(view -> {
            if (listener!=null) if(telefono.estoyHablando()) Colgar(); else Llamar();
        });
        return v;
    }

    /**
     * Método que se invoca cuando se crea la actividad que contiene el fragmento
     * @param telefono el teléfono que representa este fragmento
     * @param listener el listener que se invocará cuando se pulse el botón
     */
    public void setOnFrgTelefono(Telefono telefono, OnFrgTelefono listener) {
        this.telefono=telefono;
        this.listener=listener;
        actualizarUI();
    }

    /**
     * Método que se invoca cuando se pulsa el botón y no se está hablando
     */
    private void Llamar() {
        int numeroDestino;
        try { numeroDestino = Integer.parseInt(etNumeroDestino.getText().toString()); }
        catch (NumberFormatException x) {
            etNumeroDestino.setError(getString(R.string.num_telefono_incorrecto));
            return;
        }
        if(numeroDestino==telefono.getNumero()) {
            etNumeroDestino.setError(getString(R.string.num_telefono_incorrecto));
            return;
        }
        if (listener.llamar(telefono,numeroDestino))
            setEmpiezaLlamada(numeroDestino, true);
    }

    /**
     * Método que se invoca cuando se pulsa el botón y se está hablando
     */
    private void Colgar() {
        setEstoyLibre();
        listener.colgar(telefono,telefono.getNumeroDestino());
    }

    /**
     * Método que se invoca cuando se quiere llamar a este teléfono
     * @param telefonoQueLlama el teléfono que llama
     * @return true si se puede llamar, false en caso contrario
     */
    public boolean llaman(Telefono telefonoQueLlama) {
        if(telefono.estoyHablando()) return false;
        setEmpiezaLlamada(telefonoQueLlama.getNumero(),false);
        return true;
    }

    /**
     * Método que se invoca cuando se quiere colgar este teléfono
     */
    public void cuelgan() {
        if(telefono.estoyHablando()) setEstoyLibre();
    }

    /**
     * Método que se invoca cuando se quiere llamar desde este teléfono
     * @param numeroDestino el número de teléfono al que se quiere llamar
     * @param inicioYo true si el teléfono que llama es este, false en caso contrario
     */
    private void setEmpiezaLlamada(int numeroDestino, boolean inicioYo) {
        telefono.setEmpiezaLlamada(numeroDestino,inicioYo);
        actualizarUI();
    }

    /**
     * Setter que se invoca cuando se quiere colgar desde este teléfono
     */
    private void setEstoyLibre() {
        telefono.setEstoyLibre();
        actualizarUI();
    }

    /**
     * Método que actualiza la interfaz del fragmento
     */
    private void actualizarUI() {
        tvNumeroPropio.setText(telefono.toString());
        etNumeroDestino.setText("");
        etNumeroDestino.setEnabled(!telefono.estoyHablando());
        //bLlamarColgar.setBackgroundColor(telefono.estoyHablando() ?Color.RED:Color.GREEN);
        bLlamarColgar.setBackgroundResource(telefono.estoyHablando()
                ?android.R.drawable.sym_call_incoming:android.R.drawable.ic_menu_call);
    }
}
