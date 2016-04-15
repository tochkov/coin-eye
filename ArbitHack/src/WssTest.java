
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketFactory;

import java.net.URI;

/**
 * Created by user on 4/14/2016.
 */
public class WssTest {

    public static void main(String[] args) throws Exception {

        //https://github.com/TooTallNate/Java-WebSocket
//
//        new URI("wss://api2.bitfinex.com:3000/ws")
//
//
        // Connect to "wss://echo.websocket.org" and send "Hello." to it.
        // When a response from the WebSocket server is received, the
        // WebSocket connection is closed.
        WebSocket ws = new WebSocketFactory()
                .createSocket("wss://api2.bitfinex.com:3000/ws")
                .addListener(new WebSocketAdapter() {
                    @Override
                    public void onTextMessage(WebSocket ws, String message) {
                        // Received a response. Print the received message.
                        System.out.println(message);

                        // Close the WebSocket connection.
                    }
                })
                .connect();



        String jsonString = "{\n" +
                "   \"event\":\"subscribe\",\n" +
                "   \"channel\":\"book\",\n" +
                "   \"pair\":\"BTCUSD\",\n" +
                "   \"prec\":\"P0\",\n" +
                "   \"freq\":\"F0\"\n" +
                "}";

//        String jsonString = "{\n" +
//                "   \"event\":\"subscribe\",\n" +
//                "   \"channel\":\"book\",\n" +
//                "   \"pair\":\"BTCUSD\",\n" +
//                "   \"prec\":\"R0\",\n" +
//                "   \"len\":\"25\"\n" +
//                "}";


        ws.sendText(jsonString);

        while (true) {
            Thread.sleep(1000);
        }


    }

}
