package com.spamdetector.util;

import com.spamdetector.domain.TestFile;

import java.io.*;
import java.util.*;
import java.util.TreeMap;
import java.util.Map;


/**
 * TODO: This class will be implemented by you
 * You may create more methods to help you organize you strategy and make you code more readable
 */

public class SpamDetector {

    public List<TestFile> trainAndTest(File mainDirectory) {
//        TODO: main method of loading the directories and files, training and testing the model
        

        return new ArrayList<TestFile>();
    }

    //Calculate word probability on spams
    public Map<String, Integer> calPRWS(int fileNum, Map<String, Integer> wordMap) {
        
        //Creating probability map
        Map<String, Integer> spamProb = new TreeMap<>();
        
        //Looping through original map and adding new probabilitys for each word to new map
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            String key = entry.getKey();
            Integer value = (entry.getValue() / fileNum);
            spamProb.put(key, value);
        }

        //Return probability map
        return spamProb;
    }

    //Calculate word probability on hams
    public Map<String, Integer> calPRWH(int fileNum, Map<String, Integer> wordMap) {
        
        //Creating probability map
        Map<String, Integer> hamProb = new TreeMap<>();
        
        //Looping through original map and adding new probabilitys for each word to new map
        for (Map.Entry<String, Integer> entry : wordMap.entrySet()) {
            String key = entry.getKey();
            Integer value = (entry.getValue() / fileNum);
            hamProb.put(key, value);
        }

        //Return probability map
        return hamProb;
    }

    //Not finished yet, for calculating total probability
    public Map<String, Integer> calPRSW(Map<String, Integer> hamProb, Map<String, Integer> spamProb) {
        Map<String, Integer> fileSpamProb = new TreeMap<>();

        for (Map.Entry<String, Integer> entry : hamProb.entrySet()) {
            fileSpamProb.put(entry.getKey(), entry.getValue());
        }

        return fileSpamProb;
    }

    //Train spam reading method
    public Map<String, Integer> trainSpamMap(File mainDirectiory) {

        //Main tree map for spam
        Map<String, Integer> trainSpamFreq = new TreeMap<>();

        //Storing all file names to be read
        File filesList[] = mainDirectiory.listFiles(); //coming back as null and i dont know why
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
        File filesList[] = mainDirectiory.listFiles(); //coming back as null and i dont know why

        //Creates tree map from file to be added to main tree map, if file is valid
        for (File file : filesList) {
            try {
                trainHamFreq.putAll(countWordFile(file.getAbsolutePath()));
            } catch (Exception e) {
                System.out.println("Invalid file type");
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
}




