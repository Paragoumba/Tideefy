package fr.paragoumba.tideefy;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Spotify extends Provider {

    public Spotify(HashMap<String, String> urls, String id, String secret){

        super(urls, id, secret);

    }

    protected String tokenType;

    @Override
    public boolean exchangeToken(String code){

        token = code;

        return true;

    }

    @Override
    public List<String> getPlaylists() throws IOException {

        URL url = new URL(urls.get("playlistsUrl"));

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestProperty("Accept", "application/json");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestProperty("Authorization", tokenType + " " + token);



        return new ArrayList<>();

    }
}
