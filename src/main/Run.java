package main;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

// last updated in 02/01/2020
public class Run
{
	// TODO: ????????????????????????????????????????
	// Unify all prints
	//Print total sum of sum and total sum of max and SUM MAS / SUM
	// order to the years in print/
	/**
	 * ליישר את ההדפסות כך ש: מס 1 ישב מתחת לתקרה 1 והסכומים יהיו באותה העמודה (עימוד)
	 * כל שנה להוסיף הדפסה:
	 *
	 *
	 */


	private Map<Integer, Integer> yearToYish = new HashMap<>();
	private LocalDate birthDate;
	private int shevshuli;
	private int shevah20;
	private int shevah25;
	private static final Map<Integer, Map<Integer, TaxRecord>> TABLES_MAP = Data.getInstance().getTablesMap();
	private final Scanner scanner = new Scanner(System.in);  // Create a Scanner object

	public static void main(String[] args) throws Exception
	{
		Run run = new Run();
		run.run();
	}

	private void run() throws Exception
	{
		final boolean realInput = false; // TODO: FOR PROD!!!!
		if (realInput) {
			System.out.print("Enter Birth Date in the following format dd-mm-yyyy, for example 01-01-1959, and press enter to continue: ");
			String birthDateStr = scanner.next();
			birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			System.out.print("Enter ShevahSuli for all years combined and press enter to continue: ");
			shevshuli = scanner.nextInt();
			System.out.print("Enter Shevah20 for all years combined and press enter to continue: ");
			shevah20 = scanner.nextInt();
			System.out.print("Enter Shevah25 for all years combined and press enter to continue: ");
			shevah25 = scanner.nextInt();
			System.out.print("Enter an array of Year and YegiaIshit for that year with a comma (,) between each input in a single line. For example: 2016,300000,2017,400000 and press enter to continue: ");
			String yearsToYishString = scanner.next();
			String[] strArr = yearsToYishString.split(",");
			for (int i=0; i<strArr.length; i+=2) {
				int year = Integer.parseInt(strArr[i]);
				int yish = Integer.parseInt(strArr[i+1]);
				yearToYish.put(year, yish);
			}
		}
		else {
			final String birthDateStr = "08-01-1959";
			birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
			shevshuli = 1;
			shevah20 = 1;
			shevah25 = 146525;
			yearToYish = Map.of(2020,60000, 2017,88911, 2018,91806, 2019,86498);
			//yearToYish = Map.of(2019,259641);
		}

		// 1. Collecting data from the user
		final Map<Integer, Integer> yearToAgeMap = RunHelper.getMapYearToAge(yearToYish.keySet(), birthDate);
		final int numOfYears = yearToAgeMap.size();
		final int shevshuliPerYear = shevshuli / numOfYears;
		final int shevah20PerYear = shevah20 / numOfYears;
		final int shevah25PerYear = shevah25 / numOfYears;

		List<UserData> userDataList = yearToAgeMap.entrySet().stream().map(entry -> {
			final int year = entry.getKey();
			final int age = entry.getValue();
			final int yish = yearToYish.get(year);
			final Map<Integer, TaxRecord> tav = TABLES_MAP.get(year);
			return new UserData(yish, year, age, shevshuliPerYear, shevah20PerYear, shevah25PerYear, tav);
		}).sorted(Comparator.comparing(UserData::getYear)).collect(Collectors.toList());

		for (UserData userData : userDataList) {
			inspectUserData(userData);
		}

		System.out.println();
		System.out.println("---- FINISHED !!! In order to start program again enter the digit 1 and press enter, otherwise close the program! -----");

		int shouldGoAgain = scanner.nextInt();
		if (shouldGoAgain > 0) {
			System.out.println();
			run();
		}
	}

	/**
	 * Decide how many iteration the input data will be processed
	 *
	 * @param userData
	 */
	private void inspectUserData(UserData userData) throws Exception
	{
		int shevshuli = userData.getShMax();
		int sh20 = userData.getSh20();
		int sh25 = userData.getSh25();
		int yish = userData.getYish();
		int age = userData.getAge();
		Map<Integer, TaxRecord> tav = userData.getTav();

		if (yish > tav.get(5).getMax()) {            // special case #1
			final double tax6 = tav.get(6).getTax();
			final int tikra6 = shevshuli;
			final double sMas6 = tikra6 * tax6;
			RunHelper.print(userData, Collections.singletonList(new RoutineOutput(-1, sMas6, -1, tikra6, tax6, 6)));
		}
		else {
			int index;
			if (age < 60) {                        // special case #2
				index = getSelectedMaxIndex(yish, tav, 4);
			}
			else {									 // regular case: age >= 60
				index = getSelectedMaxIndex(yish, tav, 1);
			}
			Map<Integer, RoutineOutput> indexToOutputMap = runIterations(index, yish, shevshuli, tav);
			RunHelper.print(userData, new ArrayList<>(indexToOutputMap.values()));
			if (age < 60) {
				final double sh20Total = sh20 * 0.2;
				System.out.println("TOTALMASSHANASh20: " + RunHelper.round(sh20Total));
				final double sh25Total = sh25 * 0.25;
				System.out.println("TOTALMASSHANASh25: " + RunHelper.round(sh25Total));
			}
			else {
				// TODO: SHEVah 20 + 25
				calculateShevah20(userData);
				calculateShevah25(userData);
			}
		}
	}

	private Map<Integer, RoutineOutput> runIterations(int index, int yish, int shevshuli, Map<Integer, TaxRecord> tav)
	{
		int maxIteration = 5;
		Map<Integer, RoutineOutput> indexToOutputMap = new HashMap<>();
		Map<Integer, RoutineInput> indexToInputMap = new HashMap<>();
		for (int i = index; i <= maxIteration; i++) {
			RoutineInput routineInput;
			RoutineOutput routineOutput;
			if (i == index) {
				TaxRecord taxRecord = tav.get(i);
				routineInput = new RoutineInput(i, yish, shevshuli, taxRecord.getMax(), taxRecord.getTax(), 0, shevshuli);
			}
			else {
				RoutineInput prevRoutineInput = indexToInputMap.get(i - 1);
				RoutineOutput prevRoutineOutput = indexToOutputMap.get(i - 1);
				TaxRecord taxRecord = tav.get(i);
				routineInput = new RoutineInput(i, prevRoutineInput.getMax(), prevRoutineOutput.getResultForNextStep(), taxRecord.getMax(), taxRecord.getTax(),
						prevRoutineOutput.getSumOfTikrot(), shevshuli);
			}
			routineOutput = routine(routineInput); // RUN
			indexToInputMap.put(i, routineInput);
			indexToOutputMap.put(i, routineOutput);
			if (routineOutput.getResultForNextStep() == 0) { // STOP RUN
				break;
			}
			// Handle 6 iteration (partial iteration)
			if (i == 5) {
				RoutineOutput routineOutput5 = indexToOutputMap.get(5);
				double tax6 = tav.get(6).getTax();
				int tikra6 = routineOutput5.getResultForNextStep();
				double sMas6 = tikra6 * tax6;
				RoutineOutput routineOutput6 = new RoutineOutput(-1, sMas6, -1, tikra6, tax6, 6);
				indexToOutputMap.put(6, routineOutput6);
			}
		}
		return indexToOutputMap;
	}

	private RoutineOutput routine(RoutineInput input)
	{
		// input data
		int val1 = input.getVal1();        // if first iteration == yish, else prevMax
		int val2 = input.getVal2();        // if first iteration == shevahsuli, else resultFromPrevStepForCurrentStep
		int currentMax = input.getMax();
		double currentTax = input.getTax();
		int sumOfTikrotTillNow = input.getSumOfTikrotTillNow();  // if first iteration == 0, else should  be > 0.
		int shevahshuliPerYear = input.getShevahshuliYear();
		int index = input.getIndex();

		//output data
		int tikra;
		double currentSMas;
		int nextResult = 0;

		//routine
		int currentResult = currentMax - val1;
		if (val2 <= currentResult) {
			tikra = val2;
		}
		else {
			tikra = currentResult;
			nextResult = shevahshuliPerYear - (tikra + sumOfTikrotTillNow);
		}
		currentSMas = tikra * currentTax;
		return new RoutineOutput(sumOfTikrotTillNow + tikra, currentSMas, nextResult, tikra, currentTax, index);
	}

	/**
	 * @param yish
	 * @param tav
	 * @param startIndex
	 * @return
	 * @throws Exception
	 */
	private int getSelectedMaxIndex(int yish, Map<Integer, TaxRecord> tav, int startIndex) throws Exception
	{
		for (int i = startIndex; i <= 6; i++) {
			int max = tav.get(i).getMax();
			if (yish <= max) {
				return i;
			}
		}
		throw new Exception("getSelectedMaxIndex didn't find a match");
	}

	private void calculateShevah20(UserData userData)
	{
		int shevshuli = userData.getShMax();
		int sh20 = userData.getSh20();
		int yish = userData.getYish();
		final int year = userData.getYear();
		final Map<Integer, TaxRecord> tav = userData.getTav();

		int tikra11 = 0;
		int tikra12 = 0;
		int tikra13 = 0;
		double smas11 = 0;
		double smas12 = 0;
		double smas13 = 0;

		final int result = yish + shevshuli;
		int result20 = 0;
		if (result <= tav.get(1).getMax()) {
			result20 = tav.get(1).getMax() - result;
			if (sh20 <= result20) {
				tikra11 = sh20;
				smas11 = tikra11 * tav.get(1).getTax();
				// TODO: PRINT
			}
			else {
				tikra11 = result20;
				smas11 = tikra11 * tav.get(1).getTax();
				result20 = sh20 - tikra11;
				if (result20 <= (tav.get(2).getMax() - tav.get(1).getMax())) {
					tikra12 = result20;
					smas12 = tikra12 * tav.get(2).getTax();
					// TODO: PRINT
				}
				else {
					tikra12 = tav.get(2).getMax() - tav.get(1).getMax();
					smas12 = tikra12 * tav.get(2).getTax();
					tikra13 = sh20 - (tikra11 + tikra12);
					smas13 = tikra13 * 0.2; // CHANGED FROM tav.get(3).getTax() to 0.2
					// TODO: PRINT

				}
			}
		}
		else if (result > tav.get(1).getMax() && result <= tav.get(2).getMax()) {
			result20 = tav.get(2).getMax() - result;
			if (sh20 <= result20) {
				tikra12 = sh20;
				smas12 = tikra12 * tav.get(2).getTax();
				// TODO: PRINT
			}
			else {
				tikra12 = result20;
				smas12 = tikra12 * tav.get(2).getTax();
				tikra13 = sh20 - tikra12;
				smas13 = tikra13 * 0.2;
				// TODO: PRINT
			}
		}
		else if (result > tav.get(2).getMax()) {
			tikra13 = sh20;
			smas13 = tikra13 * 0.2;
			// TODO: PRINT
		}

		// PRINT
		int totalShanaSh20 = tikra11 + tikra12 + tikra13;
		double totalMasShanaSh20 = smas11 + smas12 + smas13;
		double totalTaxShanaSh20 = totalMasShanaSh20 / totalShanaSh20;
		if (Double.isNaN(totalTaxShanaSh20)) {
			totalTaxShanaSh20 = 0;
		}

		// System.out.println("Year: " + year);
		System.out.println(String.format("TIKRA1 = %s,TIKRA2 = %s, TIKRA3 = %s", tikra11,tikra12,tikra13));
		System.out.println(String.format("MAS1 = %s,MAS2 = %s, MAS3 = %s", RunHelper.round(smas11), RunHelper.round(smas12), RunHelper.round(smas13)));
		final String totalShanaSh20Str = RunHelper.printWithCommas(totalShanaSh20);
		final String totalMasShanaSh20Str = RunHelper.printWithCommas(RunHelper.round(totalMasShanaSh20));
		final String totalTaxShanaSh20Str = new BigDecimal(Double.toString(totalTaxShanaSh20)).setScale(3, RoundingMode.HALF_DOWN).toPlainString();
		System.out.println("TOTALSHANASh20: " + totalShanaSh20Str + "| TOTALMASSHANASh20: " + totalMasShanaSh20Str + " | TAXSHANASh20: " + totalTaxShanaSh20Str + " | ");
	}

	private void calculateShevah25(UserData userData)
	{
		int shevshuli = userData.getShMax();
		int sh20 = userData.getSh20();
		int sh25 = userData.getSh25();
		int yish = userData.getYish();
		final int year = userData.getYear();
		final Map<Integer, TaxRecord> tav = userData.getTav();

		int tikra111 = 0;
		int tikra112 = 0;
		int tikra113 = 0;
		int tikra114 = 0;
		double smas111 = 0;
		double smas112 = 0;
		double smas113 = 0;
		double smas114 = 0;

		final int result = yish + shevshuli + sh20;
		int result25 = 0;
		if (result <= tav.get(1).getMax()) {
			result25 = tav.get(1).getMax() - result;
			if (sh25 <= result25) {
				tikra111 = sh25;
				smas111 = tikra111 * tav.get(1).getTax();
				// TODO: PRINT
			}
			else {
				tikra111 = result25;
				smas111 = tikra111 * tav.get(1).getTax();
				result25 = sh25 - tikra111;
				if (result25 <= (tav.get(2).getMax() - tav.get(1).getMax())) {
					tikra112 = result25;
					smas112 = tikra112 * tav.get(2).getTax();
					// TODO: PRINT
				}
				else {
					tikra112 = tav.get(2).getMax() - tav.get(1).getMax();
					smas112 = tikra112 * tav.get(2).getTax();
					result25 = sh25 - (tikra111 + tikra112);  // TODO: RESULT 25 or TIKRA113

					if (result25 <= (tav.get(3).getMax() - tav.get(2).getMax())) {
						tikra113 = result25;
						smas113 = tikra113 * 0.2;
						// TODO: PRINT
					}
					else {
						tikra113 = tav.get(3).getMax() - tav.get(2).getMax();
						smas113 = tikra113 * 0.2;
						tikra114 = sh25 - (tikra111 + tikra112 + tikra113);
						smas114 = tikra114 * 0.25;
						// TODO: PRINT
					}
				}
			}
		}
		else if (result > tav.get(1).getMax() && result <= tav.get(2).getMax()) {
			result25 = tav.get(2).getMax() - result;
			if (sh25 <= result25) {
				tikra112 = sh25;
				smas112 = tikra112 * tav.get(2).getTax();
				// TODO: PRINT
			}
			else {
				tikra112 = result25;
				smas112 = tikra112 * tav.get(2).getTax();
				result25 = sh25 - tikra112;

				if (result25 <= (tav.get(3).getMax() - tav.get(2).getMax())) {
					tikra113 = result25;
					smas113 = tikra113 * 0.2;
					// TODO: PRINT
				}
				else {
					tikra113 = tav.get(3).getMax() - tav.get(2).getMax();
					smas113 = tikra113 * 0.2;
					tikra114 = sh25 - (tikra112 + tikra113);
					smas114 = tikra114 * 0.25;
					// TODO: PRINT
				}
			}
		}
		else if (result > tav.get(2).getMax() && result <= tav.get(3).getMax()) {
			result25 = tav.get(3).getMax() - result;
			if (sh25 <= result25) {
				tikra113 = sh25;
				smas113 = tikra113 * 0.2;
				// TODO: PRINT
			}
			else {
				tikra113 = result25;
				smas113 = tikra113 * 0.2;
				tikra114 = sh25 - tikra113;
				smas114 = tikra114 * 0.25;
				// TODO: PRINT
			}
		}
		else if (result > tav.get(3).getMax()) {
			tikra114 = sh25;
			smas114 = tikra114 * 0.25;
			// TODO: PRINT
		}

		// PRINT
		int totalShanaSh25 = tikra111 + tikra112 + tikra113 + tikra114;
		double totalMasShanaSh25 = smas111 + smas112 + smas113 + smas114;
		double totalTaxShanaSh25 = totalMasShanaSh25 / totalShanaSh25;
		if (Double.isNaN(totalTaxShanaSh25)) {
			totalTaxShanaSh25 = 0;
		}

		// System.out.println("Year: " + year);
		System.out.println();
		System.out.println(String.format("TIKRA1 = %s,TIKRA2 = %s, TIKRA3 = %s, TIKRA4 = %s", tikra111,tikra112,tikra113,tikra114));
		System.out.println(String.format("MAS1 = %s,MAS2 = %s, MAS3 = %s, MAS4 = %s", RunHelper.round(smas111), RunHelper.round(smas112), RunHelper.round(smas113), RunHelper.round(smas114)));
		final String totalShanaSh25Str = RunHelper.printWithCommas(totalShanaSh25);
		final String totalMasShanaSh25Str = RunHelper.printWithCommas(RunHelper.round(totalMasShanaSh25));
		final String totalTaxShanaSh25Str = new BigDecimal(Double.toString(totalTaxShanaSh25)).setScale(3, RoundingMode.HALF_DOWN).toPlainString();
		System.out.println("TOTALSHANASh25: " + totalShanaSh25Str + "| TOTALMASSHANASh25: " + totalMasShanaSh25Str + " | TAXSHANASh25: " + totalTaxShanaSh25Str + " | ");
		System.out.println();
	}
}
