package healthcalc.BDD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import healthcalc.Person;
import healthcalc.PersonImpl;
import healthcalc.HealthCalc;
import healthcalc.HealthCalcImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class BMISteps {

    private HealthCalc calculator;
    private double weight;
    private double height;
    private double bmiResult;
    private String classificationResult;
    private Exception exception;

    // --- BACKGROUND  ---

    @Given("la calculadora de salud debe estar iniciada para BMI")
    public void la_calculadora_de_salud_debe_estar_iniciada_para_BMI() {
        HealthCalc calculator = HealthCalcImpl.getInstance();
        this.calculator = calculator;
    }

    @Given("el usuario ha seleccionado la métrica de BMI para calcular")
    public void el_usuario_ha_seleccionado_la_metrica_de_bmi_para_calcular() {
        // Paso descriptivo 
    }

    // --- CÁLCULO DE BMI (BMI.feature)  ---

    @Given("el usuario ingresa un peso para BMI de {double} kg")
    public void el_usuario_ingresa_un_peso_para_bmi_de_kg(double weight) {
        this.weight = weight;
    }

    @Given("el usuario ingresa una altura para BMI de {double} cm")
    public void el_usuario_ingresa_una_altura_para_bmi_de_cm(double height) {
        this.height = height;
    }

    @When("el sistema intenta calcular el BMI")
    public void el_sistema_intenta_calcular_el_bmi() {
        try {
            // El método bmi en HealthCalcImpl espera la altura en metros.
            // Los features la proporcionan en cm (ej: 175), por lo que dividimos entre 100.
            Person person = new PersonImpl(this.weight, this.height/100.0, null, 0);
            this.bmiResult = calculator.basalMetabolicIndex(person);
            this.exception = null;
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("el resultado debe ser {double} para BMI")
    public void el_resultado_debe_ser_para_bmi(double expected) {
        // Delta de 0.01 para manejar redondeos de decimales
        assertEquals(expected, this.bmiResult, 0.01); 
    }

    // --- CLASIFICACIÓN DE BMI (Clasificacion.feature)  ---

    @Given("el sistema recibe un valor de BMI de {double}")
    public void el_sistema_recibe_un_valor_de_bmi_de(double bmi) {
        this.bmiResult = bmi;
    }

    @When("el sistema intenta clasificar el valor para BMI")
    public void el_sistema_intenta_clasificar_el_valor_para_bmi() {
        try {
            double altura = 1.75;
            double peso = this.bmiResult * Math.pow(altura, 2);
            this.classificationResult = calculator.category(new PersonImpl(peso, altura, null, 0));
            this.exception = null;
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("el resultado debe ser {string}")
    public void el_resultado_debe_ser(String expected) {
        assertEquals(expected, this.classificationResult);
    }

    // --- MANEJO DE EXCEPCIONES ---

    @Then("el sistema debe lanzar una excepción de datos de salud inválido para BMI")
    public void el_sistema_debe_lanzar_una_excepcion_de_datos_de_salud_invalido_para_bmi() {
        assertNotNull(this.exception, "Se esperaba una excepción pero el cálculo no falló.");
    }
}