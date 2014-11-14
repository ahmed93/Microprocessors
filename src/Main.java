import java.io.IOException;

import simulator.Simulator;

public class Main {

	public static void main (String [] args) throws IOException{
		Simulator simulator = Simulator.getInstance();
		simulator.Initialize();
		simulator.runInstructions();
	}

}
