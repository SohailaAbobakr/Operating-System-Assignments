import java.util.ArrayList;
import java.util.Comparator;

public class PriorityScheduling {
    private double averageWaitingTime;
    private double averageTurnaroundTime;
    private ArrayList<Process> allProcesses;
    public int contextSwitching;


    public PriorityScheduling()
    {
        allProcesses = new ArrayList<Process>();
        averageWaitingTime = 0.0;
        averageTurnaroundTime = 0.0;
    }

    public PriorityScheduling(ArrayList<Process> allProcesses,int cs )
    {
        this.allProcesses = allProcesses;
        this.contextSwitching=cs;
        allProcesses.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2)
            {
                //for avoiding the starvation problem make the process that has minimum arrival time entered first
                if (p1.getArrivalTime() < p2.getArrivalTime())
                    return -1;

                else if (p1.getArrivalTime() == p2.getArrivalTime() && p1.getPriorityNumber() < p2.getPriorityNumber() )
                    return -1;


                else if (p1.getArrivalTime() == p2.getArrivalTime() && p1.getPriorityNumber() == p2.getPriorityNumber() && p1.getBurstTime() < p2.getBurstTime())
                    return -1;

                else  return 1;
            }
        });
        System.out.println("================================");
        System.out.println("Priority Scheduling: ");
        System.out.println("-------------------");
        System.out.print("Processes execution order : \n");
        for (Process process : allProcesses) System.out.print(process.getName() + " ");
        System.out.println();


        int n = allProcesses.size();
        int leavingTime[] = new int[n];
        int turnAroundTime[] = new int[n];
        int waitingTime[] = new int[n];


        //calculate leaving Time
        for (int i = 0; i < n; i++)
        {
            if(i==0)
            {
                leavingTime[i] = allProcesses.get(i).getBurstTime()  + allProcesses.get(i).getArrivalTime();
            }
            else
            {
                leavingTime[i] = contextSwitching + leavingTime[i-1] + allProcesses.get(i).getBurstTime() ;
            }
        }


        // calculate TurnAroundTime & average TurnAroundTime for all processes
        for (int i = 0; i < n; i++)
        {
            turnAroundTime[i] = leavingTime[i] - allProcesses.get(i).getArrivalTime(); //TAT = leave - arrive
            allProcesses.get(i).turnAroundTime = turnAroundTime[i];
            averageTurnaroundTime += turnAroundTime[i];
        }
        averageTurnaroundTime /= n;


        // calculate WaitingTime & average WaitingTime for all processes
        for (int i = 0; i < n; i++)
        {
            waitingTime[i] = turnAroundTime[i] - allProcesses.get(i).getBurstTime(); //WT = TAT - BT
            allProcesses.get(i).waitingTime = waitingTime[i];
            averageWaitingTime += waitingTime[i];
        }
        averageWaitingTime /= n;



        //sort all processes depend on their names
        allProcesses.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2)
            {
                if (p1.getName().compareToIgnoreCase(p2.getName()) == -1 ) //p1.name < p2.name
                    return -1;

                else  return 1;
            }
        });


        System.out.println();
        System.out.println("Process  ArrivalTime  BurstTime  Priority  TurnaroundTime  WaitingTime ");
        for (Process allProcess : allProcesses) {
            System.out.print(allProcess.getName() + "        ");
            System.out.print(allProcess.getArrivalTime() + "	 	       ");
            System.out.print(allProcess.getBurstTime() + "           ");
            System.out.print(allProcess.getPriorityNumber() + "	    ");
            System.out.print(allProcess.getTurnAroundTime() + "              ");
            System.out.print(allProcess.getWaitingTime());
            System.out.println();

        }
        System.out.println();
        System.out.println("Average Waiting Time = " + averageWaitingTime);
        System.out.println("Average Turnaround Time = " + averageTurnaroundTime);

    }


}
