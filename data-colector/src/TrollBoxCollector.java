import rx.functions.Action1;
import rx.schedulers.Schedulers;
import ws.wamp.jawampa.PubSubData;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;

import java.io.FileWriter;
import java.io.IOException;

import static info.coineye.CoinEye.ANSI_CYAN;
import static info.coineye.CoinEye.ANSI_RESET;

/**
 * Created by fefo on 29-Mar-16.
 */
public class TrollBoxCollector {

    public static boolean keepGoing = true;

    private static FileWriter fileWriter;

    public static void main(String[] args) throws InterruptedException {


        String folderPath = "";
        if (args != null && args.length > 0)
            folderPath = args[0];
//        String folderPath = "C:/Users/fefo/Desktop/CSV_TEST/";

        try {

            fileWriter = new FileWriter(folderPath + "/trollbox_history.csv");


            fileWriter.append("id,date,username,message,rep");
            fileWriter.append("\n");
//            fileWriter.append(NEW_LINE_SEPARATOR);


            go();


        } catch (Exception e) {
            e.printStackTrace();
        }


        while (keepGoing) {
            Thread.sleep(30);
        }

    }


    private static void go() throws Exception {
        final WampClient client = new WampClientBuilder()
                .withUri("wss://api.poloniex.com")
                .withRealm("realm1")
                .build();


        connectAndSubscribe(client);

    }

    private static void connectAndSubscribe(final WampClient client) {
        // open connection
        client.open();

        // subscribe to connection status events
        client.statusChanged().observeOn(Schedulers.newThread()).subscribe(new Action1<WampClient.State>() {
            public void call(WampClient.State state) {


                if (state instanceof WampClient.ConnectingState) {
                    System.out.println("Connecting");
                } else if (state instanceof WampClient.ConnectedState) {
                    System.out.println(ANSI_CYAN + "Connected" + ANSI_RESET);

                    // ticker, trollbox, BTC_ETH
                    client.makeSubscription("trollbox").observeOn(Schedulers.newThread()).subscribe(new Action1<PubSubData>() {
                        public void call(PubSubData pubSubData) {
//                            System.out.println(ANSI_RED + "pubDat: " + pubSubData.arguments() + ANSI_RESET);

                            writeLineToCSV(pubSubData);

                        }
                    }, new Action1<Throwable>() {
                        public void call(Throwable throwable) {
                            System.out.println("pubDat: " + throwable);
                            throwable.printStackTrace();

                            client.close();
                            connectAndSubscribe(client);

                        }
                    });
                } else if (state instanceof WampClient.DisconnectedState) {
                    System.out.println("Disconnected");
                    client.close();
                    connectAndSubscribe(client);

                }

            }
        });
    }

    private static void writeLineToCSV(PubSubData pubSubData) {


        String type = String.valueOf(pubSubData.arguments().get(0).toString());

        if (type == null || !type.equals("\"trollboxMessage\""))
            return;

        String id = String.valueOf(pubSubData.arguments().get(1).toString());
        String date = String.valueOf(System.currentTimeMillis());
        String username = String.valueOf(pubSubData.arguments().get(2).toString());
        String message = String.valueOf(pubSubData.arguments().get(3).toString());
        String rep = String.valueOf(pubSubData.arguments().get(4));


        try {

            fileWriter
                    .append(id).append(',')
                    .append(date).append(',')
                    .append(username).append(',')
                    .append(message).append(',')
                    .append(rep)
                    .append("\n")
                    .flush();


        } catch (Exception e) {
            e.printStackTrace();

            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException ex) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                ex.printStackTrace();
            }

        } finally {

        }


    }


}
