/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeteroperations;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.StringTokenizer;
import jxl.read.biff.BiffException;

/**
 *
 * @author fatma_m
 */
public class Normalisation {

    static File tweetsEn = new File("/home/fatma_m/NetBeansProjects/tweeterOperations2/normalisation/test.en.POS.txt");
    static File words = new File("/home/fatma_m/NetBeansProjects/tweeterOperations2/normalisation/NormalizedWord");
    static File file;

    /**
     * La fonction main de la classe Normalisation
     *
     * @param args
     */
    public static void main(String[] args) {

        String tweet = "";
        int cmp = 0;
        int index;
        String mot = "";
        String pos = "";
        String token;
        String tweetFinal = "";
       
        file = new File("/home/fatma_m/NetBeansProjects/tweeterOperations2/normalisation/data.test.normalized.txt")

        try {
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(tweetsEn), "UTF-8"));

            while ((tweet = buffer.readLine()) != null) {//tant qu'il y a encore des ligne dans le fichier
                
                tweetFinal="";

                //si la ligne n'est pas vide
                if (!tweet.isEmpty()) // segmentation syntaxique et normalisation
                {
                    System.out.println(tweet);
                    // normalisation en parcourant la liste des mots normalisés
                    //lecture du fichier ligne par ligne

                    //segmenter pour avoir les différent Tokens (token_POS)
                    StringTokenizer S = new StringTokenizer(tweet, " ", true);
                    while (S.hasMoreElements()) {
                        cmp = 0;

                        //segmenter chaque Token (token_POS) par le tiret bas _
                        StringTokenizer tok = new StringTokenizer(S.nextToken(), "_", true);
                        while (tok.hasMoreElements()) {
                            //System.out.println(tok.nextToken());
                            token = tok.nextToken();
                            //System.out.println(token);
                            if (cmp == 0 && token.compareTo(" ") != 0) {
                                System.out.println("Le mot " + token);
                                mot = token;
                            }
                            if (cmp == 2) {
                                System.out.println("Le POS " + token);
                                pos = token;
                                if (pos.contains("HT") || pos.contains("USR") || pos.contains("URL") || pos.contains("RT")) {

                                    System.out.println("C'est un retweet, hashtag , user ou URL " + mot);
                                    tweetFinal = tweetFinal.concat(mot).concat(" ");
                                } else {
                                    //tweetFinal.concat(mots).concat(" ");
                                    tweetFinal = tweetFinal.concat(verifMot(mot.toLowerCase())).concat(" ");
                                }
                            }
                            cmp++;
                        }
                    }
                }//fin if
                //affichage du twitter normalisé et son insertion dans le fichier final;
                System.out.println("Le tweet normalisé :" + tweetFinal);
                saveTweet(tweetFinal);
                //réinitialiser la variable tweetFinal après de l'écrire dans le fichier final
                
            }//fin while
            

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * La méthode qui vérifier si le mot est normalisé ou non, en le vérifiant
     * avec le diccionnaire words;
     *
     * @param mot le mot à normaliser
     * @return String
     */
    public static String verifMot(String mot) {
        String motFinal = "";
        //String motNormalized="";
        String word = "";
        String token = "";
        String token2 = "";
        String srcWord = "";
        String trgWord = "";
        int cmp = 0;
        boolean trouve = false;
        //segmenter le mot en plus selon les ponctuation

        StringTokenizer S = new StringTokenizer(mot, " ,’º,.¿Ü¡/:! *-_().;:?! : ?.?<>{}()[]$\"\\?-/,،", true);
        while (S.hasMoreElements()) {
            token = S.nextToken(); //token prend la place du mot passer en paramètre
            System.out.println("********" + token);
            trouve = false;

            //parcourir le dico et vérifier
            try {
                BufferedReader buffer = new BufferedReader(
                        new InputStreamReader(
                                new FileInputStream(words), "UTF-8"));

                while ((word = buffer.readLine()) != null && trouve) {

                    //si la ligne n'est pas vide
                    if (!word.isEmpty()) {
                        cmp = 0;
                        // extraire le mot src et sible
                        StringTokenizer tok = new StringTokenizer(word, "|", true);
                        while (tok.hasMoreElements()) {

                            token2 = tok.nextToken();
                            //System.out.println(token);
                            if (cmp == 0 && token2.compareTo(" ") != 0) {
                                srcWord = token2;
                                System.out.println("Le mot source" + srcWord);
                            }
                            if (cmp == 2) {
                               trgWord = token2;
                                System.out.println("Le mot target" + trgWord);
                            }
                            cmp++;
                        }// fin while (tok.hasMoreElements()) 
                    }
                    //comparer le srcWord avec token; en cas d'égalité motFinal.concat(trgWord) sinon motFinal.concat(token) 
                    if (srcWord.compareToIgnoreCase(token) == 0) {
                        trouve = true;
                    }
                }
                if (trouve) {
                    motFinal = motFinal.concat(trgWord);
                } else {
                    motFinal = motFinal.concat(token);
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }//fin while (S.hasMoreElements())

        return motFinal;
    }

    /**
     * saveTweet
     *
     * @param tweet
     */
    public static void saveTweet(String tweet) {
        //file.createNewFile();
        try {
            FileWriter finalFile = new FileWriter(file, true);
            
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
