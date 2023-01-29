package process;

import java.util.ArrayList;

public class team {
	private ArrayList<Student> members = new ArrayList<Student>();
	private int group_num = -1;
	private double K1K2AVG = -1;
	
	
	public team(){}
	
	public void updateK1K2AVG() {
		K1K2AVG =0;
		for(Student i:members)K1K2AVG+=i.get_k1k2SUM();
		K1K2AVG = K1K2AVG/member_number();
	}
	
	public double getK1K2AVG() {
		return K1K2AVG;
	}
	
	public void change_TEAMID(int ID) {
		this.group_num = ID;
	}
	
	public void update3(Student s1, Student s2, Student s3) {
		members.add(s1);
		members.add(s2);
		members.add(s3);
		updateK1K2AVG();
		
	}
	public void update4(Student s1, Student s2, Student s3, Student s4) {
		members.add(s1);
		members.add(s2);
		members.add(s3);
		members.add(s4);
		updateK1K2AVG();
	}
	public void append (Student a) {members.add(a);updateK1K2AVG();}
	
	public int get_groupNUM() {return group_num;}
	public void set_group_number (int num) {group_num=num;}
	
	public void remember3(Student s1, Student s2, Student s3) {
		members.clear();
		members.add(s1);
		members.add(s2);
		members.add(s3);
	}
	public void remember4(Student s1, Student s2, Student s3,Student s4) {
		members.clear();
		members.add(s1);
		members.add(s2);
		members.add(s3);
		members.add(s4);
	}
	public ArrayList<Student> get_members() {return members;}//返回members
	public int member_number() {
		return members.size();
	}
	public Student K1GOODstudentWITHweakestK1K2() {
		Student ak =null;
		int now=999;
		double K1AVG = PROCESS_driveCode.get_K1Average();
		for(Student a:members) {
			if (a.get_K1()>K1AVG) {//选择大于平均K1的学生
				if(a.get_k1k2SUM()<now) {
					now = a.get_k1k2SUM();
					ak = a;
				}//选择K1K2sum最小的学生
			}
		}
		return ak;
	}
	
	public Student K1BADTEAMWITHstrongestK1K2() {
		//这个组的所有人没有人的K1能力大于平均
		Student ak =null;
		int now=0;
		double K1AVG = PROCESS_driveCode.get_K1Average();
		for(Student a:members) {
			if (a.get_k1k2SUM()>now) {
				now = a.get_k1k2SUM();
				ak = a;
			}//选择K1K2sum最大的学生
			
		}
		return ak;
	}

	public Student strongestK1K2MEMBER() {
		Student strongest = null;
		int now=0;
		for(Student a:members) {
			if (a.get_k1k2SUM()>now) {
				strongest = a;
				now = strongest.get_k1k2SUM();
			}
		}
		return strongest;
		
	}
	
	public Student weakestK1K2MEMBER() {
		Student weakest = members.get(0);
		int now=weakest.get_k1k2SUM();
		for(Student a:members) {
			if (a.get_k1k2SUM()<now) {
				weakest = a;
				now = weakest.get_k1k2SUM();
			}
		}
		return weakest;
		
	}
	public void changeMEMBER(Student doomed, Student newMEMBER) {
		//删除指定成员并添加新成员
		boolean found =false;
		members.remove(doomed);
//		if (!found) throw new IllegalArgumentException("No such memebr found!");
		members.add(newMEMBER);
		updateK1K2AVG();
	}
	
	
	public void print_TEAMID() {
		System.out.println("ID: " + group_num);
	}
	
	public void print_TEAMinfo() {
		System.out.println();
		System.out.println("GROUP ID: " + group_num);
		System.out.println("MEMBER INFO:");
		for (Student i :members) {
			i.print_STUinfo();
		}
	}
}