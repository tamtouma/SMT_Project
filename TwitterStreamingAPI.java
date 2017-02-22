/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeteroperations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import static tweeteroperations.queryTest.corpusTweets;
import static tweeteroperations.queryTest.saveTweet;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;
/**
 *
 * @author mallek_f
 */
public class TwitterStreamingAPI {

    static File corpusTweets = new File("/home/mallek_f/HAMD/EnglishTweets.en");

    public static void main(String[] args) {

        StatusListener listener = new StatusListener() {

            public void onStatus(Status status) {
               // System.out.println(status.getUser().getName() + " : " + status.getText() + "  Tweeted AT: " + status.getCreatedAt());
                 System.out.println(status.getText());

                //enregistrer les tweets dans un ficgier sortie tout en éliminant les retweets
              
                  
                    saveTweet(status.getText());
                
                //saveTweet(status.getText());
               
            }

            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
            }

            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }

            @Override
            public void onScrubGeo(long arg0, long arg1) {
                // TODO Auto-generated method stub       
            }

            @Override
            public void onStallWarning(StallWarning arg0) {
                // TODO Auto-generated method stub
            }
        };

        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("aWUnsXrsmnFO6OdKrY7ElxZaX")
                .setOAuthConsumerSecret("ODIgaosfbkRsN8q6plhWFzrNjxkvc2iGwMXZ7jPhv7LRMNV22K")
                .setOAuthAccessToken("1480783934-lAnIMwhJQzcvm4KB90n6Kx0WafdDXH9ZyfWjGaD")
                .setOAuthAccessTokenSecret("fGJGCWhBf08JsnkiFsgswBEBJFHtbE5lXZ7zJKU62Cwg5");

        TwitterStreamFactory tf = new TwitterStreamFactory(cb.build());
        TwitterStream twitterStream = tf.getInstance();
        twitterStream.addListener(listener);

        FilterQuery filtre = new FilterQuery();

        //String[] keywordsArray = {"الثورة", "تونس","الربيع العربي","الثورة العربية","مصر","سوريا"}; //filter based on your choice of keywords
        String[] keywordsArray = {"revolution", "syria", "tunisia", "egypt", "arabic spring revolution", "arabic"};
        filtre.track(keywordsArray);
        //filtre.locations(new double[][]{new double[]{-126.562500,30.448674},
               // new double[]{-61.171875,44.087585
              //  }}); // See https://dev.twitter.com/docs/streaming-apis/parameters#locations for proper location doc. 
//Note that not all tweets have location metadata set.
        filtre.language(new String[]{"en"}); // Note that language does not work properly on Norwegian tweets 
        twitterStream.filter(filtre);
    }

    /**
     * saveTweet
     *
     * @param tweet
     */
    public static void saveTweet(String tweet) {
        //file.createNewFile();
        try {
            FileWriter finalFile = new FileWriter(corpusTweets, true);

            finalFile.write(tweet);  // écrire une ligne dans le fichier resultat.txt

            finalFile.write("\n");

            finalFile.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
