package main;

import java.util.HashMap;
import java.util.Map;

public class RoutineYearInput
{
	private Map<Integer, RoutineInput> indexToInputMap = new HashMap<>();
	private Map<Integer, RoutineOutput> indexToOutputMap = new HashMap<>();

	public RoutineYearInput(Map<Integer, RoutineInput> indexToInputMap, Map<Integer, RoutineOutput> indexToOutputMap)
	{
		this.indexToInputMap = indexToInputMap;
		this.indexToOutputMap = indexToOutputMap;
	}

	public Map<Integer, RoutineInput> getIndexToInputMap()
	{
		return indexToInputMap;
	}

	public Map<Integer, RoutineOutput> getIndexToOutputMap()
	{
		return indexToOutputMap;
	}
}
