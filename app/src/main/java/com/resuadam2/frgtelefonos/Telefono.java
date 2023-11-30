package com.resuadam2.frgtelefonos;

public class Telefono {
    public enum Estado { LIBRE, LLAMADA_INICIADA, LLAMADA_RECIBIDA };
    private int numero, numeroDestino;
    private Estado estado;
    public Telefono(int numero) {
        this.numero=numero;
        estado=Estado.LIBRE;
    }
    public int getNumero() { return numero; }
    public int getNumeroDestino() { return numeroDestino; }
    public boolean estoyHablando() { return (estado!=Estado.LIBRE); }
    public void setEmpiezaLlamada(int numeroDestino, boolean inicioYo) {
        this.numeroDestino=numeroDestino;
        estado=(inicioYo?Telefono.Estado.LLAMADA_INICIADA:Telefono.Estado.LLAMADA_RECIBIDA);
    }
    public void setEstoyLibre() { estado=Telefono.Estado.LIBRE; }
    @Override
    public String toString() {
        String str=numero+"";
        if(estoyHablando())
            str+=(estado==Estado.LLAMADA_INICIADA?" > ":" < ")+numeroDestino;
        return str;
    }
}