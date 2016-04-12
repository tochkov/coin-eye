package info.coineye;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import ws.wamp.jawampa.PubSubData;
import ws.wamp.jawampa.WampClient;
import ws.wamp.jawampa.WampClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CoinEye {

    public static void main(String[] args) throws IOException {

        System.out.println("===== STARTED =====");

        try {
            go();
        } catch (Exception applicationError) {
            applicationError.printStackTrace();
        }

        while (true) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

//
//        try {
//
//            URL url = new URL("http://www.coindesk.com/dubais-museum-future-sees-blockchain-smart-cities/");
//
//            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
//            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");
//
//            BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));
//
//            String inputLine;
//            while ((inputLine = in.readLine()) != null) {
////                System.out.println(inputLine);
//
////                inputLine
//
//
//                List<String> possibleDates = new ArrayList<String>();
//                String dateRegex = "([0-9]{1,2} ?([\\-/\\\\] ?[0-9]{1,2} ?| (Jan|Feb|Mar|Apr|May|Jun|Jul|Aug|Sep|Oct|Nov|Dec|January|February|March|April|May|June|July|August|September|October|November|December) ?)([\\-/\\\\]? ?('?[0-9]{2}|[0-9]{4}))?)";
//                Pattern pattern = Pattern.compile(dateRegex, Pattern.CASE_INSENSITIVE);
//                Matcher dateMatcher = pattern.matcher(inputLine);
//
//                while (dateMatcher.find()) {
//                    possibleDates.add(inputLine.substring(dateMatcher.start(0),
//                            dateMatcher.end(0)));
//                }
//
//
//                for (String possibleDate : possibleDates) {
////                    Date date = checkDate(possibleDate);
////
////                    if (date != null) {
////                        Log.e(new SimpleDateFormat().format(date));
////                    }
//
//                    Log.e(possibleDate);
//                }
//
//
//            }
//            in.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        String URL = "http://www.forbes.com/sites/rogeraitken/2016/03/14/can-bitteasers-blockchain-ad-network-disrupt-pay-per-click-market/#487542064e22";


//        try {
//            UrlExtractor.m2(URL);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        try {
//            UrlExtractor.m3(URL);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


//        SlackSession session = SlackSessionFactory.createWebSocketSlackSession("xoxb-29744082993-dR7OaGmOpmW5jVjftqndnBsj");
//        session.connect();
//
//        SlackChannel channel = session.findChannelByName("url-hound-test");
//
//        session.sendMessage(channel, "Im bahur hue hue hue");






    }




    private static Date checkDate(String stringDate) {

        for (Locale locale : DateFormat.getAvailableLocales()) {
            for (int style = DateFormat.FULL; style <= DateFormat.SHORT; style++) {
                DateFormat df = DateFormat.getDateInstance(style, locale);
                try {
                    return df.parse(stringDate);
//                    Log.e(new SimpleDateFormat().format(df.parse(stringDate)));
                } catch (ParseException ex) {
                    continue;
                }
            }
        }

        return null;

    }


    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";


    private static void go() throws Exception {
        final WampClient client = new WampClientBuilder()
//                .withUri("wss://api.poloniex.com")
                .withUri("wss://api2.bitfinex.com:3000/ws")
//                .withUri("wss://ws.bitfinex.com:3333")
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
                            System.out.println(ANSI_RED + "pubDat: " + pubSubData.arguments() + ANSI_RESET);

//                            TrollBox.get().onMessageReceived(pubSubData);


                        }
                    }, new Action1<Throwable>() {
                        public void call(Throwable throwable) {
                            System.out.println("pubDat: " + throwable);
                            throwable.printStackTrace();

                            GCM.sendMsg("!!!error", "", "Some Exception occurred");
                            client.close();
                            connectAndSubscribe(client);

                        }
                    });
                } else if (state instanceof WampClient.DisconnectedState) {
                    System.out.println("Disconnected " + ((WampClient.DisconnectedState) state).disconnectReason());
                    GCM.sendMsg("!!!error", "", "Disconnected");
                    client.close();
                    connectAndSubscribe(client);

                }

            }
        });
    }

}
