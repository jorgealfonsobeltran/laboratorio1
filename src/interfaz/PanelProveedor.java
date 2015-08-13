/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaz;

import static interfaz.PanelMuebles.NUEVO;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import mundo.ManejadorProveedor;
import mundo.ProveedorI;

/**
 *
 * @author G40
 */
public class PanelProveedor extends JPanel implements ActionListener {

    private ManejadorProveedor manager;

    public PanelProveedor( ManejadorProveedor manager) {
        this.manager=manager;
        JPanel opcionesInterface = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = -1;

        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.NORTH;

        JButton interfaceBoton = new JButton("Log Anotación Interface");
        interfaceBoton.addActionListener(this);
        interfaceBoton.setActionCommand(NUEVO);
        opcionesInterface.add(interfaceBoton, gbc);
        add(opcionesInterface, BorderLayout.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ProveedorI pi = manager.crearPoveedor();
    }

}
