import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {


    public static void main(String[] args) throws IOException {

        try (Socket socket = new Socket("192.168.10.100", 1222)) {
            PrintStream printToServer = new PrintStream(socket.getOutputStream());
            Scanner inputFromServer=new Scanner(socket.getInputStream());
            Scanner scanner = new Scanner(System.in);
            System.out.println("Client connected: " + socket.getInetAddress());



            // Create a separate thread to continuously receive server responses
            startResponseReaderThread(socket);

            while (true) {
                String command = scanner.nextLine();
                if (command.equals("exit")) {
                    break;
                }
                // Send command to server
                printToServer.println(command);
            }
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    //create method for response to server
    private static void startResponseReaderThread(Socket socket) {
        Thread responseThread = new Thread(() -> {
            try {
                Scanner serverResponse = new Scanner(socket.getInputStream());
                while (serverResponse.hasNextLine()) {
                    String response = serverResponse.nextLine();
                    System.out.println(response);
                }
            } catch (IOException e) {
                System.out.println("Server disconnected");
            }
        });
        responseThread.start();
    }





}


