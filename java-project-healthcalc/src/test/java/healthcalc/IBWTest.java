package healthcalc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import healthcalc.exceptions.InvalidHealthDataException;

/**
 * Tests for the HealthCalc interface.
 * 
 * Use the AAA pattern (Arrange, Act, Assert) for the tests.
 * 
 * @author NicoSA
 */
@DisplayName("Tests para la métrica de peso ideal IBW.")
public class IBWTest {

    private HealthCalc healthCalc;

    @BeforeEach
    void setUp() {
        healthCalc = new HealthCalcImpl();
    }
    
    @Nested
    @DisplayName("Métrica del IBW")
    class IBWMetricTests {

        @ParameterizedTest(name = "Cálculo IBW para valores estándar para hombre altura: {0} cm")
        @CsvSource({
            "165, m",
            "170, m",
            "180, m",
            "190, m",
            "201, m"
        })
        @DisplayName("IBW para hombre con altura válida")
        void testIbwHombreValido(double height, char gender) throws InvalidHealthDataException {
            double expectedIbw = (height - 100) - ((height - 150) / 4.0);
            char gender1 = 'm';

            double result = healthCalc.ibw(height, gender1);

            assertEquals(expectedIbw, result, 0.01);
        }

        @ParameterizedTest(name = "Cálculo IBW para valores estándar para mujeres altura: {0} cm")
        @CsvSource({
            "165, f",
            "170, f",
            "180, f",
            "190, f",
            "201, f"
        })
        @DisplayName("IBW para mujer con altura válida")
        void testIbwMujerValido(double height, char gender) throws InvalidHealthDataException {
            double expectedIbw = (height - 100) - ((height - 150) / 2.0);
            char gender1 = 'f';
        
            double result = healthCalc.ibw(height, gender1);

            assertEquals(expectedIbw, result, 0.01);
        }

        @Test
        @DisplayName("Lanzar error cuando altura negativa")
        void testIbwAlturaNegativa() {
            assertAll(
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.ibw(-100.0, 'm')),
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.ibw(-100.0, 'f'))
            ); 
        }

        @Test
        @DisplayName("Lanzar error cuando altura menor a 30 cm")
        void testIbwAlturaMenor30() {
            assertAll(
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.ibw(29.0, 'm')),
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.ibw(29.0, 'f'))
            );
        } 
        
        @Test
        @DisplayName("Lanzar error cuando altura sea mayor a 300 cm")
        void testIbwAlturaMayor300() {
            assertAll(
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.ibw(301.0, 'm')),
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.ibw(301.0, 'f'))
            );
        }

        @Test
        @DisplayName("Lanzar error cuando el género no sea 'm' o 'f'")
        void testIbwGeneroInvalido() {
            assertAll(
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.ibw(170.0, 'x')),
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.ibw(170.0, '1')),
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.ibw(170.0, ' '))
            );
        }
    }
}
