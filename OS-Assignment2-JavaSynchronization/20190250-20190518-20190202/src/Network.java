import java.io.IOException;

public class Network {
    Router router;
    Device[] devices;
    int nConnections = 0;
    int nDevices = 0;
    WriteToFile writeToFile;

    public void setInput(int nConnections, int nDevices) throws IOException {
        this.nDevices = nDevices;
        this.nConnections = nConnections;
        router = new Router(nConnections);
        devices = new Device[nDevices];
        writeToFile = new WriteToFile();
    }

    public void setDevices(String s) {
        System.out.println(s);
        try {
            writeToFile.print(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] splitS = s.split("\\r?\\n");
        for (int i = 0; i < splitS.length; i++) {
            String[] split = splitS[i].split("\\s+");
            devices[i] = new Device(split[0], split[1], router);
        }
    }

    public void start() throws InterruptedException {
        for (int j = 0; j < nDevices; j++) {
            devices[j].start();
            //Thread.sleep(10);
        }
    }
}
