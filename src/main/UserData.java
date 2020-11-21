package main;

import java.util.Map;

public class UserData
{
	private int yish;
	private int year;
	private int age;
	private int shMax;
	private int sh20;
	private int sh25;
	private Map<Integer, TaxRecord> tav;

	public UserData(int yish, int year, int age, int shMax, int sh20, int sh25, Map<Integer, TaxRecord> tav)
	{
		this.yish = yish;
		this.year = year;
		this.age = age;
		this.shMax = shMax;
		this.sh20 = sh20;
		this.sh25 = sh25;
		this.tav = tav;
	}

	public int getYear()
	{
		return year;
	}

	public void setYear(int year)
	{
		this.year = year;
	}

	public int getAge()
	{
		return age;
	}

	public void setAge(int age)
	{
		this.age = age;
	}

	public int getShMax()
	{
		return shMax;
	}

	public void setShMax(int shMax)
	{
		this.shMax = shMax;
	}

	public int getYish()
	{
		return yish;
	}

	public Map<Integer, TaxRecord> getTav()
	{
		return tav;
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
