package healthcalc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import healthcalc.Person;
import healthcalc.PersonImpl;
import healthcalc.HealthCalc;
import healthcalc.exceptions.InvalidHealthDataException;
import healthcalc.view.ViewBSA;

public class CtrBSA implements ActionListener {

	// El controlador guarda una referencia a su ventana y al modelo
	private  ViewBSA vistaBSA;
	private HealthCalc modeloBSA ;

	// La ventana y el modelo se inyectan al nacer el controlador
	public CtrBSA( ViewBSA vista, HealthCalc modelo) {
		this.vistaBSA = vista;
		this.modeloBSA = modelo;

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Lógica de respuesta al botón "Calcular"
		// Limpiamos todo lo que tengas de antes, así tenemos todo claro
		vistaBSA.clearErrors();
		
		// Creamos los inputs, pero los parseamos con el valueOf
		String h = vistaBSA.get_h_bas(); 
        String w =vistaBSA.get_w_bas();
        
        // Var estado errores
        boolean err = false;
        
        // Creo que no es necesario
        if (w.isEmpty()) {
            vistaBSA.setWeightError("Error: El campo no puede estar vacío.");
            err = true;
        }
        
        if (h.isEmpty()) {
            vistaBSA.setHeightError("Error: El campo no puede estar vacío.");
            err = true;
        }
        
        // Variables que uso
        double height = 0;
        double weight = 0;
        
        // ============ Vemos si los datos son válidos ============
        try {
            weight = Double.parseDouble(w);
        } catch (NumberFormatException ex) {
            vistaBSA.setWeightError("Error: Sólo puede introduzcir solo números.");
            err = true;
        }
        try {
            height = Double.parseDouble(h);
        } catch (NumberFormatException ex) {
            vistaBSA.setHeightError("Error: Sólo puede introduzcir solo números.");
            // ============ Vemos si tiene , ============
            if (w.contains(",")) {
                vistaBSA.setWeightError("Error: Usa '.' en lugar de ',' para decimales.");
                err = true;
            }
            err = true;
            
            if (h.contains(",")) {
                vistaBSA.setHeightError("Error: Usa '.' en lugar de ',' para decimales.");
                err = true;
            }
            err = true;
        }
        if (err) return;
        
        
        // ============ Vemos si los datos son negativos ============
        if (weight < 0) {
            vistaBSA.setWeightError("Error: El valor no puede ser negativo.");
            err = true;
        }
        if (height < 0) {
            vistaBSA.setHeightError("Error: El valor no puede ser negativo.");
            err = true;
        }
        if (err) return;


     // ============ Validamos límites fisiológicos ANTES de llamar al modelo ============
        if (weight < 1 || weight > 700) {
            vistaBSA.setWeightError("Error: Fuera de límites biológicos (1-700 kg).");
            err = true;
        }
        if (height < 30 || height > 300) {
            vistaBSA.setHeightError("Error: Fuera de límites biológicos (30-300 cm).");
            err = true;
        }
        if (err) return;

        
        // ============ Probamos que el resultado es posible ============
        try {
            Person person = new PersonImpl(weight, height, null, 0);
            double bsa = modeloBSA.bodySurfaceArea(person);
            String out = String.format("Resultado BSA: %.2f", bsa);
            
            vistaBSA.setResult(out);

        } catch (InvalidHealthDataException ex) {
            String error = ex.getMessage().toLowerCase();
            
            if (error.contains("peso") || error.contains("weight")) {
            	vistaBSA.setWeightError("Error inesperado: " + ex.getMessage());
            	vistaBSA.setHeightError("Error inesperado: " + ex.getMessage());
            }
        }
	}
}