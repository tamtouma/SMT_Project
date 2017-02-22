#!/bin/sh

 # written by Fatma Mallek, 
# For more tutorials about Moses, visit the site web http://www.statmt.org/moses/?n=Moses.Tutorial

# Étape d'entrainement 
echo "=== Entrainement de la table de traduction===== =" 
~/SMT/mosesdecoder/scripts/training/train-model.perl -root-dir /home/fatma/HAMD/Token/600Neuronal/moses -corpus /home/fatma/HAMD/Token/600mtok/600mUNtok -f ar -e en -alignment grow-diag-final-and -reordering msd-bidirectional-fe -external-bin-dir /home/fatma/SMT/tools -lm 0:3:/home/fatma/SMT/LM/lm_3gram_tweets_en.srilm -mgiza -mgiza-cpus 18 -parallel -cores 6 --first-step 1 --last-step 9 &>training.out &

#Étape de décodage (avant tuning)
echo "=== Le décodage avec Moses===== =" 

~/SMT/mosesdecoder/bin/moses /home/fatma/HAMD/Token/600Neuronal/moses/model/moses.ini < /home/fatma/HAMD/testTweet/arabic.tok >/home/fatma/HAMD/testTweet/outLM_TAN.en

#calculer le score Bleu 
echo "===== Calcul du Score Bleu=======" 
perl ~/SMT/mosesdecoder/scripts/generic/multi-bleu.perl /home/fatma/HAMD/testTweet/english.tok < /home/fatma/HAMD/testTweet/outLM_TAN.en

#calcul du score OOV
echo "===== Calcul du OOV=======" 
perl ~/SMT/mosesdecoder/scripts/analysis/nontranslated_words.pl --top=1000 --ignore-numbers --ignore-punct /home/fatma/HAMD/testTweet/arabic.tok /home/fatma/HAMD/testTweet/outLM_TAN.en >/home/fatma/HAMD/testTweet/oovLMTan

#Tuning#
echo "===== réordonnacement des poids=======" 
perl ~/SMT/mosesdecoder/scripts/training/mert-moses.pl -nonorm --rootdir=/home/fatma/SMT/mosesdecoder/scripts --working-dir=/home/fatma/HAMD/Token/DEVlmTan/dev/ /home/fatma/HAMD/Token/DEVlmTan/arabic.tok /home/fatma/HAMD/Token/DEVlmTan/english.tok /home/fatma/SMT/mosesdecoder/moses-cmd/bin/gcc-5.3.1/release/link-static/threading-multi/moses /home/fatma/HAMD/Token/600Neuronal/moses/model/moses.ini >/home/fatma/HAMD/Token/DEVlmTan/tuning.out 

#décodage 
/home/fatma/SMT/mosesdecoder/bin/moses -f /home/fatma/HAMD/Token/DEVlmTan/dev/filtered/moses.ini < /home/fatma/HAMD/testTweet/arabic.tok >/home/fatma/HAMD/testTweet/outDEV_TAN.en

#Bleu score 
perl ~/SMT/mosesdecoder/scripts/generic/multi-bleu.perl /home/fatma/HAMD/testTweet/english.tok < /home/fatma/HAMD/testTweet/outDEV_TAN.en
