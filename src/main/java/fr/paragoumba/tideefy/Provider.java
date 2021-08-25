package fr.paragoumba.tideefy;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

public abstract class Provider {

    public Provider(HashMap<String, String> urls, String id, String secret){

        if (urls != null){

            urls.computeIfPresent("loginUrl", (key, value) -> value
                    .replaceAll("YOUR_ID", id)
                    .replaceAll("REDIRECT_URI", "http%3A%2F%2Flocalhost%3A8080"));

            urls.computeIfPresent("tokenUrl", (key, value) -> value
                    .replaceAll("YOUR_ID", id)
                    .replaceAll("YOUR_SECRET", secret)
            );
        }

        this.urls = urls;
        this.id = id;
        this.secret = secret;

    }

    protected final HashMap<String, String> urls;
    protected final String id;
    protected final String secret;
    protected String token;

    public static Provider getInstance(String provider, HashMap<String, String> urls, String id, String secret){

        return switch (provider){
            case "spotify" -> new Spotify(urls, id, secret);
            case "deezer" -> new Deezer(urls, id, secret);
            default -> null;
        };
    }

    protected void requestUserLogin(){

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)){

            try {

                Desktop.getDesktop().browse(new URI(urls.get("loginUrl")));

            } catch (IOException | URISyntaxException e){

                e.printStackTrace();

            }

        } else {

            System.err.println("Error: could not open browser.");

        }
    }

    protected abstract boolean exchangeToken(String code) throws IOException;

    public boolean retrieveToken() throws IOException {

        requestUserLogin();

        HTTPServer server = new HTTPServer(8080);

        String code = server.retrieveCode();

        System.out.println("Code: " + code);

        if (exchangeToken(code)){

            System.out.println("Token: " + token);

            return true;

        }

        return false;

    }

    @Override
    public final String toString(){

        return getClass().getName();

    }

    public abstract List<String> getPlaylists() throws IOException;

}
