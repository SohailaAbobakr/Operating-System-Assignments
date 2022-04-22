import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String args[]){

        ArrayList<Process> processes = new ArrayList<>();
        Scanner input = new Scanner(System.in);

        System.out.println("Enter number of processes");
        int n = input.nextInt();

        System.out.println("Enter context switching");
        int contextSwitching = input.nextInt();

        for(int i = 0; i<n; i++)
        {
            Scanner input2= new Scanner(System.in);
            Process p= new Process();

            System.out.println("Enter process "+(i+1)+" name:");
            p.name=input2.nextLine();

            System.out.println("Enter process "+(i+1)+" color:");
            p.color=input2.nextLine();

            System.out.println("Enter process "+(i+1)+" arrival time:");
            p.arrivalTime=input.nextInt();

            System.out.println("Enter process "+(i+1)+" burst time:");
            p.burstTime=input2.nextInt();
            p.updatedBurstTime= p.burstTime;

            System.out.println("Enter process "+(i+1)+" priority number:");
            p.priorityNumber=input2.nextInt();

            System.out.println("Enter process "+(i+1)+" time quantum: ");
            p.quantumTime= input.nextInt();

            System.out.println("======================");

            processes.add(p);
        }

        //Test Case 1
//        Process p1=new Process("p1","",0,17,4,4);
//        Process p2=new Process("p2","",3,6,9,3);
//        Process p3=new Process("p3","",4,10,3,5);
//        Process p4=new Process("p4","",29,4,8,2);

        //Test Case 2
//        Process p1=new Process("p1","",0,6,1,4);
//        Process p2=new Process("p2","",0,5,1,3);
//        Process p3=new Process("p3","",2,3,3,5);
//        Process p4=new Process("p4","",3,3,2,2);

//        processes.add(p1);
//        processes.add(p2);
//        processes.add(p3);
//        processes.add(p4);

        Agat a= new Agat(processes);
        a.showAgatOutput();
        SRTF sr = new SRTF();
        int size = processes.size();
        sr.calculateAverageTime(processes, size, contextSwitching);
        SJF s = new SJF(processes);
        PriorityScheduling p=new PriorityScheduling(processes,contextSwitching);


    }
}
