package com.resuadam2.frgtelefonos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class FrgTelefono extends Fragment {
    Telefono telefono;
    private OnFrgTelefono listener=null;
    private TextView tvNumeroPropio, tvInfo;
    private EditText etNumeroDestino;
    private Button bLlamarColgar;

    public Telefono getTelefono() {
        return telefono;
    }

    public interface OnFrgTelefono {
        boolean llamar(Telefono telefonoOrigen, int numTelefonoDestino);
        void colgar(Telefono telefonoOrigen, int numeroTelefonoDestino);
    }

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
    public void setOnFrgTelefono(Telefono telefono, OnFrgTelefono listener) {
        this.telefono=telefono;
        this.listener=listener;
        actualizarUI();
    }
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
    private void Colgar() {
        setEstoyLibre();
        listener.colgar(telefono,telefono.getNumeroDestino());
    }
    public boolean llaman(Telefono telefonoQueLlama) {
        if(telefono.estoyHablando()) return false;
        setEmpiezaLlamada(telefonoQueLlama.getNumero(),false);
        return true;
    }
    public void cuelgan() {
        if(telefono.estoyHablando()) setEstoyLibre();
    }
    private void setEmpiezaLlamada(int numeroDestino, boolean inicioYo) {
        telefono.setEmpiezaLlamada(numeroDestino,inicioYo);
        actualizarUI();
    }
    private void setEstoyLibre() {
        telefono.setEstoyLibre();
        actualizarUI();
    }
    private void actualizarUI() {
        tvNumeroPropio.setText(telefono.toString());
        etNumeroDestino.setText("");
        etNumeroDestino.setEnabled(!telefono.estoyHablando());
        //bLlamarColgar.setBackgroundColor(telefono.estoyHablando() ?Color.RED:Color.GREEN);
        bLlamarColgar.setBackgroundResource(telefono.estoyHablando()
                ?android.R.drawable.sym_call_incoming:android.R.drawable.ic_menu_call);
    }
}
