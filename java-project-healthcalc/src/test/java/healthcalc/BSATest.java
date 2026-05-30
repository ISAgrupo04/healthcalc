package healthcalc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import healthcalc.exceptions.InvalidHealthDataException;

/**
 * Tests for the HealthCalc interface.
 */

@DisplayName("Tests para la calculadora de salud. (BSATest)")

public class BSATest {
    private HealthCalc healthCalc;

    @BeforeEach
    void setUp() {
        healthCalc = HealthCalcImpl.getInstance();
    }

    @Nested
    @DisplayName("Métrica del BSA")
    class BSACalculationTests {
        @Test
        @DisplayName("Cálculo BSA para valores estándar")
        void testBsaValido() throws InvalidHealthDataException {
            double weight = 70.0;
            double height = 175.0;
            double expectedBsa = Math.sqrt((weight * height) / 3600.0);
            
            Person person = new PersonImpl(weight, height, null, 0);
            double result = healthCalc.bodySurfaceArea(person);

            // Compare the expected results with the results from the method used
            assertEquals(expectedBsa, result, 0.01);
        }

        @Test
        @DisplayName("Lanzar excepción cuando el peso es cero")
        void testBsaPesoCero() {
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.bodySurfaceArea(new PersonImpl(0, 170, null, 0)));
        }

        @Test
        @DisplayName("Lanzar excepción cuando la altura es cero")
        void testBsaAlturaCero() {
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.bodySurfaceArea(new PersonImpl(70, 0, null, 0)));
        }

        @Test
        @DisplayName("Lanzar excepción cuando los valores son negativos")
        void testBsaNegativos() {
            assertAll(
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.bodySurfaceArea(new PersonImpl(-70, 170, null, 0))),
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.bodySurfaceArea(new PersonImpl(70, -170, null, 0)))
            );
        }

        @ParameterizedTest(name = "Peso mínimo fuera de rango: {0} kg")
        @ValueSource(doubles = {-10.0, 0.0, 0.99})
        @DisplayName("Bloqueo de pesos inferiores al límite biológico mínimo (1 kg)")
        void testPesoMinimoImposible(double weight) {
            double height = 170.0;
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.bodySurfaceArea(new PersonImpl(weight, height, null, 0)));
        }

        @ParameterizedTest(name = "Peso máximo fuera de rango: {0} kg")
        @ValueSource(doubles = {700.1, 900.0, 1000.0})
        @DisplayName("Bloqueo de pesos superiores al límite biológico máximo (700 kg)")
        void testPesoMaximoImposible(double weight) {
            double height = 170.0;   
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.bodySurfaceArea(new PersonImpl(weight, height, null, 0)));
        }

        @ParameterizedTest(name = "Altura mínima inválida: {0} cm")
        @ValueSource(doubles = {-0.50, 0.0, 0.29})
        @DisplayName("Bloqueo de alturas inferiores al límite biológico mínimo (30 c)")
        void testAlturaMinimaImposible(double height) {
            double weight = 70.0;
            
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.bodySurfaceArea(new PersonImpl(weight, height, null, 0)));
        }

        @ParameterizedTest(name = "Altura máxima inválida: {0} cm")
        @ValueSource(doubles = {301, 350, 500})
        @DisplayName("Bloqueo de alturas superiores al límite biológico máximo (300 cm)")
        void testAlturaMaximoImposible(double height) {
            double weight = 70.0;
            
            assertThrows(InvalidHealthDataException.class, () -> healthCalc.bodySurfaceArea(new PersonImpl(weight, height, null, 0)));
        }
    }



    
    
}
