package space.commandf1.unban.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public final class Server {
    private final int port;
    private final ServerSocket server;
    private Socket socket;

    public Server(int port) throws IOException {
        this.port = port;
        this.server = new ServerSocket(this.port);
    }

    public void set() throws IOException {
        this.socket = this.server.accept();
    }

    public String read() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        return bufferedReader.readLine();

    }

    public void send(String str) throws IOException {
        PrintStream printStream = new PrintStream(this.socket.getOutputStream());
        printStream.println(str);
    }

    public int getPort() {
        return this.port;
    }

    public ServerSocket getServer() {
        return this.server;
    }
}
