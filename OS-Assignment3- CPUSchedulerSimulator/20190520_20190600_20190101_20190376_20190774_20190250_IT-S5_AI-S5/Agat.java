import java.util.ArrayList;
import java.util.Comparator;
import java.lang.Math;

public class Agat {
    private ArrayList<Process> processes;
    private ArrayList<Process> readyQueue= new ArrayList<>();
    private ArrayList<Process> deadList= new ArrayList<>();;
    private final ArrayList<Process> orderProcess= new ArrayList<>();
    private final ArrayList<Integer> orderTime= new ArrayList<>();

    private float v2;
    private int time;

    public Agat(ArrayList<Process> processes)
    {
        this.processes=processes;
        time=0;
    }

    public void getDeadList() {
        System.out.println("Execution order: ");
        for (Process p : deadList) {

            System.out.println(p.name);
        }
        System.out.println("-------------------------");
    }

    public void calculateV1()
    {
        for (int i = 0; i < processes.size(); i++) {
            //last arrival time / 10
            double v1;
            if(processes.get(processes.size() - 1).arrivalTime < 10)
            {
                v1=1;
            }
            else {
                v1= ((float)processes.get(processes.size() - 1).arrivalTime)/10;
            }
            processes.get(i).ceilV1= (int) Math.ceil(processes.get(i).arrivalTime/v1);
        }

    }
    public void calculateV2()
    {
        float maxBurstTime=0;
        for (Process process : processes) {

            maxBurstTime = Math.max(maxBurstTime, process.updatedBurstTime);

        }

        if(maxBurstTime>10)
            v2=maxBurstTime/10;
        else v2=1;
    }

    public void calculateFactor()
    {
        calculateV2();

        for (Process p : processes) {

            if(deadList.contains(p)) continue;
            p.factor = (10 - p.priorityNumber) + p.ceilV1 + (int) Math.ceil(p.updatedBurstTime / v2);
            p.historyFactor.add(p.factor);
        }
    }
    public void checkTime()
    {
        for (Process p : processes) {

            if(p.arrivalTime==time)
            {
                readyQueue.add(p);
            }
        }
    }

    public Process calcMinFactor()
    {
        Process p=null;
        int minFactor=Integer.MAX_VALUE;
        for (Process process : readyQueue) {

            minFactor = Math.min(minFactor, process.factor);
        }

        for (Process p2 : readyQueue) {

            if(p2.factor==minFactor)
                p= p2;
        }
        return p;
    }
    public void printHistoryFactor()
    {
        System.out.println("History of factor calculation per process: ");
        for (Process process: processes) {
            System.out.println(process.name+": ");
            for (int i = 0; i < process.historyFactor.size(); i++) {
                System.out.print(process.historyFactor.get(i)+" ");
            }
            System.out.print("\n");
        }
        System.out.println("-------------------------");
    }
    public void printHistoryQt()
    {
        System.out.println("History of quantum per process: ");
        for (Process process: processes) {
            System.out.println(process.name+": ");
            for (int i = 0; i < process.historyQt.size(); i++) {
                System.out.print(process.historyQt.get(i)+" ");
            }
            System.out.print("\n");
        }
        System.out.println("-------------------------");
    }
    public void printWaiting()
    {
        System.out.println("Waiting time for each process: ");
        for (Process process: processes) {
            System.out.println(process.name+" time: "+ process.waitingTime);
        }
        System.out.println("-------------------------");
    }
    public void setWaiting()
    {
        for (Process process: processes) {
            process.waitingTime=process.turnAroundTime-process.burstTime;
        }
    }

    public void getOrder()
    {
        System.out.println("Order of Processing: ");
        System.out.println(orderProcess.get(0).name+" : Start time: "+ orderProcess.get(0).arrivalTime +", End time: "+orderTime.get(0));
        for (int i = 1; i < orderProcess.size() ; i++) {
            System.out.println(orderProcess.get(i).name+" : Start time: "+ orderTime.get(i-1)+", End time: "+orderTime.get(i));
        }
        System.out.println("-------------------------");
    }
    public double getAverageWaiting()
    {
        int sum=0;
        for (Process process: processes) {
            sum+=process.waitingTime;
        }
        double average=0;
        average=(float)sum/processes.size();
        return average;
    }


    public void setTurnAround(int t,Process p)
    {
        p.turnAroundTime=t- p.arrivalTime;
    }

    public void getTurnAround()
    {
        System.out.println("Turn around time for each process: ");
        for (Process process: processes) {
            System.out.println(process.name+" time: "+ process.turnAroundTime);
        }
        System.out.println("-------------------------");
    }

    public double AverageTurnAround()
    {
        int sum=0;
        for (Process process: processes) {
            sum+=process.turnAroundTime;
        }
        double average=0;
        average=(float)sum/processes.size();
        return average;
    }
    public void showAgatOutput()
    {
        scheduleAgat();
        System.out.println("AGAT OUTPUT: ");
        getOrder();
        getDeadList();
        printHistoryFactor();
        printHistoryQt();
        setWaiting();
        printWaiting();
        getTurnAround();
        System.out.println("Average waiting time: "+getAverageWaiting());
        System.out.println("Average turn around time: "+AverageTurnAround());
        System.out.println("======================");
    }
    public void sort(){
        readyQueue.sort(new Comparator<Process>() {
            @Override
            public int compare(Process p1, Process p2)
            {
                if (p1.factor < p2.factor)
                    return -1;
                else  return 1;
            }
        });
    }
    public int getMinArrivalTime()
    {
        int minTime=Integer.MAX_VALUE;
        for (Process process : processes) {

            minTime = Math.min(minTime, process.arrivalTime);
        }
        return minTime;
    }
    public void scheduleAgat() {

        time=getMinArrivalTime();

        calculateV1();
        checkTime();

        Process currProcess = new Process();
        boolean updated = false;

        if(readyQueue.size() > 1){
            calculateFactor();
        }
        for (Process p : readyQueue) {
            if(readyQueue.size() > 1){
                currProcess = calcMinFactor();
                updated = true;
            }
        }
        if(updated){
            sort();
        }

        while (deadList.size() != processes.size()) {
            int q;
            if (!updated) {
                currProcess = readyQueue.get(0);
            }

            if (!currProcess.historyQt.contains(currProcess.quantumTime))
                currProcess.historyQt.add(currProcess.quantumTime);

            q = (Math.round((float) (40 * currProcess.quantumTime) / 100));

            int remQuantum = currProcess.quantumTime;
            int quantum = currProcess.quantumTime;

            if (deadList.size() < processes.size() - 1 && !updated) {
                calculateFactor();
            } else quantum = currProcess.updatedBurstTime;

            for (int i = 1; i <= quantum; i++) {
                time++;
                checkTime();
                currProcess.updatedBurstTime--;
                if (currProcess.updatedBurstTime != 0) {
                    if (i >= q) {
                        if (calcMinFactor() != null) {
                            if (currProcess.factor > calcMinFactor().factor) {

                                int indexCurr = readyQueue.indexOf(currProcess);

                                int rep = readyQueue.indexOf(calcMinFactor());
                                readyQueue.set(indexCurr, calcMinFactor());

                                readyQueue.remove(rep);
                                readyQueue.add(currProcess);
                                remQuantum--;
                                currProcess.quantumTime = currProcess.quantumTime + remQuantum;
                                currProcess.historyQt.add(currProcess.quantumTime);
                                updated = false;
                                break;
                            }
                        }
                    }
                    remQuantum--;
                } else {
                    currProcess.quantumTime = 0;
                    currProcess.historyQt.add(currProcess.quantumTime);
                    readyQueue.remove(currProcess);
                    deadList.add(currProcess);
                    setTurnAround(time, currProcess);
                    updated = false;
                    break;
                }

            }

            orderTime.add(time);
            orderProcess.add(currProcess);
            if (currProcess.updatedBurstTime > 0 && remQuantum == 0) {
                readyQueue.remove(currProcess);
                readyQueue.add(currProcess);
                currProcess.quantumTime = currProcess.quantumTime + 2;
                currProcess.historyQt.add(currProcess.quantumTime);
                updated = false;
            }


        }

    }
}

