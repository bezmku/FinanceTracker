package com.financer;

import java.time.LocalDate;

public class Transaction{

    private int id;
    private static int idCounter=1;
    private TransactionType type;
    private Category category;
    private double amount;
    private LocalDate date;

    public Transaction(TransactionType type,Category category, double amount,LocalDate date){
        this.id = idCounter++;
        this.type = type;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
public int getId(){
        return id;
}
public double getAmount(){
        return amount;

}
public Category getCategory(){
        return category;
}
public TransactionType getType(){
        return type;
}
public LocalDate getDate(){
        return date;
}
@Override
    public String toString(){
        return String.format("| Id: %d  | Type: %s | Category: %s | Amount: %.2f | Date: %s\n",id,type,category,amount,date);
}
}