package info.coineye;

import ws.wamp.jawampa.PubSubData;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tochkov.
 */
public class TrollBox {

    private static final String[] ADMINS = new String[]{"InfiniteJest", "Quantum", "SweetJohnDee", "GeezUp", "OldManKidd"};

    private static TrollBox instance;

    public static TrollBox get() {
        if (instance == null)
            instance = new TrollBox();

        return instance;
    }

    private TrollBox() {
    }


    public void onMessageReceived(PubSubData data) {

        try {

            String type = String.valueOf(data.arguments().get(0).toString());
            String messageNumber = String.valueOf(data.arguments().get(1).toString());
            String sender = String.valueOf(data.arguments().get(2).toString());
            String message = String.valueOf(data.arguments().get(3).toString());
            String reputation = String.valueOf(data.arguments().get(4));

            Log.e("--------------------------- type " + type.substring(1, type.length()-1));
            Log.e("--------------------------- messageNumber" + messageNumber);
            Log.e("--------------------------- sender" + sender);

            for (String s : ADMINS) {
                if (s.equals(sender))
                    return;
            }

            List<String> urls = extractUrls(message);

            if (!urls.isEmpty()) {

                for(String url : urls)
                {
                    URL oracle = new URL("http://www.investopedia.com/articles/investing/032216/ethereum-more-important-bitcoin.asp");
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(oracle.openStream()));

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println(inputLine);

//                inputLine

                    }
                    in.close();
                }










                GCM.sendMsg(sender, reputation, message);
            }


        } catch (Exception e) {
            System.out.println("error");
            GCM.sendMsg("!!!error", "", "onMessageReceived " + e.toString());
        }
    }


    public static List<String> extractUrls(String text) {
        List<String> containedUrls = new ArrayList<String>();
        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
        Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);
        Matcher urlMatcher = pattern.matcher(text);

        while (urlMatcher.find()) {
            containedUrls.add(text.substring(urlMatcher.start(0),
                    urlMatcher.end(0)));
        }

        return containedUrls;
    }
}
