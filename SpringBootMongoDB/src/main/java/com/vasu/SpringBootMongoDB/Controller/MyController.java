/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vasu.SpringBootMongoDB.Controller;

import com.vasu.SpringBootMongoDB.Collection.Collections;
import com.vasu.SpringBootMongoDB.Response.Responses;
import com.vasu.SpringBootMongoDB.Service.MyService;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Vasu Rajput
 */
@RestController
public class MyController {

    private static final Logger logger = Logger.getLogger(MyController.class.getName());
    @Autowired
    private MyService service;
    @Autowired
    private Responses responses;
    Document condition = new Document();

    @GetMapping("/")
    public String test() {
        return "Nice Try";
    }

    @PostMapping("/save")
    public JSONObject save(@RequestBody JSONObject payload) {
        try {
            logger.info("vvv::  payload= " + payload);
            if (payload instanceof JSONObject && payload.containsKey(Collections.userCollection.FIRST_NAME)
                    && payload.containsKey(Collections.userCollection.LAST_NAME)
                    && payload.containsKey(Collections.userCollection.EMAIl)) {
                JSONObject saveResponse = service.save(payload);
                logger.info("vvv::  responseBack to browser= " + saveResponse);
                return saveResponse;
            }
            return responses.getResponse(responses.INSUFFICIENT_DATA);
        } catch (Exception e) {
            logger.error(e, e);
            return responses.getResponse(responses.ERROR);
        }
    }

    @PostMapping("/find")
    public Object find(@RequestBody JSONObject payload) {
        try {
            logger.info("vvv::  payload= " + payload);
            if (payload == null) {
                return responses.getResponse(responses.INSUFFICIENT_DATA);
            }
            if (!payload.containsKey(Collections.userCollection.EMAIl)) {
                return responses.getResponse(responses.INSUFFICIENT_DATA);
            }
            condition.clear();
            condition.append(Collections.userCollection.EMAIl, payload.get(Collections.userCollection.EMAIl));
            Object finalResponse = service.find(condition, Collections.userCollection.COLLECTION_NAME);
            logger.info("vvv::  finalResponse= " + finalResponse);
            return finalResponse;
        } catch (Exception e) {
            logger.error(e, e);
            return responses.getResponse(responses.ERROR);
        }
    }

    @PutMapping("/update")
    public JSONObject update(@RequestBody JSONObject payload) {
        try {
            logger.info("vvv::  payload= " + payload);
            if (payload == null || !payload.containsKey(Collections.userCollection.EMAIl)) {
                return responses.getResponse(responses.ERROR);
            }
            JSONObject whereObj = new JSONObject();
            JSONObject updateObj = new JSONObject();
            whereObj.put(Collections.userCollection.EMAIl, payload.get(Collections.userCollection.EMAIl));
            updateObj.put(Collections.userCollection.FIRST_NAME, payload.get(Collections.userCollection.FIRST_NAME));
            updateObj.put(Collections.userCollection.LAST_NAME, payload.get(Collections.userCollection.LAST_NAME));
            JSONObject finalResponse = service.update(whereObj, updateObj, Collections.userCollection.COLLECTION_NAME);
            logger.info("vvv::  finalResponse= " + finalResponse);
            return finalResponse;
        } catch (Exception e) {
            logger.error(e, e);
            return responses.getResponse(responses.ERROR);
        }
    }

    @DeleteMapping("/delete")
    public JSONObject delete(@RequestBody JSONObject payload) {
        try {
            logger.info("vvv::  payload= " + payload);
            if (payload == null || !payload.containsKey(Collections.userCollection.EMAIl)) {
                return responses.getResponse(responses.INSUFFICIENT_DATA);
            }
            condition.clear();
            condition.append(Collections.userCollection.EMAIl, payload.get(Collections.userCollection.EMAIl).toString());
            JSONObject finalResponse = service.delete(condition, Collections.userCollection.COLLECTION_NAME);
            logger.info("vvv::  finalResponse= " + finalResponse);
            return finalResponse;
        } catch (Exception e) {
            logger.error(e, e);
            return responses.getResponse(responses.ERROR);
        }
    }

    @GetMapping("/findAll")
    public Object findAll() {
        try {
            Object finalResponse = service.getAll();
            logger.info("vvv::  finalResponse= " + finalResponse);
            return finalResponse;
        } catch (Exception e) {
            logger.error(e, e);
            return responses.getResponse(responses.ERROR);
        }
    }
}
