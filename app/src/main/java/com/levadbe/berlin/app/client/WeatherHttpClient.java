package com.levadbe.berlin.app.client;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ortal Cohen on 18/7/2017.
 */

public class WeatherHttpClient {

    private static String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?APPID=038231e6e83e396a872882e7036be17e&q=";
    private static String IMG_URL = "http://openweathermap.org/img/w/";

    public String getWeatherData(String location) {
        HttpURLConnection con = null;
        InputStream is = null;

        try {
            con = (HttpURLConnection) (new URL(BASE_URL + location)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            StringBuffer buffer = new StringBuffer();
            is = con.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while ((line = br.readLine()) != null)
                buffer.append(line + "rn");

            is.close();
            con.disconnect();
            return buffer.toString();
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (Throwable t) {
            }
            try {
                con.disconnect();
            } catch (Throwable t) {
            }
        }

        return null;

    }

    public String getImage(String code) {
        return IMG_URL + code + ".png";
//        HttpURLConnection con = null ;
//        InputStream is = null;
//        try {
//            con = (HttpURLConnection) ( new URL(IMG_URL + code+".png")).openConnection();
//            con.setRequestMethod("GET");
//            con.setDoInput(true);
//            con.setDoOutput(true);
//            con.connect();
//
//            // Let's read the response
//            is = con.getInputStream();
//            byte[] buffer = new byte[1024];
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//
//            while ( is.read(buffer) != -1)
//                baos.write(buffer);
//
//            return baos.toByteArray();
//        }
//        catch(Throwable t) {
//            t.printStackTrace();
//        }
//        finally {
//            try { is.close(); } catch(Throwable t) {}
//            try { con.disconnect(); } catch(Throwable t) {}
//        }
//
//        return null;

    }
}