#!/bin/sh 

# written by Fatma Mallek, 

###normalisation de la ponctuation ---> UTF8
~/SMT/moses/scripts/tokenizer/normalize-punctuation.perl < ~/HAMD/UN_Fatiha/training/un.en> ~/HAMD/UN_Fatiha/training/un.en_utf8

##### tokénisation #####    
~/SMT/moses/scripts/tokenizer/tokenizer.perl
perl ~/SMT/moses/scripts/tokenizer/tokenizer.perl -l en < ~/HAMD/UN_Fatiha/training/un.en_utf8> ~/HAMD/UN_Fatiha/training/un.tok.en

####lowerCase--casse miniscule  ######  

  ~/SMT/moses/scripts/tokenizer/lowercase.perl

perl ~/SMT/moses/scripts/tokenizer/lowercase.perl < /home/mallek_f/HAMD/UN_Fatiha/training/un.tok.en > /home/mallek_f/HAMD/UN_Fatiha/training/un.tok.lc.en


####cleaning##### 
 ~/SMT/moses/scripts/training/clean-corpus-n.perl
perl ~/SMT/moses/scripts/training/clean-corpus-n.perl  /home/mallek_f/HAMD/UN_Fatiha/training/un.tok.lc ar en home/mallek_f/HAMD/UN_Fatiha/training/un.tok.lc.clean 1 120





