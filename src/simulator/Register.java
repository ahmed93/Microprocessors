package simulator;
import java.util.HashMap;


public class Register {
	int value;
	boolean immutable;
	
	public Register(int value, boolean immutable){
		this.value = value;
		this.immutable = immutable;
	}
	
	public int get_value(){
		return this.value;
	}
	
	public void set_value(int value){
		this.value = value;
	}
}
