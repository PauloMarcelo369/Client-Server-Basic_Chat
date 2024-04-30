import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


public class ChatServer{
    public static final int PORT = 4000;
    private ServerSocket server;
    private final List<ClientSocket> clients = new ArrayList<>();

    public void start() throws IOException{
        server = new ServerSocket(PORT);
        System.out.println("Servidor iniciado na porta: " + PORT);
        clientConnectionLoop();
    }

    private void clientConnectionLoop(){
        while(true){
            try{
                System.out.println("Entrei aqui meu amigo");
                ClientSocket clientSocket = new ClientSocket(server.accept());
                clients.add(clientSocket);
                new Thread(() -> clientMessageLoop(clientSocket)).start(); 
            }
            catch(IOException e){
                System.out.println("Erro ao estabelecer conexão com o cliente: " + e.getMessage());
            }
        }
    }

    private void clientMessageLoop(ClientSocket clientSocket){
        String msg;
        try{
            while ((msg = clientSocket.getMessage()) != null){
                if (msg.equals("sair")) return;
                System.out.printf("Msg recebida do cliente %s: %s\n", clientSocket.getRemoteSocketAddress(), msg);
                sendMsgToAll(clientSocket, msg);
            }
        }
        finally{
            clientSocket.close();
        }
    }

    private void sendMsgToAll(ClientSocket sender, String msg){
        Iterator<ClientSocket> iterator = clients.iterator();
        while (iterator.hasNext()){
            ClientSocket clientSocket = iterator.next();
            if (!sender.equals(clientSocket)) 
                if(!clientSocket.SendMsg(msg))
                    iterator.remove();
            }
    }
    
    public static void main(String[] args) {
        try {
            ChatServer chatServer = new ChatServer();
            chatServer.start();
            
        } catch (Exception e) {
            System.out.println("Erro ao iniciar servidos: " + e.getMessage());
        }
        System.out.println("Servidor finalizado com sucesso!");
    }
}