/*
 * <strong>File   ：</strong>TestExtModel.java <br/>
 * <strong>Project：</strong>JFinal-ext2 <br/>
 * <strong>Date   ：</strong>2018年4月13日 下午6:24:22 <br/>
 * 
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
