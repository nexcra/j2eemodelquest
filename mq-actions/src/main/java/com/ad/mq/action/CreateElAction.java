package com.ad.mq.action;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ad.mq.db.DBControl;
import com.ad.mq.interceptor.DataBaseAware;
import com.ad.mq.model.DataElement;
import com.ad.mq.model.DataObject;
import com.ad.mq.model.OutData;

public class CreateElAction extends ActionSupport implements DataBaseAware {

	private Logger log = Logger.getLogger(CreateElAction.class);
	private DBControl db;
	private Integer dataid;

	public void setDataid(Integer dataid) {
		this.dataid = dataid;
	}

	@Override
	public void setDBControl(DBControl ctl) {
		this.db = ctl;
	}

	@Override
	public void execute() throws Exception {
		OutData od = new OutData();
		this.out = od;
		DataObject data = (DataObject) this.db.query2Bean("select * from MQ$object where id =?", DataObject.class, new Object[] { this.dataid });
		if (log.isDebugEnabled()){
			log.debug("beanname:" + data.getBeanname());
		}
		
		if (StringUtils.isEmpty(data.getBeanname())) {
			od.setMessage("没有定义beanname!");
		} else {
			try {
				Class<?> o = Class.forName(data.getBeanname());
				Field[] fields;
				List<DataElement> els = new ArrayList<DataElement>();
				int cunt = 0;
				DataElement el;
				while (null != o) {
					fields = o.getDeclaredFields();
					for (Field field : fields) {
						if (((field.getModifiers() & Modifier.FINAL) == Modifier.FINAL) || ((field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) ){
							continue;
						}
						cunt++;
						el = new DataElement();
						el.setId(Integer.parseInt(String.valueOf(this.dataid) + StringUtils.leftPad(String.valueOf(cunt), 3, '0')));
						el.setParentid(this.dataid);
						el.setFieldname(field.getName());
						el.setFieldvalue(field.getName());
						el.setGridindex(cunt);
						el.setGridshow(true);
						el.setFormindex(cunt);
						el.setFormshow(true);
						if (field.getType() == Integer.class || field.getType() == Double.class || field.getType() == Float.class || field.getType() == Long.class || field.getType() == Byte.class || field.getType() == Short.class ) {
							el.setXtype("numberfield");
							el.setXtypeattrs("{}");
							el.setFtype("int");
							el.setColumnattrs("{filterable:true,filter : {    type : 'int'    }}");
						} else if (field.getType() == Date.class || field.getType() == Timestamp.class) {
							el.setXtype("datefield");
							el.setXtypeattrs("{}");
							el.setFtype("date");
							el.setColumnattrs("{filterable:true,filter : {    type : 'date'    }}");
						} else if (field.getType() == Boolean.class) {
							el.setXtype("combo");
							el.setXtypeattrs("{}");
							el.setFtype("bool");
							el.setColumnattrs("{filterable:true,filter : {    type : 'bool'    }}");
						} else {
							el.setXtype("textfield");
							el.setXtypeattrs("{}");
							el.setFtype("string");
							el.setColumnattrs("{filterable:true,filter : {    type : 'string'    }}");
						}
						els.add(el);
					}
					o = o.getSuperclass();
				}
				if (log.isDebugEnabled()){
					log.debug("delete from mq$element where parentid=?");
				}
				this.db.update("delete from mq$element where parentid=?", new Object[]{this.dataid});
				for (DataElement de : els) {
					this.db.insert(de);
				}

			} catch (Exception e) {
				log.error(e);
				od.setMessage(e.getMessage());
			}

		}
	}

}
