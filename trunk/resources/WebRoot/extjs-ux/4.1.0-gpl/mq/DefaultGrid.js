// author:YMQ
// version: 1.0.0

// input = {
// tree : { //来自树菜单的事件参数
// oView : view,
// oRecord : record,
// oItem : item,
// oIndex : index,
// oE : e
// },
// auth:auth,//权限值
// dataid:dataid//数据源编号
// cfg: cfg, // 数据源参数配制内容
// data: data //数据源在MQ$ELEMENT中的数据
// };

// type: -1/0->查询 ;1->新增; 2->删除 ; 4->编辑, 8->编辑[只读], 16->分组统计
//
//

Ext.define('com.ad.mq.DefaultGrid', {
			extend : 'Ext.grid.Panel',
			autoScroll : true,
			border : false,
			grouping : false,
			msgLabelField : null,
			msgType : {
				NOM : 0,
				WRN : 1,
				ERR : 2
			},
			// stateful : true,
			// stateId : 'DataObjectGrid',
			// loadMask : true,
			viewConfig : {
				stripeRows : true,
				loadMask : false
			},
			config : {
				input : null,
				_localcfgToken : null,
				_localcfgName : null,
				_localcfgAuthName : null
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
				if (_auth === -1)
					_auth = 0;
				me.input.selectionHook = [];

				Ext.apply(me, _cfg.grid || {});

				
				me.columns = me.getColumnsCfg(_data);
				me.features = [{
							ftype : 'filters',
							encode : true, 
							local : false
						}];
				var fields = me.getFieldsCfg(_data);

				var modelName = 'Model_' + _dataid;
				Ext.define(modelName, {
							extend : 'Ext.data.Model',
							fields : fields,
							idProperty : Ext.typeOf(me.idProperty) === 'string' ? me.idProperty : null
						});
				var queryActionId = _cfg.grid.select_actionid || 1000;

				var params = {
					$actionid : queryActionId
				};

				Ext.apply(params, _cfg.params || {});
				Ext.apply(params, {
							$dataid : _dataid
						});

				var storeCfg = getBaseStoreCfg();
				Ext.apply(storeCfg.proxy, {
							extraParams : params
						});
				Ext.apply(storeCfg, {
							model : modelName
						});
				Ext.apply(storeCfg, _cfg.store || {});
				Ext.apply(storeCfg, {
							filterOnLoad : false,
							pruneModifiedRecords : true,
							autoLoad : false
						});
				var store = Ext.create('App.store.JsonStore', storeCfg);
				me.store = store;

				var tbarItems = [{
							iconCls : Ext.baseCSSPrefix + 'tbar-loading',
							tooltip : '刷新[' + _dataid + ']',
							scope : me,
							itemId : '_grid_refersh',
							handler : me.doRefresh
						}];

				if ((_auth & 1) === 1) {
					tbarItems.push({
								iconCls : 'icon-add',
								tooltip : '添加',
								scope : me,
								itemId : '_grid_add',
								handler : me.onAddClick
							});

				}

				if ((_auth & 2) === 2) {
					tbarItems.push({
								iconCls : 'icon-delete',
								tooltip : '删除',
								disabled : true,
								itemId : '_grid_delete',
								scope : this,
								handler : me.onDeleteClick
							});
				}

				if ((_auth & 16) === 16) {
					tbarItems.push(Ext.create('com.ad.button.ReportBtn', {
								_grid : me
							}));
					me.enableLocking = true;
				}
				var tbarItemClazz = _cfg.grid.tbarItems || [];

				if (tbarItemClazz.length > 0) {
					var newObject;
					for (var i = 0, len = tbarItemClazz.length; i < len; i++) {

						if (tbarItemClazz[i].constructor === Object) {
							newObject = Ext.create(tbarItemClazz[i].name, {
										_grid : me
									});
							if (tbarItemClazz[i].selectionHook && tbarItemClazz[i].itemId && (!tbarItemClazz[i].auth || (_auth & tbarItemClazz[i].auth === tbarItemClazz[i].auth))) {
								me.input.selectionHook.push(tbarItemClazz[i].itemId);
							}
						} else {
							newObject = Ext.create(tbarItemClazz[i], {
										_grid : me
									});
						}
						tbarItems.push(newObject);
					}
				}
				if (tbarItems.length > 0) {
					me.tbar = {
						xtype : 'toolbar',
						items : tbarItems
					};
				}

				var rbarItemClazz = _cfg.grid.rbarItems || [];
				if (rbarItemClazz.length > 0) {
					var rbarItems = [];
					var newObject;
					for (var i = 0, len = rbarItemClazz.length; i < len; i++) {

						if (rbarItemClazz[i].constructor === Object) {
							newObject = Ext.create(rbarItemClazz[i].name, {
										_grid : me
									});
							if (rbarItemClazz[i].selectionHook && rbarItemClazz[i].itemId && (!rbarItemClazz[i].auth || (_auth & rbarItemClazz[i].auth === rbarItemClazz[i].auth))) {
								me.cfg.selectionHook.push(rbarItemClazz[i].itemId);
							}
						} else {
							newObject = Ext.create(rbarItemClazz[i], {
										_grid : me
									});
						}
						rbarItems.push(newObject);
					}
					this.rbar = {
						xtype : 'toolbar',
						items : rbarItems
					};
				}

				var lbarItemClazz = _cfg.grid.lbarItems || [];
				if (lbarItemClazz.length > 0) {
					var lbarItems = [];
					var newObject;
					for (var i = 0, len = lbarItemClazz.length; i < len; i++) {

						if (lbarItemClazz[i].constructor === Object) {
							newObject = Ext.create(lbarItemClazz[i].name, {
										_grid : me
									});
							if (lbarItemClazz[i].selectionHook && lbarItemClazz[i].itemId && (!lbarItemClazz[i].auth || (_auth & lbarItemClazz[i].auth === lbarItemClazz[i].auth))) {
								me.input.selectionHook.push(lbarItemClazz[i].itemId);
							}
						} else {
							newObject = Ext.create(lbarItemClazz[i], {
										_grid : me
									});
						}
						lbarItems.push(newObject);
					}
					this.lbar = {
						xtype : 'toolbar',
						items : lbarItems
					};
				}

				var bbarItemClazz = _cfg.grid.bbarItems || [];
				me.msgLabelField = Ext.create('Ext.form.field.Display', {
							value : ':)'
						});
				var bbarItems = [me.msgLabelField];
				if (bbarItemClazz.length > 0) {
					var newObject;
					for (var i = 0, len = bbarItemClazz.length; i < len; i++) {

						if (bbarItemClazz[i].constructor === Object) {
							newObject = Ext.create(bbarItemClazz[i].name, {
										_grid : me
									});
							if (bbarItemClazz[i].selectionHook && bbarItemClazz[i].itemId && (!bbarItemClazz[i].auth || (_auth & bbarItemClazz[i].auth === bbarItemClazz[i].auth))) {
								me.input.selectionHook.push(bbarItemClazz[i].itemId);
							}
						} else {
							newObject = Ext.create(bbarItemClazz[i], {
										_grid : me
									});
						}
						bbarItems.push(newObject);
					}
				}
				me.bbar = Ext.create('Ext.toolbar.Paging', {
							dock : 'bottom',
							store : store,
							items : bbarItems,
							displayInfo : true
						});

				me.getSelectionModel().on('selectionchange', me.onSelectChange, me);
				if (((_auth & 4) === 4) || ((_auth & 8) === 8)) {
					me.listeners = Ext.apply(me.listeners || {}, {
								itemdblclick : function(view, record, item, index, e, eOpts) {
									me.createWindow(view, record, item, index, e, eOpts);
								}
							});
				}

				me.listeners = Ext.apply(me.listeners || {}, {
							columnhide : function(ct, column, eOpts) {
								me.preGroup(me);
								var localcfg = me.getLocalCfg();
								if (!localcfg)
									return;
								var datas = localcfg.data;
								column.hidden = true;
								for (var i = 0, len = datas.length; i < len; i++) {
									if (datas[i].id === column.eleid) {
										datas[i].gridshow = false;
										me.setLocalCfg(Ext.JSON.encode(localcfg));
										return;
									}
								}

							},
							columnmove : function(ct, column, fromIdx, toIdx, eOpts) {
								var localcfg = me.getLocalCfg();

								if (!localcfg)
									return;
								var datas = localcfg.data, clmuns = ct.getGridColumns(true);

								for (var i = 0, len = clmuns.length; i < len; i++) {
									for (var j = 0, ln = datas.length; j < ln; j++) {
										if (datas[j].id === clmuns[i].eleid) {
											datas[j].gridindex = i;
										}
									}
								}
								me.setLocalCfg(Ext.JSON.encode(localcfg));

							},
							columnresize : function(ct, column, width, eOpts) {
								var localcfg = me.getLocalCfg();
								if (!localcfg)
									return;
								var datas = localcfg.data, attrs;
								for (var i = 0, len = datas.length; i < len; i++) {
									if (datas[i].id === column.eleid) {
										attrs = Ext.JSON.decode(datas[i].columnattrs);
										Ext.apply(attrs, {
													width : width
												});
										datas[i].columnattrs = Ext.JSON.encode(attrs);
										me.setLocalCfg(Ext.JSON.encode(localcfg));
										return;
									}
								}

							},
							columnshow : function(ct, column, eOpts) {
								me.preGroup(me);
								var localcfg = me.getLocalCfg();
								if (!localcfg)
									return;
								var datas = localcfg.data;
								column.hidden = false;
								for (var i = 0, len = datas.length; i < len; i++) {
									if (datas[i].id === column.eleid) {
										datas[i].gridshow = true;
										me.setLocalCfg(Ext.JSON.encode(localcfg));
										return;
									}
								}

							},
							lockcolumn : function(panel, column, eOpts) {
								me.preGroup(panel);
							},
							unlockcolumn : function(panel, column, eOpts) {
								me.preGroup(panel);
							}
						});
				
				me.callParent();
			},
			preGroup : function(panel) {
				var me = this;
				var _store = panel.getStore();
				// var isGroup = _store.remoteGroup;
				if (!me.grouping)
					return;
				me.doGroup(_store, panel);
			},
			doGroup : function(_store, _grid) {
				// _store.group('id');
				// _store.group(['id','name']);

				var me = this;
				var groupArr = [];
				var clmns = _grid.query('gridcolumn');
				var hasLocked = false;
				// me.getStore().clearGrouping();
				for (var i = 0, len = clmns.length; i < len; i++) {
					if (clmns[i].isHidden())
						continue;
					if (clmns[i].locked) {
						groupArr.push(clmns[i].dataIndex);
						hasLocked = true;
					} else {
						if (!(clmns[i].ftype === 'int' || clmns[i].ftype === 'integer' || clmns[i].ftype === 'number' || clmns[i].ftype === 'float')) {
							me.showMsg('统计项存在非数字列请锁住或隐藏！', me.msgType.ERR);
							return true;
						} else {
							switch (Ext.type(me.idProperty)) {
								case 'string' :
									if (clmns[i].dataIndex === me.idProperty) {
										groupArr.push('*' + clmns[i].dataIndex);
									}
									break;
								case 'array' :
									for (var j = 0, l = me.idProperty.length; i < l; i++) {
										if (clmns[i].dataIndex === me.idProperty[j]) {
											groupArr.push('*' + clmns[i].dataIndex);
											break;
										}
									}
									break;
								default :
									groupArr.push('_' + clmns[i].dataIndex);
									break;
							}

						}

					}
				}
				if (hasLocked && !Ext.isEmpty(groupArr)) {
					_store.group(groupArr);
					me.showMsg('分组统计完成！');
				}
			},
			getLocalCfg : function() {
				var me = this, localStorage = com.ad.getLocalStorage();
				if (localStorage) {
					return Ext.JSON.decode(localStorage.getItem(me._localcfgName));
				}
				return null;
			},
			setLocalCfg : function(cfg) {
				var me = this, localStorage = com.ad.getLocalStorage();
				if (localStorage) {
					localStorage.setItem(me._localcfgName, cfg);
				}
			},

			onSelectChange : function(selModel, selections) {
				if (this.down('#_grid_delete'))
					this.down('#_grid_delete').setDisabled(selections.length === 0);
				var btn = null;
				for (var i = 0, len = this.input.selectionHook.length; i < len; i++) {
					btn = this.down('#' + this.input.selectionHook[i]);
					if (btn)
						btn.setDisabled(selections.length === 0);
				}

			},
			doRefresh : function() {
				this.store.load();
			},
			onAddClick : function() {
				this.createWindow(this);
			},
			createWindow : function(view, record, item, index, eOpts) {
				var me = this;

				var formCfg = me.input;
				Ext.apply(formCfg, {
							grid : {
								oView : view,
								oRecord : record,
								oItem : item,
								oIndex : index,
								oOpts : eOpts
							}
						});

				var form = Ext.create('com.ad.mq.DefaultForm', {
							input : formCfg
						});
				if (record)
					form.loadRecord(record);
				var win = {
					title : '窗体',
					height : 400,
					width : 600,
					layout : 'fit',
					shadow : true,
					modal : true
				};

				Ext.apply(win, me.input.cfg.formWindow || {});
				Ext.apply(win, {
							items : form
						});

				Ext.create('Ext.window.Window', win).show();
			},
			onDeleteClick : function() {
				var me = this;
				var selection = me.getView().getSelectionModel().getSelection()[0];
				var deleteActionId = me.input.cfg.grid.delete_actionid || 1002;
				if (selection) {
					if (!me.idProperty)
						return;
					var params = {
						$actionid : deleteActionId,
						$dataid : me.input.dataid
					};
					switch (Ext.type(me.idProperty)) {
						case 'string' :
							if (!selection.get(me.idProperty)) {
								Ext.Msg.alert('错误', '定义的主键【' + me.idProperty + '】没有找到值！');
								return;
							}
							Ext.apply(params, Ext.JSON.decode('{' + me.idProperty + ':' + selection.get(me.idProperty) + '}') || {});
							break;
						case 'array' :
							for (var i = 0, len = me.idProperty.length; i < len; i++) {
								if (!selection.get(me.idProperty[i])) {
									Ext.Msg.alert('错误', '定义的主键【' + me.idProperty[i] + '】没有找到值！');
									return;
								}
								Ext.apply(params, Ext.JSON.decode('{' + me.idProperty[i] + ':' + selection.get(me.idProperty[i]) + '}') || {});
							}
							break;
						default :
							Ext.Msg.alert('错误', '定义的【idProperty = ' + me.idProperty + '】无效！');
							return;
					}

					Ext.Msg.confirm('提醒', '确定要删除选中的记录？', function(btn, text) {
								if (btn == 'yes') {
									com.ad.ajax({
												url : 'mq',
												params : params,
												callback : function(dd) {
													if (dd.data > 0)
														me.getStore().load();
													else
														Ext.Msg.alert('删除数据失败！');
												}
											});

								}
							});
				}
			},
			getFieldsCfg : function(data) {// 得到grid.fields
				if (!data)
					return null;

				var fields = [];
				var field;
				Ext.each(data, function(value) {
							field = {
								name : value.fieldvalue,
								type : value.ftype
							};
							if (value.ftypeattrs) {
								Ext.apply(field, Ext.JSON.decode(value.ftypeattrs) || {});
							}
							fields.push(field);
						});
				return fields;
			},
			getColumnsCfg : function(data) {// 得到grid.columns
				if (!data)
					return null;
				var me = this;
				data.sort(function(a, b) {
							if (a.gridindex < b.gridindex)
								return -1;
							if (a.gridindex > b.gridindex)
								return 1;
							return 0;
						});
				var columns = [];
				var column;
				Ext.each(data, function(value) {
							column = {
								dataIndex : value.fieldvalue,
								text : value.fieldname,
								eleid : value.id,
								gridindex : value.gridindex,
								ftype : value.ftype
							};
							if (value.columnattrs) {
								Ext.apply(column, Ext.JSON.decode(value.columnattrs) || {});
								if (column.renderer && column.renderer.name) {
									column.renderer = getRenderer(column.renderer.name, column.renderer);
								}
							}
							Ext.apply(column, {
										hidden : !value.gridshow
									});
							columns.push(column);
						});
				return columns;
			},
			setAddBtnDisabled : function(bool) {
				if (this.down('#_grid_add'))
					this.down('#_grid_add').setDisabled(bool);
			},
			setDelBtnDisabled : function(bool) {
				if (this.down('#_grid_delete'))
					this.down('#_grid_delete').setDisabled(bool);
			},
			setRefBtnDisabled : function(bool) {
				if (this.down('#_grid_refersh'))
					this.down('#_grid_refersh').setDisabled(bool);
				if (this.down('#refresh'))
					this.down('#refresh').setDisabled(bool);
			},
			getMsgLabel : function() {
				return this.msgLabelField;
			},
			showMsg : function(msg, type) {
				var me = this;
				var msgLabel = me.getMsgLabel();
				if (Ext.isEmpty(msgLabel))
					return;
				switch (type) {
					case me.msgType.NOM :
						msgLabel.setFieldStyle({
									color : ''
								});
						break;
					case me.msgType.WRN :
						msgLabel.setFieldStyle({
									color : 'yellow'
								});
						break;
					case me.msgType.ERR :
						msgLabel.setFieldStyle({
									color : 'red'
								});
						break;
					default :
						msgLabel.setFieldStyle({
									color : ''
								});
						break;
				}
				msgLabel.setValue(msg);
				// msgLabel.toggle(false);
			}
		});