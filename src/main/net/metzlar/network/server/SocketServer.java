package net.metzlar.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketServer extends Thread {
    private boolean stopped = false;

    private int port;

    private Server server;
    private ServerSocket serverSocket;

    public SocketServer(int port, Server server) {
        this.port = port;
        this.server = server;
    }

    @Override
    public void run() {
        super.run();

        serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        System.out.println("Server ready, awaiting requests.\n");
        while (!stopped) {
            try {
                socketAccept(serverSocket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void socketAccept(ServerSocket serverSocket) throws IOException {
        Socket clientSocket;

        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            if (stopped) {
                System.out.println("Server Stopped.");
                return;
            }
            throw new RuntimeException("Error accepting client connection", e);
        }

        server.registerClient(clientSocket);
    }

    public void terminate() {
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Could not close server gracefully");
        }

        System.out.println("Host is shut down.");

        stopped = true;
    }
}
