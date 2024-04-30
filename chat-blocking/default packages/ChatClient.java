import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ChatClient {
    private final String SERVER_ADDRESS = "127.0.0.1";
    private ClientSocket clientSocket;
    private Scanner sc;

    public ChatClient(){
        sc = new Scanner(System.in);
    }   

    public void start() throws UnknownHostException, IOException{
        try{
            clientSocket = new ClientSocket(new Socket(SERVER_ADDRESS, ChatServer.PORT));
            new Thread(() -> getMessages()).start();
            System.out.println("Cliente conectado ao servidor em: " + SERVER_ADDRESS + ":" + ChatServer.PORT);
            messageLoop();
        }
        finally{
            clientSocket.close();
        }
    }

    private void messageLoop() throws IOException{
        
        String msg;

        do{
            System.out.print("Digite uma mensagem (Ou sair para finalizar): ");
            msg = sc.nextLine();
                clientSocket.SendMsg(msg);

        }while(!msg.equalsIgnoreCase("sair"));
    }

    public void getMessages(){
        String msg;
        while ((msg = clientSocket.getMessage()) != null){
            System.out.println("Mensagem recebida: " + msg);
        }
    }
    public static void main(String[] args) {
        try {
            ChatClient client = new ChatClient();
            client.start();            
        } catch (Exception e) {
            System.out.println("Não foi possivel fazer conexão com o servidor: " + e.getMessage());
        }
        System.out.println("Cliente finalizado!");
    }
}
