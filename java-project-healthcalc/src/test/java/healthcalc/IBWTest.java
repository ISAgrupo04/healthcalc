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
        healthCalc = HealthCalcImpl.getInstance();
    }
    
    @Nested
    @DisplayName("Métrica del IBW")
    class IBWMetricTests {

        @ParameterizedTest(name = "Cálculo IBW para valores estándar para hombre altura: {0} cm")
        @CsvSource({
            "165, MALE",
            "170, MALE",
            "180, MALE",
            "190, MALE",
            "201, MALE"
        })
        @DisplayName("IBW para hombre con altura válida")
        void testIbwHombreValido(double height, Gender gender) throws InvalidHealthDataException {
            double expectedIbw = (height - 100) - ((height - 150) / 4.0);

            Person personM = new PersonImpl(0, height, gender, 0);
            double result = healthCalc.idealBodyWeight(personM);

            assertEquals(expectedIbw, result, 0.01);
        }

        @ParameterizedTest(name = "Cálculo IBW para valores estándar para mujeres altura: {0} cm")
        @CsvSource({
            "165, FEMALE",
            "170, FEMALE",
            "180, FEMALE",
            "190, FEMALE",
            "201, FEMALE"
        })
        @DisplayName("IBW para mujer con altura válida")
        void testIbwMujerValido(double height, Gender gender) throws InvalidHealthDataException {
            double expectedIbw = (height - 100) - ((height - 150) / 2.0);

            Person personF = new PersonImpl(0, height, gender, 0);
            double result = healthCalc.idealBodyWeight(personF);

            assertEquals(expectedIbw, result, 0.01);
        }

        @Test
        @DisplayName("Lanzar error cuando altura negativa")
        void testIbwAlturaNegativa() {
            assertAll(
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.idealBodyWeight(new PersonImpl(0, -100.0, Gender.MALE, 0))),
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.idealBodyWeight(new PersonImpl(0, -100.0, Gender.FEMALE, 0)))
            ); 
        }

        @Test
        @DisplayName("Lanzar error cuando altura menor a 30 cm")
        void testIbwAlturaMenor30() {
            assertAll(
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.idealBodyWeight(new PersonImpl(0, 29.0, Gender.MALE, 0))),
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.idealBodyWeight(new PersonImpl(0, 29.0, Gender.FEMALE, 0)))
            );
        } 
        
        @Test
        @DisplayName("Lanzar error cuando altura sea mayor a 300 cm")
        void testIbwAlturaMayor300() {
            assertAll(
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.idealBodyWeight(new PersonImpl(0, 301.0, Gender.MALE, 0))),
                () -> assertThrows(InvalidHealthDataException.class, () -> healthCalc.idealBodyWeight(new PersonImpl(0, 301.0, Gender.FEMALE, 0)))
            );
        }
    }
}
