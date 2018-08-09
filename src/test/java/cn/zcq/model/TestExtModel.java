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
package cn.zcq.model;

import com.jfinal.plugin.activerecord.Model;

/**
 * 
 * test extend model
 * 
 * @author yadong
 * 
 * @version
 */
@SuppressWarnings("rawtypes")
public class TestExtModel<M> extends Model<Model> {

    /**
     * 
     */
    private static final long serialVersionUID = 8675321530619575798L;

    protected static final String TABLE_KEY        = "Test";

}
