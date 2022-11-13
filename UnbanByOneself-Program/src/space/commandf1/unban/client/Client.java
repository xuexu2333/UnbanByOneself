package space.commandf1.unban.client;

import space.commandf1.unban.hwid.HWID;
import space.commandf1.unban.util.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public final class Client {
    private final String ip;
    private final int port;
    private Socket socket;

    public Client(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);
    }

    public void send(String name, HWID hwid) throws IOException {
        this.socket = new Socket(ip, port);
        PrintStream printStream = new PrintStream(socket.getOutputStream());
        printStream.println(Util.encrypt(name) + "|" + Util.encrypt(hwid.getHWID()));
    }

    public int read() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        return Integer.parseInt(bufferedReader.readLine());
    }

    public int getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }
}
