package com.resuadam2.frgtelefonos;

/**
 * Clase que representa un teléfono
 */
public class Telefono {
    public enum Estado { LIBRE, LLAMADA_INICIADA, LLAMADA_RECIBIDA }; // los estados posibles
    private int numero, numeroDestino; // el número de teléfono y el número de teléfono destino
    private Estado estado; // el estado del teléfono

    /**
     * Constructor de la clase
     * @param numero el número de teléfono
     */
    public Telefono(int numero) {
        this.numero=numero;
        estado=Estado.LIBRE;
    }
    public int getNumero() { return numero; }
    public int getNumeroDestino() { return numeroDestino; }
    public boolean estoyHablando() { return (estado!=Estado.LIBRE); }

    /**
     * Método que se invoca cuando se quiere llamar desde este teléfono
     * @param numeroDestino el número de teléfono al que se quiere llamar
     * @param inicioYo true si el teléfono que llama es este, false en caso contrario
     */
    public void setEmpiezaLlamada(int numeroDestino, boolean inicioYo) {
        this.numeroDestino=numeroDestino;
        estado=(inicioYo?Telefono.Estado.LLAMADA_INICIADA:Telefono.Estado.LLAMADA_RECIBIDA);
    }

    /**
     * Setter que se invoca cuando se quiere colgar desde este teléfono
     */
    public void setEstoyLibre() { estado=Telefono.Estado.LIBRE; }

    /*+
        * Método que devuelve una cadena con el formato: <nº teléfono> <estado> <nº teléfono destino>
        El estaod puede ser: > (llamada iniciada), < (llamada recibida) o vacío (libre)
     */
    @Override
    public String toString() {
        String str=numero+"";
        if(estoyHablando())
            str+=(estado==Estado.LLAMADA_INICIADA?" > ":" < ")+numeroDestino;
        return str;
    }
}