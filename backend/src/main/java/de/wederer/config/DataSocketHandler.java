package de.wederer.config;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class DataSocketHandler extends TextWebSocketHandler {
	
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
		double[] previousSensorValues = new double[4];
		for(int i = 0; i<4; i++){
		    previousSensorValues[i] = Math.random()*1000;
        }
        String[] columns = {"id", "timestamp", "sensor", "value", "msg"};
        int i = 0;
        while(true){
            String msg = "{";
            Random random = new Random();
            int sensor = random.nextInt(4);
            for (int u = 0; u<5; u++) {
                switch(columns[u]) {
                    case "id":
                        msg += "\"" + columns[u] + "\":\"" + i + "\"" + ",";
                        break;
                    case "timestamp":
                        //String timeStamp = new SimpleDateFormat("HH:mm:ss.SSS").format(Calendar.getInstance().getTime());
                        long timeStamp =System.currentTimeMillis();
                        msg += "\"" + columns[u] + "\":\"" + timeStamp + "\""+ ",";
                        break;
                    case "sensor":
                        msg += "\"" + columns[u] + "\":\"" + sensor + "\""+ ",";
                        break;
                    case "value":
                        double newSensorValue = Math.min(Math.max((previousSensorValues[sensor] + (Math.random()*300)-Math.random()*300), 0), 10000);
                        msg += "\"" + columns[u] + "\":\"" + newSensorValue + "\""+ ",";
                        //System.out.println("previous: "+previousSensorValues[sensor] + " new: " +newSensorValue);
                        previousSensorValues[sensor] = newSensorValue;
                        break;
                    case "msg":
                        String dummy = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua.";
                        msg += "\"" + columns[u] + "\":\"" + dummy + "\"";
                        break;
                }
            }
            msg += "}";
            //System.out.println(msg);
            double rand = Math.random();
            try
            {
                Thread.sleep(1000);
            }
            catch(InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
            session.sendMessage(new TextMessage(msg));
            i++;
        }
	}

}