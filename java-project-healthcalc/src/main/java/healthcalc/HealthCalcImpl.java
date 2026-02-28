package healthcalc;

import healthcalc.exceptions.InvalidHealthDataException;

public class HealthCalcImpl implements HealthCalc {

    @Override
    public String bmiClassification(double bmi) throws InvalidHealthDataException {
        if (bmi < 0) {
            throw new InvalidHealthDataException("BMI cannot be negative.");
        }
        if (bmi > 150) {
            throw new InvalidHealthDataException("BMI must be within a possible biological range [0-150].");
        }
        String result = "Obesity";
        // if (bmi < 18.5) {
        //     result = "Underweight";
        // } else if (bmi >= 18.5 && bmi < 25) {
        //     result = "Normal weight";
        // } else if (bmi >= 25 && bmi < 30) {
        //     result = "Overweight";
        // }
        if (bmi < 16) {
            result = "Severe thinness";
        } else if (bmi >= 16 && bmi < 17) {
            result = "Moderate thinness";
        } else if (bmi >= 17 && bmi < 18.5) {
            result = "Mild thinness";
        } else if (bmi >= 18.5 && bmi < 25) {
            result = "Normal weight";
        } else if (bmi >= 25 && bmi < 30) {
            result = "Overweight";
        } else if (bmi >= 30 && bmi < 35) {
            result = "Obese Class I";
        } else if (bmi >= 35 && bmi < 40) {
            result = "Obese Class II";
        } else if (bmi >= 40) {
            result = "Obese Class III";
        }
        return result;
    }

    @Override
    public double bmi(double weight, double height) throws InvalidHealthDataException {
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
        return weight / Math.pow(height, 2);
    }

    @Override
    public double ibw(double height, char gender) throws InvalidHealthDataException {
        // Initial exception
        if (height < 30 || height > 300) {
            throw new InvalidHealthDataException("Height must be within a possible biological range [30-300] cm.");
        }
        
        // Management of the gender parameter
        double v_aux = -1;
        if (Character.toUpperCase(gender)== 'M'){
            v_aux = 4;
        } else if (Character.toUpperCase(gender) == 'F'){
            v_aux = 2;
        } else {
            throw new InvalidHealthDataException("Gender must be 'm' or 'f'."); // Exception for non-considered gender parameters
        }

        // Calculations of the result
        double ibw = (height - 100) - ((height-150)/v_aux);
        return ibw;
    }

    
}
