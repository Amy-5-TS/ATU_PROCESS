package process;
import java.util.Comparator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class PROCESS_driveCode {
	static int student_NUM = 0;
	private static Student[] list;
	private static PROCESS_final[] final_grouping_list;
	static ArrayList<Student> K1L = new ArrayList<Student>();
	static ArrayList<Student> K2L = new ArrayList<Student>();
	static ArrayList<Student> K3L = new ArrayList<Student>();
	static ArrayList<team> lameTEAMS = new ArrayList<team>();
	static ArrayList<team> strongTEAMS = new ArrayList<team>();
	static int [] K1good;
	static boolean balancedK1 = true;
	private static int[][] stu_info;
	private static double avek1 =0;
	private static double avek2 =0;
    static team[] teams;//从大到小
    static int TEAM_NUM;
	static boolean flag_C2_played = false;
	
	
	public static void runCode() {
		loadCSV();
		calculate_average();
		System.out.println(get_K1Average());
		System.out.println(get_K2Average());
		teamUp1();
		//the team under CONSTRAIN1
		 //VERSION 1, recursive for C3, then C2	/*by QiWU 20677500*/
		for(int i=0;i<50;i++) {CONSTRAIN3();}//iterate for 50 times
		sortTEAMbyK1K2AVG();
		print_currentTEAM();
		//C2 is valid for now
		//If not, CONTRIANT2 will be in force. 
		CONTRAIN2();
		//reID the groups
		for (int i=0;i<TEAM_NUM;i++) {teams[i].change_TEAMID(i+1);}
		print_currentTEAM();
		//generate the target dataset
		generate_final_OUTPUT();
		System.out.println("-------------------------------------------------------");
		print_final_result();
		System.out.println("-------------------------------------------------------");
		System.out.println("The program concludes here.");
		System.out.println("Presented by Qi WU, 20677500");
		System.out.println("-------------------------------------------------------");

	}
	
	public static void print_final_result() {
		System.out.println("Below is the final grouping result in the format as stated in course pdf");
		System.out.println("to acess student data with K1, K2, as grouped by TEAM, refer to JAVA class: team ");
		System.out.println("------------------------------");
		System.out.println("  TEAM ID   |   STUDENT ID");
		System.out.println("------------------------------");
		for (int i=0;i<100;i++) {
			final_grouping_list[i].print_data();
		}
	}
	
	public static PROCESS_final[] get_PROCESS_OUTPUT(){
		return final_grouping_list;
	}
	
	public static void generate_final_OUTPUT() {
		final_grouping_list = new PROCESS_final[student_NUM];
		int stu_count=0;
		for (int i=0;i<TEAM_NUM;i++) {
			for (Student thisOne: teams[i].get_members()) {
				final_grouping_list[stu_count]= new PROCESS_final(i+1,thisOne.getID());
				stu_count++;
			}
		}

		if(stu_count!=100)System.out.println("Something is wrong.");
		else{
			System.out.println("-------------------------------------------------------");
			System.out.println("Congratulations! The PROCESS part has been completed and the # of student records stays the same (100)");
			System.out.println("To access the final grouping result, refer to class: PROCESS_final");
			System.out.println();
		}
		

		
	}
	
	public static void strongNlame() {
		checkNgenerateK1good();
		int sum =0;
		for(int i=0;i<TEAM_NUM;i++) {
			sum+=K1good[i];
		}
		if (sum<TEAM_NUM)System.out.println("There is always an incapable group.");
		else {
			sortTEAMbyK1K2AVG();//now the team is sorted by K1K2 descending
			strongTEAMS.clear();
			lameTEAMS.clear();
			//clear the team lists since they have been static lists
			for(int i=0;i<TEAM_NUM;i++) {
				if(K1good[i]>1) strongTEAMS.add(teams[i]);//among which, the 75strongest team is strongTEAMS[0]
				if(K1good[i]==0) lameTEAMS.add(teams[i]);//among which,the 75strongest team is lameTEAMS[0]
			}
			//observe the strong and lame lists
			System.out.println("STRONG TEAM LIST");
			for(team i:strongTEAMS) i.print_TEAMID();
			System.out.println("LAME TEAM LIST");
			for(team i:lameTEAMS) i.print_TEAMID();
		}
	}
	public static void C2_popNreplace() {
		Student worstINstongGROUP = strongTEAMS.get(strongTEAMS.size()).K1GOODstudentWITHweakestK1K2();//强组中最弱的组里合格的学生中最弱的一个
		Student bestINlameGROUP = lameTEAMS.get(0).K1BADTEAMWITHstrongestK1K2();//废组中在及格线边缘的组里，挑矮子里的大个
		
		strongTEAMS.get(strongTEAMS.size()).changeMEMBER(worstINstongGROUP,bestINlameGROUP);
		lameTEAMS.get(0).changeMEMBER(bestINlameGROUP,worstINstongGROUP);
		//are the groups the same version of 75 groups we have for output?
		//now the worstINstongGROUP has been swqpped with bestINlameGROUP
	}
	
	public static void CONTRAIN2() {
		checkNgenerateK1good();
		while(balancedK1==false) {
			flag_C2_played = true;
			//just pop the worstly acceptable one in strong team and swap with the strongest in lame team
			//swap the weakest member in winner team VS strongest member in lame team
			C2_popNreplace();
			//check if now all group is good with the constraints
			checkNgenerateK1good();
		}
		
	}
	public static void checkNgenerateK1good() {
		//if a certain group is free of free riders
		//每次都要重新更新一遍K1good
		System.out.println();
		System.out.println("Checking C2[each group should has at least one person's K1>AVG]");
		System.out.println();
		K1good = new int[33];//everytime K1good is reupdated
		for (int i=0;i<TEAM_NUM;i++) {//一共33个组
			K1good[i] =0;
			ArrayList<Student> tm = teams[i].get_members();
			for (Student a:tm) {//对一个组里的每一个学生
				if (a.get_K1()>get_K1Average())K1good[i]++;
			}
		}
		for(int i=0;i<TEAM_NUM;i++) {
			if (K1good[i]==0) {
				System.out.println("Group "+(i+1)+" is in trouble");
				balancedK1=false;
			}
		} 
		if(!balancedK1)System.out.println("Something in K1 is wrong.");
		else{
			System.out.println("We are good with C2.");
			System.out.println("No group is an obvious bravado.");}
	}
	
	
	public static void CONSTRAIN3() {
		//we are now going to sort the teams by its K1K2AVG again
		sortTEAMbyK1K2AVG();
		//swap the strongest student in the strongest team with weakest student in the weakest team
		//to minimise the 75 standard deviation of SUM(K1,K2) among each group
		Student strong = theOne(true, teams[0]);
		Student weak = theOne(false, teams[TEAM_NUM-1]);
		teams[0].changeMEMBER(strong,weak);//strongest team, remove strongest member and add a weak member/*by Qi WU 20677500*/
		teams[TEAM_NUM-1].changeMEMBER(weak,strong);
		//now we have updated the list: teams
		//here we are
	}
	
	 public static Student theOne(boolean a, team layoff) {
		 //a=true: return strongest student in a team,a=false,;return weakest student
		 if (a) return layoff.strongestK1K2MEMBER();
		 else return layoff.weakestK1K2MEMBER();
	 }
	
	
	public static void sortTEAMbyK1K2AVG() {
		//team K1K2AVG 从大到小排列
		for (int i = TEAM_NUM-1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (teams[j].getK1K2AVG() < teams[i].getK1K2AVG()) {
                    team tmp = teams[i];
                    teams[i] = teams[j];
                    teams[j] = tmp;
                }
            }
		}
		
	}
	public static void printTEAMorder() {

		System.out.println();
		System.out.println("The current team order:");
		for(int i=0;i<TEAM_NUM;i++) {///Qi WU 20677500
			System.out.println();
			System.out.print("TEAM ID: ");
			teams[i].print_TEAMID();
			System.out.println("totalEnergy: "+teams[i].getK1K2AVG());
			System.out.println();
		}
		
	}
	

	
	public static double get_K1Average() {
		return avek1;
	}
	public static double get_K2Average() {
		return avek2;
	}
	
	
	public static int[][] get_STUinfo(){
		return stu_info;
	}
	
	public static void iniSTU() {
		list = new Student[student_NUM-1];
		for (int i=1;i<student_NUM;i++) {//101
			list[i-1]=(new Student(stu_info[i][0], stu_info[i][1],stu_info[i][2]));
		//first line of student_info is non-sense
		}
	}
	public static void loadCSV() {
        String line = "";//Qi WU 20677500
        String cvsSplitBy = ",";
 
		try {
			BufferedReader br1 = new BufferedReader(new FileReader("/Users/amystark/Desktop/3111/process/src/process/data.csv"));//第一行是000
			if (br1==null) System.out.println("invalid read");
			int count =0;
			while ((line = br1.readLine()) != null) {
				count++;
			}
			stu_info= new int[count][3];
			BufferedReader br2 = new BufferedReader(new FileReader("/Users/amystark/Desktop/3111/process/src/process/data.csv"));//第一行是000
			if (br2==null) System.out.println("invalid read");
			while ((line = br2.readLine()) != null) {
				 
                // use comma as separator
                String[] data = line.split(cvsSplitBy);
                try{
                    stu_info[student_NUM][0] = Integer.parseInt(data[0]);//student id
                    stu_info[student_NUM][1] = Integer.parseInt(data[4]);//K1 energy
                    stu_info[student_NUM][2] = Integer.parseInt(data[5]);//K2 energy
                }
                catch (NumberFormatException ex){
                    ex.printStackTrace();
                }
                student_NUM++;//should be 101
            }

		} catch (Exception e) {
		e.printStackTrace();
		}
		iniSTU();
	}
	
	public static void calculate_average() {
		//K1,K2平均值
		int totalK1=0;
		int totalK2=0;
		for(int i=1; i<student_NUM; i++){
			totalK1 = totalK1 + stu_info[i][1];//stu_info is still 101 rows
			totalK2 = totalK2 + stu_info[i][2];
        }
		student_NUM--;//because this was the count of rows in CSV
		int rem = student_NUM%3;
		if(rem == 0) {
			TEAM_NUM = student_NUM/3;
		}
		else {
			TEAM_NUM = (student_NUM-rem)/3;
		}
		System.out.println(TEAM_NUM);
		//the # of students is -1
		//in sample, now student_NUM =100
		avek1 = totalK1 / student_NUM;
		avek2 = totalK2 / student_NUM;
	}
	


	public static void teamUp1() {
		//从大到小排序
		for (int i = student_NUM-1; i >= 0; i--) {
            for (int j = i - 1; j >= 0; j--) {
                if (list[j].get_K1() > list[i].get_K1()) {
                    Student tmp = list[i];
                    list[i] = list[j];
                    list[j] = tmp;
                }
            }
		}//sorted
		//现在100个学生已经被排好序
		//我们将从中选出33个学生放入第一组，剩下的要新建一个数组
		
        Student[] temp1 = new Student[student_NUM-TEAM_NUM]; //not in L1 but still in list
        for (int i1=0;i1<student_NUM;i1++) {
        	if (i1<TEAM_NUM)K1L.add(list[i1]);//前33个学生放入第一组
        	else temp1[i1-TEAM_NUM] = list[i1]; //剩下的学生放入新建的数组
        }//teamp1 is the remainder, K1L is the list of K1 strong students
        
        
        
        for (int i =student_NUM-TEAM_NUM-1 ; i >= 0; i--) {///Qi WU 20677500 现在将剩下的67个学生排序
            for (int j = i - 1; j >= 0; j--) {
                if (temp1[j].get_K2() > temp1[i].get_K2()) {
                    Student tmp = temp1[i];
                    temp1[i] = temp1[j];
                    temp1[j] = tmp;
                }
            }
        }//sort temp1 in descending value of K2
        
        //本次分组后将会剩下100-33-33个学生，需要放入第二个备用清单
        Student[] temp2 = new Student[student_NUM-2*TEAM_NUM];
        for (int i2=0;i2<student_NUM-TEAM_NUM;i2++) {
        	if (i2<TEAM_NUM)K2L.add(temp1[i2]);//from those not in L1 but still in list
        	else temp2[i2-TEAM_NUM] = temp1[i2];//temp2 should of size 34
        }//temp2 is the remainder, K2L is the list of K2 strong students

        //本次分组后将会剩下<3个学生
        //对于100-33-33个学生
        ArrayList<Student> worst = new ArrayList<Student>();
        for(int a=0; a<student_NUM-2*TEAM_NUM;a++) {
        	if (a<TEAM_NUM)K3L.add(temp2[a]);
        	else worst.add(temp2[a]);
        }
        //now we have generated K1L, K2L, K3L and worst
        teams= new team[33];
        for(int i=0;i<TEAM_NUM;i++) {
        	teams[i]=new team();
        	teams[i].update3(K1L.get(i),K2L.get(i),K3L.get(i));//set the teammembers
        	teams[i].set_group_number(i+1);//for each group set its number
        }//KxL are lists while worst is an array of size 一
       // worst[0].print_STUinfo();
        //对于没有组的学生，随机分入最后两组
        int here =0;
        for(Student i:worst) {
        	teams[TEAM_NUM-1-here].append(i);
        	here++;
        }
        
        //teams is an array of size 33
        //so far e expect 33 /*Qi WU 20677500*/groups with 32-3, 1-4.
        //we are in function teamUP1
	}
	
	public static void print_currentTEAM() {
		System.out.println();
		System.out.println("Printing current teaming arrangement...");
		for (int i=0;i<TEAM_NUM;i++) {
			teams[i].print_TEAMinfo();
		}
	}

}