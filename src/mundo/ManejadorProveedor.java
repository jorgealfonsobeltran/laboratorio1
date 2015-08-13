/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mundo;

import anotaciones.Driver;

/**
 *
 * @author G40
 */
public class ManejadorProveedor implements ProveedorI{

    public ManejadorProveedor() {
    }

    @Override
    public ManejadorProveedor crearPoveedor() {
        ManejadorProveedor  manejadorProveedor = null;
        ProveedorI proveedorI = (ProveedorI)Driver.instanciar(ProveedorI.class);
        return manejadorProveedor;
    }

    @Override
    public void consultarPoveedor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void eliminarPoveedor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void modificarPoveedor() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
}
