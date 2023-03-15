package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;
import java.util.TreeMap;
import java.util.Map;
import java.lang.Math;

public class SpamDetector {

    public List<TestFile> trainAndTest(File mainDirectory) {

        //Creating TreeMaps for ham and spam frequency
        Map<String, Integer> trainHam = new TreeMap<>();
        Map<String, Double> trainHamFreq = new TreeMap<>();
        
        Map<String, Integer> trainHam2 = new TreeMap<>();

        Map<String, Integer> trainSpam = new TreeMap<>();
        Map<String, Double> trainSpamFreq = new TreeMap<>();

        Map<String, Double> probFinal = new TreeMap<>();

        //Creating file paths
        File ham = new File(mainDirectory.getAbsolutePath() + "/train/ham");
        File ham2 = new File(mainDirectory.getAbsolutePath() + "/train/ham2");
        File spam = new File(mainDirectory.getAbsolutePath() + "/train/spam");

        //Setting training map
        trainHam = trainHamMap(ham);
        trainHam2 = trainHamMap(ham2);
        trainSpam = trainSpamMap(spam);

        //Looping through freq list to add both hams together
        for (Map.Entry<String, Integer> entry : trainHam2.entrySet()) {
            //If word exists in list, add 1 to count, otherwise add word
            if (trainHam.containsKey(entry.getKey())) {
                trainHam.put(entry.getKey(), entry.getValue() + 1);
            } else {
                trainHam.put(entry.getKey(), entry.getValue());
            }
        }

        trainHamFreq = calPRWH(countFiles(ham) + countFiles(ham2), trainHam);
        trainSpamFreq = calPRWS(countFiles(spam), trainSpam);

        probFinal = calPRSW(trainHamFreq, trainSpamFreq);
        

        return new ArrayList<TestFile>();
    }

    //Calculate word probability on spams
    public Map<String, Double> calPRWS(int fileNum, Map<String, Integer> wordMap) {
        
        //Creating probability map
        Map<String, Double> spamProb = new TreeMap<>();
        
        //Looping through original map and adding new probabilitys for each word to new map
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            String key = entry.getKey();
            Double value = ((double)entry.getValue() / fileNum);
            spamProb.put(key, value);
        }

        //Return probability map
        return spamProb;
    }

    //Calculate word probability on hams
    public Map<String, Double> calPRWH(int fileNum, Map<String, Integer> wordMap) {
        
        //Creating probability map
        Map<String, Double> hamProb = new TreeMap<>();
        
        //Looping through original map and adding new probabilitys for each word to new map
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            String key = entry.getKey();
            Double value = ((double)entry.getValue() / fileNum);
            hamProb.put(key, value);
        }

        //Return probability map
        return hamProb;
    }

    //Calculating probability total
    public Map<String, Double> calPRSW(Map<String, Double> hamProb, Map<String, Double> spamProb) {
        
        //Creating probability map
        Map<String, Double> fileSpamProb = new TreeMap<>();

        //Combining values that are present in both maps
        for (String key : hamProb.keySet()) {
            double num1 = hamProb.get(key);
            double num2 = spamProb.getOrDefault(key, 0.0);

            double combineNum = (num1 / (num1 + num2));
            fileSpamProb.put(key, combineNum);
        }

        //Combining remaining values
        for (String key : spamProb.keySet()) {
            if (!hamProb.containsKey(key)) {
                double num = spamProb.get(key);
                double combineNum = (num) / (num);
                fileSpamProb.put(key, combineNum);
            }
        }

        //Returning final probability map
        return fileSpamProb;
    }

    public double calcSpamProb(Map<String, Double> wordProb) {
        double sProb = 0.0;
        double n = 0.0;

        for (Map.Entry<String, Double> entry : wordProb.entrySet()) {
            n += (Math.log(1-entry.getValue()) - Math.log(entry.getValue()));
        }

        sProb = 1 / (1 + Math.pow(Math.E, n));
        return sProb;
    }

    //Train spam reading method
    public Map<String, Integer> trainSpamMap(File mainDirectiory) {

        //Main tree map for spam
        Map<String, Integer> trainSpamFreq = new TreeMap<>();

        //Storing all file names to be read
        File filesList[] = mainDirectiory.listFiles();
        System.out.println(filesList);
        
        //Looping through files
        for (File file : filesList) {

            //Creates tree map from file to be added to main tree map, if file is valid
            try {
                trainSpamFreq.putAll(countWordFile(file.getAbsolutePath()));
            } catch (Exception e) {
                System.out.println("Invalid file type");
            }
        }

        //Return tree map
        return trainSpamFreq;
    }

    //Train ham reading method
    public Map<String, Integer> trainHamMap(File mainDirectiory) {

        //Main tree map for spam
        Map<String, Integer> trainHamFreq = new TreeMap<>();
        
        //Storing all file names to be read
        File filesList[] = mainDirectiory.listFiles();

        //Creates tree map from file to be added to main tree map, if file is valid
        for (File file : filesList) {
            try {
                //Setting map from wordcount call
                Map <String, Integer> temp = new TreeMap<>();
                temp = countWordFile(file.getAbsolutePath());
                
                //Looping through freq list
                for (Map.Entry<String, Integer> entry : temp.entrySet()) {
                    //If word exists in list, add 1 to count, otherwise add word
                    if (trainHamFreq.containsKey(entry.getKey())) {
                        trainHamFreq.put(entry.getKey(), entry.getValue() + 1);
                    } else {
                        trainHamFreq.put(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                System.out.println("An error has occured");
            }
        }

        //Return tree map
        return trainHamFreq;
    }

    private Map<String, Integer> countWordFile(String currentfile) throws IOException {
        
        Map<String, Integer> wordMap = new TreeMap<>();

        File file = new File(currentfile);
        
        if(file.exists()) {
        // Load all the data and process it into words
            Scanner scanner  = new Scanner(file);
            while(scanner.hasNext()) {
                //Ignore the casing for words
                String word = (scanner.next()).toLowerCase();
                
                if (isWord(word)) {
                    
                    //add the word if not exists yet
                    if(!wordMap.containsKey(word)){
                        wordMap.put(word, 1);
                    }
                }
            }
            //Closing scanner
            scanner.close();
        }
        return wordMap;
    }

    //Checking if word is valid
    private Boolean isWord(String word) {
        
        //If word is null return false
        if (word == null){
            return false;
        }

        //Setting word pattern
        String pattern = "^[a-zA-Z]*$";
        
        //If word matches pattern 
        if(word.matches(pattern)){
            return true;
        }

        return false;

    }

    //Count total files in directory
    public static int countFiles(File mainDirectory) {
        int fileCount = 0;
        if (mainDirectory.isDirectory()) {
            for (File file : mainDirectory.listFiles()) {
                if (file.isFile()) {
                    fileCount++;
                } else if (file.isDirectory()) {
                    fileCount += countFiles(file);
                }
            }
        }
        return fileCount;
    }
}




