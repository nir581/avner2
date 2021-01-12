package main;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Data
{
	private static Data INSTANCE = new Data();

	private Data() {}

	public static Data getInstance() {
		return INSTANCE;
	}

	public Map<Integer, Map<Integer, TaxRecord>> getTablesMap()
	{
		final Map<Integer, TaxRecord> map_2016 = new LinkedHashMap<>()
		{{
			put(1, new TaxRecord(62640, 0.1));
			put(2, new TaxRecord(107040, 0.14));
			put(3, new TaxRecord(166320, 0.21));
			put(4, new TaxRecord(237600, 0.31));
			put(5, new TaxRecord(496920, 0.34));
			put(6, new TaxRecord(496921, 0.48));
		}};

		final Map<Integer, TaxRecord> map_2017 = new LinkedHashMap<>()
		{{
			put(1, new TaxRecord(74640, 0.1));
			put(2, new TaxRecord(107040, 0.14));
			put(3, new TaxRecord(171840, 0.2));
			put(4, new TaxRecord(238800, 0.31));
			put(5, new TaxRecord(496920, 0.35));
			put(6, new TaxRecord(496921, 0.47));
		}};

		final Map<Integer, TaxRecord> map_2018 = new LinkedHashMap<>()
		{{
			put(1, new TaxRecord(74880, 0.1));
			put(2, new TaxRecord(107400, 0.14));
			put(3, new TaxRecord(172320, 0.2));
			put(4, new TaxRecord(239520, 0.31));
			put(5, new TaxRecord(498360, 0.35));
			put(6, new TaxRecord(498361, 0.47));
		}};

		final Map<Integer, TaxRecord> map_2019 = new LinkedHashMap<>()
		{{
			put(1, new TaxRecord(75720, 0.1));
			put(2, new TaxRecord(108600, 0.14));
			put(3, new TaxRecord(174360, 0.2));
			put(4, new TaxRecord(242400, 0.31));
			put(5, new TaxRecord(504360, 0.35));
			put(6, new TaxRecord(504361, 0.47));
		}};

		final Map<Integer, TaxRecord> map_2020 = new LinkedHashMap<>()
		{{
			put(1, new TaxRecord(75960, 0.1));
			put(2, new TaxRecord(108960, 0.14));
			put(3, new TaxRecord(174960, 0.2));
			put(4, new TaxRecord(243120, 0.31));
			put(5, new TaxRecord(505920, 0.35));
			put(6, new TaxRecord(505921, 0.47));
		}};

		return new LinkedHashMap<>()
		{{
			put(2016, map_2016);
			put(2017, map_2017);
			put(2018, map_2018);
			put(2019, map_2019);
			put(2020, map_2020);
		}};
	}
}
