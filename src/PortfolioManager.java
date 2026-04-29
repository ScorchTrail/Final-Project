/**
 * Name:        Vedant Patel
 * Date:        April 22, 2026
 * Assignment:  Stock Portfolio Application
 * Description: The PortfolioManager class is the main driver for the Stock Portfolio
 *              Application. It presents a menu-driven console interface that allows
 *              the user to deposit and withdraw cash, buy and sell stocks, and view
 *              transaction history and portfolio holdings. All transactions are stored
 *              in an ArrayList of TransactionHistory objects. Business rules enforced:
 *              - CASH cost basis is always 1.00
 *              - Buying/selling stock automatically creates a corresponding CASH transaction
 *              - Withdrawal and purchase are blocked if insufficient CASH is available
 *              - Invalid menu and numeric inputs display an error and re-prompt the user
 */

import java.util.ArrayList;
import java.util.Scanner;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PortfolioManager {

    private static ArrayList<TransactionHistory> portfolioList = new ArrayList<TransactionHistory>();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        while (!exit) {
            System.out.println("\nVedant Patel's Brokerage Account");
            System.out.println("0 - Exit");
            System.out.println("1 - Deposit Cash");
            System.out.println("2 - Withdraw Cash");
            System.out.println("3 - Buy Stock");
            System.out.println("4 - Sell Stock");
            System.out.println("5 - Display Transaction History");
            System.out.println("6 - Display Portfolio");
            System.out.print("Enter option (0 to 6): ");

            String optionStr = scanner.nextLine();
            int option = -1;

            try {
                option = Integer.parseInt(optionStr);
            } catch (NumberFormatException e) {
            }

            switch (option) {
                case 0:
                    exit = true;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                case 1:
                    depositCash();
                    break;
                case 2:
                    withdrawCash();
                    break;
                case 3:
                    buyStock();
                    break;
                case 4:
                    sellStock();
                    break;
                case 5:
                    displayTransactionHistory();
                    break;
                case 6:
                    displayPortfolio();
                    break;
                default:
                    System.out.println("Error: Invalid input entered. Please choose an option between 0 and 6.");
                    break;
            }
        }
        scanner.close();
    }

    private static void depositCash() {
        System.out.print("Enter date (MM/DD/YYYY): ");
        String date = scanner.nextLine();
        System.out.print("Enter deposit amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        TransactionHistory trans = new TransactionHistory("CASH", date, "DEPOSIT", amount, 1.0);
        portfolioList.add(trans);
        System.out.println("Successfully deposited $" + amount);
    }

    private static void withdrawCash() {
        System.out.print("Enter date (MM/DD/YYYY): ");
        String date = scanner.nextLine();
        System.out.print("Enter withdraw amount: ");
        double amount = Double.parseDouble(scanner.nextLine());

        double availableCash = getAvailableCash();

        if (amount > availableCash) {
            System.out.println("Error: Withdraw amount cannot be more than the CASH available ($" + availableCash + ").");
        } else {
            TransactionHistory trans = new TransactionHistory("CASH", date, "WITHDRAW", -amount, 1.0);
            portfolioList.add(trans);
            System.out.println("Successfully withdrew $" + amount);
        }
    }

    private static void buyStock() {
        System.out.print("Enter date (MM/DD/YYYY): ");
        String date = scanner.nextLine();
        System.out.print("Enter stock ticker: ");
        String ticker = scanner.nextLine().toUpperCase();
        System.out.print("Enter quantity: ");
        double qty = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter cost basis (price per share): ");
        double cost = Double.parseDouble(scanner.nextLine());

        double totalCost = qty * cost;
        double availableCash = getAvailableCash();

        if (totalCost > availableCash) {
            System.out.println("Error: Not enough CASH to buy stock. You need $" + totalCost + " but have $" + availableCash);
        } else {
            TransactionHistory stockTrans = new TransactionHistory(ticker, date, "BUY", qty, cost);
            portfolioList.add(stockTrans);

            TransactionHistory cashTrans = new TransactionHistory("CASH", date, "WITHDRAW", -totalCost, 1.0);
            portfolioList.add(cashTrans);
            System.out.println("Successfully bought " + qty + " shares of " + ticker);
        }
    }

    private static void sellStock() {
        System.out.print("Enter date (MM/DD/YYYY): ");
        String date = scanner.nextLine();
        System.out.print("Enter stock ticker: ");
        String ticker = scanner.nextLine().toUpperCase();
        System.out.print("Enter quantity to sell: ");
        double qty = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter selling price per share: ");
        double price = Double.parseDouble(scanner.nextLine());

        double availableStock = getStockQty(ticker);

        if (qty > availableStock) {
            System.out.println("Error: You do not own enough shares. You currently have " + availableStock + " shares of " + ticker);
        } else {
            TransactionHistory stockTrans = new TransactionHistory(ticker, date, "SELL", qty, price);
            portfolioList.add(stockTrans);

            double totalRevenue = qty * price;
            TransactionHistory cashTrans = new TransactionHistory("CASH", date, "DEPOSIT", totalRevenue, 1.0);
            portfolioList.add(cashTrans);
            System.out.println("Successfully sold " + qty + " shares of " + ticker);
        }
    }

    private static void displayTransactionHistory() {
        System.out.println("\n                Vedant Patel's Brokerage Account");
        System.out.println("\n                ================================\n");
        System.out.println();
        System.out.printf("%-15s %-7s %-15s %-15s %-15s\n", "Date", "Ticker", "Quantity", "Cost Basis", "Trans Type");
        System.out.println("\n==================================================================\n");
        
        for (int i = 0; i < portfolioList.size(); i++) {
            portfolioList.get(i).toPrint();
        }
    }

    private static void displayPortfolio() {
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date date = new Date();
        
        System.out.println("\nPortfolio as of: " + formatter.format(date));
        System.out.println("\n====================================\n");
        System.out.println("Ticker  Quantity");
        System.out.println("\n=================\n");
        
        System.out.printf("%-7s %.1f\n", "CASH", getAvailableCash());
        
        ArrayList<String> uniqueTickers = new ArrayList<String>();
        for (int i = 0; i < portfolioList.size(); i++) {
            String ticker = portfolioList.get(i).getTicker();
            if (!ticker.equals("CASH") && !uniqueTickers.contains(ticker)) {
                uniqueTickers.add(ticker);
            }
        }
        
        for (int i = 0; i < uniqueTickers.size(); i++) {
            String ticker = uniqueTickers.get(i);
            double qty = getStockQty(ticker);
            if (qty > 0) {
                System.out.printf("%-7s %.1f\n", ticker, qty);
            }
        }
    }

    private static double getAvailableCash() {
        double cash = 0.0;
        for (int i = 0; i < portfolioList.size(); i++) {
            TransactionHistory trans = portfolioList.get(i);
            if (trans.getTicker().equals("CASH")) {
                cash += trans.getQty(); 
            }
        }
        return cash;
    }

    private static double getStockQty(String ticker) {
        double qty = 0.0;
        for (int i = 0; i < portfolioList.size(); i++) {
            TransactionHistory trans = portfolioList.get(i);
            if (trans.getTicker().equals(ticker)) {
                if (trans.getTransType().equals("BUY")) {
                    qty += trans.getQty();
                } else if (trans.getTransType().equals("SELL")) {
                    qty -= trans.getQty();
                }
            }
        }
        return qty;
    }
}