/**
 * Name:        Vedant Patel
 * Date:        April 20, 2026
 * Assignment:  Stock Portfolio Application
 * Description: The TransactionHistory class represents a single transaction record
 *              in a brokerage account. It stores the stock ticker (or CASH), the
 *              transaction date, transaction type (BUY/SELL/DEPOSIT/WITHDRAW),
 *              quantity, and cost basis. Includes default and overloaded constructors
 *              along with getter and setter methods for all fields.
 */

public class TransactionHistory {
    
    private String ticker;
    private String transDate;
    private String transType;
    private double qty;
    private double costBasis;

    public TransactionHistory() {
        this.ticker = "";
        this.transDate = "";
        this.transType = "";
        this.qty = 0.0;
        this.costBasis = 0.0;
    }

    public TransactionHistory(String ticker, String transDate, String transType, double qty, double costBasis) {
        this.ticker = ticker;
        this.transDate = transDate;
        this.transType = transType;
        this.qty = qty;
        this.costBasis = costBasis;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getTransDate() {
        return transDate;
    }

    public void setTransDate(String transDate) {
        this.transDate = transDate;
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public double getQty() {
        return qty;
    }

    public void setQty(double qty) {
        this.qty = qty;
    }

    public double getCostBasis() {
        return costBasis;
    }

    public void setCostBasis(double costBasis) {
        this.costBasis = costBasis;
    }

    public void toPrint() {
        System.out.printf("%-15s %-7s %-15.1f $%-14.1f %-15s\n", transDate, ticker, qty, costBasis, transType);
    }
}