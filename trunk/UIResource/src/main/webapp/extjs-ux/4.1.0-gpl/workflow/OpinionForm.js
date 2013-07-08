// author:YMQ
// version: 1.0.0
// create:2012-6-15
// 工作流申请的form
Ext.define('com.ad.workflow.OpinionForm', {
			extend : 'Ext.panel.Panel',
			// bodyStyle : 'padding:0 0 0 0',
			// autoScroll : true,
			layout : 'border',
			border : 0,
			config : {
				_document : null,
				_view : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;

				var formPanel = Ext.create('Ext.form.Panel', {
							title : '意见内容',
							// autoScroll : true,
							split : true,
							region : 'west',
							layout : 'fit',
							width : 400,
							border : 0,
							items : {
								xtype : 'textarea',
								name : 'msg',
								itemId : 'msg',
								allowBlank : me.allowBlank,
								value : me._document.msg,
								originalValue : me._document.msg
							},
							buttons : [{
										text : '保存',
										type : 'submit',
										handler : function() {
											var _form = this.up('form');
											var msgField = _form.down('#msg');
											var stepId = me._document.stepid;
											if (!_form.isValid())
												return;
											_form.getForm().submit({
														url : 'mq',
														params : {
															$actionid : 202,
															sid : stepId,
															msg : msgField.getValue()
														},
														success : function(form, action) {
															if (!action.result.session) {
																Ext.Msg.alert('警告', '登录信息过期 ，请重新登录 ！');
																return;
															}
															if (action.result.success) {
																me._document.msg = msgField.getValue();
																Ext.Msg.alert('提示', '保存成功！');
															}
															if (action.result.message)
																Ext.Msg.alert('提示', action.result.message);
															msgField.originalValue = msgField.getValue();
														},
														failure : function(form, action) {
															switch (action.failureType) {
																case Ext.form.action.Action.CLIENT_INVALID :
																	Ext.Msg.alert('错误', '请先填写意见内容！');
																	break;
																case Ext.form.action.Action.CONNECT_FAILURE :
																	Ext.Msg.alert('错误', '操作失败!');
																	break;
																case Ext.form.action.Action.SERVER_INVALID :
																	Ext.Msg.alert('错误', action.result.message);
															}
														}
													});
										}
									}]
						});

				var gridPanel = Ext.create('com.ad.mq.DefaultGridPanel', {
							title : '步骤意见列表',
							region : 'center',
							_dataid : 203,
							_extraParams : {
								did : me._document.id
							}
						});
				me.items = [formPanel, gridPanel];
				me.doFocus = function(){
					formPanel.down('#msg').focus(false ,100);
				}
				me.callParent();
			},
			beforeSubmit : function() {
				var me = this;
				var msgField = me.down('form').down('#msg');
				if (msgField.originalValue !== msgField.getValue()) {
					Ext.Msg.alert('提醒', '请先保存意见！');
					return false;
				}
				if (Ext.isEmpty(me.allowBlank))
					return true;

				if (!me.allowBlank && Ext.isEmpty(msgField.getValue())) {
					Ext.Msg.alert('提醒', '请填写意见！');
					return false;
				}
				return true;
			}

		});