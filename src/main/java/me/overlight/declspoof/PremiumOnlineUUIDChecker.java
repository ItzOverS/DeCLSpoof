package me.overlight.declspoof;

import com.google.gson.JsonObject;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PremiumOnlineUUIDChecker {
    public enum isPrem{
        Yes,
        No,
        Undefined
    }
    public static isPrem isPremium(Player player) {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.ashcon.app/mojang/v2/user/" + player.getName()).openConnection();
            conn.setConnectTimeout(2000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("accept", "application/json");
            conn.setRequestProperty("userAgent", "Mozilla/5.0");
            if(conn.getResponseCode() == 200){
                JSONObject json = getAsJsoN(conn.getInputStream());
                if(!((String)json.get("uuid")).equals(player.getUniqueId().toString())) return isPrem.No;
                if(!((String)json.get("name")).equals(player.getName())) return isPrem.No;
                return isPrem.Yes;
            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
        return isPrem.Undefined;
    }

    private static JSONObject getAsJsoN(InputStream c) throws IOException, ParseException {
        BufferedReader in = new BufferedReader(new InputStreamReader(c));
        String inputLine;
        StringBuilder response = new StringBuilder();
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        JSONParser json = new JSONParser();
        return (JSONObject) (json.parse(response.toString()));
    }

}
