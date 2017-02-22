/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tweeteroperations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;

import java.util.StringTokenizer;
import java.util.List;
import java.util.Optional;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import edu.stanford.nlp.international.arabic.ArabicMorphoFeatureSpecification;
import edu.stanford.nlp.international.arabic.Buckwalter;
import edu.stanford.nlp.international.arabic.pipeline.DefaultLexicalMapper;
import edu.stanford.nlp.international.arabic.process.*;
import edu.stanford.nlp.util.PropertiesUtils;
import edu.stanford.nlp.ie.crf.*;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileBuilder;
import com.optimaize.langdetect.profiles.*;
import com.optimaize.langdetect.*;
import com.optimaize.langdetect.ngram.*;
import com.optimaize.langdetect.text.*;
import com.optimaize.langdetect.profiles.*;
import com.optimaize.langdetect.profiles.util.*;
import com.optimaize.langdetect.cybozu.*;
import com.optimaize.langdetect.i18n.LdLocale;

import java.util.Properties;
import static tweeteroperations.Normalisation.file;

/**
 *
 * @author mallek_f
 */
public class Pretraitement {

    static File file = new File("/home/fatma_m/NetBeansProjects/tweeterOperations2/normalisation/data.ar");
    static File file2 = new File("/home/fatma_m/HAMD/Test/corpus.ar");
    static File outfile = new File("/home/fatma_m/HAMD/Test/Corpus.tok.ar");
    static FileWriter finalFile;
    /**
     * La méthode main()
     */
    public static void main(String[] args) throws IOException {

        //segmentation et normalisation pour les exemples ci-bas
        Properties props = new Properties();

        ArabicSegmenter s = new ArabicSegmenter(props);
        s.loadSegmenter("/home/fatma_m/NetBeansProjects/stanford-segmenter/data/arabic-segmenter-atb+bn+arztrain.ser.gz");
        System.out.println(s.segmentString("كما رفض اردوغان فرضية فتح المفاوضات لاقامة شراكة مميزة بين تركيا والاتحاد  الاوروبي كبديل عن مفاوضات الانضمام."));
        ////System.out.println(s.segmentString("مرحبآآآآ"));
        //System.out.println(s.segmentString("لوووول"));
        //System.out.println(s.segmentString("و سيكتبونها"));
        //System.out.println(s.segmentString("الأهلآآاوييي"));
        //System.out.println(s.segmentString("حبيبـــــــــــــي"));
        /*
        //Gate POS-tagger pour les tweets : Extraire les segments: Exemple (à appliquer pour tout le corpus)
        String text = "My sister won't tell me where she hid my food. She's fueling my anorexia. #bestsisteraward #not 😭💀";
        MaxentTagger tagger = new MaxentTagger("models/gate-EN-twitter.model");
        String taggedText = tagger.tagString(text);
        System.out.println(taggedText);*/
    }//fin du main   

    public static void normalizingArabicTweets(File f, String modèle) {
        String tok="";
        
        Properties props = new Properties();
        ArabicSegmenter s = new ArabicSegmenter(props);
        s.loadSegmenter(modèle);
        //lecture du fichier ligne par ligne
        String ligne;
        //StringBuffer buf = new StringBuffer();
        try {
            finalFile = new FileWriter(outfile, true);
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(f), "UTF-8"));

            while ((ligne = buffer.readLine()) != null) {//tant qu'il y a encore des ligne dans le fichier

                //si la ligne n'est pas vide
                if (!ligne.isEmpty()) // segmentation syntaxique et normalisation
                {
                    System.out.println("Tokenizer : "+ligne);
                   

                        finalFile.write(s.segmentString(ligne));  // écrire une ligne dans le fichier resultat.txt

                        finalFile.write("\n");

                }

            }//fin while
        finalFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
       
    }

    /**
     * Prétraitement des données : A partir des tweets 
     *
     * @return le crpus dans un fichier .txt sans RT (retweets)
     */
    public static File dataCleaning() {

        Workbook corpus = null;
        int nb_rows = 0;
        String tweet = "";
        String cName = "";
        File file = new File("/home/mallek_f/NetBeansProjects/tweeterOperations/tweets1Res.txt");
        try {

            //Le corpus qu'on va nettoyer 
            corpus = Workbook.getWorkbook(new File("/home/mallek_f/NetBeansProjects/tweeterOperations/tweets3.xls"));
            //creer lurl du corpus resultat
            cName = corpus.toString();
            System.out.println(cName);

            file.createNewFile();
            FileWriter finalFile = new FileWriter(file, true);
            // définir l'arborescence
            /*parcourir tout les feuilles du fichier Excel*/
            String s[];
            s = corpus.getSheetNames();

            //on récupère la feuille qui contient les tweets /
            Sheet sh = corpus.getSheet(0);

            //nombre de lignes dans la feuille (rows of the sheet)
            nb_rows = sh.getRows();
            System.out.println(sh.getName() + "***" + nb_rows);

            //les textes des tweets
            Cell[] c = sh.getColumn(3);//colonne D : textes des tweets

            if (c != null) {
                System.out.println("c'est la colonne :" + c[1].getContents());
                //lister le contenue des cellule de colonne 4
                for (int j = 2; j < c.length; j++) {
                    //afficher le texte du Tweets 
                    //System.out.println(c[j].getContents());
                    //Vérifier si c'est un retweets ou non, si il ne contient pas RT on l'insère dans fichier txt
                    tweet = c[j].getContents();
                    if (!tweet.contains("RT")) {
                        //si c'est pas un retweet ,ecrit le dans le corpus resultat
                        try {

                            finalFile.write(tweet);  // écrire une ligne dans le fichier resultat.txt
                            finalFile.write("\n"); // forcer le passage à la ligne
                            // fermer le fichier à la fin des traitements

                        } catch (Exception e) {
                        }
                    }
                }

                finalFile.close();
            }
        } catch (BiffException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (corpus != null) {
                // On ferme le worbook pour libérer la mémoire
                corpus.close();
            }
        }

        return file;

    }//fin de la méthode dataCleaning

    /**
     * *
     * Calculer la taille d'aun corpus
     *
     * @params f le corpus dont on veux calculer la taille
     * @return le nombre de tweets (lignes) dans le corpus
     *
     */
    public static int TailleCorpus(File f) {
        int nbLigne = 0;
        //lecture du fichier ligne par ligne
        String ligne;
        //StringBuffer buf = new StringBuffer();
        try {
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(f), "Cp1256"));

            while ((ligne = buffer.readLine()) != null) {//tant qu'il y a encore des ligne dans le fichier

                //si la ligne n'est pas vide
                if (!ligne.isEmpty()) {
                    nbLigne++;
                }
            }//fin while

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //compter le nombre de tweets dans le corpus final
        System.out.println("La taille de corpus est " + nbLigne + " tweets");
        return (nbLigne);

    }

    /**
     * Nettoyer le corpus des hashtags
     *
     * @param file le fichier du corpus en .txt
     */
    public static void eliminerHashtags(File file) {

        String ligne;
        //lecture du fichier ligen par ligne
        try {
            BufferedReader buffer = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(file), "Cp1256"));

            while ((ligne = buffer.readLine()) != null) {//tant qu'il y a encore des ligne dans le fichier

                if (ligne.compareTo("") != 0) {
                    /**
                     * ********segmentation de la ligen en mots suivants la
                     * ponctuation***********
                     */
                    StringTokenizer S = new StringTokenizer(ligne, ",’º,.¿Ü¡/:! *-_().;:?! : ?.?<>{}()[]$\"\\?-/,،", true);

                    while (S.hasMoreTokens()) {
                        String mot = S.nextToken();
                        if ((mot.compareTo(" ") != 0) && (mot.compareTo("\n\n") != 0) && (mot.compareTo("\n") != 0)) {
                            //System.out.println(mot);
                        }

                    }//la ligne n'a plus de token
                }//fin if
            }//fin du fichier...pas de nouveaux lignes
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}//fin de la classe Pretraitement

