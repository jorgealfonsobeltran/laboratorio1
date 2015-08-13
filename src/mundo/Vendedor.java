/**
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * $Id: Vendedor.java,v 1.1 ga.sotelo69 Exp $
 * Universidad de los Andes (Bogotá - Colombia)
 * Departamento de Ingeniería de Sistemas y Computación
 * Licenciado bajo el esquema Academic Free License version 2.1
 *
 * Ejercicio: Taller 1 - anotaciones
 * Autor: German Augusto Sotelo
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */
package mundo;

import anotaciones.Init;
import anotaciones.Log;
import anotaciones.NoInit;
import anotaciones.PostConstructor;

/**
 * Entidad que representa a un vendedor de MLA
 * @author Germán Sotelo
 */
@Init(String="-- Vacio --")
public class Vendedor
{

    /**
     * Id del vendedor. Tiene que ser único.
     */
    @NoInit
    private int id;

    /**
     * Nombres del vendedor.
     */
    private String nombres;

    /**
     * Apellidos del vendedor
     */
    private String apellidos;

    /**
     * Sexo del vendedor
     */
    private String sexo;

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }
    
    public int getId() {
        return id;
    }

    @Log
    protected void setId(int id) {
        this.id = id;
    }

    @PostConstructor
    public void postConstructor(){
        boolean valido = true;
        
        //Valida que los nombres no sean nulos o vacios
        valido &= (this.nombres != null && !this.nombres.isEmpty());
        
        //Valida que los apellidos no sean nulos o vacios
        valido &= (this.apellidos != null && !this.apellidos.isEmpty());
        
        //Valida que el valor de sexo no sea nulo o vacio
        valido &= (this.sexo != null && !this.sexo.isEmpty());

        if (valido) {
            System.out.println("La información del vendedor es valida"); 
        }
        else{
            System.out.println("La información del vendedor no es valida");
        } 
    }
}
