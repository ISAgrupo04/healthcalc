package healthcalc;

import healthcalc.exceptions.InvalidHealthDataException;

/**
 * Calculator of some health parameters of persons.
 * 
 * @author ISA
 *
 */
public interface HealthCalc {
	
	/**
	 * Calculate the BMI classification of a person.
	 * The BMI classification is based on the following table:
	 * Underweight: BMI < 18.5
	 * Normal weight: 18.5 <= BMI < 25
	 * Overweight: 25 <= BMI < 30
	 * Obesity: BMI >= 30
	 *
	 * @param person	The person for whom to calculate the BMI classification.
	 * @return	  		The BMI classification of the person.
	 * @throws Exception
	 */
	public BMICategory category(Person person) throws InvalidHealthDataException;
	
	/**
	 * Calculate the Body Mass Index (BMI) of a person with the Harris-Benedict formula:
	 *
	 * @param person	The person for whom to calculate the BMI.
	 * @return	  		The Body Mass Index of the person (kg/m2).
	 * @throws Exception
	 */
	public float basalMetabolicIndex(Person person) throws InvalidHealthDataException;

	/**
=======
	 * Calculate the Ideal Body Weight (IBM) of a person with the Lorenz formula:
	 * 
	 * @param person	The person for whom to calculate the IBW.
	 * @return	  		The Ideal Body Weight of the person (kg).
	 * @throws Exception
	 */
	public float idealBodyWeight(Person person) throws InvalidHealthDataException;

	/**
	 * Calculate the IBW of a person using the Lorentz formula:
	 * - For men: IBW = height (cm) - 100 - ((height (cm) - 150) / 4)
	 * - For women: IBW = height (cm) - 100 - ((height (cm) - 150) / 2.5)
	 * 
	 * @param person	The person for whom to calculate the IBW.
	 * @return	  		The Ideal Body Weight of the person (kg).
	 * @throws Exception
	 */

	public float bodySurfaceArea(Person person) throws InvalidHealthDataException;
	/**
     * Calculate the Body Surface Area (BSA) of a person using the Mosteller formula.
     * Formula: BSA (m2) = square root of ((height (cm) * weight (kg)) / 3600)
     * @param person	The person for whom to calculate the BSA.
     * @return          The Body Surface Area of the person (m2).
     * @throws Exception
     */
}
