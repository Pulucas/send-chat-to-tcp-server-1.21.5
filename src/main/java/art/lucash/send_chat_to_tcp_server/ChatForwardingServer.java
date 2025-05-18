package art.lucash.send_chat_to_tcp_server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatForwardingServer {
    private final Set<PrintWriter> clients = ConcurrentHashMap.newKeySet();
    private final int port;
    private Thread serverThread;

    public ChatForwardingServer(int port) {
        this.port = port;
    }

    public void start() {
        serverThread = new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("ChatForwardingServer started on port " + port);

                while (!Thread.currentThread().isInterrupted()) {
                    Socket client = serverSocket.accept();
                    PrintWriter out = new PrintWriter(client.getOutputStream(), true);
                    clients.add(out);

                    new Thread(() -> {
                        try {
                            client.getInputStream().read(); // Block until disconnect
                        } catch (IOException ignored) {
                        } finally {
                            clients.remove(out);
                            try {
                                client.close();
                            } catch (IOException ignored) {}
                        }
                    }).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        serverThread.setDaemon(true);
        serverThread.start();
    }

    public void sendMessage(String message) {
        for (PrintWriter client : clients) {
            client.println(message);
        }
    }
}