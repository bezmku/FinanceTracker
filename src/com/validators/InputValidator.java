package com.validators;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;
public class InputValidator{

    public static <T>T validator(String inputPrompt, String errorPrompt,Class<T> type, Scanner scanner) {
        T input;
        while(true){
            try{
                System.out.print(inputPrompt);
                if(type==Integer.class){
                    input = type.cast(scanner.nextInt());
                    scanner.nextLine();
                    return input;
                }
                else if(type==Double.class){
                    input = type.cast(scanner.nextDouble());
                    scanner.nextLine();
                    return input;
                }
                else if(type.isEnum()){
                 @SuppressWarnings("unchecked")
                         String rawInput = scanner.nextLine().toUpperCase();
                         input=(T) Enum.valueOf((Class<? extends Enum>) type, rawInput);
                         return input;
                }
                else if(type== LocalDate.class){
                    String date = scanner.nextLine().trim();
                    return type.cast(LocalDate.parse(date));
                }

            }catch(InputMismatchException | IllegalArgumentException | DateTimeParseException e){
                System.out.println(errorPrompt);
                if(e instanceof InputMismatchException){
                scanner.nextLine();
                                              }

            }
        }

    }

}
