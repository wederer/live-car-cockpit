package de.wederer.config;

import org.apache.tomcat.util.codec.binary.Base64;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

@Component
public class MySocketHandler extends TextWebSocketHandler {

    private static File[] fileList;
    private static double[] previousSensorValues = {
            Math.random() * 1000,
            Math.random() * 1000,
            Math.random() * 1000,
            Math.random() * 1000,
    };
    private static int id = 0;

    public MySocketHandler() {
        File imgDir = new File("./resources/dummy_images");
        File[] fileList = imgDir.listFiles((__, filename) -> filename.endsWith(".jpg"));
        assert fileList != null;
        Arrays.sort(fileList);
        MySocketHandler.fileList = fileList;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        JSONObject obj = new JSONObject();

        // send messages forever
        while(true) {
            for (File currentFile : MySocketHandler.fileList) {
                obj.put("image", encodeFileToBase64Binary(currentFile));
                addSensorValues(obj);
                session.sendMessage(new TextMessage(obj.toString()));

                try {
                    Thread.sleep(200);
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    private void addSensorValues(JSONObject obj) {
        obj.put("id", MySocketHandler.id);
        MySocketHandler.id++;

        obj.put("timestamp", System.currentTimeMillis());

        int sensor = new Random().nextInt(4);
        obj.put("sensor", sensor);

        double newSensorValue = Math.min(Math.max((previousSensorValues[sensor] + (Math.random() * 300) - Math.random() * 300), 0), 10000);
        obj.put("value", newSensorValue);
        previousSensorValues[sensor] = newSensorValue;

        obj.put("msg", "42");
    }

    private String encodeFileToBase64Binary(File file) {
        String encodedfile = null;
        try {
            FileInputStream fileInputStreamReader = new FileInputStream(file);
            byte[] bytes = new byte[(int) file.length()];
            fileInputStreamReader.read(bytes);
            encodedfile = new String(Base64.encodeBase64(bytes), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return encodedfile;
    }
}

