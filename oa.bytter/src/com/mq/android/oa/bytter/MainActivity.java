package com.mq.android.oa.bytter;

import java.io.IOException;
import java.lang.ref.WeakReference;

import org.apache.http.client.ClientProtocolException;

import com.mq.android.oa.bytter.service.HttpService;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Button btnLogin;
	private Button btnUpwork;
	private Button btnDownwork;
	private ProgressBar progressBar;
	private EditText textUsername;
	private EditText textPassword;
	private Spinner spinnerSite;
	private HttpService service;

	private ServiceHandler handler;
	private ArrayAdapter<CharSequence> adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		{
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects().detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());
		}
		service = new HttpService();
		handler = new ServiceHandler(this);
		btnLogin = (Button) this.findViewById(R.id.loginBtn);
		btnUpwork = (Button) this.findViewById(R.id.upworkBtn);
		btnDownwork = (Button) this.findViewById(R.id.downworkBtn);
		progressBar = (ProgressBar) this.findViewById(R.id.progressBar);
		textUsername = (EditText) this.findViewById(R.id.usernameText);
		textPassword = (EditText) this.findViewById(R.id.passwordText);
		spinnerSite = (Spinner) this.findViewById(R.id.siteSpinner);

		// 将可选内容与ArrayAdapter连接起来
		adapter = ArrayAdapter.createFromResource(this, R.array.plantes, android.R.layout.simple_spinner_item);
		// 设置下拉列表的风格
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// 将adapter2 添加到spinner中
		spinnerSite.setAdapter(adapter);
		// 添加事件Spinner事件监听
		// spinnerSite.setOnItemSelectedListener(new
		// SpinnerXMLSelectedListener());
		// 设置默认值
		spinnerSite.setVisibility(View.VISIBLE);

		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String site = spinnerSite.getSelectedItem().toString();
				if (site.length() == 0) {
					Toast.makeText(getApplicationContext(), R.string.msg_site_isnull, Toast.LENGTH_SHORT).show();
					return;
				}
				if (textUsername.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), R.string.msg_username_isnull, Toast.LENGTH_SHORT).show();
					return;
				}
				if (textPassword.getText().toString().length() == 0) {
					Toast.makeText(getApplicationContext(), R.string.msg_password_isnull, Toast.LENGTH_SHORT).show();
					return;
				}

				service.setLoginUrl(site + "/Jhsoft.Web.login/PassWord.aspx");
				service.setUpworkUrl(site + "/JHSoft.web.HRMAttendance/attendance_on.aspx");
				service.setDownworkUrl(site + "/JHSoft.web.HRMAttendance/attendance_off.aspx");
				progressBar.setVisibility(View.VISIBLE);
				new Thread(new ServiceThread("login")).start();

			}
		});

		btnUpwork.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				progressBar.setVisibility(View.VISIBLE);

				new Thread(new ServiceThread("upwork")).start();
			}
		});
		btnDownwork.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				progressBar.setVisibility(View.VISIBLE);

				new Thread(new ServiceThread("downwork")).start();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.menu_exit) {
			this.finish();
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Handler
	 * 
	 * @author Administrator
	 * 
	 */
	static class ServiceHandler extends Handler {
		WeakReference<MainActivity> activity;

		public ServiceHandler(MainActivity activity) {
			this.activity = new WeakReference<MainActivity>(activity);
		}

		public ServiceHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity page = this.activity.get();
			page.progressBar.setVisibility(View.GONE);
			if (msg.arg2 < 0) {
				Toast.makeText(page.getApplicationContext(), R.string.msg_err, Toast.LENGTH_SHORT).show();
			} else {
				switch (msg.what) {
				case 1:
					if ((msg.arg2 & 1) == 1) {
						page.btnUpwork.setEnabled(true);
					}
					if ((msg.arg2 & 2) == 2) {
						page.btnDownwork.setEnabled(true);
					}
					page.btnLogin.setText(R.string.btn_login_again);
					break;
				case 2:
					page.btnUpwork.setEnabled(false);
					break;
				case 3:
					page.btnDownwork.setEnabled(false);
					break;
				}
			}

		}
	}

	class ServiceThread implements Runnable {
		private String method;

		ServiceThread(String method) {
			this.method = method;
		}

		@Override
		public void run() {
			Message msg = Message.obtain();
			msg.arg2 = 1;
			if ("login".equals(this.method)) {

				try {
					msg.what = 1;
					msg.arg2 = MainActivity.this.service.doLogin();
				} catch (ClientProtocolException e) {
					msg.arg2 = -1;
					e.printStackTrace();
				} catch (IOException e) {
					msg.arg2 = -1;
					e.printStackTrace();
				}

			} else if ("upwork".equals(this.method)) {
				try {
					msg.what = 2;
					MainActivity.this.service.doUpwork();
				} catch (ClientProtocolException e) {
					msg.arg2 = -1;
					e.printStackTrace();
				} catch (IOException e) {
					msg.arg2 = -1;
					e.printStackTrace();
				}
			} else if ("downwork".equals(this.method)) {
				try {
					msg.what = 3;
					MainActivity.this.service.doDownwork();
				} catch (ClientProtocolException e) {
					msg.arg2 = -1;
					e.printStackTrace();
				} catch (IOException e) {
					msg.arg2 = -1;
					e.printStackTrace();
				}
			}
			MainActivity.this.handler.sendMessage(msg);
		}

	}
}
