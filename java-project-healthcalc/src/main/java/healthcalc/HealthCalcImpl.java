package healthcalc;

import healthcalc.exceptions.InvalidHealthDataException;

public class HealthCalcImpl implements HealthCalc {

    // Realizar Singleton
    private static HealthCalcImpl instance;

    // Constructor
    private HealthCalcImpl() {}

    // Método getInstance
    public static HealthCalcImpl getInstance() {
        if (instance == null) {
            instance = new HealthCalcImpl();
        }
        return instance;
    }

    @Override
    public BMICategory category(Person person) throws InvalidHealthDataException {
        double bmi = basalMetabolicIndex(person);
        // Tratamos los posibles errores
        if (bmi < 0) {
            throw new InvalidHealthDataException("BMI must be a positive value.");
        }
        if (bmi > 150) {
            throw new InvalidHealthDataException("BMI must be within a possible biological range [0-150].");
        }
        for (BMICategory category : BMICategory.values()) {
            if (bmi >= category.getMinBMI() && bmi < category.getMaxBMI()) {
                return category;
            }
        }
        throw new InvalidHealthDataException("BMI value is out of any defined category range.");
    }

    @Override
    public float basalMetabolicIndex(Person person) throws InvalidHealthDataException {
        double weight = person.weight();
        double height = person.height();
        if (weight <= 0) {
            throw new InvalidHealthDataException("Weight must be positive.");
        }
        if (height <= 0) {
            throw new InvalidHealthDataException("Height must be positive.");
        }
        if (weight < 1 || weight > 700) {
            throw new InvalidHealthDataException("Weight must be within a possible biological range [1-700] kg.");
        }
        if (height < 0.30 || height > 3.00) {
            throw new InvalidHealthDataException("Height must be within a possible biological range [0.30-3.00] m.");
        }
        return (float) (weight / Math.pow(height, 2));
    }

    @Override
    public float idealBodyWeight(Person person) throws InvalidHealthDataException {
        double height = person.height();
        Gender gender = person.gender();
        // Initial exception
        if (height < 30 || height > 300) {
            throw new InvalidHealthDataException("Height must be within a possible biological range [30-300] cm.");
        }
        
        // Management of the gender parameter
        double v_aux = -1;
        if (gender == Gender.MALE){
            v_aux = 4;
        } else if (gender == Gender.FEMALE){
            v_aux = 2;
        } else {
            throw new InvalidHealthDataException("     must be 'MALE' or 'FEMALE'."); // Exception for non-considered gender parameters
        }

        // Calculations of the result
        double ibw = (height - 100) - ((height-150)/v_aux);
        return (float) ibw;
    }

    @Override
    public float bodySurfaceArea(Person person) throws InvalidHealthDataException {
        double weight = person.weight();
        double height = person.height();

        if (weight <= 0) {
            throw new InvalidHealthDataException("Weight must be positive.");
        }
        if (height <= 0) {
            throw new InvalidHealthDataException("Height must be positive.");
        }
        if (weight < 1.00 || weight > 700.00) {
            throw new InvalidHealthDataException("Weight must be within a possible biological range [1-700] kg.");
        }
        if (height < 30 || height > 300) {
            throw new InvalidHealthDataException("Height must be within a possible biological range [30-300] cm.");
        }


        return (float) Math.sqrt((height * weight) / 3600.0);
    }

}
