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
			layout : 'anchor',
			fieldDefaults : {
				labelAlign : 'right',
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
			doRefresh : function(gridView) {
				var me = this;
				var gridView = me.input.grid.oView;
				gridView.store.load();
				gridView.getSelectionModel().clearSelections();
			},
			showMsg : function(msg, type) {
				var m = this.msgComp;
				if (type === 0)
					m.setFieldStyle('color:red');
				else
					m.setFieldStyle('color:#3892D3');
				m.setValue(msg);
			},
			afterSubmit : function() {
				return false;
			},
			initComponent : function() {
				var me = this;
				var _cfg = me.input.cfg;
				var _dataid = me.input.dataid;
				var _auth = me.input.auth;
				var _data = me.input.data;
				me.msgComp = Ext.create('Ext.form.field.Display', {
							flex : 1,
							fieldStyle : 'color:#3892D3',
							value : ':-)'
						});

				// me.layout = 'border';

				// me.items = [{
				// region : 'north',
				// bodyStyle : 'padding:0 0 0 10px;background-color:yellow;',
				// height : 20,
				// itemId : 'msgpanel',
				// items : {
				// itemId : 'msgbox',
				// xtype : 'displayfield',
				// value : _cfg.form.msg ? _cfg.form.msg : ':-)'
				// }
				// }, {
				// region : 'center',
				// layout : 'anchor',
				// border : 0,
				// items : me.getFormItems(me.input)
				// }];
				me.items = me.getFormItems(me.input);
				Ext.apply(me, _cfg.form || {});
				me.buttons = [me.msgComp];
				var saveBtnState = _cfg.form.saveBtn || false;

				if (me.input.grid.oRecord) { // 修改
					if (((_auth & 4) === 4) || ((_auth & 1) === 1) || saveBtnState) {
/*
						me.buttons.push({
									text : '保存并继续修改',
									handler : function() {
										me.doSave(2);
									}
								});*/
						me.buttons.push({
									text : '保存并关闭',
									handler : function() {
										me.doSave(0);
									}
								});
					}
				} else {

					if (((_auth & 4) === 4) || ((_auth & 1) === 1) || saveBtnState) {

						me.buttons.push({
									text : '保存并关闭',
									handler : function() {
										me.doSave(0);
									}
								});
					}

					if (((_auth & 1) === 1) || saveBtnState) {

						me.buttons.push({
									text : '保存并新增',
									handler : function() {
										me.doSave(1);
									}
								});
					}
				}

				me.buttons.push({
							text : '关闭',
							itemId : 'formCloseBtn',
							handler : function() {
								me.doRefresh();
								me.up('window').destroy();
							}
						});

				me.callParent();
			},
			doSave : function(type) {
				var me = this;
				var form = me.getForm();
				if (!form.isValid())
					return;
				var _extraParams = Ext.clone(me.input.grid.oView.getStore().getProxy().extraParams);
				var _cfg = me.input.cfg;
				var insertActionId = _cfg.form.insert_actionid || 1003;
				var updateActionId = _cfg.form.update_actionid || 1001;
				Ext.apply(_extraParams, {
							$actionid : me.input.grid.oRecord ? updateActionId : insertActionId,
							$dataid : me.input.dataid
						});
				// var mb = me.getComponent('msgpanel').getComponent('msgbox');
				// mb.setFieldStyle({
				// color : ''
				// });
				form.submit({
							waitTitle : '请稍候',
							waitMsg : '处理中...',
							url : 'mq',
							params : _extraParams,
							success : function(form, action) {
								var rtn = Ext.JSON.decode(action.response.responseText || '{}');
								if (rtn.session) {
									// mb.setValue(rtn.message || '处理成功！');

									switch (type) {
										case 0 :
											me.showMsg(rtn.message || '处理成功！');
											me.doRefresh();
											me.up('window').destroy();
											break;
										case 1 :
											me.doRefresh();
											if (!me.afterSubmit())
												form.reset();
											me.showMsg(rtn.message || '1条数据保存成功，请继续录入!');
											/*
										case 2 :
											// form.reset();
											var gridpanel = me.input.grid.oView;
											// var currectRecord =
											// me.input.grid.oRecord;
											var store = gridpanel.store;
											var record = form.getRecord();
											record.commit();
											console.log(gridpanel);
											console.log(store);
											store.findBy(function(recd ,idx){
												
											
											});
//											var selectIdx = store.getById(currectRecord.getId());
//											console.log(selectIdx);
											if (!me.afterSubmit()) {
												//												

											}

											me.showMsg(rtn.message || '1条数据保存成功，请继续修改下一条!');
											// mb.setValue( );
											break;*/
									}
								} else {
									me.showMsg(rtn.message || '登录信息丢失 ，请重新登录!', 0);
								}
							},
							failure : function(form, action) {
								// mb.setFieldStyle({
								// color : 'red'
								// });
								switch (action.failureType) {
									case Ext.form.action.Action.CONNECT_FAILURE :
										me.showMsg('错误:操作无法完成，请检查你的数据项!', 0);
										break;
									case Ext.form.action.Action.SERVER_INVALID :
										me.showMsg('错误:操作无法完成，请检查你的数据项!!', 0);
										break;
									default :
										me.showMsg('错误:操作无法完成，请检查你的数据项!!!', 0);
										break;
								}
							}
						});
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