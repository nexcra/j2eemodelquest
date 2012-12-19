Ext.define('com.ad.report.ReportDesignLeftPanel', {
			extend : 'Ext.panel.Panel',
			// layout : 'vbox',
			border : 0,
			reportDataid : null,
			initComponent : function() {
				var me = this;
				me.autoScroll = true;
				var reportSrcCombo = Ext.create('Ext.form.field.ComboBox', {
							fieldLabel : '数据',
							labelAlign : 'right',
							labelWidth : 30,
							flex : 1,
							displayField : 'name',
							valueField : 'id',
							editable : 0,
							listConfig : {
								loadingText : '加载中...',
								emptyText : '无数据...',
								getInnerTpl : function() {
									return '({id}){name}';
								}
							},
							store : getStoreByName('auto-store-combo', {
										name : 'auto-store-combo',
										proxy : {
											extraParams : {
												$actionid : 1004,
												$dataid : -6008
											}
										},
										fields : ['id', 'name']
									}),
							listeners : {
								change : function(_this, newValue, oldValue, eOpts) {
									me.reportDataid = newValue;
									me.items.each(function(item) {
												item.getStore().removeAll();
											});

									var _store = me.getComponent('clmnsPanel').getStore();
									_store.getProxy().extraParams.srcid = me.reportDataid;
									_store.load();
								}
							}
						});

				me.tbar = [{
							xtype : 'button',
							iconCls : Ext.baseCSSPrefix + 'tbar-loading',
							tooltip : '刷新',
							scope : me,
							itemId : 'refershBtn',
							handler : function() {
								reportSrcCombo.getStore().load();
								this.items.each(function(item) {
											item.getStore().removeAll();
										});

							}
						}, reportSrcCombo];

				Ext.define('ReportSrcClmn', {
							extend : 'Ext.data.Model',
							fields : ['id', 'srcid', 'clmnname', 'labelname', 'datatype', 'rmk'],
							idProperty : 'id'
						});
				Ext.define('ReportSrcWhere', {
							extend : 'Ext.data.Model',
							fields : ['id', 'srcid', 'clmnname', 'labelname', 'datatype', 'rmk', '', 'expre', 'value'],
							idProperty : 'id'
						});
				var clmnsPanelStore = getStoreByName('auto-store', {
							name : 'auto-store',
							autoLoad : false,
							remoteSort : false,
							proxy : {
								extraParams : {
									$actionid : 1004,
									$dataid : -6001,
									srcid : 100
								}
							},
							model : 'ReportSrcClmn'
						});
				var rowsPanelStore = Ext.create('Ext.data.ArrayStore', {
							model : 'ReportSrcClmn',
							data : []
						});
				var colsPanelStore = Ext.create('Ext.data.ArrayStore', {
							model : 'ReportSrcClmn',
							data : []
						});
				var groupbyPanelStore = Ext.create('Ext.data.ArrayStore', {
							model : 'ReportSrcClmn',
							data : []
						});
				var groupbyPanelStore = Ext.create('Ext.data.ArrayStore', {
							model : 'ReportSrcWhere',
							data : []
						});

				me.items = [

				{
							xtype : 'gridpanel',
							title : '字段',
							height : 200,
							itemId : 'clmnsPanel',
							autoScroll : 1,
							autoHeight : 1,
							collapsible : 1,
							animCollapse : 1,
							border : 0,
							deleteRecordAble : 0,
							insertRecordAble : 0,
							style : 'border-bottom: 1px solid #8db2e3;',
							columns : [{
										header : '名称',
										dataIndex : 'labelname'
									}, {
										header : '说明',
										dataIndex : 'rmk',
										flex : 1
									}],
							store : clmnsPanelStore,
							listeners : {
								beforeitemmousedown : function(view, record, item, index, e, eOpts) {
									me.mousedownHandler(view, record);
								},
								beforeitemmouseup : function(view, record, item, index, e, eOpts) {
									me.mouseupHandler(view);
								},
								beforecontainermouseup : function(view, e, eOpts) {
									me.mouseupHandler(view);
								}
							}
						}, {
							xtype : 'gridpanel',
							itemId : 'colPanel',
							title : '列',
							height : 100,
							autoScroll : 1,
							autoHeight : 1,
							collapsible : 1,
							animCollapse : 1,
							border : 0,
							style : 'border-bottom: 1px solid #8db2e3;',
							store : rowsPanelStore,
							deleteRecordAble : 1,
							insertRecordAble : 1,
							columns : [{
										header : '名称',
										dataIndex : 'labelname'
									}, {
										header : '说明',
										dataIndex : 'rmk',
										flex : 1
									}],
							listeners : {
								beforeitemmousedown : function(view, record, item, index, e, eOpts) {
									me.mousedownHandler(view, record);
								},
								beforeitemmouseup : function(view, record, item, index, e, eOpts) {
									me.mouseupHandler(view);
								},
								beforecontainermouseup : function(view, e, eOpts) {
									me.mouseupHandler(view);
								}
							}
						}, {
							xtype : 'gridpanel',
							itemId : 'rowPanel',
							title : '行',
							height : 100,
							autoScroll : 1,
							autoHeight : 1,
							collapsible : 1,
							animCollapse : 1,
							border : 0,
							deleteRecordAble : 1,
							insertRecordAble : 1,
							style : 'border-bottom: 1px solid #8db2e3;',
							store : colsPanelStore,
							columns : [{
										header : '名称',
										dataIndex : 'labelname'
									}, {
										header : '说明',
										dataIndex : 'rmk',
										flex : 1
									}],
							listeners : {
								beforeitemmousedown : function(view, record, item, index, e, eOpts) {
									me.mousedownHandler(view, record);
								},
								beforeitemmouseup : function(view, record, item, index, e, eOpts) {
									me.mouseupHandler(view);
								},
								beforecontainermouseup : function(view, e, eOpts) {
									me.mouseupHandler(view);
								}
							}
						}, {
							xtype : 'gridpanel',
							itemId : 'groupPanel',
							title : '汇总',
							height : 100,
							autoScroll : 1,
							autoHeight : 1,
							collapsible : 1,
							animCollapse : 1,
							border : 0,
							deleteRecordAble : 1,
							insertRecordAble : 1,
							style : 'border-bottom: 1px solid #8db2e3;',
							store : groupbyPanelStore,
							columns : [{
										header : '名称',
										dataIndex : 'labelname'
									}, {
										header : '说明',
										dataIndex : 'rmk',
										flex : 1
									}],
							listeners : {
								beforeitemmousedown : function(view, record, item, index, e, eOpts) {
									me.mousedownHandler(view, record);
								},
								beforeitemmouseup : function(view, record, item, index, e, eOpts) {
									me.mouseupHandler(view);
								},
								beforecontainermouseup : function(view, e, eOpts) {
									me.mouseupHandler(view);
								}
							}
						}, {
							xtype : 'gridpanel',
							itemId : 'wherePanel',
							title : '筛选',
							height : 100,
							autoScroll : 1,
							autoHeight : 1,
							collapsible : 1,
							animCollapse : 1,
							border : 0,
							deleteRecordAble : 1,
							insertRecordAble : 1,
							style : 'border-bottom: 1px solid #8db2e3;',
							columns : [{
										header : '名称',
										dataIndex : 'labelname'
									}, {
										header : '运算符',
										dataIndex : 'expre'
									}, {
										header : '值',
										dataIndex : 'value',
										flex : 1
									}],
							listeners : {
								beforeitemmousedown : function(view, record, item, index, e, eOpts) {
									me.mousedownHandler(view, record);
								},
								beforeitemmouseup : function(view, record, item, index, e, eOpts) {
									me.addWhereHandler(view, me.moveRecord);
								},
								beforecontainermouseup : function(view, e, eOpts) {
									me.addWhereHandler(view, me.moveRecord);

								}
							}
						}];
				me.callParent();
			},
			mousedownHandler : function(view, record) {
				this.moveRecord = record;
				this.moveRecordView = view;
			},
			mouseupHandler : function(view) {
				if (!Ext.isEmpty(this.moveRecord)) {
					if (this.moveRecordView.getItemId() !== view.getItemId()) {
						if (this.moveRecordView.panel.deleteRecordAble || view.panel.insertRecordAble) {
							if (this.moveRecordView.panel.deleteRecordAble)
								this.moveRecordView.getStore().remove(this.moveRecord);
							if (view.panel.insertRecordAble) {
								view.getStore().remove(this.moveRecord);
								view.getStore().add(this.moveRecord);
							}

						}
						this.preview();
					}
					this.moveRecordView = null;
					this.moveRecord = null;
				}
			},
			addWhereHandler : function(view, record) {
				if (Ext.isEmpty(this.moveRecord)) {
					return;
				}
				var me = this;
				var _msg = Ext.create('Ext.form.field.Display', {
							value : ':)',
							style : {
								color : 'red'
							},
							flex : 1
						});
				var valType = 'textfield';
				var expreDatas = [{
							"value" : "="
						}, {
							"value" : "为空"
						}, {
							"value" : "非空"
						}, {
							"value" : "模糊"
						}, {
							"value" : "左模糊"
						}, {
							"value" : "右模糊"
						}];
				switch (record.get('datatype')) {
					case 'NUMBER' :
						valType = 'numberfield';
						expreDatas = [{
									"value" : "="
								}, {
									"value" : ">"
								}, {
									"value" : ">="
								}, {
									"value" : "<"
								}, {
									"value" : "<="
								}, {
									"value" : "为空"
								}, {
									"value" : "非空"
								}];
						break;
					case 'DATE' :
						valType = 'datefield';
						expreDatas = [{
									"value" : "="
								}, {
									"value" : ">"
								}, {
									"value" : ">="
								}, {
									"value" : "<"
								}, {
									"value" : "<="
								}, {
									"value" : "为空"
								}, {
									"value" : "非空"
								}];
						break;
				}

				var win = Ext.create('Ext.window.Window', {
							title : '请录入条件',
							height : 150,
							width : 320,
							modal : 1,
							resizable : 0,
							// layout : 'fit',
							items : [{
										xtype : 'displayfield',
										labelAlign : 'right',
										labelWidth : 50,
										fieldLabel : '名称',
										value : record.get('labelname')
									}, {
										xtype : 'combo',
										labelAlign : 'right',
										fieldLabel : '条件',
										labelWidth : 50,
										itemId : 'expre',
										allowBlank : 0,
										store : Ext.create('Ext.data.Store', {
													fields : ['value'],
													data : expreDatas
												}),
										queryMode : 'local',
										editable : 0,
										displayField : 'value',
										valueField : 'value'
									}, {
										xtype : valType,
										labelAlign : 'right',
										labelWidth : 50,
										fieldLabel : '内容',
										itemId : 'value',
										value : null,
										width : 300
									}],
							dockedItems : [{
										xtype : 'toolbar',
										dock : 'bottom',
										ui : 'footer',
										// defaults : {
										// minWidth : minButtonWidth
										// },
										items : [_msg, {
													xtype : 'button',
													text : '确定',
													handler : function() {
														var expreCmp = win.getComponent('expre');
														var valueCmp = win.getComponent('value');

														if (Ext.isEmpty(expreCmp.getValue())) {
															_msg.setValue('请选择条件!');
															expreCmp.focus();
															return;
														}
														if (Ext.isEmpty(valueCmp.getValue()) && !(expreCmp.getValue() === '为空' || expreCmp.getValue() === '为空')) {
															_msg.setValue('请录入内容!');
															valueCmp.focus();
															return;
														}

														me.moveRecord = Ext.create('ReportSrcWhere', {
																	expre : expreCmp.getValue(),
																	value : valueCmp.getValue(),
																	id : Ext.id(),
																	clmnname : record.get('clmnname'),
																	labelname : record.get('labelname'),
																	datatype : record.get('datatype'),
																	rmk : record.get('rmk')
																});

														me.mouseupHandler(view);
														win.destroy();
													}
												}, {
													text : '取消',
													handler : function() {
														win.destroy();
													}
												}]
									}]
						});
				win.show();
			},
			preview : function() {
				var me = this;
				var colpanel = me.getComponent('colPanel');
				var rowpanel = me.getComponent('rowPanel');
				var grouppanel = me.getComponent('groupPanel');
				var wherepanel = me.getComponent('wherePanel');

				var cfg = {
					dataid : me.reportDataid
				};
				// clmnname ,datatype
				var cols = [];
				colpanel.getStore().each(function(record) {
							cols.push({
										clmnname : record.get('clmnname'),
										datatype : record.get('datatype'),
										labelname : record.get('labelname')
									});
						});
				Ext.apply(cfg, {
							cols : cols
						});

				var rows = [];
				rowpanel.getStore().each(function(record) {
							rows.push({
										clmnname : record.get('clmnname'),
										datatype : record.get('datatype'),
										labelname : record.get('labelname')
									});
						});
				Ext.apply(cfg, {
							rows : rows
						});

				var groups = [];
				grouppanel.getStore().each(function(record) {
							groups.push({
										clmnname : record.get('clmnname'),
										datatype : record.get('datatype'),
										labelname : record.get('labelname')
									});
						});
				Ext.apply(cfg, {
							groups : groups
						});
				var wheres = [];
				wherepanel.getStore().each(function(record) {
							wheres.push({
										clmnname : record.get('clmnname'),
										datatype : record.get('datatype'),
										expre : record.get('expre'),
										value : record.get('value')
									});
						});
				Ext.apply(cfg, {
							wheres : wheres
						});
				// data
				me.up().getComponent('reportDesignContent').repaint(cfg);

			}
		});