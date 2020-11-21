package main;

public class TaxRecord
{
	private int max;
	private double tax;

	public TaxRecord(int max, double tax)
	{
		this.max = max;
		this.tax = tax;
	}

	public int getMax()
	{
		return max;
	}

	public double getTax()
	{
		return tax;
	}
}
