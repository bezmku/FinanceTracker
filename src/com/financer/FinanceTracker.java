package com.financer;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FinanceTracker {

    private List<Transaction> transactions;

    public FinanceTracker(){
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction t){
        transactions.add(t);
    }

    public List<Transaction> getAllTransactions(){

        return transactions;
    }
    public double calculateBalance(){
        double balance=0;
        for(Transaction t : transactions){
            if(t.getType()==TransactionType.INCOME){
                balance += t.getAmount();
            }
            else { balance -= t.getAmount();}
        }
        return balance;
    }
    public List<Transaction> getTransactionByCategory(Category c){
        List<Transaction> tr = new ArrayList<>();
        for(Transaction t : transactions){
            if(t.getCategory()==c){
                tr.add(t);
            }
        }

        return tr;

    }

    public List<Transaction> getTransactionByDateRange(LocalDate start, LocalDate end){
        List<Transaction> tr = new ArrayList<>();
        for(Transaction t : transactions){
            if(!t.getDate().isBefore(start) && !t.getDate().isAfter(end)){
                tr.add(t);
            }
        }

        return tr;
    }
}