import java.util.ArrayList;

public class SJF {
    public static ArrayList<Process> processes;
    public static ArrayList<Process> sortedprocess = new ArrayList<>();

    public SJF(ArrayList<Process>processes){
        this.processes = processes;
        System.out.println("SJF: ");
        System.out.println("------");
        System.out.println("processes execution order ");
        calCompletionTime(processes);
        calTurnaroundTime(processes);
        calWaitingTime(processes);
    }

    public static void calCompletionTime(ArrayList<Process> processes )
    {
        int startTime=processes.get(1).getArrivalTime();

        for(int i =1;i<processes.size();i++){
            if(processes.get(i).getArrivalTime()<startTime){
                startTime = processes.get(i).arrivalTime;
            }
            break;
        }

        for(int j=0;j<processes.size();j++)
        {
            Process minBTprocess = new Process();
            int minBT = Integer.MAX_VALUE;
            for(int i = 0; i<processes.size(); i++)
            {
                if(startTime-processes.get(i).getArrivalTime()>=processes.get(i).age && processes.get(i).getEndTime()==-1&& processes.get(i).getArrivalTime()<=startTime) {
                    minBT = processes.get(i).getBurstTime();
                    minBTprocess = processes.get(i);
                    break;
                }
                if(processes.get(i).getBurstTime() < minBT&& processes.get(i).getEndTime()==-1&& processes.get(i).getArrivalTime()<=startTime)
                {
                    minBT = processes.get(i).getBurstTime();
                    minBTprocess = processes.get(i);
                }
            }
            startTime+=minBT;
            System.out.print(minBTprocess.getName()+" : "+(startTime)+"  ");
            if(minBTprocess != null)
            {

                minBTprocess.setEndTime(startTime);
                sortedprocess.add(minBTprocess);

            }


        }
    }

    public static void calTurnaroundTime(ArrayList<Process> processes){
        double avgTurnaroundTime = 0.0;
        System.out.println();
        System.out.println("turnaround time for each process is:");
        for(int i =0;i<processes.size();i++){
            int tat = processes.get(i).getEndTime() - processes.get(i).getArrivalTime();
            avgTurnaroundTime+=tat;
            System.out.println(processes.get(i).getName()+": "+ tat);
            processes.get(i).setturnAroundTime(tat);
        }
        System.out.println("Average turnaround time is " + (avgTurnaroundTime/processes.size()));
    }
    public static void calWaitingTime(ArrayList<Process>processes){
        double avgWaitingTime = 0.0;
        System.out.println();
        System.out.println("waiting time for each process is ");
        for(int i =0; i<processes.size();i++)
        {
            int wt = processes.get(i).getTurnAroundTime()-processes.get(i).getBurstTime();
            System.out.println(processes.get(i).getName()+" : "+wt);
            avgWaitingTime+=wt;
        }
        System.out.println("Average waiting time is " +(avgWaitingTime/processes.size()));

    }




}