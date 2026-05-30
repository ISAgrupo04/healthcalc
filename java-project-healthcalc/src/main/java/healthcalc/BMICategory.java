package healthcalc;

public enum BMICategory {
    SEVERE_THINNESS ( "Severe thinness", 0.0, 16.0),
    MODERATE_THINNESS ( "Moderate thinness", 16.0, 17.0),
    MILD_THINNESS ( "Mild thinness", 17.0, 18.50),
    NORMAL ( "Normal weight", 18.5, 25.0),
    OVERWEIGHT ( "Overweight", 25.0, 30.0),
    OBESE_CLASS_I ( "Obese Class I", 30.0, 35.0),
    OBESE_CLASS_II ( "Obese Class II", 35.0, 40.0),
    OBESE_CLASS_III ( "Obese Class III", 40.0, 150.0);


    private final String label;
    private final double minBMI;
    private final double maxBMI;

    BMICategory(String label, double minBMI, double maxBMI) {
        this.label  = label;
        this.minBMI = minBMI;
        this.maxBMI = maxBMI;
    }

    public String getLabel() {
            return label;
    }

    public double getMinBMI() {
        return minBMI;
    } 

    public double getMaxBMI() {
        return maxBMI;
    }
}
// Tenemos que crear un contructor para verificar el rango
