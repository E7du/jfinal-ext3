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

import com.jfinal.plugin.IPlugin;
import com.mongodb.MongoClient;

public class MongodbPlugin implements IPlugin {
    
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final int DEFAUL_PORT = 27017;

    private MongoClient client;
    private String host;
    private int port;
    private String db;

    public MongodbPlugin(String db) {
        this.host = DEFAULT_HOST;
        this.port = DEFAUL_PORT;
        this.db = db;
    }

    public MongodbPlugin(String host, int port, String database) {
        this.host = host;
        this.port = port;
        this.db = database;
    }

    @Override
    public boolean start() {
    	this.client = new MongoClient(this.host, this.port);
        Mongodb.init(this.client, this.db);
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
