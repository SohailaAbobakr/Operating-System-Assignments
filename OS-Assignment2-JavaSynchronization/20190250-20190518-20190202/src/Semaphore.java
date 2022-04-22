import java.io.IOException;

public class Semaphore {
    public static int value;
    public Device device;
    WriteToFile writeToFile;

    Semaphore(int v) throws IOException {
        value = v;
        writeToFile = new WriteToFile();
    }

    public synchronized void P(Device device) {
        this.device = device;
        value--;
        if (value < 0) {
            try {
                System.out.println(device.getName() + "(" + device.getDeviceType() + ")" + " arrived and waiting");
                try {
                    writeToFile.print(device.getName() + "(" + device.getDeviceType() + ")" + " arrived and waiting");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else {
            System.out.println("(" + device.getName() + ")" + "(" + device.getDeviceType() + ")" + " arrived");
            try {
                writeToFile.print("(" + device.getName() + ")" + "(" + device.getDeviceType() + ")" + " arrived");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void V() {
        value++;
        if (value <= 0)
            notify();
    }
}