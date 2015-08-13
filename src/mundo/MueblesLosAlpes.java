/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id: MueblesLosAlpes.java,v 1.1 ga.sotelo69 Exp $
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 * Licenciado bajo el esquema Academic Free License version 2.1
 *
 * Ejercicio: Taller 1 - anotaciones
 * Autor: German Augusto Sotelo
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package mundo;

import anotaciones.Cargar;

/**
 * Punto de entrada a la capa de logica de negocio
 * @author Germán Sotelo
 */
public class MueblesLosAlpes {

    /**
     * Manajador de la lista de muebles
     */
    @Cargar
    private ManejadorMuebles muebles;

    /**
     * Manejador de la lista de vendedores
     */
    @Cargar
    private ManejadorVendedores vendedores;

    @Cargar
    private ManejadorProveedor proveedores;
    
    public MueblesLosAlpes() {
        System.out.println("Constructor ");
    }

    /**
     * Retorna el manejador de la lista de muebles
     * @return
     */
    public ManejadorMuebles getManejadorMuebles() {
        return muebles;
    }

    /**
     * Retorna el manejador de la lista de vendedores
     * @return
     */
    public ManejadorVendedores getManejadorVendedores() {
        return vendedores;
    }

    /**
     * Retorna el manejador de la lista de vendedores
     * @return
     */
    public ManejadorProveedor getManejadorProveedor() {
        return proveedores;
    }

    /**
     * Metodo de prueba de la anotación PreConstructor
     */
    public static void antesDelConstructor(){
        System.out.println("Antes del constructor 1");
    }

    /**
     * Metodo de prueba de la anotación PostConstructor
     */
    public void despuesDelConstructor(){
        System.out.println("Despues del constructor");
    }
    
}
