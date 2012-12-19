// 公共方法

Ext.onReady(function() {
			Ext.namespace('com.ad');
			com.ad = {
				version : '1.0',
				company : 'ad',
				ajax : function(config) {// 异步Ajas
					var wait = Ext.Msg.wait('处理中...', '请销等...', {
								text : '进度', // 进度条文字
								duration : 3000, // 进度条持续更新3秒
								interval : 1000, // 每1秒更新一次
								increment : 3, // 3次更新完毕
								scope : this, // 回调函数的执行范围
								fn : function() {
								} // 更新完毕后调用回调函数

							});

					Ext.Ajax.request({
								url : config.url || 'mq',
								params : config.params,
								method : 'post',
								success : function(response, options) {
									var json = Ext.JSON.decode(response.responseText || '{}');
									if (config.callback && json && json.session) {
										config.callback(json);
									} else {
										Ext.create('App.login.LoginWindow').show();
									}
									wait.close();
								},
								failure : function(response, options) {
									wait.close();
									Ext.MessageBox.alert('错误', '服务器发生错误！');
								}
							});
					return false;
				},
				getLocalStorage : function() {
					try {
						if (!!window.localStorage)
							return window.localStorage;
					} catch (e) {
						return undefined;
					}
				},
				getCustomEditors : function(name, cfg) {
					var editor;
					var _cfg = {};
					switch (name) {
						case 'bool' :
							_cfg = {
								editable : false,
								displayField : "name",
								valueField : "name",
								mode : "local",
								triggerAction : "all",
								store : new Ext.data.SimpleStore({
											fields : ['name', 'val'],
											data : [['是', '1'], ['否', '0']]
										})
							};
							editor = Ext.create('Ext.form.ComboBox', Ext.apply(_cfg, cfg || {}));
							break;
						case 'date' :
							_cfg = {
								format : 'Y-m-d'
							};
							editor = Ext.create('Ext.form.DateField', Ext.apply(_cfg, cfg || {}));
							break;
						case 'time' :
							editor = Ext.create('Ext.form.TimeField', Ext.apply(_cfg, cfg || {}));
							break;
						case 'number' :
							editor = Ext.create('Ext.form.field.Number', Ext.apply(_cfg, cfg || {}));
							break;
						case 'file' :
							_cfg = {
								xtype : 'filefield',
								name : 'photo',
								fieldLabel : 'Photo',
								labelWidth : 50,
								msgTarget : 'side',
								allowBlank : false,
								anchor : '100%',
								buttonText : 'Select Photo...'
							};
							editor = Ext.create('Ext.form.field.File', Ext.apply(_cfg, cfg || {}));
							break;
						default :
							editor = Ext.create('Ext.form.field.Text', Ext.apply(_cfg, cfg || {}));
							break;
					}
					return editor;
				},
				accDiv : function(arg1, arg2) {// 除法函数
					var t1 = 0, t2 = 0, r1, r2;
					try {
						t1 = arg1.toString().split(".")[1].length;
					} catch (e) {
					}
					try {
						t2 = arg2.toString().split(".")[1].length;
					} catch (e) {
					}
					with (Math) {
						r1 = Number(arg1.toString().replace(".", ""));
						r2 = Number(arg2.toString().replace(".", ""));
						return (r1 / r2) * pow(10, t2 - t1);
					}
				},
				accMul : function(arg1, arg2) {// 乘法函数
					var m = 0, s1 = arg1.toString(), s2 = arg2.toString();
					try {
						m += s1.split(".")[1].length;
					} catch (e) {
					}
					try {
						m += s2.split(".")[1].length;
					} catch (e) {
					}
					return Number(s1.replace(".", "")) * Number(s2.replace(".", "")) / Math.pow(10, m);
				},
				accAdd : function(arg1, arg2) {// 加法函数
					var r1, r2, m;
					try {
						r1 = arg1.toString().split(".")[1].length;
					} catch (e) {
						r1 = 0;
					}
					try {
						r2 = arg2.toString().split(".")[1].length;
					} catch (e) {
						r2 = 0;
					}
					m = Math.pow(10, Math.max(r1, r2));
					return (arg1 * m + arg2 * m) / m;
				},
				subtr : function(arg1, arg2) {// 减法函数
					var r1, r2, m, n;
					try {
						r1 = arg1.toString().split(".")[1].length;
					} catch (e) {
						r1 = 0;
					}
					try {
						r2 = arg2.toString().split(".")[1].length;
					} catch (e) {
						r2 = 0;
					}
					m = Math.pow(10, Math.max(r1, r2));
					// last modify by deeka
					// 动态控制精度长度
					n = (r1 >= r2) ? r1 : r2;
					return ((arg1 * m - arg2 * m) / m).toFixed(n);
				}
			};
		});