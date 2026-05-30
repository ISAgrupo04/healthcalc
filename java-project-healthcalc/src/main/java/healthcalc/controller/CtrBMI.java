package healthcalc.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import healthcalc.Person;
import healthcalc.PersonImpl;
import healthcalc.HealthCalc;
import healthcalc.exceptions.InvalidHealthDataException;
import healthcalc.view.ViewBMI;

public class CtrBMI implements ActionListener {
    private HealthCalc modelo;
    private ViewBMI vistaBMI;

    public CtrBMI(HealthCalc modelo, ViewBMI vistaBMI) {
        this.modelo = modelo;
        this.vistaBMI = vistaBMI;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        vistaBMI.clearErrors();
        
        String w = vistaBMI.getWeightInput();
        String h = vistaBMI.getHeightInput();
    
        boolean hayError = false;

//=======================================================================================================================
        // Validar si el campo está vacío 
        
        if (w.isEmpty()) {
            vistaBMI.setWeightError("Error: El campo no puede estar vacío.");
            hayError = true;
        }
        if (h.isEmpty()) {
            vistaBMI.setHeightError("Error: El campo no puede estar vacío.");
            hayError = true;
        }

        if (hayError) return; 

//=======================================================================================================================      
        // Validar si se han puesto comas. 
        
        if (w.contains(",")) {
            vistaBMI.setWeightError("Error: Use '.' en lugar de ',' para los decimales.");
            hayError = true;
        }
        if (h.contains(",")) {
            vistaBMI.setHeightError("Error: Use '.' en lugar de ',' para los decimales.");
            hayError = true;
        }

        if (hayError) return;

//=======================================================================================================================
        // Ver si se han introducido números o letras.
        
        double weight = 0;
        double heightCm = 0;

        try {
            weight = Double.parseDouble(w);
        } catch (NumberFormatException ex) {
            vistaBMI.setWeightError("Error: Introduzca solo números válidos.");
            hayError = true;
        }

        try {
            heightCm = Double.parseDouble(h);
        } catch (NumberFormatException ex) {
            vistaBMI.setHeightError("Error: Introduzca solo números válidos.");
            hayError = true;
        }

        if (hayError) return;

      //=======================================================================================================================

        // Validar números negativos
        if (weight < 0) {
            vistaBMI.setWeightError("Error: El valor no puede ser negativo.");
            hayError = true;
        }
        if (heightCm < 0) {
            vistaBMI.setHeightError("Error: El valor no puede ser negativo.");
            hayError = true;
        }

        if (hayError) return;

      //=======================================================================================================================
        
        // Valores fisiológicos
        
        try {
            Person person = new PersonImpl(weight, heightCm / 100.0, null, 0);
            double bmi = modelo.basalMetabolicIndex(person);
            String out = String.format("Resultado BMI: %.2f", bmi);

            if (vistaBMI.wantsClassification()) {
                out += " - " + modelo.category(person);
            }
            
            vistaBMI.setResult(out);

            // También tratamos el hecho de que la clasficación no se puede calcular si el BMI es mayor que 150. 
        } catch (InvalidHealthDataException ex) {
            String errorMsg = ex.getMessage().toLowerCase();
            
            if (errorMsg.contains("peso") || errorMsg.contains("weight")) {
                vistaBMI.setWeightError("Error: Fuera de límites biológicos (1-700 kg).");
            } else if (errorMsg.contains("altura") || errorMsg.contains("height")) {
                vistaBMI.setHeightError("Error: Fuera de límites biológicos (30-300 cm).");
            } else {
                vistaBMI.setResult("<html><font color='red'>Error: El BMI es mayor que 150.<br>No se puede calcular la clasificación.</font></html>");
            }
        }
        }
}