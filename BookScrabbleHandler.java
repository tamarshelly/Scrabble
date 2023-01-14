package test;


import java.io.*;
import java.nio.Buffer;
import java.util.Scanner;

public class BookScrabbleHandler implements ClientHandler{
    BufferedReader in;
    PrintWriter out;

    @Override
    public void handleClient(InputStream inFromclient, OutputStream outToClient) {
            in = new BufferedReader(new InputStreamReader(inFromclient));
            try {
                String[] clientMessage = in.readLine().split(","); //the first line of in
                DictionaryManager dm = DictionaryManager.get();
                int length = clientMessage.length -1;
                String[] toCheck= new String[length];
                System.arraycopy(clientMessage, 1, toCheck, 0, length );
                boolean serverAnswer;
                if (clientMessage[0].equals("Q"))
                    serverAnswer = dm.query(toCheck);
                else
                    serverAnswer = dm.challenge(toCheck);
                out = new PrintWriter(outToClient, true);
                out.println(serverAnswer + "\n");
            }catch (IOException e){}
    }

    @Override
    public void close() {
        try{
            in.close();
            out.close();
        }
        catch (IOException e){}
    }
}
