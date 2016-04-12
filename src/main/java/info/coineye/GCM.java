package info.coineye;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tochkov.
 */
public class GCM {

    public static final String SENDER_ID = "128138322270";


    public static final String API_KEY = "AIzaSyAssJk2pnS-oMsB4rjgxYmtsLezLcK22i8";
    public static final String REG_ID = "fAoD4iTJHwg:APA91bE_W7hDbR4BFlhkEeAi98wC9YtOcHkGCycgFG5mEJUlQA93vSUTPxDl4f0M2RtYRxXORGuPg46ZS1yiI-0mM4IRU-wGyBTg0rFGv42pYI1zrl5fgKqGc-bCpxpNSQXM_rGCT9XM";
    public static final int RETRIES = 3;

    public static void sendMsg(String senderTroll, String reputation, String urlString)
    {
        try {
        Sender sender = new Sender(API_KEY);
        Message message = new Message.Builder()
//                .addData("type", "xxx")
//                .addData("senderTroll", senderTroll + " " + reputation)
//                .addData("urlString", urlString)
                .addData("message", "[" + new SimpleDateFormat().format(new Date()) + "] " + senderTroll + " (" + reputation + ") :  " + urlString)
                .addData("priority","")
                .build();

            Result result = sender.send(message, REG_ID, RETRIES);
//            System.out.println(result.getErrorCodeName());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception ex){

        }
    }

}
