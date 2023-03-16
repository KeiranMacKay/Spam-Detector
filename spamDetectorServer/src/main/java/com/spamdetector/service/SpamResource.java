package com.spamdetector.service;

import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.List;
import java.lang.Math;

import jakarta.ws.rs.core.Response;

@Path("/spam")
public class SpamResource {
    private String readFileContents(String filename) {
        /**
         * if there is no '/' at the beginning, the following function call will return `null`
         */
        String f;
        if (filename.charAt(0) != '/') {
            f = '/' + filename;
        } else {
            f = filename;
        }

        /**
         * trying to open and read the file
         */
        try {
            java.nio.file.Path file = java.nio.file.Path.of(
                    SpamResource.class.getResource(f)
                            .toString()
                            .substring(6));
            return Files.readString(file);
        } catch (IOException e) {
            // something went wrong
            return "Did you forget to create the file?\n" +
                    "Is the file in the right location?\n" +
                    e.toString();
        }
    }

    //    your SpamDetector Class responsible for all the SpamDetecting logic
    SpamDetector detector = new SpamDetector();


    SpamResource() throws IOException{
//        TODO: load resources, train and test to improve performance on the endpoint calls
        System.out.print("Training and testing the model, please wait");

//      TODO: call  this.trainAndTest();
        this.trainAndTest();


    }
    @GET
    @Produces("application/json")
    public Response getSpamResults() {
//       TODO: return the test results list of TestFile, return in a Response object

        Response myResp = Response.status(200)
                .header("Content-Type", "application/json")
                .entity(this.readFileContents("/test/ham"))
                .build();

        return myResp;
    }

    @GET
    @Path("/accuracy")
    @Produces("application/json")
    public Response getAccuracy() {
//      TODO: return the accuracy of the detector, return in a Response object

        ArrayList<String, Integer> list1= new ArrayList<>();
        int cor = 0;
        int incor = 0;

        cor = correctG(list1);
        incor = incorrectG(list1);

        double acc = 0.0;

        list1 = trainAndTest();

        acc = (cor + incor) / list1.size();


        Response myResp = Response.status(200)
                .header("Content-Type", "application/json")
                .entity(acc)
                .build();

        return myResp;
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
        //      TODO: return the precision of the detector, return in a Response object

        ArrayList<String, Integer> list1= new ArrayList<>();
        int cor = 0;
        int incor = 0;

        cor = correctG(list1);
        incor = incorrectG(list1);

        double per = 0.0;

        list1 = trainAndTest();

        per = cor / (incor+cor);

        Response myResp = Response.status(200)
                .header("Content-Type", "application/json")
                .entity(per)
                .build();

        return myResp;
    }


    //Get correct guesses
    private int correctG(List<TestFile> files) {
        int cor = 0;

        for (int i = 0; i < files.size(); i++) {
            if (Math.round(files.get(i).getSpamProbability()) == 1 && files.get(i).getActualClass() == "spam" ) {
                cor++;
            } else if (Math.round(files.get(i).getSpamProbability()) == 0 && files.get(i).getActualClass() == "ham" ) {
                cor++;
            }
        }
        
        return cor;
    }

    //Get incorrect guesses
    private int incorrectG(List<TestFile> files) {
        int incor = 0;

        for (int i = 0; i < files.size(); i++) {
            if (Math.round(files.get(i).getSpamProbability()) == 0 && files.get(i).getActualClass() == "spam" ) {
                incor++;
            } else if (Math.round(files.get(i).getSpamProbability()) == 1 && files.get(i).getActualClass() == "ham" ) {
                incor++;
            }
        }

        return incor;
    }

    private List<TestFile> trainAndTest()  {
        if (this.detector==null) {
            this.detector = new SpamDetector();
        }

//        TODO: load the main directory "data" here from the Resources folder
<<<<<<< HEAD
        String path = "\\resources\\data";
        File file = new File(path);
        File mainDirectory = file;
=======

        File mainDirectory = "\\resources\\data";

>>>>>>> 568b1a34635aac5073324668bf4f9365581e3cfb
        return this.detector.trainAndTest(mainDirectory);
    }



    private String wrap_json(String tag, String data) {
        return "\"" + tag + "\"" + ":" + data + ",";

    }

    @GET
    @Path("/json")
    @Produces("application/json")
    public String toJSON() {
        //starting file
        String files = "[";

        ArrayList<TestFile> testFileList = new ArrayList<>();

        testFileList = (ArrayList<TestFile>) trainAndTest();



        String inner = "";
        //wrapping ech Student object in json braces
        for (TestFile file: testFileList ) {
            inner += "{" + wrap_json("spamProbRounded", String.valueOf(file.getSpamProbability())) +
                    wrap_json("file", file.getFilename()) +
                    wrap_json("spamProbability", String.valueOf(file.getSpamProbability())) +
                    wrap_json("actualClass", String.valueOf(file.getSpamProbability())) +
                    "},";
        }

        // ending file
        files += inner + "]}";

        return files;

    }
}
 
    