package healthcalc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import healthcalc.Person;
import healthcalc.PersonImpl;
import healthcalc.Gender;
import healthcalc.HealthCalc;
import healthcalc.exceptions.InvalidHealthDataException;
import healthcalc.view.ViewIBW;

public class CtrIBW implements ActionListener {
    private HealthCalc modeloIBW;
    private ViewIBW vistaIBW;

    public CtrIBW(HealthCalc modeloIBW, ViewIBW vistaIBW) {
        this.modeloIBW = modeloIBW;
        this.vistaIBW = vistaIBW;
    }

    // Pulsar calcular
    @Override
    public void actionPerformed(ActionEvent e) {
    
    	//Set up
    	// Limpieza de lo anterior
        vistaIBW.clearErrors();
        
        // Lectura de datos
        String h = vistaIBW.getHeightInput();
    
        boolean Error = false;

//=======================================================================================================================
        
        // Comrpobamos que el campo no está vacío
        if (h.isEmpty()) {
            vistaIBW.setHeightError("Error: El campo no puede estar vacío.");
            Error = true;
        }
        if (Error) return; 

//=======================================================================================================================
        
        // Comprobamos que esté en cm u no en metros
        double heightCm = 0;
        try {
            heightCm = Double.parseDouble(h);
        } catch (NumberFormatException ex) {
            vistaIBW.setHeightError("Error: Introduzca solo números.");
            if (h.contains(",")) {
        	    vistaIBW.setHeightError("Error: Use '.' en lugar de ',' para los decimales.");
            }
            Error = true;
        }
        if (Error) return;

//=======================================================================================================================

        // No se introduce un valor negativo
        if (heightCm < 0) {
            vistaIBW.setHeightError("Error: El valor no puede ser negativo.");
            Error = true;
        }
        if (Error) return;

//=======================================================================================================================

        // Lectura de género
        Gender gender = Gender.MALE;
        if (!vistaIBW.isMaleSelected()) {
            gender = Gender.FEMALE;
        }
        
//=======================================================================================================================
               
        // Valores fisiológicos
        try {
        	Person person = new PersonImpl(0, heightCm, gender, 0);
        	double ibw = modeloIBW.idealBodyWeight(person);
        	vistaIBW.setResult(String.format("Resultado IBW: %.2f", ibw));
        } catch (InvalidHealthDataException ex) {
            String errorMsg = ex.getMessage().toLowerCase();
            
            if (errorMsg.contains("altura") || errorMsg.contains("height") || errorMsg.contains("range")) {
            	vistaIBW.setHeightError("Error: Fuera de límites biológicos (30-300 cm).");
            } else {
            	vistaIBW.setHeightError("Error: " + ex.getMessage());
            }
        }
        
    }
}