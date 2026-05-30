package healthcalc.BDD;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import healthcalc.Person;
import healthcalc.PersonImpl;
import healthcalc.Gender;
import healthcalc.HealthCalc;
import healthcalc.HealthCalcImpl;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class IBWSteps {

    private HealthCalc calculator = HealthCalcImpl.getInstance();
    private double height;
    private Gender gender;
    private double result;
    private Exception exception;

    /*@Given("la calculadora de salud está iniciada")
    public void la_calculadora_de_salud_iniciada_IBW() {
        calculator = new HealthCalcImpl();
    }*/
    
    @Given("el usuario debe haber seleccionado la métrica de cálculo de IBW")
    public void el_usuario_selecciona_metrica_IBW() {
    }
    
    @Given("el usuario ingresa para IBW una altura de {double} cm")
    public void el_usuario_ingresa_una_altura_IBW(double height) {
        this.height = height;
    }

    @Given("el genero de la persona es {word}")
    public void el_usuario_ingresa_un_genero(String genderStr) {
        if (genderStr == null || genderStr.trim().isEmpty()) {
            this.gender = null;
            return;
        }

        char firstChar = Character.toUpperCase(genderStr.charAt(0));
        
        if (firstChar == 'M' || firstChar == 'H') {
            this.gender = Gender.MALE;     // Masculino / Hombre -> MALE
        } else if (firstChar == 'F' || firstChar == 'W') {
            this.gender = Gender.FEMALE;   // Femenino / Woman -> FEMALE
        } else {
            this.gender = null;
        }
    }

    @When("ejecuto la operación de cálculo de IBW")
    public void ejecuto_operacion_IBW() {
        try {
            Person person = new PersonImpl(0, this.height, this.gender, 0);
            this.result = calculator.idealBodyWeight(person);
            this.exception = null;
        } catch (Exception e) {
            this.exception = e;
        }
    }

    @Then("el resultado debe ser {double} kg")
    public void el_resultado_debe_ser_IBW(double expected) {
        assertEquals(expected, result, 0.01);
    }

    @Then("el sistema debe lanzar una excepción")
    public void el_sistema_debe_lanzar_una_excepcion_IBW() {
        assertNotNull(exception, "Se esperaba un error pero el cálculo funcionó.");
    }

}