package speculation;

import java.util.HashMap;

public class ReorderBuffer {
	private HashMap<String, String>[] entries;
	private int head = 1;
	private int tail = 1;
	private int Size;
	private boolean full = false;

	@SuppressWarnings("unchecked")
	public ReorderBuffer(int Size) {
		this.Size = Size;
		this.entries = (HashMap<String, String>[]) new HashMap[Size+1];
		for (int i = 1; i <= Size; i++) {
			HashMap<String, String> entry = new HashMap<String, String>();
			entry.put("Type", "");
			entry.put("Destination", "");
			entry.put("Value", "");
			entry.put("Ready", "");
			entries[i] = entry;
		}
	}

	public void addEntry(HashMap<String, String> entry) {
		if (!full) {
			entries[tail] = entry;
			moveTail();
		}
	}

	public int getHead() {
		return head;
	}

	public int getTail() {
		return tail;
	}
	
	public HashMap<String,String> getEntryAtHead()
	{
		return entries[head];
		
	}
	
	public HashMap<String,String> getEntryAt(int i)
	{
		return entries[i];
		
	}
	
	public void reset()
	{
		for (int i = 1; i <= Size; i++) {
			HashMap<String, String> entry = new HashMap<String, String>();
			entry.put("Type", "");
			entry.put("Destination", "");
			entry.put("Value", "");
			entry.put("Ready", "");
			entries[i] = entry;
		}
	}

	public void moveHead() {
		head = (head + 1) % Size;
		if (head == 0)
			head = 1;
		setFull();

	}

	public void moveTail() {
			tail = (tail + 1) % Size;
			if (tail == 0)
				tail = 1;
			setFull();
	}

	private void setFull() {
		if (head == tail)
			full = true;
	}
	
	public boolean isFull()
	{
		return full;
	}
}
