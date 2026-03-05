package healthcalc;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Instance of HealthCalImple
        HealthCalcImpl calculator = new HealthCalcImpl();

        // Scanner for user input
        Scanner scanner = new Scanner(System.in);

        // Get user inputs
        try{
            System.out.print("Enter your weight (kg): ");
            double weight = scanner.nextDouble();

            System.out.print("Enter your height (cm): ");
            double height = scanner.nextDouble();

            System.out.print("Enter your gender (m = male, f = female): ");
            char gender = scanner.next().charAt(0);

            scanner.close();
        /* ==================================================================================================
                                        Calculate health metrics 
        ==================================================================================================*/
        // Calculate BMI (in metres (m))
        double bmi = calculator.bmi(weight, (height / 100));
        System.out.println("Your BMI is: " + String.format("%.3f", bmi));

        // Calculate IBW
        double ibw = calculator.ibw(weight, gender);
        System.out.println("Your IBW is: " + String.format("%.3f", ibw));
    
        // Calculate BSA
        double bsa = calculator.bsa(weight, height);
        System.out.println("Your BSA is: " +  String.format("%.3f",bsa));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return; 
        }

    }

}


       