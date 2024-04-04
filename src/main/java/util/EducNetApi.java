package util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

/**
 *
 * @author DASI Team
 *
 */
/*

DÉPENDANCES Maven:
    <dependency>
       <groupId>com.google.code.gson</groupId>
       <artifactId>gson</artifactId>
       <version>2.8.5</version>
    </dependency>
    <dependency>
       <groupId>org.apache.httpcomponents</groupId>
       <artifactId>httpclient</artifactId>
       <version>4.5.7</version>
    </dependency>

Description des API Web:
    https://data.education.gouv.fr/explore/?sort=modified&q=ips

Exemple d'utilisation:

    EducNetApi api = new EducNetApi();

    List<String> result = api.getInformationCollege("0692155T");
    // List<String> result = api.getInformationLycee("0690132U");
    if (result != null) {
        String uai = result.get(0);
        String nom = result.get(1);
        String secteur = result.get(2);
        String codeCommune = result.get(3);
        String nomCommune = result.get(4);
        String codeDepartement = result.get(5);
        String nomDepartement = result.get(6);
        String academie = result.get(7);
        String ips = result.get(8);
        System.out.println("Etablissement " + uai + ": " + nom + " à " + nomCommune + ", " + nomDepartement);
    }
    else {
        System.out.println("Etablissement inconnu");
    }

*/

public class EducNetApi {

    private static final String ENCODING_UTF8 = "UTF-8";

    private static final String EDUCNET_API_URL = "https://data.education.gouv.fr/api/records/1.0/search/";

    /*
     * Constructeur
     */
    public EducNetApi() {              
    }

    /*
     * Méthode pour appeler le Service Web EduNet pour les collèges.
     * Renvoie null si le code établissement na-'a pas été trouvé.
     */
    public List<String> getInformationCollege(String codeEtablissement) throws IOException {
        return this.getInformationEtablissement("fr-en-ips_colleges", codeEtablissement);
    }

    /*
     * Méthode pour appeler le Service Web EduNet pour les lycées
     * Renvoie null si le code établissement n'a pas été trouvé.
     */
    public List<String> getInformationLycee(String codeEtablissement) throws IOException {
        return this.getInformationEtablissement("fr-en-ips_lycees", codeEtablissement);
    }
    
    /*
     * Méthode pour appeler le Service Web EduNet en précisant le dataset ciblé (collèges ou lycées)
     */
    protected List<String> getInformationEtablissement(String dataset, String codeEtablissement) throws IOException {

        ArrayList<String> result = null;

        JsonObject response = this.get(EDUCNET_API_URL,
                new BasicNameValuePair("dataset", dataset),
                //new BasicNameValuePair("q", codeEtablissement),
                new BasicNameValuePair("refine.uai", codeEtablissement),
                new BasicNameValuePair("refine.rentree_scolaire", "2021-2022")
        );

        JsonObject targetJsonObject = null;
        
        JsonArray results = response.getAsJsonArray("records");
        Iterator<JsonElement> it = results.iterator();
        while (it.hasNext()) {
            JsonObject jsonResult = (JsonObject)it.next();
            JsonObject jsonResultFields = jsonResult.getAsJsonObject("fields");
            
            if (codeEtablissement.equals(jsonResultFields.get("uai").getAsString())) {
                targetJsonObject = jsonResultFields;
            }
        }
        
        if (targetJsonObject != null) {
            //System.out.println("JSON >>>"+targetJsonObject.toString()+"<<<");
            
            result = new ArrayList<String>();
            result.add(targetJsonObject.get("uai").getAsString());
            result.add(targetJsonObject.get("nom_de_l_etablissment").getAsString());
            result.add(targetJsonObject.get("secteur").getAsString());
            result.add(targetJsonObject.get("code_insee_de_la_commune").getAsString());
            result.add(targetJsonObject.get("nom_de_la_commune").getAsString());
            result.add(targetJsonObject.get("code_du_departement").getAsString());
            result.add(targetJsonObject.get("departement").getAsString());
            result.add(targetJsonObject.get("academie").getAsString());
            
            JsonElement jsonIPS = targetJsonObject.get("ips");
            if (jsonIPS != null) {
                result.add(targetJsonObject.get("ips").getAsString());
            }
            else {
                jsonIPS = targetJsonObject.get("ips_ensemble_gt_pro");
                result.add(jsonIPS.getAsString());
            }
            
        }

        return result;
    }

    /*
     * Méthode interne pour réaliser un appel HTTP et interpréter le résultat comme Objet JSON
     */
    protected JsonObject get(String url, NameValuePair... parameters) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();

        JsonElement responseElement = null;
        JsonObject responseContainer = null;
        Integer responseStatus = null;

        try {

            HttpGet httpGet = new HttpGet(url);
            //httpGet.setEntity(new UrlEncodedFormEntity(Arrays.asList(parameters), ENCODING_UTF8));
            ArrayList<NameValuePair> parameterList = new ArrayList<NameValuePair>();
            for (NameValuePair parameter : parameters) {
                parameterList.add(parameter);
            }
            URI uri = new URIBuilder(httpGet.getURI())
                    .addParameters(parameterList)
                    .build();
            httpGet.setURI(uri);
            CloseableHttpResponse response = httpclient.execute(httpGet);
            try {

                responseStatus = response.getStatusLine().getStatusCode();
                HttpEntity entity = response.getEntity();

                if (entity != null) {
                    JsonReader jsonReader = new JsonReader(new InputStreamReader(entity.getContent(), ENCODING_UTF8));
                    try {

                        JsonParser parser = new JsonParser();
                        responseElement = parser.parse(jsonReader);

                    } finally {
                        jsonReader.close();
                    }
                }

            } finally {
                response.close();
            }

            if (responseStatus != null && responseStatus == 200 && responseElement != null) {
                responseContainer = responseElement.getAsJsonObject();
            }
        }
        catch (URISyntaxException ex) {
            throw new EducNetApiIOException("Service Request FAILED: Could NOT BUILD GET URL with parameters ~~> check parameters ???" + "\n******** Target URL =>  " + url + "  <= ********" + "\n");
        } catch (UnknownHostException ex) {
            throw new EducNetApiIOException("Service Request FAILED: Could NOT CONNECT to remote Server ~~> check target URL ???" + "\n******** Target URL =>  " + url + "  <= ********" + "\n");
        } catch (HttpHostConnectException ex) {
            throw new EducNetApiIOException("Service Request FAILED: Could NOT CONNECT to remote Server ~~> check target URL ???" + "\n******** Target URL =>  " + url + "  <= ********" + "\n");
        } catch (IllegalStateException ex) {
            throw new EducNetApiIOException("Service Request FAILED: Wrong HTTP Response FORMAT - not a JSON Object ~~> check target URL output ???" + "\n******** Target URL =>  " + url + "  <= ********" + "\n**** Parameters:\n" + EducNetApiIOException.debugParameters(" * ", parameters));
        }

        httpclient.close();

        if (responseContainer == null) {
            String statusLine = "???";
            if (responseStatus != null) {
                statusLine = responseStatus.toString();
                if (responseStatus == 400) {
                    statusLine += " - BAD REQUEST ~~> check request parameters ???";
                }
                if (responseStatus == 404) {
                    statusLine += " - NOT FOUND ~~> check target URL ???";
                }
                if (responseStatus == 500) {
                    statusLine += " - INTERNAL SERVER ERROR ~~> check target Server Log ???";
                }
            }
            throw new EducNetApiIOException("Service Request FAILED with HTTP Error " + statusLine + "\n******** Target URL =>  " + url + "  <= ********" + "\n**** Parameters:\n" + EducNetApiIOException.debugParameters(" * ", parameters));
        }

        return responseContainer;
    }

    static public class EducNetApiIOException extends IOException {

        public EducNetApiIOException(String message) {
            super(message);
        }

        public EducNetApiIOException(String message, Throwable cause) {
            super(message, cause);
        }

        public EducNetApiIOException(Throwable cause) {
            super(cause);
        }

        public static String debugParameters(String alinea, NameValuePair... parameters) {

            StringBuilder debug = new StringBuilder();

            for (NameValuePair parameter : parameters) {
                debug.append(alinea).append(parameter.getName()).append(" = ").append(parameter.getValue()).append("\n");
            }

            return debug.toString();
        }

    }

}