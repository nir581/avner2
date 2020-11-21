package main;

public class RoutineOutput
{
	private int sumOfTikrot;
	private double smas;
	private int resultForNextStep;
	private int tikra;
	private double tax;
	private int index;
	private int sh20;
	private int sh25;

	public RoutineOutput(int sumOfTikrot, double smas, int resultForNextStep, int tikra, double tax, int index)
	{
		this.sumOfTikrot = sumOfTikrot;
		this.smas = smas;
		this.resultForNextStep = resultForNextStep;
		this.tikra = tikra;
		this.tax = tax;
		this.index = index;
	}

	public int getSumOfTikrot()
	{
		return sumOfTikrot;
	}

	public void setSumOfTikrot(int sumOfTikrot)
	{
		this.sumOfTikrot = sumOfTikrot;
	}

	public double getSmas()
	{
		return smas;
	}

	public void setSmas(double smas)
	{
		this.smas = smas;
	}

	public int getResultForNextStep()
	{
		return resultForNextStep;
	}

	public void setResultForNextStep(int resultForNextStep)
	{
		this.resultForNextStep = resultForNextStep;
	}

	public int getTikra()
	{
		return tikra;
	}

	public void setTikra(int tikra)
	{
		this.tikra = tikra;
	}

	public double getTax()
	{
		return tax;
	}

	public void setTax(double tax)
	{
		this.tax = tax;
	}

	public int getIndex()
	{
		return index;
	}

	public int getSh20()
	{
		return sh20;
	}

	public void setSh20(int sh20)
	{
		this.sh20 = sh20;
	}

	public int getSh25()
	{
		return sh25;
	}

	public void setSh25(int sh25)
	{
		this.sh25 = sh25;
	}
}
