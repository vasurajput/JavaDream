/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vasu.SpringBootMongoDB.Service;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.result.UpdateResult;
import com.vasu.SpringBootMongoDB.Collection.Collections;
import com.vasu.SpringBootMongoDB.Repositry.MyRepositry;
import com.vasu.SpringBootMongoDB.Response.Responses;
import java.util.Iterator;
import java.util.Set;
import org.apache.log4j.Logger;
import org.bson.Document;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

/**
 *
 * @author Vasu Rajput
 */
@Service
public class MyService implements MyRepositry {

    @Autowired
    private MongoTemplate template;
    @Autowired
    private Responses responses;

    private static final Logger logger = Logger.getLogger(MyService.class.getName());
    private JSONParser parser = new JSONParser();
    Document condition = new Document();

    @Override
    public JSONObject save(JSONObject payload) {
        try {
            if (payload == null) {
                return responses.getResponse(responses.INSUFFICIENT_DATA);
            }
            String fName = payload.get(Collections.userCollection.FIRST_NAME).toString();
            String lName = payload.get(Collections.userCollection.LAST_NAME).toString();
            String email = payload.get(Collections.userCollection.EMAIl).toString();

            if (fName.equals("") || lName.equals("") || email.equals("")) {
                return responses.getResponse(responses.ERROR);
            }

            condition.clear();
            condition.append(Collections.userCollection.FIRST_NAME, fName)
                    .append(Collections.userCollection.LAST_NAME, lName)
                    .append(Collections.userCollection.EMAIl, email);

            //Save Data
            MongoCollection<Document> collection = template.getCollection(Collections.userCollection.COLLECTION_NAME);
            collection.insertOne(condition);
            Object getInsertId = condition.get("_id");
            logger.info("vvv::  insertId= " + getInsertId);
            if (getInsertId.toString().equals("")) {
                return responses.getResponse(responses.ERROR);
            }
            logger.info("vvv::  succesfully append to collections");
            return responses.getResponse(responses.OK);
        } catch (Exception e) {
            logger.error(e, e);
            return responses.getResponse(responses.ERROR);
        }
    }

    @Override
    public JSONObject update(JSONObject whereObj, JSONObject updateObj, String collectionName) {
        try {
            logger.info("vvv::  whereObj= " + whereObj + ", updateObj= " + updateObj + ", collName= " + collectionName);
            if (whereObj == null || updateObj == null || collectionName == null) {
                logger.info("vvv::  insufficentData");
                return responses.getResponse(responses.INSUFFICIENT_DATA);
            }
            Query query = new Query();
            Update update = new Update();

            //iterate where Condition
            Set keySet = whereObj.keySet();
            Iterator iterator = keySet.iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                query.addCriteria(Criteria.where(key).is(whereObj.get(key)));
            }

            //iterate Update Value
            Set updateSet = updateObj.keySet();
            Iterator updateItr = updateSet.iterator();
            while (updateItr.hasNext()) {
                String key = (String) updateItr.next();
                update.set(key, updateObj.get(key));
                //  update.addToSet(key).value(updateVal.get(key));
            }
            logger.info("vvv::  query= " + query.toString());
            logger.info("vvv::  update= " + update.toString());
            UpdateResult updateMulti = template.updateMulti(query, update, collectionName);
            logger.info("vvv::  updateMulti= " + updateMulti);
            if (updateMulti.getModifiedCount() == 1) {
                return responses.getResponse(responses.OK);
            }
            return responses.getResponse(responses.ERROR);
        } catch (Exception e) {
            logger.error(e, e);
            return responses.getResponse(responses.ERROR);
        }
    }

    @Override
    public JSONObject delete(Document condition, String collectionName) {
        try {
            logger.info("vvv::  docs= " + condition + " ,collection= " + collectionName);
            Document findOneAndDeleteResponse = template.getCollection(collectionName).findOneAndDelete(condition);
            logger.info("vvv::  deletedData= " + findOneAndDeleteResponse);
            return responses.getResponse(responses.OK);
        } catch (Exception e) {
            logger.error(e, e);
            return responses.getResponse(responses.ERROR);
        }
    }

    @Override
    public Object find(Document condition, String collectionName) {
        try {
            logger.info("vvv::  getAllDocument load :::  condition= " + condition);
            FindIterable<Document> find = template.getCollection(collectionName).find(condition);
            logger.info("vvv::  find= " + find);
            MongoCursor<Document> iterator = find.iterator();
            Document docsResponse = new Document();
            JSONArray responseArr = new JSONArray();
            while (iterator.hasNext()) {
                responseArr.add((JSONObject) parser.parse(iterator.next().toJson().trim()));
            }
            logger.info("vvv::  finalResponse= " + responseArr);
            logger.info("vvv::  finsl ResponseARR Size= " + responseArr.size());
            if (responseArr.size() > 0) {
                return responseArr;
            }
            return null;
        } catch (Exception e) {
            logger.error(e, e);
            return null;
        }
    }

    @Override
    public Object getAll() {
        try {
            condition.clear();
            condition.append(Collections.userCollection.EMAIl, new Document("$ne", ""));

            Object findAll = find(condition, Collections.userCollection.COLLECTION_NAME);
            return findAll;
        } catch (Exception e) {
            logger.error(e, e);
            return responses.getResponse(responses.ERROR);
        }
    }

}
