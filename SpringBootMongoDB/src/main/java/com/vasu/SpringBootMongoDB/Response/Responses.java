/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vasu.SpringBootMongoDB.Response;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author Vasu Rajput
 */
@Component
public class Responses {

    private static final Logger logger = Logger.getLogger(Responses.class.getName());

    public static final int OK = 0;
    public static final int ERROR = 1;
    public static final int INSUFFICIENT_DATA = 3;
    public static final int UNKNOWN = 4;

    public static JSONObject getResponse(int code) {
        logger.info("vvv::  responseCode= " + code);
        JSONObject response = new JSONObject();
        switch (code) {
            case OK:
                response.put("code", OK);
                response.put("message", "OK");
                break;
            case ERROR:
                response.put("code", ERROR);
                response.put("message", "Internal Error");
                break;
            case INSUFFICIENT_DATA:
                response.put("code", INSUFFICIENT_DATA);
                response.put("message", "Insufficient Data");
                break;

            default:
                response.put("code", UNKNOWN);
                response.put("message", "Unknown Response code");

        }
        logger.info("vvv::  MakeResponse= " + response);
        return response;
    }
}
