package com.mq.android.oa.bytter.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpService {
	private static final String CHARSET = HTTP.UTF_8;
	private HttpClient client;
	private String usercode;
	private String loginUrl;
	private String upworkUrl;
	private String downworkUrl;

	public HttpService() {
		this.client = this.getHttpClient();
	}

	public HttpClient getClient() {
		return client;
	}

	public void setClient(HttpClient client) {
		this.client = client;
	}

	public String getUsercode() {
		return usercode;
	}

	public void setUsercode(String usercode) {
		this.usercode = usercode;
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

	public String getUpworkUrl() {
		return upworkUrl;
	}

	public void setUpworkUrl(String upworkUrl) {
		this.upworkUrl = upworkUrl;
	}

	public String getDownworkUrl() {
		return downworkUrl;
	}

	public void setDownworkUrl(String downworkUrl) {
		this.downworkUrl = downworkUrl;
	}

	public void doUpwork() throws ClientProtocolException, IOException {
		HttpResponse response = this.doPost(this.upworkUrl, this.getUpWorkData());
		if (response.getStatusLine().getStatusCode() != 200) {
			new IOException("请求[POST]" + this.downworkUrl + "发生错误!");
		}
	}

	public void doDownwork() throws ClientProtocolException, IOException {

		HttpResponse response = this.doPost(this.downworkUrl, this.getDownWorkData());
		if (response.getStatusLine().getStatusCode() != 200) {
			new IOException("请求[POST]" + this.downworkUrl + "发生错误!");
		}
	}

	/**
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public Integer doLogin() throws ClientProtocolException, IOException {
		Integer state = 0;
		this.doPost(this.loginUrl, this.getLoginData());

		{
			String upworkUrlStr = this.doGetByString(this.upworkUrl);
			this.usercode = this.getUsercode(upworkUrlStr);
			if (null == this.usercode) {
				new IOException("从签到页面中取不到用户编号！");
			}
			if (this.getBtnStatus(upworkUrlStr)) {
				state |=1;
			}
		}
		{
			String downworkUrlStr = this.doGetByString(this.downworkUrl);
			if (this.getBtnStatus(downworkUrlStr)) {
				state |=2;
			}
		}
		return state;
	}

	private HttpResponse doPost(String url, List<NameValuePair> params) throws ClientProtocolException, IOException {
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(params, CHARSET);
		HttpPost post = new HttpPost(url);
		post.setEntity(entity);
		return client.execute(post);

	}

	//
	// private String doPostByString(String url, List<NameValuePair> params)
	// throws ClientProtocolException, IOException {
	// HttpResponse response = this.doPost(url, params);
	// if (response.getStatusLine().getStatusCode() != 200) {
	// // System.out.println(EntityUtils.toString(response.getEntity()));
	// new IOException("请求[POST]" + url + "发生错误!");
	// }
	// return EntityUtils.toString(response.getEntity());
	//
	// }

	private HttpResponse doGet(String url) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		return client.execute(get);

	}

	private String doGetByString(String url) throws ClientProtocolException, IOException {
		HttpResponse response = this.doGet(url);
		if (response.getStatusLine().getStatusCode() != 200) {
			new IOException("请求[GET]" + url + "发生错误!");
		}
		return EntityUtils.toString(response.getEntity());

	}

	private HttpClient getHttpClient() {
		if (null == client) {
			HttpParams params = new BasicHttpParams();
			// 设置一些根蒂根基参数
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, CHARSET);
			HttpProtocolParams.setUseExpectContinue(params, true);
			HttpProtocolParams.setUserAgent(params, "Mozilla/5.0(Linux; U; Android 2.2; en-gb; LG-P500 Build/FRF91) AppleWebKit/533.0 (KHTML, like Gecko) Version/4.0 Mobile Safari/533.1");

			// 超时设置
			/* 从连接池中取连接的超不时候 */
			ConnManagerParams.setTimeout(params, 1000);
			/* 连接超时 */
			HttpConnectionParams.setConnectionTimeout(params, 2000);
			/* 恳求超时 */
			HttpConnectionParams.setSoTimeout(params, 4000);
			// 设置我们的HttpClient支撑HTTP和HTTPS两种模式
			SchemeRegistry schReg = new SchemeRegistry();
			schReg.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
			schReg.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
			// 应用线程安然的连接经管来创建HttpClient
			ClientConnectionManager conMgr = new ThreadSafeClientConnManager(params, schReg);
			client = new DefaultHttpClient(conMgr, params);

		}

		return client;

	}

	public List<NameValuePair> getLoginData() {
		List<NameValuePair> paramPair = new ArrayList<NameValuePair>();

		// PostMethod post = new
		// PostMethod("/c6/Jhsoft.Web.login/PassWord.aspx");

		paramPair.add(new BasicNameValuePair("Password2", "0"));
		paramPair.add(new BasicNameValuePair("UserName", "yangmq"));

		paramPair.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		paramPair.add(new BasicNameValuePair("__EVENTTARGET", "LBEnter"));
		paramPair.add(new BasicNameValuePair("__LASTFOCUS", ""));
		paramPair
				.add(new BasicNameValuePair(
						"__VIEWSTATE",
						"/wEPDwUKLTY1OTY3NDk5Mg9kFgJmD2QWDgIBDw8WAh4EVGV4dAUM5a+G56CB55m75b2VZGQCAw8WAh4HVmlzaWJsZWhkAgQPFgIfAWhkAgUPFgIfAWhkAgYPFgIfAWhkAgcPDxYCHwAFDOeugOS9k+S4reaWh2RkAgoPFgIeCWlubmVyaHRtbAXDATxkaXYgY2xhc3M9ImNvcHlyaWdodCI+CTxzcGFuPumHkeWSjOi9r+S7tiZuYnNwOyZjb3B5OzIwMTA8YSBocmVmPSIiIG9uY2xpY2s9ImphdmFzY3JpcHQ6d2luZG93Lm9wZW4oJ2h0dHA6Ly93d3cuamgwMTAxLmNvbScsJycsJycpO3ZvaWQoMCk7cmV0dXJuIGZhbHNlOyI+Jm5ic3A7SmluaGVyIFNvZnR3YXJlPC9hPjwvc3Bhbi0tPjwvZGl2PmRk"));
		paramPair.add(new BasicNameValuePair("hidEpass", "2"));
		paramPair.add(new BasicNameValuePair("hidIsValid", "0"));
		paramPair.add(new BasicNameValuePair("hidLoginState", ""));
		paramPair.add(new BasicNameValuePair("txtLan", "简体中文"));
		paramPair.add(new BasicNameValuePair("txtPassword", "密码登录"));
		return paramPair;
	}

	public List<NameValuePair> getUpWorkData() {

		List<NameValuePair> paramPair = new ArrayList<NameValuePair>();
		paramPair.add(new BasicNameValuePair("Password2", "0"));
		paramPair.add(new BasicNameValuePair("DDLClass", "11111-11111-11111-11111-11111"));
		paramPair.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		paramPair.add(new BasicNameValuePair("__EVENTTARGET", "btn_ok"));
		paramPair.add(new BasicNameValuePair("__LASTFOCUS", ""));
		paramPair
				.add(new BasicNameValuePair(
						"__VIEWSTATE",
						"/wEPDwUKMTg3OTc1MjE3OQ8WAh4JQURPbkNsYXNzMsMVAAEAAAD/////AQAAAAAAAAAMAgAAAE5TeXN0ZW0uRGF0YSwgVmVyc2lvbj0yLjAuMC4wLCBDdWx0dXJlPW5ldXRyYWwsIFB1YmxpY0tleVRva2VuPWI3N2E1YzU2MTkzNGUwODkFAQAAABVTeXN0ZW0uRGF0YS5EYXRhVGFibGUDAAAAGURhdGFUYWJsZS5SZW1vdGluZ1ZlcnNpb24JWG1sU2NoZW1hC1htbERpZmZHcmFtAwEBDlN5c3RlbS5WZXJzaW9uAgAAAAkDAAAABgQAAADwDDw/eG1sIHZlcnNpb249IjEuMCIgZW5jb2Rpbmc9InV0Zi0xNiI/Pg0KPHhzOnNjaGVtYSB4bWxucz0iIiB4bWxuczp4cz0iaHR0cDovL3d3dy53My5vcmcvMjAwMS9YTUxTY2hlbWEiIHhtbG5zOm1zZGF0YT0idXJuOnNjaGVtYXMtbWljcm9zb2Z0LWNvbTp4bWwtbXNkYXRhIj4NCiAgPHhzOmVsZW1lbnQgbmFtZT0iVGFibGUxIj4NCiAgICA8eHM6Y29tcGxleFR5cGU+DQogICAgICA8eHM6c2VxdWVuY2U+DQogICAgICAgIDx4czplbGVtZW50IG5hbWU9Imxhd19HdWlkIiB0eXBlPSJ4czpzdHJpbmciIG1zZGF0YTp0YXJnZXROYW1lc3BhY2U9IiIgbWluT2NjdXJzPSIwIiAvPg0KICAgICAgICA8eHM6ZWxlbWVudCBuYW1lPSJwdXRfYWhlYWRUaW1lIiB0eXBlPSJ4czppbnQiIG1zZGF0YTp0YXJnZXROYW1lc3BhY2U9IiIgbWluT2NjdXJzPSIwIiAvPg0KICAgICAgICA8eHM6ZWxlbWVudCBuYW1lPSJwdXRfYmVnaW5UaW1lIiB0eXBlPSJ4czpkYXRlVGltZSIgbXNkYXRhOnRhcmdldE5hbWVzcGFjZT0iIiBtaW5PY2N1cnM9IjAiIC8+DQogICAgICAgIDx4czplbGVtZW50IG5hbWU9ImNsYXNzX25hbWUiIHR5cGU9InhzOnN0cmluZyIgbXNkYXRhOnRhcmdldE5hbWVzcGFjZT0iIiBtaW5PY2N1cnM9IjAiIC8+DQogICAgICAgIDx4czplbGVtZW50IG5hbWU9ImNsYXNzX3NpbXBOYW1lIiB0eXBlPSJ4czpzdHJpbmciIG1zZGF0YTp0YXJnZXROYW1lc3BhY2U9IiIgbWluT2NjdXJzPSIwIiAvPg0KICAgICAgICA8eHM6ZWxlbWVudCBuYW1lPSJjbGFzc19iZWdpblRpbWUiIHR5cGU9InhzOmRhdGVUaW1lIiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgICAgPHhzOmVsZW1lbnQgbmFtZT0iY2xhc3NfZW5kVGltZSIgdHlwZT0ieHM6ZGF0ZVRpbWUiIG1zZGF0YTp0YXJnZXROYW1lc3BhY2U9IiIgbWluT2NjdXJzPSIwIiAvPg0KICAgICAgICA8eHM6ZWxlbWVudCBuYW1lPSJjbGFzc19mbGFnIiB0eXBlPSJ4czppbnQiIG1zZGF0YTp0YXJnZXROYW1lc3BhY2U9IiIgbWluT2NjdXJzPSIwIiAvPg0KICAgICAgICA8eHM6ZWxlbWVudCBuYW1lPSJjbGFzc19HdWlkIiB0eXBlPSJ4czpzdHJpbmciIG1zZGF0YTp0YXJnZXROYW1lc3BhY2U9IiIgbWluT2NjdXJzPSIwIiAvPg0KICAgICAgICA8eHM6ZWxlbWVudCBuYW1lPSJjbGFzc19zdHJldGNodGltZSIgdHlwZT0ieHM6aW50IiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgICAgPHhzOmVsZW1lbnQgbmFtZT0iYXR0X2lzT24iIHR5cGU9InhzOmludCIgbXNkYXRhOnRhcmdldE5hbWVzcGFjZT0iIiBtaW5PY2N1cnM9IjAiIC8+DQogICAgICA8L3hzOnNlcXVlbmNlPg0KICAgIDwveHM6Y29tcGxleFR5cGU+DQogIDwveHM6ZWxlbWVudD4NCiAgPHhzOmVsZW1lbnQgbmFtZT0iTmV3RGF0YVNldCIgbXNkYXRhOklzRGF0YVNldD0idHJ1ZSIgbXNkYXRhOk1haW5EYXRhVGFibGU9IlRhYmxlMSIgbXNkYXRhOlVzZUN1cnJlbnRMb2NhbGU9InRydWUiPg0KICAgIDx4czpjb21wbGV4VHlwZT4NCiAgICAgIDx4czpjaG9pY2UgbWluT2NjdXJzPSIwIiBtYXhPY2N1cnM9InVuYm91bmRlZCIgLz4NCiAgICA8L3hzOmNvbXBsZXhUeXBlPg0KICA8L3hzOmVsZW1lbnQ+DQo8L3hzOnNjaGVtYT4GBQAAAKYGPGRpZmZncjpkaWZmZ3JhbSB4bWxuczptc2RhdGE9InVybjpzY2hlbWFzLW1pY3Jvc29mdC1jb206eG1sLW1zZGF0YSIgeG1sbnM6ZGlmZmdyPSJ1cm46c2NoZW1hcy1taWNyb3NvZnQtY29tOnhtbC1kaWZmZ3JhbS12MSI+DQogIDxOZXdEYXRhU2V0Pg0KICAgIDxUYWJsZTEgZGlmZmdyOmlkPSJUYWJsZTExIiBtc2RhdGE6cm93T3JkZXI9IjAiPg0KICAgICAgPGxhd19HdWlkPlJhM1o3LThWYzBiLUs5SWFDLTlIQkNmLVNGMmRSPC9sYXdfR3VpZD4NCiAgICAgIDxwdXRfYWhlYWRUaW1lPjA8L3B1dF9haGVhZFRpbWU+DQogICAgICA8cHV0X2JlZ2luVGltZT4yMDEwLTAzLTAxVDAwOjAwOjAwKzA4OjAwPC9wdXRfYmVnaW5UaW1lPg0KICAgICAgPGNsYXNzX25hbWU+5rex5ZyzLeeZveePrTwvY2xhc3NfbmFtZT4NCiAgICAgIDxjbGFzc19zaW1wTmFtZT7nmb08L2NsYXNzX3NpbXBOYW1lPg0KICAgICAgPGNsYXNzX2JlZ2luVGltZT4yMDAwLTAxLTAxVDA4OjMxOjAwKzA4OjAwPC9jbGFzc19iZWdpblRpbWU+DQogICAgICA8Y2xhc3NfZW5kVGltZT4yMDAwLTAxLTAxVDE3OjU5OjAwKzA4OjAwPC9jbGFzc19lbmRUaW1lPg0KICAgICAgPGNsYXNzX2ZsYWc+MDwvY2xhc3NfZmxhZz4NCiAgICAgIDxjbGFzc19HdWlkPjExMTExLTExMTExLTExMTExLTExMTExLTExMTExPC9jbGFzc19HdWlkPg0KICAgICAgPGNsYXNzX3N0cmV0Y2h0aW1lPjI8L2NsYXNzX3N0cmV0Y2h0aW1lPg0KICAgICAgPGF0dF9pc09uPjA8L2F0dF9pc09uPg0KICAgIDwvVGFibGUxPg0KICA8L05ld0RhdGFTZXQ+DQo8L2RpZmZncjpkaWZmZ3JhbT4EAwAAAA5TeXN0ZW0uVmVyc2lvbgQAAAAGX01ham9yBl9NaW5vcgZfQnVpbGQJX1JldmlzaW9uAAAAAAgICAgCAAAAAAAAAP//////////CxYCZg9kFgxmDxAPFgYeDURhdGFUZXh0RmllbGQFCkNsYXNzX05hbWUeDkRhdGFWYWx1ZUZpZWxkBQpDbGFzc19HdWlkHgtfIURhdGFCb3VuZGdkEBUBDea3seWcsy3nmb3nj60VAR0xMTExMS0xMTExMS0xMTExMS0xMTExMS0xMTExMRQrAwFnFgFmZAIBDw8WAh4EVGV4dAUQ5rex5ZyzLeeZveePre+8mmRkAgIPDxYCHwQFDDg6MzEgLSAxNzo1OWRkAgMPDxYCHwQFBDg6MjRkZAIEDxYCHgVzdHlsZQUMZGlzcGxheTpub25lZAIGDxYCHghkaXNhYmxlZGRkZA=="));
		paramPair.add(new BasicNameValuePair("hidXml", "<root><userCode>" + this.usercode + "</userCode><onTime>" + this.getUpworkTime()
				+ "</onTime><LateResult><![CDATA[]]></LateResult><adjType>Normal</adjType><adjValue></adjValue><lawGuid>Ra3Z7-8Vc0b-K9IaC-9HBCf-SF2dR</lawGuid><classGuid>11111-11111-11111-11111-11111</classGuid><className>深圳-白班</className><classOnTime>8:31</classOnTime><classOffTime>17:59</classOffTime><putBeginTime>2010/3/1 0:00:00</putBeginTime><isLate>0</isLate></root>"));
		paramPair.add(new BasicNameValuePair("hid_PutBeginTime", "2010/3/1 0:00:00"));
		paramPair.add(new BasicNameValuePair("Normal", ""));
		paramPair.add(new BasicNameValuePair("hid_adjValue", ""));
		paramPair.add(new BasicNameValuePair("hid_classGuid", "11111-11111-11111-11111-11111"));
		paramPair.add(new BasicNameValuePair("hid_className", "深圳-白班"));
		paramPair.add(new BasicNameValuePair("hid_classOnTime", "8:31"));
		paramPair.add(new BasicNameValuePair("hid_clsssOffTime", "17:59"));
		paramPair.add(new BasicNameValuePair("hid_isLate", "0"));
		paramPair.add(new BasicNameValuePair("hid_lawGuid", "Ra3Z7-8Vc0b-K9IaC-9HBCf-SF2dR"));
		paramPair.add(new BasicNameValuePair("txt_LateResult", ""));

		return paramPair;
	}

	public List<NameValuePair> getDownWorkData() {

		List<NameValuePair> paramPair = new ArrayList<NameValuePair>();
		paramPair.add(new BasicNameValuePair("DDLClass", "11111-11111-11111-11111-11111"));
		paramPair.add(new BasicNameValuePair("__EVENTARGUMENT", ""));
		paramPair.add(new BasicNameValuePair("__EVENTTARGET", "btn_ok"));
		paramPair.add(new BasicNameValuePair("__LASTFOCUS", ""));
		paramPair
				.add(new BasicNameValuePair(
						"__VIEWSTATE",
						"/wEPDwULLTEzNzExNTkwNDUPFgIeCkFET2ZmQ2xhc3My/RQAAQAAAP////8BAAAAAAAAAAwCAAAATlN5c3RlbS5EYXRhLCBWZXJzaW9uPTIuMC4wLjAsIEN1bHR1cmU9bmV1dHJhbCwgUHVibGljS2V5VG9rZW49Yjc3YTVjNTYxOTM0ZTA4OQUBAAAAFVN5c3RlbS5EYXRhLkRhdGFUYWJsZQMAAAAZRGF0YVRhYmxlLlJlbW90aW5nVmVyc2lvbglYbWxTY2hlbWELWG1sRGlmZkdyYW0DAQEOU3lzdGVtLlZlcnNpb24CAAAACQMAAAAGBAAAAOoMPD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTE2Ij8+DQo8eHM6c2NoZW1hIHhtbG5zPSIiIHhtbG5zOnhzPSJodHRwOi8vd3d3LnczLm9yZy8yMDAxL1hNTFNjaGVtYSIgeG1sbnM6bXNkYXRhPSJ1cm46c2NoZW1hcy1taWNyb3NvZnQtY29tOnhtbC1tc2RhdGEiPg0KICA8eHM6ZWxlbWVudCBuYW1lPSJUYWJsZTEiPg0KICAgIDx4czpjb21wbGV4VHlwZT4NCiAgICAgIDx4czpzZXF1ZW5jZT4NCiAgICAgICAgPHhzOmVsZW1lbnQgbmFtZT0iYXR0X1R5cGUiIHR5cGU9InhzOnN0cmluZyIgbXNkYXRhOnRhcmdldE5hbWVzcGFjZT0iIiBtaW5PY2N1cnM9IjAiIC8+DQogICAgICAgIDx4czplbGVtZW50IG5hbWU9ImF0dF9kYXRlIiB0eXBlPSJ4czpkYXRlVGltZSIgbXNkYXRhOnRhcmdldE5hbWVzcGFjZT0iIiBtaW5PY2N1cnM9IjAiIC8+DQogICAgICAgIDx4czplbGVtZW50IG5hbWU9ImF0dF9JRCIgdHlwZT0ieHM6aW50IiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgICAgPHhzOmVsZW1lbnQgbmFtZT0iY2xhc3NfR3VpZCIgdHlwZT0ieHM6c3RyaW5nIiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgICAgPHhzOmVsZW1lbnQgbmFtZT0iY2xhc3NfbmFtZSIgdHlwZT0ieHM6c3RyaW5nIiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgICAgPHhzOmVsZW1lbnQgbmFtZT0iY2xhc3NfYmVnaW5UaW1lIiB0eXBlPSJ4czpkYXRlVGltZSIgbXNkYXRhOnRhcmdldE5hbWVzcGFjZT0iIiBtaW5PY2N1cnM9IjAiIC8+DQogICAgICAgIDx4czplbGVtZW50IG5hbWU9ImNsYXNzX2VuZFRpbWUiIHR5cGU9InhzOmRhdGVUaW1lIiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgICAgPHhzOmVsZW1lbnQgbmFtZT0iY2xhc3NfZmxhZyIgdHlwZT0ieHM6aW50IiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgICAgPHhzOmVsZW1lbnQgbmFtZT0iYXR0X29mZnRpbWUiIHR5cGU9InhzOmRhdGVUaW1lIiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgICAgPHhzOmVsZW1lbnQgbmFtZT0iYXR0X29udGltZSIgdHlwZT0ieHM6ZGF0ZVRpbWUiIG1zZGF0YTp0YXJnZXROYW1lc3BhY2U9IiIgbWluT2NjdXJzPSIwIiAvPg0KICAgICAgICA8eHM6ZWxlbWVudCBuYW1lPSJjbGFzc19zdHJldGNodGltZSIgdHlwZT0ieHM6aW50IiBtc2RhdGE6dGFyZ2V0TmFtZXNwYWNlPSIiIG1pbk9jY3Vycz0iMCIgLz4NCiAgICAgIDwveHM6c2VxdWVuY2U+DQogICAgPC94czpjb21wbGV4VHlwZT4NCiAgPC94czplbGVtZW50Pg0KICA8eHM6ZWxlbWVudCBuYW1lPSJ0bXBEYXRhU2V0IiBtc2RhdGE6SXNEYXRhU2V0PSJ0cnVlIiBtc2RhdGE6TWFpbkRhdGFUYWJsZT0iVGFibGUxIiBtc2RhdGE6VXNlQ3VycmVudExvY2FsZT0idHJ1ZSI+DQogICAgPHhzOmNvbXBsZXhUeXBlPg0KICAgICAgPHhzOmNob2ljZSBtaW5PY2N1cnM9IjAiIG1heE9jY3Vycz0idW5ib3VuZGVkIiAvPg0KICAgIDwveHM6Y29tcGxleFR5cGU+DQogIDwveHM6ZWxlbWVudD4NCjwveHM6c2NoZW1hPgYFAAAA5gU8ZGlmZmdyOmRpZmZncmFtIHhtbG5zOm1zZGF0YT0idXJuOnNjaGVtYXMtbWljcm9zb2Z0LWNvbTp4bWwtbXNkYXRhIiB4bWxuczpkaWZmZ3I9InVybjpzY2hlbWFzLW1pY3Jvc29mdC1jb206eG1sLWRpZmZncmFtLXYxIj4NCiAgPHRtcERhdGFTZXQ+DQogICAgPFRhYmxlMSBkaWZmZ3I6aWQ9IlRhYmxlMTEiIG1zZGF0YTpyb3dPcmRlcj0iMCI+DQogICAgICA8YXR0X1R5cGU+MDwvYXR0X1R5cGU+DQogICAgICA8YXR0X2RhdGU+MjAxMi0xMi0yNlQwMDowMDowMCswODowMDwvYXR0X2RhdGU+DQogICAgICA8YXR0X0lEPjU4MjkzPC9hdHRfSUQ+DQogICAgICA8Y2xhc3NfR3VpZD4xMTExMS0xMTExMS0xMTExMS0xMTExMS0xMTExMTwvY2xhc3NfR3VpZD4NCiAgICAgIDxjbGFzc19uYW1lPua3seWcsy3nmb3nj608L2NsYXNzX25hbWU+DQogICAgICA8Y2xhc3NfYmVnaW5UaW1lPjE5MDAtMDEtMDFUMDg6MzE6MDArMDg6MDA8L2NsYXNzX2JlZ2luVGltZT4NCiAgICAgIDxjbGFzc19lbmRUaW1lPjE5MDAtMDEtMDFUMTc6NTk6MDArMDg6MDA8L2NsYXNzX2VuZFRpbWU+DQogICAgICA8Y2xhc3NfZmxhZz4wPC9jbGFzc19mbGFnPg0KICAgICAgPGF0dF9vbnRpbWU+MjAxMi0xMi0yNlQwODozMjowMCswODowMDwvYXR0X29udGltZT4NCiAgICAgIDxjbGFzc19zdHJldGNodGltZT4yPC9jbGFzc19zdHJldGNodGltZT4NCiAgICA8L1RhYmxlMT4NCiAgPC90bXBEYXRhU2V0Pg0KPC9kaWZmZ3I6ZGlmZmdyYW0+BAMAAAAOU3lzdGVtLlZlcnNpb24EAAAABl9NYWpvcgZfTWlub3IGX0J1aWxkCV9SZXZpc2lvbgAAAAAICAgIAgAAAAAAAAD//////////wsWAmYPZBYKZg8QDxYGHg1EYXRhVGV4dEZpZWxkBQpjbGFzc19uYW1lHg5EYXRhVmFsdWVGaWVsZAUKY2xhc3NfZ3VpZB4LXyFEYXRhQm91bmRnZBAVAQ3mt7HlnLMt55m954+tFQEdMTExMTEtMTExMTEtMTExMTEtMTExMTEtMTExMTEUKwMBZxYBZmQCAQ8PFgIeBFRleHQFDDg6MzEgLSAxNzo1OWRkAgIPDxYCHwQFBTE5OjU4ZGQCAw8WAh4Fc3R5bGUFDGRpc3BsYXk6bm9uZWQCBg8WAh4IZGlzYWJsZWRkZGQ="));
		paramPair.add(new BasicNameValuePair("hidXml", "<root><userCode>" + this.usercode + "</userCode><onTime>" + this.getDownworkTime() + "</onTime><LateResult><![CDATA[]]></LateResult><isLeave>0</isLeave><attID>58293</attID></root>"));
		paramPair.add(new BasicNameValuePair("hid_attID", "58293"));
		paramPair.add(new BasicNameValuePair("hid_isLeaveEarly", "0"));
		paramPair.add(new BasicNameValuePair("txt_LateResult", ""));

		return paramPair;
	}

	public String getUsercode(String str) {
		Pattern pattern1 = Pattern.compile("<userCode>([^>]*)</userCode>");
		Matcher matcher1 = pattern1.matcher(str);
		if (matcher1.find()) {
			return matcher1.group(1);
		}
		return null;

	}

	public Boolean getBtnStatus(String str) {
		Pattern pattern2 = Pattern.compile("<input([^>]*)name=\"btn_ok\"([^>]*)>");
		Matcher matcher2 = pattern2.matcher(str);
		if (matcher2.find()) {
			return (matcher2.group(0).indexOf("disabled") > 0 ? false : true);
		}
		return false;
	}

	public String getUpworkTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date(System.currentTimeMillis()));
		time += " 7:" + (new Random().nextInt(49) + 10);
		return time;
	}

	public String getDownworkTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String time = sdf.format(new Date(System.currentTimeMillis()));
		time += " 18:" + (new Random().nextInt(49) + 10);
		return time;
	}
}
