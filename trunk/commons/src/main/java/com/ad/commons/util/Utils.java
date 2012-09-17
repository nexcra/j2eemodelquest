package com.ad.commons.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ad.commons.cfg.App;
import com.ad.commons.domain.EntityModel;

public class Utils {
	private final static Logger LOG = Logger.getLogger(Utils.class);

	/**
	 * 常用数据类型转换
	 * 
	 * @param value
	 * @param clazz
	 * @return
	 * @throws ParseException
	 */
	public static Object transferType(Object value, Class<?> clazz) throws ParseException {
		if (null == value)
			return null;
		String temp = value.toString();
		if (StringUtils.isEmpty(temp)) {
			return null;
		}
		if (clazz == String.class) {
			return temp;
		} else if (clazz == Integer.class) {
			return Integer.parseInt(temp);
		} else if (clazz == Double.class) {
			return Double.parseDouble(temp);
		} else if (clazz == Date.class) {
			SimpleDateFormat sdf = new SimpleDateFormat(App.DEFAULT_DATE_PATTERN);
			return new java.sql.Date(sdf.parse(temp).getTime());
		} else if (clazz == Time.class) {
			SimpleDateFormat sdf = new SimpleDateFormat(App.DEFAULT_TIME_PATTERN);

			return new java.sql.Time(sdf.parse(temp).getTime());
		} else if (clazz == Timestamp.class) {
			SimpleDateFormat sdf = new SimpleDateFormat(App.DEFAULT_TIMESTAMP_PATTERN);
			return new java.sql.Timestamp(sdf.parse(temp).getTime());
		} else if (clazz == Boolean.class) {
			if (temp.equals("0") || temp.equalsIgnoreCase("false") || temp.isEmpty()) {
				return false;
			} else {
				return true;
			}
		}
		return null;
	}

	/**
	 * 提取数组类型
	 * 
	 * @param req
	 * @param fieldName
	 * @param componentType
	 * @return
	 * @throws IOException
	 * @throws ServletException
	 * @throws ParseException
	 */
	public static Object transferTypes(HttpServletRequest req, String fieldName, Class<?> componentType) throws IOException, ServletException, ParseException {
		if (null == fieldName) {
			return null;
		}
		return transferArray(req.getParameterValues(fieldName), componentType);

	}

	/**
	 * 转换数据型
	 * 
	 * @param values
	 * @param clazz
	 * @return
	 * @throws ParseException
	 */
	public static Object transferArray(Object[] values, Class<?> clazz) throws ParseException {
		if (values == null || values.length == 0) {
			return null;
		}
		Object[] vs = null;
		if (clazz == String.class) {
			vs = new String[values.length];
		} else if (clazz == Integer.class) {
			vs = new Integer[values.length];
		} else if (clazz == Double.class) {
			vs = new Double[values.length];
		} else if (clazz == Date.class) {
			vs = new Date[values.length];
		} else if (clazz == Boolean.class) {
			vs = new Boolean[values.length];
		}
		if (null == vs)
			return null;
		for (int i = 0, len = vs.length; i < len; i++) {
			vs[i] = transferType(values[i], clazz);
		}
		return vs;
	}

	/**
	 * 输入流转成字符串
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(is));
		StringBuffer buffer = new StringBuffer();
		String line = "";
		while ((line = in.readLine()) != null) {
			buffer.append(line);
		}
		return buffer.toString();
	}

	/**
	 * 得到字段名的Get方法
	 * 
	 * @param objectClass
	 * @param fieldName
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method getGetMethod(Class<?> objectClass, String fieldName) throws SecurityException {

		StringBuffer sb = new StringBuffer();
		sb.append("get");
		sb.append(fieldName.substring(0, 1).toUpperCase());
		sb.append(fieldName.substring(1));
		Method method = null;
		try {
			method = objectClass.getMethod(sb.toString());
		} catch (NoSuchMethodException e) {
		}
		return method;
	}

	/**
	 * 得到字段名的Set方法
	 * 
	 * @param objectClass
	 * @param fieldName
	 * @return
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Method getSetMethod(Class<?> objectClass, String fieldName) throws SecurityException {

		Class<?>[] parameterTypes = new Class[1];
		Field field = null;
		Class<?> clazz = objectClass;
		while (clazz != null) {
			try {
				field = clazz.getDeclaredField(fieldName);
				if (null != field) {
					break;
				}
			} catch (Exception e) {
				clazz = objectClass.getSuperclass();
			}

		}
		if (null == field) {
			return null;
		}

		parameterTypes[0] = field.getType();
		StringBuffer sb = new StringBuffer();
		sb.append("set");
		sb.append(fieldName.substring(0, 1).toUpperCase());
		sb.append(fieldName.substring(1));
		Method method = null;
		try {
			method = objectClass.getMethod(sb.toString(), parameterTypes);
		} catch (NoSuchMethodException e) {

		}
		return method;

	}

	/**
	 * 执行字段名的Set方法
	 * 
	 * @param o
	 * @param fieldName
	 * @param value
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static void invokeSet(Object o, String fieldName, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException, NoSuchMethodException {
		if (null == value) {
			return;
		}
		Method method = getSetMethod(o.getClass(), fieldName);
		method.invoke(o, new Object[] { value });

	}

	/**
	 * 执行字段名的Get方法
	 * 
	 * @param o
	 * @param fieldName
	 * @return
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 */
	public static Object invokeGet(Object o, String fieldName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, SecurityException {
		Method method = getGetMethod(o.getClass(), fieldName);
		if (null == method) {
			return null;
		}
		return method.invoke(o, new Object[0]);
	}

	/**
	 * 获取实体类描述
	 * 
	 * @param clazz
	 * @return
	 */
	public static EntityModel getEntityModel(Class<?> clazz) {
		Map<String, Id> pks = new Hashtable<String, Id>();
		Map<String, Column> columns = new Hashtable<String, Column>();
		Map<String, Field> fields = new Hashtable<String, Field>();

		String fieldName = null;
		while (null != clazz) {
			for (Field field : clazz.getDeclaredFields()) {
				if (null != field.getAnnotation(Transient.class)) {
					continue;
				}

				fieldName = field.getName();
				fields.put(fieldName, field);

				if (null != field.getAnnotation(Id.class)) {
					pks.put(fieldName, field.getAnnotation(Id.class));
				}
				if (null != field.getAnnotation(Column.class)) {
					columns.put(fieldName, field.getAnnotation(Column.class));
				}
				// anno = field.getAnnotation(Column.class);
				// if (null != anno && !StringUtils.isEmpty(((Column)
				// anno).name())) {
				// if (null == field.getAnnotation(Id.class)) {
				// columns.put(fieldName, ((Column) anno).name());
				// } else {
				// pks.put(fieldName, ((Column) anno).name());
				// }
				//
				// } else {
				// if (null == field.getAnnotation(Id.class)) {
				// columns.put(fieldName, fieldName);
				// } else {
				// pks.put(fieldName, fieldName);
				// }
				// }
			}
			clazz = clazz.getSuperclass();
		}
		EntityModel mode = new EntityModel();
		mode.setColumns(columns);
		mode.setPks(pks);
		mode.setFields(fields);
		return mode;
	}

	/**
	 * 获取实体的Update形式的SQL
	 * 
	 * @param o
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public static String getUpdateSQL(Object o, List<Object> values, Map<String, Object> request) throws Exception {
		StringBuilder sql = new StringBuilder();
		Entity entity = o.getClass().getAnnotation(Entity.class);
		String tableName = entity.name();
		if (StringUtils.isEmpty(tableName)) {
			tableName = o.getClass().getSimpleName();
		}
		sql.append("UPDATE ").append(tableName).append(" SET ");
		EntityModel entityMode = Utils.getEntityModel(o.getClass());
		Object value = null;
		for (String key : entityMode.getFields().keySet()) {
			if (!request.containsKey(key)) {
				continue;
			}
			if (null == Utils.getGetMethod(o.getClass(), key) || null != entityMode.getPks().get(key)) {
				continue;
			}
			value = Utils.invokeGet(o, key);
			// if (null == value){
			// continue;
			// }
			if (null == entityMode.getColumns().get(key) || StringUtils.isEmpty(entityMode.getColumns().get(key).name())) {
				sql.append(key).append("=?,");
			} else {
				sql.append(entityMode.getColumns().get(key).name()).append("=?,");
			}
			values.add(value);
		}
		if (sql.substring(sql.length() - 1).equals(",")) {
			sql.delete(sql.length() - 1, sql.length());
		}
		sql.append(" WHERE ");

		for (String key : entityMode.getPks().keySet()) {
			if (null == Utils.getGetMethod(o.getClass(), key)) {
				continue;
			}

			if (null == entityMode.getColumns().get(key) || StringUtils.isEmpty(entityMode.getColumns().get(key).name())) {
				sql.append(key).append("=? and ");
			} else {
				sql.append(entityMode.getColumns().get(key).name()).append("=?,");
			}
			values.add(Utils.invokeGet(o, key));
		}

		sql.append(" 1=1 ");

		return sql.toString();
	}

	/**
	 * 获取实体的Update形式的SQL
	 * 
	 * @param o
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public static String getUpdateSQL(Object o, List<Object> values) throws Exception {
		StringBuilder sql = new StringBuilder();
		Entity entity = o.getClass().getAnnotation(Entity.class);
		String tableName = entity.name();
		if (StringUtils.isEmpty(tableName)) {
			tableName = o.getClass().getSimpleName();
		}
		sql.append("UPDATE ").append(tableName).append(" SET ");
		EntityModel entityMode = Utils.getEntityModel(o.getClass());
		Object value = null;
		for (String key : entityMode.getFields().keySet()) {

			if (null == Utils.getGetMethod(o.getClass(), key) || null != entityMode.getPks().get(key)) {
				continue;
			}
			value = Utils.invokeGet(o, key);
			if (null == value) {
				continue;
			}
			if (null == entityMode.getColumns().get(key) || StringUtils.isEmpty(entityMode.getColumns().get(key).name())) {
				sql.append(key).append("=?,");
			} else {
				sql.append(entityMode.getColumns().get(key).name()).append("=?,");
			}
			values.add(value);
		}
		if (sql.substring(sql.length() - 1).equals(",")) {
			sql.delete(sql.length() - 1, sql.length());
		}
		sql.append(" WHERE ");

		for (String key : entityMode.getPks().keySet()) {
			if (null == Utils.getGetMethod(o.getClass(), key)) {
				continue;
			}

			if (null == entityMode.getColumns().get(key) || StringUtils.isEmpty(entityMode.getColumns().get(key).name())) {
				sql.append(key).append("=? and ");
			} else {
				sql.append(entityMode.getColumns().get(key).name()).append("=?,");
			}
			values.add(Utils.invokeGet(o, key));
		}

		sql.append(" 1=1 ");

		return sql.toString();
	}

	/**
	 * 获取实体的Delete形式的SQL
	 * 
	 * @param o
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public static String getDeleteSQL(Object o, List<Object> values) throws Exception {
		StringBuilder sql = new StringBuilder();
		Entity entity = o.getClass().getAnnotation(Entity.class);
		String tableName = entity.name();
		if (StringUtils.isEmpty(tableName)) {
			tableName = o.getClass().getSimpleName();
		}
		sql.append("DELETE ").append(tableName).append(" WHERE ");
		EntityModel entityMode = Utils.getEntityModel(o.getClass());
		for (String key : entityMode.getPks().keySet()) {
			if (null == Utils.getGetMethod(o.getClass(), key)) {
				continue;
			}

			if (null == entityMode.getColumns().get(key) || StringUtils.isEmpty(entityMode.getColumns().get(key).name())) {
				sql.append(key).append("=? and ");
			} else {
				sql.append(entityMode.getColumns().get(key).name()).append("=?,");
			}
			values.add(Utils.invokeGet(o, key));
		}
		sql.append(" 1=1 ");
		return sql.toString();
	}

	/**
	 * 获取实体的Insert形式的SQL
	 * 
	 * @param o
	 * @param values
	 * @return
	 * @throws Exception
	 */
	public static String getInsertSQL(Object o, Map<String, Object> generatedValue, List<Object> values) throws Exception {
		StringBuilder sql = new StringBuilder();
		StringBuilder vb = new StringBuilder();
		Entity entity = o.getClass().getAnnotation(Entity.class);
		if (null == entity){
			throw new Exception("没有定义实体类型！");
		}
		String tableName = entity.name();
		if (StringUtils.isEmpty(tableName)) {
			tableName = o.getClass().getSimpleName();
		}

		sql.append("INSERT INTO ").append(tableName).append("(");
		EntityModel entityMode = Utils.getEntityModel(o.getClass());
		Object value = null;
		for (String key : entityMode.getFields().keySet()) {
			if (null == Utils.getGetMethod(o.getClass(), key)) {
				continue;
			}

			// value = (null == Utils.invokeGet(o, key)) ?
			// generatedValue.get(key) : Utils.invokeGet(o, key);
			value = Utils.invokeGet(o, key);
			if (null == value) {
				value = generatedValue.get(key);
			}

			if ((value instanceof Integer) && (((Integer) value) == 0)) {
				value = generatedValue.get(key);
			} else if ((value instanceof Long) && (((Long) value) == 0L)) {
				value = generatedValue.get(key);
			}
			if (null == value) {
				continue;
			}
			if (null == entityMode.getColumns().get(key) || StringUtils.isEmpty(entityMode.getColumns().get(key).name())) {
				sql.append(key).append(",");
			} else {
				sql.append(entityMode.getColumns().get(key).name()).append(",");
			}
			vb.append("?,");
			values.add(value);
		}

		// for (String key : entityMode.getPks().keySet()) {
		// if (null != Utils.getGetMethod(o.getClass(), key)) {
		// sql.append(key).append(",");
		// vb.append("?,");
		// values.add((null == Utils.invokeGet(o, key)) ?
		// generatedValue.get(key) : Utils.invokeGet(o, key));
		// }
		// }
		//
		// for (String key : entityMode.getColumns().keySet()) {
		// if (null != Utils.getGetMethod(o.getClass(), key)) {
		// sql.append(key).append(",");
		// vb.append("?,");
		// values.add((null == Utils.invokeGet(o, key)) ?
		// generatedValue.get(key) : Utils.invokeGet(o, key));
		// }
		// }

		if (sql.substring(sql.length() - 1).equals(",")) {
			sql.delete(sql.length() - 1, sql.length());
			vb.delete(vb.length() - 1, vb.length());
		}
		sql.append(") VALUES(").append(vb).append(")");
		return sql.toString();
	}

	/**
	 * MD5
	 * 
	 * @param code
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static String MD5(String code) throws NoSuchAlgorithmException {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

		byte[] btInput = code.getBytes();
		// 获得MD5摘要算法的 MessageDigest 对象
		MessageDigest mdInst = MessageDigest.getInstance("MD5");
		// 使用指定的字节更新摘要
		mdInst.update(btInput);
		// 获得密文
		byte[] md = mdInst.digest();
		// 把密文转换成十六进制的字符串形式
		int j = md.length;
		char str[] = new char[j * 2];
		int k = 0;
		for (int i = 0; i < j; i++) {
			byte byte0 = md[i];
			str[k++] = hexDigits[byte0 >>> 4 & 0xf];
			str[k++] = hexDigits[byte0 & 0xf];
		}
		return new String(str);
	}

	/**
	 * 获取客户端IP
	 * 
	 * @param header
	 * @return
	 */
	public static String getRequestIP(Map<String, String> header) {
		String ip = header.get("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = header.get("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = header.get("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = header.get("$RemoteAddr");
		}
		return ip;
	}

	/**
	 * Map中取值生成对象
	 * 
	 * @param clazz
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Object map2ObjectByPK(Class<?> clazz, Map<String, Object> request) throws Exception {
		Object object = clazz.newInstance();
		EntityModel model = Utils.getEntityModel(clazz);
		Map<String, Id> pks = model.getPks();
		Map<String, Field> fields = model.getFields();
		Object value;
		for (String key : pks.keySet()) {
			value = request.get(key);
			if (null == value) {
				LOG.error("实体类需要一个主键[" + key + "]值！");
				throw new Exception("实体类需要一个主键[" + key + "]值！");
			} else {
				if (LOG.isDebugEnabled()) {
					LOG.debug("fieldName:" + key + " , value:" + value);
				}

				value = Utils.transferType(value, fields.get(key).getType());
				Utils.invokeSet(object, key, value);
			}
		}
		return object;
	}

	/**
	 * Map中取值生成对象
	 * 
	 * @param clazz
	 * @param request
	 * @return
	 * @throws Exception
	 */
	public static Object map2Object(Class<?> clazz, Map<String, Object> request, Map<String, Object> application, Map<String, Object> session) throws Exception {
		Object object = clazz.newInstance();
		EntityModel model = Utils.getEntityModel(clazz);
		Map<String, Field> fields = model.getFields();
		Object value = null;
		Object cvt = null;
		for (String key : fields.keySet()) {
			value = request.get(key);
			if (null == value) {
				continue;
			}
			cvt = convertValue(String.valueOf(value), application, session);
			if (null != cvt) {
				value = cvt;
			}
			if (LOG.isDebugEnabled()) {
				LOG.debug("fieldName:" + key + " , cvt: " + cvt + " ,value:" + value);
			}
			value = Utils.transferType(value, fields.get(key).getType());
			Utils.invokeSet(object, key, value);
		}

		// for (String key : pks.keySet()) {
		// value = request.get(key);
		// if (null == value) {
		// continue;
		// } else {
		// if (LOG.isDebugEnabled()) {
		// LOG.debug("fieldName:" + key + " , value:" + value);
		// }
		//
		// value = Utils.transferType(value, fields.get(key).getType());
		// Utils.invokeSet(object, key, value);
		// }
		// }
		//
		// Object cvt = null;
		// for (String key : model.getColumns().keySet()) {
		// value = request.get(key);
		// if (null == value) {
		// continue;
		// }
		// cvt = convertValue(String.valueOf(value), application, session);
		//
		// if (null != cvt) {
		// value = cvt;
		// }
		// if (LOG.isDebugEnabled()) {
		// LOG.debug("fieldName:" + key + " , cvt: " + cvt + " ,value:" +
		// value);
		// }
		// value = Utils.transferType(value, fields.get(key).getType());
		// Utils.invokeSet(object, key, value);
		// }
		return object;
	}

	/**
	 * 字符串转换成值
	 * 
	 * @param str
	 * @param application
	 * @param session
	 * @return
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static Object convertValue(String str, Map<String, Object> application, Map<String, Object> session) throws IllegalArgumentException, SecurityException, IllegalAccessException,
			InvocationTargetException {
		String tmp;
		if (str.length() > 7 && str.substring(0, 1).equals("{") && str.substring(str.length() - 1, str.length()).equals("}")) {
			tmp = str.substring(1, str.length() - 1);
			String[] strs = StringUtils.split(tmp, "[.]");
			if (strs.length == 2 && strs[0].equalsIgnoreCase("application")) {
				return application.get(strs[1]);
			} else if (strs.length == 3 && strs[0].equalsIgnoreCase("session")) {
				return Utils.invokeGet(session.get(strs[1]), strs[2]);
			}
		}
		return null;
	}
}
