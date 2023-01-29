package process;

public class PROCESS_final {

	private int TEAM_ID =-1;
	private int STDUENT_ID =-1;
	public PROCESS_final(int group,int stu) {
		set_STDUENT_ID(stu);
		set_TEAM_ID(group);
	}
	
	public void print_data() {
		System.out.println("      "+TEAM_ID+"         "+STDUENT_ID);
	}
	
	
	public void set_STDUENT_ID(int stu) {
		this.STDUENT_ID = stu;
	}
	public void set_TEAM_ID(int team) {
		this.TEAM_ID = team;
	}
	
	
	
	public int get_STDUENT_ID() {
		return STDUENT_ID;
	}
	public int get_TEAM_ID() {
		return TEAM_ID;
	}
	
	
}