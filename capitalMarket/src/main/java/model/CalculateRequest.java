package model;

public class CalculateRequest {
	   private String symbol;
	    private int quantity;

	    public CalculateRequest() {
	    }

	    public CalculateRequest(String symbol, int quantity) {
	        this.symbol = symbol;
	        this.quantity = quantity;
	    }

	    public int getQuantity() {
	        return quantity;
	    }

	    public void setQuantity(int quantity) {
	        this.quantity = quantity;
	    }

		public String getSymbol() {
			return symbol;
		}

		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}
	}