package fr.paragoumba.tideefy;

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Tideefy {

    public static void main(String[] args){

        Yaml yaml = new Yaml();

        try (InputStream is = Tideefy.class.getResourceAsStream("/secrets.yml")){

            var secretsMap = (HashMap) yaml.load(is);
            List<String> providerList = (List<String>) secretsMap.get("providers");
            List<Provider> providers = new ArrayList<>();

            for (String providerName : providerList){

                HashMap secrets = (HashMap) secretsMap.get(providerName);

                if (secrets != null){

                    Provider provider = Provider.getInstance(providerName,
                            (HashMap<String, String>) secrets.get("urls"),
                            (String) secrets.get("id"),
                            (String) secrets.get("secret"));

                    if (provider != null){

                        providers.add(provider);

                    } else {

                        System.err.println("Unknown provider " + providerName + '.');

                    }
                }
            }

            for (Provider provider : providers){

                System.out.println(provider);

                try {

                    if (provider.retrieveToken()){

                        System.out.println("List of playlists:");

                        for (String playlist : provider.getPlaylists()){

                            System.out.println(playlist);

                        }

                    } else {

                        System.err.println("Failed to retrieve token for provider " + provider + '.');

                    }

                } catch (IOException e){

                    e.printStackTrace();

                }
            }

        } catch (IOException e){

            e.printStackTrace();

        }
    }
}
