// author:YMQ
// version: 1.0.0
// create:2012-6-15
// 工作流申请的form
Ext.define('com.ad.workflow.ApplyForm', {
			extend : 'Ext.form.Panel',
			bodyStyle : 'padding:5px 50px 0 0',
			autoScroll : true,
			layout : 'anchor',
			border : 0,
			fieldDefaults : {
				labelAlign : 'right',
				height : 25,
				labelWidth : 90,
				anchor : '100%',
				msgTarget : 'side'
			},
			initComponent : function() {
				var me = this;
				me.items = [{
							fieldLabel : '类型',
							xtype : 'combo',
							store : getStoreByName('auto-store-combo', {
										fields : ['id', 'name', 'description', 'status'],
										proxy : {
											extraParams : {
												$actionid : 1004,
												$dataid : 200
											}
										}
									}),
							displayField : 'name',
							valueField : 'id',
							editable : false,
							allowBlank : false,
							itemId : 'workflow',
							listeners : {
								change : function(field, newValue, oldValue, eOpts) {
									var record = field.findRecordByValue(newValue);
									me.down('#description').setValue(record.get('description'));
									me.down('#status').setValue(record.get('status') ? '有效' : '无效');
									if (record.get('status')) {
										me.down('#applyBtn').setDisabled(false);
									} else {
										me.down('#applyBtn').setDisabled(true);
									}
								}
							}
						}, {
							fieldLabel : '说明',
							xtype : 'displayfield',
							itemId : 'description'
						}, {
							fieldLabel : '状态',
							xtype : 'displayfield',
							itemId : 'status'
						}];
				me.callParent();
			},
			tbar : [{
						text : '申请',
						iconCls : 'application-business',
						tooltip : '申请',
						disabled : true,
						itemId : 'applyBtn',
						handler : function() {
							var wf = this.up('form').down('#workflow');
							Ext.Msg.confirm('业务申请', '确定要申请\"' + wf.getRawValue() + '[' + wf.getValue() + ']\"流程？', function(opt) {
										if (opt == 'no')
											return;
										com.ad.ajax({
													params : {
														$actionid : 200,
														wfid : wf.getValue()
													},
													callback : function(input) {
														if (input.message) {
															Ext.Msg.alert('警告', input.message);
														} else {
															Ext.create('com.ad.workflow.ApproveWindow', {
																		_document : input.data
																	}).show();
														}

													}
												});
									});

						}
					}]
		});