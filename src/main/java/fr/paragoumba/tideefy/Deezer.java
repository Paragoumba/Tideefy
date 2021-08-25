package fr.paragoumba.tideefy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Deezer extends Provider {

    public Deezer(HashMap<String, String> urls, String id, String secret){

        super(urls, id, secret);

    }

    @Override
    public boolean exchangeToken(String code) throws IOException {

        String tokenUrl = urls.get("tokenUrl");
        System.out.println("Token URL: " + tokenUrl);

        if (tokenUrl != null){

            URL url = new URL(tokenUrl.replaceAll("YOUR_CODE", code));

            URLConnection connection = url.openConnection();

            try(InputStreamReader isr = new InputStreamReader(connection.getInputStream());
                BufferedReader in = new BufferedReader(isr)){

                String line = in.readLine();

                if (line != null){

                    System.out.println(line);

                    token = line.split("&")[0].split("=")[1];

                    return true;

                }
            }
        }

        return false;

    }

    @Override
    public List<String> getPlaylists(){

        return new ArrayList<>();

    }
}
