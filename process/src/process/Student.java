package process;


// Person: store a single student's private info
// All properties are stored with SimpleStringProperty
// Helper functions (set/get) expect String parameter/return-value
public class Student {
	private  int id;
	private  int k1;
	private  int k2;
	


	public Student(int student_id, int k1_energy, int k2_energy) {
		this.id = student_id;
		
		this.k1 = k1_energy;
		this.k2 = k2_energy;
		
	}
	public int getID() {return id;}
	
	public int get_k1k2SUM() {
		return (k1+k2);
	}
	
	public int get_K1() {
		return k1;
	}
	public int get_K2() {
		return k2;
	}
	public void print_STUinfo() {
		System.out.println("ID:"+id+", K1:"+k1+", K2:"+k2);
	}
	
}
	