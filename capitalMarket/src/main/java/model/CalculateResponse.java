package model;

public class CalculateResponse {
	private String symbol;
	private double currentPrice;
    private double totalValue;


    public CalculateResponse() {
    }

    public CalculateResponse(String symbol,double currentPrice, double totalValue) {
    	this.symbol = symbol;
    	this.currentPrice = currentPrice;
        this.totalValue = totalValue;
    }

   

	public double getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(double currentPrice) {
		this.currentPrice = currentPrice;
	}

	public double getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(double totalValue) {
		this.totalValue = totalValue;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

}
