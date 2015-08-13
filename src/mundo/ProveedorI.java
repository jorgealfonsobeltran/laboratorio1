/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mundo;

import anotaciones.InterfaceDetector;

/**
 *
 * @author G40
 */
@InterfaceDetector
public interface ProveedorI {
    
    public ManejadorProveedor crearPoveedor();
    public void consultarPoveedor();
    public void eliminarPoveedor();
    public void modificarPoveedor();
    
}
