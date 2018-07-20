/*
 * Copyright 2018 Jobsz (zcq@zhucongqi.cn)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
*/
package com.jfinal.ext.plugin.monogodb;

import java.util.Map.Entry;

import org.bson.Document;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Record;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class Mongodb {

    private static MongoClient client;
    private static MongoDatabase database = null;

    public static void init(MongoClient client, String database) {
        Mongodb.client = client;
        if (StrKit.notBlank(database)) {
            Mongodb.database = client.getDatabase(database);	
		}
    }

    public static MongoDatabase getDatabase() {
        return Mongodb.database;
    }

    public static MongoDatabase getDatabase(String databaseName) {
        return Mongodb.client.getDatabase(databaseName);
    }

    public static MongoCollection<Document> getCollection(String name) {
        return Mongodb.database.getCollection(name);
    }

    public static MongoCollection<Document> getDatabaseCollection(String databaseName, String collectionName) {
        return getDatabase(databaseName).getCollection(collectionName);
    }

    public static MongoClient getClient() {
        return Mongodb.client;
    }
    
    public static Record toRecord(Document doc) {
        Record record = new Record();
        for (String key : doc.keySet()) {
			record.set(key, doc.get(key));
		}
        return record;
    }
    
    public static Document toDocument(Record record) {
    	Document doc = new Document();
        for (Entry<String, Object> e : record.getColumns().entrySet()) {
            doc.append(e.getKey(), e.getValue());
        }
        return doc;
    }
}
