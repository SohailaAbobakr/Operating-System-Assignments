import java.util.ArrayList;

public class Process {
    public String name;
    public String color;
    public int arrivalTime;
    public int burstTime;
    public int updatedBurstTime;
    public int priorityNumber;
    public int quantumTime;
    public int endTime;
    public int age;
    public int waitingTime;
    public int turnAroundTime;

    //ceil Arrival time/v1
    public int ceilV1;
    public int factor;

    public ArrayList<Integer> historyQt=new ArrayList<>();
    public ArrayList<Integer> historyFactor=new ArrayList<>();

    public Process()
    {
        name = "";
        arrivalTime = 0;
        burstTime = 0;
        endTime = -1;
        age=6;
    }

    public Process(String name, String color, int arrivalTime, int burstTime, int priorityNumber, int quantumTime) {
        this.name = name;
        this.color = color;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priorityNumber = priorityNumber;
        this.quantumTime = quantumTime;
        updatedBurstTime=burstTime;
        endTime=-1;
        age=6;
    }

    public void setEndTime(int time)
    {
        endTime=time;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setturnAroundTime(int turnAroundTime) {this.turnAroundTime = turnAroundTime;}


    public String getName() {
        return name;
    }


    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriorityNumber() {
        return priorityNumber;
    }


    public int getTurnAroundTime() { return turnAroundTime; }

    public int getWaitingTime() { return waitingTime; }

}
