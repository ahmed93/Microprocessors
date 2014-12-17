package GUI.utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class Setting implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1219384301878160488L;

	private String filePath;

	int instruction_starting_address;
	int memoryAccessTime;
	ArrayList<HashMap<String, Integer>> input_caches;

	int ROB_Size;
	int nWay;
	int cacheLevels;
	HashMap<String, Integer> inputReservationStations;
	HashMap<String, Integer> inputinstructionsLatencies;

	public Setting(String filePath, int instruction_starting_address,
			int memoryAccessTime,
			ArrayList<HashMap<String, Integer>> input_caches,
			int ROB_Size, int nWay,
			HashMap<String, Integer> inputReservationStations,
			HashMap<String, Integer> inputinstructionsLatencies, int cacheLevels) {
		
		this.filePath = filePath;
		this.instruction_starting_address = instruction_starting_address;
		this.memoryAccessTime = memoryAccessTime;
		this.input_caches = input_caches;
		this.ROB_Size = ROB_Size;
		this.nWay = nWay;
		this.inputReservationStations = inputReservationStations;
		this.inputinstructionsLatencies =  inputinstructionsLatencies;
		this.cacheLevels = cacheLevels;
	}
	
	
	public String getFilePath() {
		return filePath;
	}

	public int getInstruction_starting_address() {
		return instruction_starting_address;
	}

	public int getMemoryAccessTime() {
		return memoryAccessTime;
	}

	public ArrayList<HashMap<String, Integer>> getInput_caches() {
		return input_caches;
	}

	public int getROB_Size() {
		return ROB_Size;
	}

	public int getnWay() {
		return nWay;
	}

	public HashMap<String, Integer> getInputReservationStations() {
		return inputReservationStations;
	}

	public HashMap<String, Integer> getInputinstructionsLatencies() {
		return inputinstructionsLatencies;
	}
	
	public int getCacheLevels() {
		return cacheLevels;
	}

}
