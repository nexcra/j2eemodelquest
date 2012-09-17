// author:YMQ
// version: 1.0.0

// input : {
// grid : { //Grid的事件参数
// oView : view,
// oRecord : record,
// oItem : item,
// oIndex : index,
// oOpts : eOpts
// },
// dataid:dataid,
// cfg : me.cfg.cfg, //基本数据配制
// data : me.cfg.data // 数据元素配制
// }

Ext.define('com.ad.mq.DefaultForm', {
			extend : 'Ext.form.Panel',
			bodyStyle : 'padding:5px 5px 0',
			autoScroll : true,
			// width : 600,
			fieldDefaults : {
				labelAlign : 'top',
				msgTarget : 'side'
			},
			config : {
				input : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				var _cfg = me.input.cfg;
				var _dataid = me.input.dataid;
				var _auth = me.input.auth;
				var _data = me.input.data;
				var _extraParams = Ext.clone(me.input.grid.oView.getStore().getProxy().extraParams);

				me.items = me.getFormItems(me.input);
				// var input = me.cfg;
				// var formCfg = _cfg.form || {};
				Ext.apply(me, _cfg.form || {});
				me.buttons = [];
				// var type = me.cfg.auth || input.cfg.grid.type || 0;

				if (((_auth & 4) === 4) || ((_auth & 1) === 1)) {

					var insertActionId = _cfg.form.insert_actionid || 1003;
					var updateActionId = _cfg.form.update_actionid || 1001;
					me.buttons.push({
								text : '保存',
								handler : function() {
									var form = me.getForm();
									if (!form.isValid())
										return;
									Ext.apply(_extraParams, {
												$actionid : me.input.grid.oRecord ? updateActionId : insertActionId,
												$dataid : _dataid
											});
									form.submit({
												waitTitle : '请稍候',
												waitMsg : '正在验证...',
												url : 'mq',
												params : _extraParams,
												success : function(form, action) {
													var rtn = Ext.JSON.decode(action.response.responseText || '{}');
													if (rtn.session) {
														if (me.input.grid.oRecord)
															me.input.grid.oView.store.load();
														else {
															me.input.grid.oView.store.load();
															me.up('window').destroy();
														}
													} else {
														Ext.Msg.alert('警告', rtn.message);
													}
												},
												failure : function(form, action) {
													me.setActive(false);
													switch (action.failureType) {
														case Ext.form.action.Action.CONNECT_FAILURE :
															Ext.Msg.alert('错误', '操作无法完成，请检查你的数据项!');
															break;
														case Ext.form.action.Action.SERVER_INVALID :
															Ext.Msg.alert('错误', '操作无法完成，请检查你的数据项!!');
															break;
														default :
															Ext.Msg.alert('错误', '操作无法完成，请检查你的数据项!!!');

													}
												}
											});
								}
							});
				}

				me.buttons.push({
							text : '关闭',
							handler : function() {
								me.up('window').destroy();
							}
						});

				me.callParent();
			},
			getFormItems : function(cfg) {// 创建 所有form元素
				if (!cfg.data)
					return null;
				var data = cfg.data;
				var record = cfg.grid.oRecord;
				data.sort(function(a, b) {
							if (a.formindex < b.formindex)
								return -1;
							if (a.formindex > b.formindex)
								return 1;
							return 0;
						});
				var items = [];
				var item;
				Ext.each(data, function(value) {
							item = {
								rowspan : 1,
								colspan : 1,
								xtype : 'fieldcontainer',
								layout : 'fit',
								items : {
									// labelWidth : 40,
									xtype : 'textfield',
									labelAlign : 'right',
									height : 25
								}
							};

							Ext.apply(item, Ext.JSON.decode(value.xtypeattrs || '{}') || {});
							if (Ext.isEmpty(item.items.fieldLabel))
								item.items.fieldLabel = value.fieldname;
							if (Ext.isEmpty(item.items.name))
								item.items.name = value.fieldvalue;

							if (item.items.store && item.items.store.name) {
								item.items.store = getStoreByName(item.items.store.name, item.items.store, record);
							}

							if (value.formshow) {
								item.items.xtype = value.xtype || 'textfield', items.push(item);

								if (value.vtype) {
									item.items.vtype = value.vtype;
									var string = '{' + value.vtype + ':' + (value.vtypeattrs || '{}') + '}';
									Ext.apply(item.items, Ext.JSON.decode(string) || {});

								}
							} else {
								item.items.xtype = 'hidden';
							}

							if (value.auth == '1') {
								item.items.readOnly = true;
							} else if (value.auth == '2') {
								item.items.xtype = 'hidden';
							}
						});
				return items;
			}

		});