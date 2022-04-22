public class Device extends Thread {

    public static Router router;
    public int assignedToConnection;
    public String deviceType;
    public String deviceName;

    Device(String deviceName, String t, Router r) {
        super(deviceName);
        setDeviceName(deviceName);
        setDeviceType(t);
        router = r;
    }

    public void setDeviceName(String name) {
        deviceName = name;
    }

    public void setDeviceType(String t) {
        deviceType = t;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void run() {
        router.connection.P(this);
        try {
            assignedToConnection = router.occupy(this);
            router.login(assignedToConnection, this);
            router.activity(assignedToConnection, this);
            router.logout(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        router.connection.V();
    }
}