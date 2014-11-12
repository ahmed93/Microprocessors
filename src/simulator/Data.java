package simulator;

import interfaces.Word;

public class Data implements Word {
	int value;
	
	public void set_value(int value){
		this.value = value;
	}
	
	public int get_value(){
		return this.value;
	}
}
