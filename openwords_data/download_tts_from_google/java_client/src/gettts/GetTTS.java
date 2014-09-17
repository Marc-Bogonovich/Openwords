package gettts;

import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * A script function that get word-pages from current server and request the
 * audio of each word one by one and then upload the audio file to the current
 * server.
 *
 * @author hanaldo
 */
public class GetTTS {

    public static void main(String[] args) throws InterruptedException {
        int start = Integer.parseInt(args[0]);//start page
        int max = Integer.parseInt(args[1]);//last page
        int lang = Integer.parseInt(args[2]);//language id
        String code = args[3];//language code
        System.out.println("Start from page: " + start);

        for (int i = start; i <= max; i++) {
            try {
                List<Word> words = getWords(lang, i, 100);

                for (Word word : words) {
                    try {
                        System.out.println(word.getId() + " " + word.getWord());
                        File f = getAudio(word.getId(), word.getWord(), code);
                        uploadFile("http://openwords.org/api-v1/uploadWordAudio.php", f, word.getId());
                        Thread.sleep(5000);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("Retry word " + word.getId() + ":" + word.getWord() + "  in 10 seconds...");
                        Thread.sleep(10000);
                        try {
                            File f = getAudio(word.getId(), word.getWord(), code);
                            uploadFile("http://openwords.org/api-v1/uploadWordAudio.php", f, word.getId());
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            System.err.println("Word " + word.getId() + ":" + word.getWord() + " fail");
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                i -= 1;
                System.out.println("Retry page " + i + "  in 10 seconds...");
                Thread.sleep(10000);
                continue;
            }
            Thread.sleep(10000);
        }

    }

    public static void uploadFile(String url, File file, int wordId) {
        HttpPost post = new HttpPost(url);

        FileBody f = new FileBody(file);
        StringBody word = new StringBody(String.valueOf(wordId), ContentType.TEXT_PLAIN);

        HttpEntity reqEntity = MultipartEntityBuilder.create()
                .addPart("file", f)
                .addPart("word", word)
                .build();
        post.setEntity(reqEntity);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        try (CloseableHttpResponse response = httpclient.execute(post)) {
            System.out.println(response.getStatusLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getAudio(int wordId, String text, String languageOutput) throws IOException {
        URL url = new URL("http://translate.google.com/translate_tts?" + "q="
                + text.replace(" ", "%20") + "&tl=" + languageOutput);

        URLConnection urlConn = url.openConnection();
        urlConn.addRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36");

        BufferedInputStream audioSrc = new BufferedInputStream(urlConn.getInputStream());
        File save = new File(wordId + ".mp3");
        FileUtils.copyInputStreamToFile(audioSrc, save);
        return save;
    }

    public static List<Word> getWords(int langId, int pageNumber, int pageSize) throws URISyntaxException {
        String url = "http://www.openwords.org/api-v1/getWordsPage.php";
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpUriRequest post = RequestBuilder.post()
                .setUri(new URI(url))
                .addParameter("lang_id", String.valueOf(langId))
                .addParameter("page_number", String.valueOf(pageNumber))
                .addParameter("page_size", String.valueOf(pageSize))
                .build();

        try (CloseableHttpResponse response = httpclient.execute(post)) {
            HttpEntity body = response.getEntity();
            Gson gson = new Gson();
            Result r = gson.fromJson(new InputStreamReader(body.getContent()), Result.class);
            return r.getWords();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private class Result {

        private List<Word> words;

        public List<Word> getWords() {
            return words;
        }

    }

    private class Word {

        private int id;
        private String word;

        public int getId() {
            return id;
        }

        public String getWord() {
            return word;
        }

    }

}
