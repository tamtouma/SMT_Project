#!/bin/sh 

# written by Fatma Mallek, 


echo "Génération du modèle de langue avec SRILM"

 cd ~/SMT/srilm-1.7.1/bin/i686-m64/
./ngram-count -text /home/mallek_f/Corpus+/raw/UN/un.en -lm ~/HAMD/UN_Fatiha/LM/un.srilm
