//shortest-remaining-time-first program that solves the starvation problem through
// preventing any more processes to enter until all the processes are finished

import java.util.ArrayList;

public class SRTF {
    public void calculateWaitingTime(ArrayList<Process> processes, int size, int contextSwitching)
    {
        resetWaiting(processes);
        int timer = 0, minimum = 900000, smallestIndex = -1;
        int flag = 0;
        int burstTime[] = new int[size];
        int temp = -1;
        ArrayList<String> processOrder = new ArrayList<String>();
        ArrayList<String> processExecution = new ArrayList<String>();

        for(int i = 0; i < size; i++)
        {
            burstTime[i] = processes.get(i).burstTime;
        }

        int finished = size;
        while (finished != 0)
        {
            int j = 0;
            while(processes.get(j).arrivalTime <= timer)
            {
                flag = 1;
                if((burstTime[j] < minimum) && (burstTime[j] > 0))
                {
                    minimum = burstTime[j];
                    smallestIndex = j;
                }
                j++;
                if(j == size)
                    break;
            }
            if (flag == 0)         //inner while loop wasn't entered
            {
                timer++;
                continue;
            }

            if(temp != smallestIndex)     //getting the order of the processes
            {
                temp = smallestIndex;
                processes.get(temp).waitingTime += contextSwitching;
                processOrder.add(processes.get(temp).name);
            }

            burstTime[smallestIndex] =  burstTime[smallestIndex] - 1;
            minimum = burstTime[smallestIndex];


            if (minimum <= 0)
                minimum = 900000;

            int k = 0;
            while(processes.get(k).arrivalTime <= timer)       //calculate the waiting time
            {
                if((k != smallestIndex) && (burstTime[k] > 0) )
                    processes.get(k).waitingTime++;

                k++;
                if(k == size)
                    break;
            }

            //solving the starvation problem through putting a threshold, when the waiting time of any process
            // exceeds it, we put it in to get processed.
            for(int i = 0; i < size; i++)
            {
                if(processes.get(i).waitingTime >= 50)
                {
                    burstTime[i] = 0;
                    smallestIndex = i;
                }
            }

            if(burstTime[smallestIndex] == 0)
            {
                finished--;
                processExecution.add(processes.get(smallestIndex).name);
            }
            timer++;
        }
        System.out.println("SRTF :-");
        System.out.println("-----");
        System.out.print("Processes execution order :");
        System.out.print(processOrder);
        System.out.println();
        System.out.print("Processes execution order 2:");
        System.out.print(processExecution);
        System.out.println();
    }
    public void calculateTurnAroundTime(ArrayList<Process> processes, int size)
    {
        for (int i = 0; i < size; i++)
            processes.get(i).turnAroundTime = processes.get(i).burstTime + processes.get(i).waitingTime;
    }

    public void calculateAverageTime(ArrayList<Process> processes, int size, int contextSwitching)
    {
        double sumWaitingTime = 0;
        double sumTurnAroundTime = 0;
        calculateWaitingTime(processes, size, contextSwitching);
        calculateTurnAroundTime(processes, size);
        System.out.println("Process    Waiting Time    TurnAround Time");

        for(int i = 0; i < size; i++)
        {
            sumWaitingTime += processes.get(i).waitingTime;
            sumTurnAroundTime += processes.get(i).turnAroundTime;
            System.out.println("P" + (i+1) + "         " + processes.get(i).waitingTime + "               " + processes.get(i).turnAroundTime);
        }

        System.out.println("Average waiting time = " + (double)sumWaitingTime / size);
        System.out.println("Average turnAround time = " + (double)sumTurnAroundTime / size);
        System.out.println("================================");
    }
    ///waiting time
    public void resetWaiting(ArrayList<Process> processes)
    {
        for (Process process : processes) {
            process.waitingTime = 0;
        }

    }
}
