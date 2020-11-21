package main;

public class RoutineInput
{
	private int index;
	private int val1;
	private int val2;
	private int max;
	private double tax;
	private int sumOfTikrotTillNow;
	private int shevahshuliYear;

	public RoutineInput(int index, int val1, int val2, int max, double tax, int sumOfTikrotTillNow, int shevahshuliYear)
	{
		this.index = index;
		this.val1 = val1;
		this.val2 = val2;
		this.max = max;
		this.tax = tax;
		this.sumOfTikrotTillNow = sumOfTikrotTillNow;
		this.shevahshuliYear = shevahshuliYear;
	}

	public int getIndex()
	{
		return index;
	}

	public void setIndex(int index)
	{
		this.index = index;
	}

	public int getVal1()
	{
		return val1;
	}

	public void setVal1(int val1)
	{
		this.val1 = val1;
	}

	public int getVal2()
	{
		return val2;
	}

	public void setVal2(int val2)
	{
		this.val2 = val2;
	}

	public int getMax()
	{
		return max;
	}

	public double getTax()
	{
		return tax;
	}

	public int getSumOfTikrotTillNow()
	{
		return sumOfTikrotTillNow;
	}

	public void setSumOfTikrotTillNow(int sumOfTikrotTillNow)
	{
		this.sumOfTikrotTillNow = sumOfTikrotTillNow;
	}

	public int getShevahshuliYear()
	{
		return shevahshuliYear;
	}

	public void setShevahshuliYear(int shevahshuliYear)
	{
		this.shevahshuliYear = shevahshuliYear;
	}
}
