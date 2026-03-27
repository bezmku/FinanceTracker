//Run this one for console based manipulation of you transactions

package com.financer;

import com.db.DatabaseHandler;
import com.validators.InputValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static java.lang.System.exit;

public class Main{


    public static void main(String[] args){
        DatabaseHandler.initializeDatabase();
        Scanner scanner = new Scanner(System.in);
        FinanceTracker tracker = new FinanceTracker();
        List<Transaction> savedData = DatabaseHandler.getAllTransactions();
        for(Transaction t : savedData){
            tracker.addTransaction(t);
        }
        while(true){
            int choice;
            System.out.printf("Enter: \n " +
                    "1. to add transaction\n " +
                    "2. view all transactions\n " +
                    "3. view balance \n" +
                    "4. to search transaction by date range\n" +
                    "5. search transaction by category\n" +
                    "6. remove transaction by id\n"+
                    "7. to exit\n");
            choice=InputValidator.validator("Enter choice: ","Invalid input",Integer.class,scanner);
            switch(choice){
                case 1 :{
                    System.out.println("-----Adding transaction...");
                    TransactionType type = InputValidator.validator("Enter Transaction type(INCOME/EXPENSE): ","Invalid transaction type",TransactionType.class,scanner);
                    Category category = InputValidator.validator("Enter Transaction category(FOOD, TRANSPORT,SALARY,ENTERTAINMENT,RENT,OTHER): ","Invalid transaction category",Category.class,scanner);
                    double amount = InputValidator.validator("Enter amount of money: ","Invalid input",Double.class,scanner);
                    Transaction t = new Transaction(type, category, amount,LocalDate.now());
                    tracker.addTransaction(t);
                    System.out.println("transaction added");
                    DatabaseHandler.saveTransaction(t);
                    System.out.println("----------------------------------------");
                    break;
                }
                case 2:{
                    if(!tracker.getAllTransactions().isEmpty()){
                        System.out.println(tracker.getAllTransactions());
                    }
                    else System.out.println(" no transaction found");
                    System.out.println("----------------------------------------");
                    break;
                }
                case 3:{
                    System.out.println(tracker.calculateBalance());
                    break;
                }
                case 4:{
                    if(!tracker.getAllTransactions().isEmpty()){
                        LocalDate start = InputValidator.validator("Enter start date(yyyy-mm-dd): ","invalid input",LocalDate.class,scanner);
                        LocalDate end = InputValidator.validator("Enter end date(yyyy-mm-dd): ","invalid input",LocalDate.class,scanner);
                        List<Transaction> filtered = new ArrayList<>();
                        filtered = tracker.getTransactionByDateRange(start,end);
                        if(!filtered.isEmpty()){
                            System.out.println(filtered);
                        }
                    }
                    else System.out.println(" no transaction found");
                    System.out.println("----------------------------------------");
                    break;
                }
                case 5 : {
                    if(!tracker.getAllTransactions().isEmpty()){
                        Category category = InputValidator.validator("Enter category(FOOD, TRANSPORT,SALARY,ENTERTAINMENT,RENT,OTHER): ","Invalid category",Category.class,scanner);
                        List<Transaction> filtered = new ArrayList<>();
                        filtered = tracker.getTransactionByCategory(category);
                        if(!filtered.isEmpty()){
                            System.out.println(filtered);
                        }
                    }
                    else System.out.println("no transaction found");
                    System.out.println("----------------------------------------");
                    break;
                }
                case 6:{
                    if(!tracker.getAllTransactions().isEmpty()){
                        int idToDelete = InputValidator.validator("Enter ID of transaction to delete","Invalid ID",Integer.class,scanner);
                        DatabaseHandler.deleteTransaction(idToDelete);
                        boolean removed = tracker.removeTransactionById(idToDelete);
                        if(removed){
                            System.out.println("Transaction removed successfully.");
                        }
                        else System.out.println(" couldn't find transaction with the given id");
                    }
                    else System.out.println("no transaction to delete");
                    System.out.println("------------------------------------");
                    break;
                }
                case 7:{
                    System.out.println("Exiting....");
                    System.out.println("----------------------------------------");
                    exit(0);
                }
                default :
                    System.out.println("invalid choice");
                    System.out.println("----------------------------------------");
            }


        }
    }
}
