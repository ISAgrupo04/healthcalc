package healthcalc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;


import healthcalc.exceptions.InvalidHealthDataException;

/**
 * Tests for the HealthCalc interface.
 * 
 * Use the AAA pattern (Arrange, Act, Assert) for the tests.
 * 
 * @author ISA
 */
@DisplayName("Tests para la calculadora de salud.")

public class BMITest {

	private HealthCalc healthCalc;

	@BeforeEach
	void setUp() {
		healthCalc = HealthCalcImpl.getInstance();
	}

    @Nested
    @DisplayName("Métrica del BMI")
    class BMIMetricTests {

        @Test
        @DisplayName("Cálculo de BMI con valores estándar válidos")
        void testBmiValido() throws InvalidHealthDataException {
            double weight = 70.0;
            double height = 1.75;
            double expectedBmi = 70.0 / Math.pow(1.75, 2);

            Person person = new PersonImpl(weight, height, null, 0);
            double result = healthCalc.basalMetabolicIndex(person);

            assertEquals(expectedBmi, result, 0.01);
        }

        @Test
        @DisplayName("Lanzar excepción cuando el peso es cero")
        void testBmiPesoCero() {
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.basalMetabolicIndex(new PersonImpl(0, 170, null, 0)));
        }

        @Test
        @DisplayName("Lanzar excepción cuando la altura es cero")
        void testBmiAlturaCero() {
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.basalMetabolicIndex(new PersonImpl(70, 0, null, 0)));
        }

        @Test
        @DisplayName("Lanzar excepción cuando los valores son negativos")
        void testBmiNegativos() {
            assertAll(
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.basalMetabolicIndex(new PersonImpl(-70, 170, null, 0))),
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.basalMetabolicIndex(new PersonImpl(70, -170, null, 0)))
            );
        }

        @ParameterizedTest(name = "Peso mínimo inválido: {0} kg")
        @ValueSource(doubles = {-10.0, 0.0, 0.99})
        @DisplayName("Bloqueo de pesos inferiores al límite biológico mínimo (1 kg)")
        void testPesoMinimoImposible(double weight) {
            double height = 170.0;
            
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.basalMetabolicIndex(new PersonImpl(weight, height, null, 0)));
        }

        @ParameterizedTest(name = "Peso máximo inválido: {0} kg")
        @ValueSource(doubles = {700.1, 1000.0, 5000.0})
        @DisplayName("Bloqueo de pesos superiores al límite biológico máximo (700 kg)")
        void testPesoMaximoImposible(double weight) {
            double height = 170.0;
            
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.basalMetabolicIndex(new PersonImpl(weight, height, null, 0)));
        }

        @ParameterizedTest(name = "Altura mínima inválida: {0} m")
        @ValueSource(doubles = {-0.50, 0.0, 0.29})
        @DisplayName("Bloqueo de alturas inferiores al límite biológico mínimo (30 m)")
        void testAlturaMinimaImposible(double height) {
            double weight = 70.0;
            
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.basalMetabolicIndex(new PersonImpl(weight, height, null, 0)));
        }

        @ParameterizedTest(name = "Altura máxima inválida: {0} m")
        @ValueSource(doubles = {3.01, 3.50, 5.00})
        @DisplayName("Bloqueo de alturas superiores al límite biológico máximo (300 m)")
        void testAlturaMaximoImposible(double height) {
            double weight = 70.0;
            
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.basalMetabolicIndex(new PersonImpl(weight, height, null, 0)));
        }
    }

	@Nested
    @DisplayName("Clasificación básica a partir del BMI")
    class BMIClassificationTests {

        @ParameterizedTest(name = "BMI {0} debe ser clasificado como Severe thinness")
        @ValueSource(doubles = {10.0, 15.5, 15.99})
        @DisplayName("Validación de categoría Severe thinness (Delgadez severa)")
        void testBmiSevereThinness(double bmi) throws InvalidHealthDataException {
            String expected = "Severe thinness";
            
            double altura = 1.75;
            double peso = bmi * Math.pow(altura, 2);

            String result = healthCalc.category(new PersonImpl(peso, altura, null, 0));

            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "BMI {0} debe ser clasificado como Moderate thinness")
        @ValueSource(doubles = {16.0, 16.5, 16.99})
        @DisplayName("Validación de categoría Moderate thinness (Delgadez moderada)")
        void testBmiModerateThinness(double bmi) throws InvalidHealthDataException {
            String expected = "Moderate thinness";
            
            double altura = 1.75;
            double peso = bmi * Math.pow(altura, 2);

            String result = healthCalc.category(new PersonImpl(peso, altura, null, 0));

            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "BMI {0} debe ser clasificado como Mild thinness")
        @ValueSource(doubles = {17.0, 17.5, 18.49})
        @DisplayName("Validación de categoría Mild thinness (Delgadez leve)")
        void testBmiMildThinness(double bmi) throws InvalidHealthDataException {
            String expected = "Mild thinness";
            
            double altura = 1.75;
            double peso = bmi * Math.pow(altura, 2);

            String result = healthCalc.category(new PersonImpl(peso, altura, null, 0));

            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "BMI {0} debe ser clasificado como Normal weight")
        @ValueSource(doubles = {18.5, 22.0, 24.9, 24.99})
        @DisplayName("Validación de categoría Normal weight (Peso saludable)")
        void testBmiNormalWeight(double bmi) throws InvalidHealthDataException {
            String expected = "Normal weight";

            double altura = 1.75;
            double peso = bmi * Math.pow(altura, 2);
            String result = healthCalc.category(new PersonImpl(peso, altura, null, 0));

            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "BMI {0} debe ser clasificado como Overweight")
        @ValueSource(doubles = {25.0, 27.5, 29.9, 29.99})
        @DisplayName("Validación de categoría Overweight (Sobrepeso)")
        void testBmiOverweight(double bmi) throws InvalidHealthDataException {
            String expected = "Overweight";

            double altura = 1.75;
            double peso = bmi * Math.pow(altura, 2);
            String result = healthCalc.category(new PersonImpl(peso, altura, null, 0));

            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "BMI {0} debe ser clasificado como Obese Class I")
        @ValueSource(doubles = {30.0, 32.5, 34.99})
        @DisplayName("Validación de categoría Obese Class I (Obesidad Clase I)")
        void testBmiObeseClassI(double bmi) throws InvalidHealthDataException {
            String expected = "Obese Class I";

            double altura = 1.75;
            double peso = bmi * Math.pow(altura, 2);
            String result = healthCalc.category(new PersonImpl(peso, altura, null, 0));

            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "BMI {0} debe ser clasificado como Obese Class II")
        @ValueSource(doubles = {35.0, 37.5, 39.99})
        @DisplayName("Validación de categoría Obese Class II (Obesidad Clase II)")
        void testBmiObeseClassII(double bmi) throws InvalidHealthDataException {
            String expected = "Obese Class II";

            double altura = 1.75;
            double peso = bmi * Math.pow(altura, 2);
            String result = healthCalc.category(new PersonImpl(peso, altura, null, 0));

            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "BMI {0} debe ser clasificado como Obese Class III")
        @ValueSource(doubles = {40.0, 45.0, 50.0})
        @DisplayName("Validación de categoría Obese Class III (Obesidad Clase III)")
        void testBmiObeseClassIII(double bmi) throws InvalidHealthDataException {
            String expected = "Obese Class III";

            double altura = 1.75;
            double peso = bmi * Math.pow(altura, 2);
            String result = healthCalc.category(new PersonImpl(peso, altura, null, 0));

            assertEquals(expected, result);
        }

        @ParameterizedTest(name = "BMI máximo extremo: {0}")
        @ValueSource(doubles = {150.1, 200.0, 500.0})
        @DisplayName("Bloqueo de valores de BMI superiores al límite humano razonable (150)")
        void testBmiClassificationMaximoImposible(double bmi) {
            double altura = 1.00;
            double peso = bmi * Math.pow(altura, 2);
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.category(new PersonImpl(peso, altura, null, 0)));
        }

        /* Test adicionales para mostrar que se pueden definir de otra forma. */

        @Test
        @DisplayName("Clasificación correcta para sobrepeso (Overweight)")
        void testBmiOverweight() throws InvalidHealthDataException {
            double bmiLimiteInferior = 25.0;
            double bmiMedio = 27.5;
            double bmiLimiteSuperior = 29.99;

            double altura = 1.75;
            double pesoInferior = bmiLimiteInferior * Math.pow(altura, 2);
            double pesoMedio = bmiMedio * Math.pow(altura, 2);
            double pesoSuperior = bmiLimiteSuperior * Math.pow(altura, 2);

            String resultInferior = healthCalc.category(new PersonImpl(pesoInferior, altura, null, 0));
            String resultMedio = healthCalc.category(new PersonImpl(pesoMedio, altura, null, 0));
            String resultSuperior = healthCalc.category(new PersonImpl(pesoSuperior, altura, null, 0));

            assertAll(
                () -> assertEquals("Overweight", resultInferior),
                () -> assertEquals("Overweight", resultMedio),
                () -> assertEquals("Overweight", resultSuperior)
            );
        }

        @ParameterizedTest(name = "BMI {0} debe ser clasificado como {1}")
        @CsvSource({
            "10.0, Severe thinness",
            "16.0, Moderate thinness",
            "17.0, Mild thinness",
            "18.4, Mild thinness",
            "18.5, Normal weight",
            "24.9, Normal weight",
            "25.0, Overweight",
            "29.9, Overweight",
            "30.0, Obese Class I",
            "34.9, Obese Class I",
            "35.0, Obese Class II",
            "39.9, Obese Class II",
            "40.0, Obese Class III",
            "45.0, Obese Class III"
        })
        @DisplayName("Clasificación de BMI en los límites exactos de cada categoría")
        void testBmiClassificationLimites(double bmi, String expectedCategory) throws InvalidHealthDataException {
            double altura = 1.75;
            double peso = bmi * Math.pow(altura, 2);
            String result = healthCalc.category(new PersonImpl(peso, altura, null, 0));

            assertEquals(expectedCategory, result);
        }
    }

}








