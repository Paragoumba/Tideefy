package fr.paragoumba.tideefy;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class HTTPServer {

    public HTTPServer() throws IOException {

        this(8080);

    }

    public HTTPServer(int port) throws IOException {

        this.server = new ServerSocket(port);

    }

    private final ServerSocket server;

    public String retrieveCode(){

        String code = null;

        while (code == null){

            try(Socket socket = server.accept();
                InputStreamReader isr = new InputStreamReader(socket.getInputStream());
                BufferedReader in = new BufferedReader(isr);
                OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
                BufferedWriter out = new BufferedWriter(osw)) {

                // get first line of the request from the client
                String input = in.readLine();

                System.out.println(input);

                // we parse the request with a string tokenizer
                StringTokenizer parse = new StringTokenizer(input);
                String method = parse.nextToken().toUpperCase(); // we get the HTTP method of the client
                // we get file requested
                String fileRequested = parse.nextToken();

                System.out.println("Client requested: " + fileRequested);

                if (!fileRequested.startsWith("/?")){

                    System.out.println("Skipping request...");
                    out.write("HTTP/1.1 404 Not Found\n");
                    out.write("Content-length: 0\n");
                    out.newLine();
                    out.flush();
                    continue;

                }

                String[] arguments = fileRequested.split("\\?")[1].split("&");

                if (method.equals("GET")){

                    for (String argument : arguments){

                        String[] keyValue = argument.split("=");

                        if (keyValue[0].equals("code")){

                            code = keyValue[1];

                        }
                    }
                }

                String pageContent =
                        "<html>\n" +
                        "   <head>\n" +
                        "       <title>Authentication successful</title>\n" +
                        "   </head>\n" +
                        "   <body>\n" +
                        "       <h1>You are now connected!</h1>\n" +
                        "       <h2>You can close this page.</h2>\n" +
                        "   </body>\n" +
                        "</html>\n";

                out.write("HTTP/1.1 200 OK\n");
                out.write("Content-length: " + pageContent.getBytes().length + "\n");
                out.newLine();
                out.write(pageContent);
                out.flush();

            } catch (IOException e){

                e.printStackTrace();

            }
        }

        stop();

        return code;

    }

    public void stop(){

        try {

            server.close();

        } catch (IOException e){

            e.printStackTrace();

        }
    }
}
