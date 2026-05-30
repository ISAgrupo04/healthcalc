package healthcalc;

public class HealthHospitalAdapter implements HealthHospital {

    private HealthCalc calculator;

    public HealthHospitalAdapter() {
        this.calculator = HealthCalcImpl.getInstance();
    }

    @Override
    public Tuple<Float, String> indiceMasaCorporal(float altura, int peso) {
        double pesoKg = peso / 1000.0;
        
        Person person = new PersonImpl(pesoKg, altura, null, 0);
        // Pasamos "altura" directamente, ya que viene en metros (ej. 1.83) y bmi() espera metros.
        double bmiResult = calculator.basalMetabolicIndex(person);
        BMICategory clasificacion = calculator.category(person);

        return new Tuple<>((float) bmiResult, clasificacion.getLabel());
    }

    @Override
    public int pesoCorporalIdeal(char genero, float altura) {
        // 1. Traducimos el género del Hospital (char en español) al Enum de la calculadora
        Gender generoCalculadora;
        char g = Character.toUpperCase(genero);
    
        if (g == 'M') {
            generoCalculadora = Gender.MALE; 
        } else if (g == 'F') {
            generoCalculadora = Gender.FEMALE;
        } else {
            throw new IllegalArgumentException("Género recibido del hospital no válido: " + genero);
        }

        // 2. Traducimos la altura a centímetros (esto ya lo teníamos bien)
        double alturaCm = altura * 100.0;
        
        Person person = new PersonImpl(0, alturaCm, generoCalculadora, 0);

        // 3. Llamamos a la calculadora base con la letra correcta en inglés
        double pesoIdeal = calculator.idealBodyWeight(person);
        
        return (int) pesoIdeal;
    }
}