package simulator;
import java.util.HashMap;


public class Register {
	int value;
	boolean can_change;
	
	public Register(int value){
		this.value = value;
	}
	
	public int get_value(){
		return this.value;
	}
	
	public void set_value(int value){
		this.value = value;
	}
}
