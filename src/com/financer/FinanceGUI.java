//Run this one for gui manipulation of your transactions
package com.financer;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
 public class FinanceGUI {
    private JFrame frame;
    private JLabel balanceLabel;

    public FinanceGUI(){
        frame = new JFrame("Your Finance Tracker");
        frame.setSize(800,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setBackground(new Color(101, 40, 40));

        JPanel northPanel = new JPanel(new GridLayout(2 ,1));
        northPanel.setBackground(new Color(50,50,50));
        northPanel.setBorder(BorderFactory.createEmptyBorder(10,0,10,0));

        JLabel titleLabel = new JLabel("Finance Tracker Dashboard");
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setFont(new Font("Arial",Font.BOLD,24));
        titleLabel.setForeground(Color.white);

        balanceLabel = new JLabel("Current Balance: $0.00");
        balanceLabel.setHorizontalAlignment(JLabel.CENTER);
        balanceLabel.setFont(new Font("Arial",Font.ITALIC,18));
        northPanel.add(titleLabel);
        northPanel.add(balanceLabel);

        frame.add(northPanel,BorderLayout.NORTH);

        String[] columns = {"id","type","category","amount","date"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        JTable table = new JTable(model);
        table.setBackground(new Color(200,200,200));
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif",Font.PLAIN,14));
        table.getTableHeader().setFont(new Font("SansSerif",Font.PLAIN,15));
        table.getTableHeader().setBackground(Color.LIGHT_GRAY);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);

        for(int i = 0; i < table.getColumnCount(); i++){
            table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        table.setSelectionBackground(new Color(173,216,230));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.getViewport().setBackground(new Color(71, 53, 53));
        frame.add(scrollPane,BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setBackground(new Color(50,50,50));

        //Dimension size = new Dimension(150,40);

        JButton refreshButton = new JButton("Refresh");
        styleButton(refreshButton,new Color(0,255,200));

        JButton addButton = new JButton("Add Transaction");
        styleButton(addButton,new Color(150,200,255));
        JButton deleteButton = new JButton("Delete Selected");
        styleButton(deleteButton,Color.red);

        buttonPanel.add(refreshButton);
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        frame.add(buttonPanel,BorderLayout.SOUTH);

        refreshButton.addActionListener(e->{
            loadData(model);

            JOptionPane.showMessageDialog(frame,"Data Successfully refreshed");
        });

        deleteButton.addActionListener(e->{
            int selectedRow = table.getSelectedRow();

            if(selectedRow==-1){
                JOptionPane.showMessageDialog(frame,"Please Select a row first","Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            Integer idToDelete = (Integer) model.getValueAt(selectedRow,0);
            com.db.DatabaseHandler.deleteTransaction(idToDelete);
            loadData(model);
            JOptionPane.showMessageDialog(frame,"Transaction #"+idToDelete +" deleted.");
        });

        addButton.addActionListener(e->{
            JDialog addDialog = new JDialog(frame,"Add new Transaction",true);

            addDialog.setLayout(new GridLayout(5,2,10,10));
            addDialog.setSize(400,300);
            addDialog.getContentPane().setBackground(new Color(245,245,245));
            Font labelFont = new Font("Arial",Font.BOLD,14);


            JComboBox<TransactionType> typeBox = new JComboBox<>(TransactionType.values());
            JComboBox<Category> catBox = new JComboBox<>(Category.values());
            JTextField amountField = new JTextField();
            JButton saveButton = new JButton("Save");
            styleButton(saveButton,Color.green);
            saveButton.setMargin(new Insets(5,5,5,5));

            JLabel typeLabel = new JLabel("Type: ");
            typeLabel.setFont(labelFont);

            JLabel catLabel = new JLabel("Category: ");
            catLabel.setFont(labelFont);

            JLabel amtLabel = new JLabel("Amount($): ");
            amtLabel.setFont(labelFont);

            addDialog.add(typeLabel);
            addDialog.add(typeBox);
            addDialog.add(catLabel);
            addDialog.add(catBox);
            addDialog.add(amtLabel);
            addDialog.add(amountField);
            addDialog.add(new JLabel(""));
            addDialog.add(saveButton);

            saveButton.addActionListener(saveEvent->{
                try{
                    TransactionType type = (TransactionType) typeBox.getSelectedItem();
                    Category cat = (Category) catBox.getSelectedItem();
                    double amount = Double.parseDouble(amountField.getText());
                    if(amount<=0){
                        JOptionPane.showMessageDialog(addDialog,"Invalid amount!","Error",JOptionPane.ERROR_MESSAGE);
                    }
                   else{
                        Transaction t = new Transaction(type,cat,amount,java.time.LocalDate.now());
                        com.db.DatabaseHandler.saveTransaction(t);
                        addDialog.dispose();
                        loadData(model);
                        JOptionPane.showMessageDialog(frame,"Transaction added Successfully!");
                    }
                }catch(NumberFormatException ex ){
                    JOptionPane.showMessageDialog(addDialog,"Please  enter valid numeric amount of income/expense","Error",JOptionPane.ERROR_MESSAGE);
                }
            });
            addDialog.setLocationRelativeTo(frame);
            addDialog.setVisible(true);
        });

        loadData(model);
        frame.setVisible(true);
    }
    private void loadData(DefaultTableModel model){
        model.setRowCount(0);
        List<Transaction> transactions = com.db.DatabaseHandler.getAllTransactions();
        FinanceTracker tempTracker = new FinanceTracker();

        for(Transaction t : transactions){
            tempTracker.addTransaction(t);
            Object[] rawData ={

                    t.getId(),
                    t.getType(),
                    t.getCategory(),
                    t.getAmount(),
                    t.getDate()
            };
            model.addRow(rawData);
        }
        double balance = tempTracker.calculateBalance();
        balanceLabel.setText(String.format("Current balance: $%.2f",balance));
        if(balance<0){
            balanceLabel.setForeground(Color.red);
        }
        else balanceLabel.setForeground(new Color(34,139,34));
    }
    public static void main(String[] args){
        com.db.DatabaseHandler.initializeDatabase();

        SwingUtilities.invokeLater(()->{
            new FinanceGUI();
        });
    }
    private void styleButton(JButton btn,Color bgColor){
        btn.setBackground(bgColor);
        btn.setFocusable(false);
        btn.setForeground(Color.white);
        btn.setFont(new Font("Arial",Font.BOLD,13));
        btn.setMargin(new Insets(8,20,8,20));

    }
}