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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.IPlugin;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

public class MongodbPlugin implements IPlugin {
    
    private MongoClient client;
    private String username;
    private String password;
    private String database;
    private boolean sslEnabled = false;
    private Map<String, ServerAddress> servers = new HashMap<String, ServerAddress>();

    public MongodbPlugin(String database) {
    	this.setDefaultDatabase(database);
    }

    public MongodbPlugin(String host, int port) {
    	this.addServer(new ServerAddress(host, port));
    }
    
    public MongodbPlugin(String host, int port, String username, String password) {
        this.addServer(new ServerAddress(host, port));
    	this.username = username;
        this.password = password;
    }
    
    public MongodbPlugin(ServerAddress server, String username, String password) {
        this.addServer(server);
    	this.username = username;
        this.password = password;
    }
    
    public void addServer(ServerAddress server) {
    	String host = server.getHost();
    	 if (!this.servers.containsKey(host)) {
 			this.servers.put(host, server);
 		}
    }
    
    public void addServer(ServerAddress server, String database) {
    	this.addServer(server);
    	this.setDefaultDatabase(database);
    }
    
    public void setDefaultDatabase(String database) {
    	this.database = database;
    }
    
    public void sslEnabled(boolean sslEnabled) {
    	this.sslEnabled = sslEnabled;
    }
    
    public MongodbPlugin(ServerAddress srv) {
    	
    }

    @Override
    public boolean start() {
    	if (StrKit.notBlank(this.username) && StrKit.notBlank(this.password)) {
        	List<ServerAddress> srvs = new ArrayList<ServerAddress>();
        	srvs.addAll(this.servers.values());
            MongoCredential credential = MongoCredential.createScramSha1Credential(this.username, this.database, this.password.toCharArray());  
            MongoClientSettings settings = MongoClientSettings.builder()
                    .credential(credential)
                    .applyToSslSettings(builder -> builder.enabled(this.sslEnabled))
                    .applyToClusterSettings(builder -> 
                        builder.hosts(srvs))
                    .build();
            this.client = MongoClients.create(settings);
		} else {
	    	this.client = MongoClients.create();
		}
        Mongodb.init(this.client, this.database);
        return true;
    }

    @Override
    public boolean stop() {
        if (this.client != null) {
            this.client.close();
        }
        return true;
    }
}
