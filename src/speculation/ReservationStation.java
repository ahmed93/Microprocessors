package speculation;

public class ReservationStation {
	String Name;
	boolean Busy;
	String OP;
	int Vj;
	int Vk;
	int Qj;
	int Qk;

	public ReservationStation(String name, boolean busy, String oP, int vj,
			int vk, int qj, int qk, int dest, int a) {
		super();
		Name = name;
		Busy = busy;
		OP = oP;
		Vj = vj;
		Vk = vk;
		Qj = qj;
		Qk = qk;
		Dest = dest;
		A = a;
	}

	public ReservationStation(String name) {
		this.Name = name;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public boolean isBusy() {
		return Busy;
	}

	public void setBusy(boolean busy) {
		Busy = busy;
	}

	public String getOP() {
		return OP;
	}

	public void setOP(String oP) {
		OP = oP;
	}

	public int getVj() {
		return Vj;
	}

	public void setVj(int vj) {
		Vj = vj;
	}

	public int getVk() {
		return Vk;
	}

	public void setVk(int vk) {
		Vk = vk;
	}

	public int getQj() {
		return Qj;
	}

	public void setQj(int qj) {
		Qj = qj;
	}

	public int getQk() {
		return Qk;
	}

	public void setQk(int qk) {
		Qk = qk;
	}

	public int getDest() {
		return Dest;
	}

	public void setDest(int dest) {
		Dest = dest;
	}

	public int getA() {
		return A;
	}

	public void setA(int a) {
		A = a;
	}

	int Dest;
	int A;

}
