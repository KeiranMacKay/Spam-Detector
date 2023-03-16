package com.spamdetector.service;

import com.spamdetector.domain.TestFile;
import com.spamdetector.util.SpamDetector;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

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


    SpamResource(){
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

        Response myResp = Response.status(200)
                .header("Content-Type", "application/json")
                .entity(this.readFileContents("/test/ham"))
                .build();

        return myResp;
    }

    @GET
    @Path("/precision")
    @Produces("application/json")
    public Response getPrecision() {
        //      TODO: return the precision of the detector, return in a Response object

        Response myResp = Response.status(200)
                .header("Content-Type", "application/json")
                .entity(this.readFileContents("/test/ham"))
                .build();

        return myResp;
    }

    private int correctG(List<TestFile> files) {
        int cor = 0;

        for (TestFile i : files) {
            if (i.getActualClass() == i.getClass()) {
                cor++;
            }
        }
        return cor;
    }

    private int incorrectG(List<TestFile> files) {
        int incor = 0;

        for (TestFile i : files) {
            if (i.getActualClass() != i.getClass()) {
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

        File mainDirectory = "\\resources\\data";

        return this.detector.trainAndTest(mainDirectory);
    }
}