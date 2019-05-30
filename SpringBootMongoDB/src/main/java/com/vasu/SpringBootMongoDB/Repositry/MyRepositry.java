/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vasu.SpringBootMongoDB.Repositry;

import org.bson.Document;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Vasu Rajput
 */
public interface MyRepositry {

    public JSONObject save(JSONObject payload);

    public JSONObject update(JSONObject whereObj, JSONObject updateObj, String collectionName);

    public JSONObject delete(Document condition, String CollectionName);

    public Object find(Document condition, String CollectionName);

    public Object getAll();

}
