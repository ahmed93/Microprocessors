package simulator;


public class Register {
	private int value;
	boolean immutable;
	String name;
	
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
	
	public void setName(String name){
		this.name = name;
	}
}
