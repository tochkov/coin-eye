package info.coineye;

import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * Created by fefo on 24-Mar-16.
 */
public class UrlExtractor {

//    public static void m1(){
//
//
//
//        Tika t = new Tika();
//
//        WSEndpointReference.Metadata md = new WSEndpointReference.Metadata();
//        URL u = null;
//        try {
//            u = new URL("http://www.xyz.com/documents/files/xyz-china.pdf");
//
//            String content1= t.parseToString(u);
//            System.out.println("hello" +content1);
//        } catch (MalformedURLException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (TikaException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        try {
//            Reader r = t.parse(u.openStream(), md);
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        try {
//            for (String name : md.names()){
//                String value = md.get(name);
//                System.out.println("key:- " +name);
//                System.out.println("value:- " +value);
//                //getMetaData().put(name.toLowerCase(), md.get(name));
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//
//
//
//    }







    public static void m2(String urlString) throws IOException {




        URL url = new URL(urlString);

        System.out.println("URL:- " +url);
        URLConnection connection = url.openConnection();


        System.out.println("M2: " + connection.getHeaderField("Last-Modified"));



    }





    public static void m3(String urlString) throws IOException {


        URL url = new URL(urlString);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();

        long date = httpCon.getLastModified();
        if (date == 0)
            System.out.println("M3: " + "No last-modified information.");
        else
            System.out.println("M3: " + "Last-Modified: " + new Date(date));





    }



}
