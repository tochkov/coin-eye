import com.xeiam.xchange.Exchange;
import com.xeiam.xchange.ExchangeFactory;
import com.xeiam.xchange.bitfinex.v1.BitfinexExchange;
import com.xeiam.xchange.exceptions.ExchangeException;
import com.xeiam.xchange.service.streaming.*;
import info.coineye.GCM;
import info.coineye.Log;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import ws.wamp.jawampa.ApplicationError;
import ws.wamp.jawampa.PubSubData;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;

import java.net.URISyntaxException;
import java.util.HashMap;

/**
 * Created by user on 4/14/2016.
 */
public class WssTest {

    public static void main(String[] args) throws InterruptedException, URISyntaxException, ApplicationError {




                        //https://github.com/TooTallNate/Java-WebSocket


        final WampClient client = new WampClientBuilder()
                .withUri("wss://api2.bitfinex.com:3000/ws")
//                .withUri("wss://ws.bitfinex.com:3333")
                .withRealm("realm1")
                .withStrictUriValidation(false)
                .build();


//        client.call()

        client.statusChanged().subscribe(new Action1<WampClient.State>() {
            public void call(WampClient.State state) {


                if (state instanceof WampClient.ConnectingState) {
                    System.out.println("Connecting");
                } else if (state instanceof WampClient.ConnectedState) {
                    System.out.println("Connected");

                    // ticker, trollbox, BTC_ETH
//                    client.makeSubscription("trollbox").observeOn(Schedulers.newThread()).subscribe(new Action1<PubSubData>() {
//                        public void call(PubSubData pubSubData) {
//                            System.out.println( "pubDat: " + pubSubData.arguments() );
//
////                            TrollBox.get().onMessageReceived(pubSubData);
//
//
//                        }
//                    }, new Action1<Throwable>() {
//                        public void call(Throwable throwable) {
////                            System.out.println("pubDat: " + throwable);
////                            throwable.printStackTrace();
////
////                            GCM.sendMsg("!!!error", "", "Some Exception occurred");
////                            client.close();
////                            connectAndSubscribe(client);
//
//                        }
//                    });
                } else if (state instanceof WampClient.DisconnectedState) {
                    System.out.println("Disconnected " + ((WampClient.DisconnectedState) state).disconnectReason());
//                    GCM.sendMsg("!!!error", "", "Disconnected");
//                    client.close();
//                    connectAndSubscribe(client);

                }

            }
        });


        client.open();

        Log.blue("---------------------------------------");
        while (true) {
            Thread.sleep(1000);
        }


    }

}
