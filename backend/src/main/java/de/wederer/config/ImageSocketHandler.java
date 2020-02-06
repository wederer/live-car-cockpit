package de.wederer.config;

import com.google.gson.Gson;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class ImageSocketHandler extends TextWebSocketHandler {

    private List<WebSocketSession> sessions = new CopyOnWriteArrayList<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message)
            throws IOException {
        Map<String, String> value = new Gson().fromJson(message.getPayload(), Map.class);
        //session.sendMessage(new TextMessage("Hello " + value.get("name") + " !"));
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //the messages will be broadcasted to all users.1
        sessions.add(session);
        File dir = new File("./backend/resources/dummy_images");
        File[] fileList = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename) {
                return filename.endsWith(".jpg");
            }
        });
        Arrays.sort(fileList);
        int i = 109000;
        while (true) {
            File currentFile = fileList[i%fileList.length];
            String base64 = encodeFileToBase64Binary(currentFile);
            double rand = Math.random();
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            session.sendMessage(new TextMessage(base64));
            i++;
        }
    }

    private String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), "UTF-8");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return encodedfile;
    }
}

