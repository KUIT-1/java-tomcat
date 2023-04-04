package webserver;

import javax.swing.text.Document;
import javax.swing.text.Element;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;


public class RequestHandler implements Runnable{
    Socket connection;
    private static final Logger log = Logger.getLogger(RequestHandler.class.getName());

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.log(Level.INFO, "New Client Connect! Connected IP : " + connection.getInetAddress() + ", Port : " + connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()){
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            DataOutputStream dos = new DataOutputStream(out);

            //요구사항 1
            String url = br.readLine();
            String[] info=url.split(" ");
            String type=info[0];
            String fileName=info[1].split("/")[1];

            if (url != null) {
                System.out.println(url);
            }

            if(fileName.equals("index.html")) {
                try {
                    // read all bytes
                    byte[] index = Files.readAllBytes(Paths.get("webapp/" + fileName));

                    // convert bytes to string
                    String content = new String(index, StandardCharsets.UTF_8);

                    // print contents
                    System.out.println(content);

                    Document doc = Jsoup.parseBodyFragment(content);
                    Element body = doc.body();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }

//            byte[] body = "Hello World".getBytes();
//            response200Header(dos, body.length);
//            responseBody(dos, body);

        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.log(Level.SEVERE, e.getMessage());
        }
    }

}
