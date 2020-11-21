package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

class RunHelper
{
	/**
	 *
	 * @param divisionYears
	 * @param birthDate
	 * @return a map of Year to Age (what was the age of the client at every year)
	 */
	static Map<Integer, Integer> getMapYearToAge(Set<Integer> divisionYears, LocalDate birthDate)
	{
		Map<Integer, Integer> yearToAge = new LinkedHashMap<>();
		divisionYears.forEach(year -> {
			LocalDate relevantDate = LocalDate.of(year, 12, 31);
			int age = getAgeInYears(birthDate, relevantDate);
			yearToAge.put(year, age);
		});
		return yearToAge;
	}

	private static int getAgeInYears(LocalDate birthDate, LocalDate relevantYear)
	{
		return Period.between(birthDate, relevantYear).getYears();
	}

	static String printWithCommas(Number number)
	{
		return NumberFormat.getNumberInstance(Locale.US).format(number);
	}

	static int round(double d)
	{
		return new BigDecimal(Double.toString(d)).setScale(0, RoundingMode.HALF_DOWN).intValue();
	}

	static void print(UserData input, List<RoutineOutput> outputList)
	{
		System.out.println("---------------------------------");
		System.out.println("Year: " + input.getYear()+" | Age: " + input.getAge() + " | ShevahshuliOfYear: " + printWithCommas(input.getShMax())
				+ " | Shevah20OfYear: " + printWithCommas(input.getSh20())  + " | Shevah25OfYear: " + printWithCommas(input.getSh25()));
		System.out.println();
		int totalShana = 0;
		double totalMasShana = 0;
		for (RoutineOutput o : outputList) {
			int tikra = o.getTikra();
			totalShana += tikra;
			double smas = o.getSmas();
			totalMasShana += smas;
			int index = o.getIndex();
			String s = "HASHLAMA LE TIKRA%s = %s | TAX%s = %s | SMAS%s = %s";
			System.out.println(String.format(s, index, printWithCommas(tikra), index, getPercentageStr(o.getTax()), index, printWithCommas(Double.valueOf(smas).intValue())));
		}
		double taxShana = totalMasShana / totalShana;
		if (Double.isNaN(taxShana)) {
			taxShana = 0;
		}
		final String totalShanaStr = printWithCommas(totalShana);
		final String totalMasShanaStr = printWithCommas(Double.valueOf(totalMasShana).intValue());
		final BigDecimal totalTaxShanaStr = new BigDecimal(Double.toString(taxShana)).setScale(3, RoundingMode.HALF_DOWN);
		System.out.println("TOTALSHANA: " + totalShanaStr + "| TOTALMASSHANA: " + totalMasShanaStr + " | TAXSHANA: " + totalTaxShanaStr + " | ");
		System.out.println();
	}

	private static String getPercentageStr(double d)
	{
		BigDecimal bd = new BigDecimal(Double.toString(d));
		NumberFormat formatter = NumberFormat.getPercentInstance();
		BigDecimal bdWith2Digits = bd.setScale(2, RoundingMode.HALF_DOWN);
		return formatter.format(bdWith2Digits);
	}
}
