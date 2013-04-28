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
// type: -1/0->查询 ;1->新增; 2->删除 ; 4->编辑, 8->编辑[只读]
//
//

Ext.define('com.ad.mq.DefaultEditGrid', {
			extend : 'Ext.grid.Panel',
			autoScroll : true,
			border : false,
			// stateful : true,
			// stateId : 'DataObjectGrid',
			// loadMask : true,
			emptyText : '没有数据',
			viewConfig : {
				stripeRows : true,
				loadMask : true
			},
			config : {
				input : null,
				_localcfgToken : null,
				_localcfgName : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				var _cfg = me.input.cfg; // mq$object.cfg
				var _dataid = me.input.dataid; // 数据源编号
				var _auth = me.input.auth; // 权限
				var _data = me.input.data; // mq$element
				if (_auth === -1)
					_auth = 0;
				me.input.selectionHook = [];

				Ext.apply(me, _cfg.grid || {});

				var filtersFeature = Ext.create('Ext.ux.grid.FiltersFeature', {
							encode : true,
							local : false
						});

				me.features = [filtersFeature];

				if ((_auth & 4) === 4) {
					var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
								saveBtnText : '保存',
								cancelBtnText : '取消',
								errorsText : '错误',
								dirtyText : '请先保存修改！',
								clicksToMoveEditor : 1,
								autoCancel : false,
								pluginId : 'rowEditing',
								listeners : {
									edit : function(editor, context, eOpts) {
										me.doSave(editor, context, eOpts);
									},
									validateedit : function(editor, context, eOpts) {
										me.doValidateedit(editor, context, eOpts);
									}
								}
							});
					me.plugins = [rowEditing];
				}
				var store = Ext.create('App.store.JsonStore', me.parseStoreCfg(_cfg, _data, _dataid));
				me.store = store;
				me.columns = me.parseClmns(_data);
				me.initMenuBar(_cfg, _auth, _dataid);

				me.listeners = Ext.apply(me.listeners || {}, {
							columnhide : function(ct, column, eOpts) {
								var localcfg = me.getLocalCfg();
								if (!localcfg)
									return;
								var datas = localcfg.data;
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
								var localcfg = me.getLocalCfg();
								if (!localcfg)
									return;
								var datas = localcfg.data;
								for (var i = 0, len = datas.length; i < len; i++) {
									if (datas[i].id === column.eleid) {
										datas[i].gridshow = true;
										me.setLocalCfg(Ext.JSON.encode(localcfg));
										return;
									}
								}
							}
						});
				me.callParent();
			},
			doRefresh : function() {
				this.getStore().load();
				this.getSelectionModel().clearSelections();
			},
			doValidateedit : function(editor, context, eOpts) {
				
				
//				if (hasChange) {
//					var newData = Ext.clone(context.record.data);
//					Ext.apply(newData, data);
//					context.originalValues = newData;
//				}else{
//					context.originalValues = context.record.data;
//				}

			},
			doSave : function(editor, context) {
				var me = this;
				var dirty = context.record.dirty;
				if (!dirty)
					return;
				if (!me.idProperty) {
					Ext.Msg.alert('提醒', '需要指定一个主键字段！');
					return;
				}

				
				var columns = editor.grid.columns;
				var data = context.record.data;
			    var hasChange = false;
				Ext.Array.each(columns, function(colmn, index, countriesItSelf) {
							var field = colmn.field;
							if (field.$className === 'Ext.form.field.ComboBox' && field.store.aliasFields) {
								if (field.getValue() !== field.getRawValue()) {
									data[field.store.aliasFields[0]] = field.getValue();
									data[field.store.aliasFields[1]] = field.getRawValue();
									 hasChange = true;
									 console.log(field.getValue());
								}

							}
						});
				
				
				var params = Ext.clone(me.getStore().getProxy().extraParams);
				var insertActionId = me.input.cfg.form.insert_actionid || 1003;
				var updateActionId = me.input.cfg.form.update_actionid || 1001;
				var isNew = false;
				if (me.idProperty.constructor == String) {
					if (!context.record.get(me.idProperty)) {
						isNew = true;
					}
				} else if (me.idProperty.constructor == Array) {
					for (var i = 0, len = me.idProperty.length; i < len; i++) {
						if (!context.record.get(me.idProperty[i])) {
							isNew = true;
							break;
						}
					}
				}
				Ext.apply(params, {
							$actionid : !isNew ? updateActionId : insertActionId,
							$dataid : me.input.dataid
						});
				Ext.apply(params, data);
				com.ad.ajax({
							params : params,
							callback : function(returndata) {
								if (returndata.data) {
									if (isNew) {
										me.doRefresh();
									} else {
										context.record.commit();
									}

									// 
								} else {
									Ext.Msg.alert('提醒', '数据保存失败！');
								}

							}
						});

			},
			doAdd : function() {
				var me = this;
				var params = Ext.clone(me.getStore().getProxy().extraParams);
				var rowEditing = me.getPlugin('rowEditing');
				// if (!rowEditing)
				// return;
				rowEditing.cancelEdit();
				// Create a model instance
				var r = Ext.create(me.getStore().model, params);
				me.getStore().insert(0, r);
				rowEditing.startEdit(0, 0);
			},
			doDelete : function() {
				var me = this;
				var selection = me.getView().getSelectionModel().getSelection()[0];
				var deleteActionId = me.input.cfg.grid.delete_actionid || 1002;
				if (selection) {
					if (!me.idProperty) {
						Ext.Msg.alert('提醒', '需要指定一个主键字段，请联系管理员！');
						return;
					}
					var params = {
						$actionid : deleteActionId,
						$dataid : me.input.dataid
					};
					if (me.idProperty.constructor == String) {
						if (!selection.get(me.idProperty)) {
							Ext.Msg.alert('错误', '定义的主键【' + me.idProperty + '】没有找到值！');
							return;
						}
						Ext.apply(params, Ext.JSON.decode('{' + me.idProperty + ':' + selection.get(me.idProperty) + '}') || {});

					} else if (me.idProperty.constructor == Array) {
						for (var i = 0, len = me.idProperty.length; i < len; i++) {
							if (!selection.get(me.idProperty[i])) {
								Ext.Msg.alert('错误', '定义的主键【' + me.idProperty[i] + '】没有找到值！');
								return;
							}
							Ext.apply(params, Ext.JSON.decode('{' + me.idProperty[i] + ':' + selection.get(me.idProperty[i]) + '}') || {});
						}
					} else {
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
														me.store.remove(selection);
													else
														Ext.Msg.alert('删除数据失败！');
												}
											});

								}
							});
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
			parseStoreCfg : function(cfg, data, dataid) {
				var me = this;
				var fields = me.parseFields(data);
				var modelName = 'Model_' + dataid;
				Ext.define(modelName, {
							extend : 'Ext.data.Model',
							fields : fields,
							idProperty : Ext.typeOf(me.idProperty) === 'string' ? me.idProperty : null
						});

				var queryActionId = cfg.grid.select_actionid || 1000;

				var params = {
					$actionid : queryActionId
				};

				Ext.apply(params, cfg.params || {});
				Ext.apply(params, {
							$dataid : dataid
						});

				var storeCfg = getBaseStoreCfg();
				Ext.apply(storeCfg.proxy, {
							extraParams : params
						});
				Ext.apply(storeCfg, {
							model : modelName
						});
				Ext.apply(storeCfg, cfg.store || {});
				Ext.apply(storeCfg, {
							filterOnLoad : false,
							pruneModifiedRecords : true,
							autoLoad : false
						});
				return storeCfg;

			},

			parseFields : function(data) {// 得到grid.fields
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
			parseClmns : function(data) {// 得到grid.columns
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
								header : value.fieldname,
								editor : {},
								eleid : value.id,
								gridindex : value.gridindex
							};
							if (value.columnattrs) {
								Ext.apply(column, Ext.JSON.decode(value.columnattrs) || {});
								if (column.renderer && column.renderer.name) {
									column.renderer = getRenderer(column.renderer.name, column.renderer);
								}
							}
							if (value.xtypeattrs) {
								var attrs = Ext.JSON.decode(value.xtypeattrs) || {};
								Ext.apply(column.editor, attrs.items || {});
								if (!column.editor.xtype) {
									column.editor.xtype = value.xtype;
								}
							}
							if (column.editor.store && column.editor.store.name) {
								column.editor.store = getStoreByName(column.editor.store.name, column.editor.store, null);
							}
							if (!column.xtype) {
								switch (value.ftype) {
									case 'float' :
									case 'int' :
										column.xtype = 'numbercolumn';
										break;
									case 'date' :
										column.xtype = 'datecolumn';
										break;
									case 'bool' :
										column.xtype = 'checkcolumn';
										break;
								}
							}

							Ext.apply(column, {
										hidden : !value.gridshow
									});
							columns.push(column);
						});
				return columns;
			},
			initMenuBar : function(_cfg, _auth, _dataid) {
				var me = this;
				var tbarItems = [{
							iconCls : Ext.baseCSSPrefix + 'tbar-loading',
							tooltip : '刷新[' + _dataid + ']',
							scope : this,
							itemId : '_grid_refersh',
							handler : me.doRefresh
						}];

				if ((_auth & 1) === 1) {
					tbarItems.push({
								iconCls : 'icon-add',
								tooltip : '添加',
								scope : this,
								itemId : '_grid_add',
								handler : me.doAdd
							});

				}

				if ((_auth & 2) === 2) {
					tbarItems.push({
								iconCls : 'icon-delete',
								tooltip : '删除',
								disabled : true,
								itemId : '_grid_delete',
								scope : this,
								handler : me.doDelete
							});
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
					me.rbar = {
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
					me.lbar = {
						xtype : 'toolbar',
						items : lbarItems
					};
				}

				var bbarItemClazz = _cfg.grid.bbarItems || [];
				var bbarItems = [];
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
							store : me.getStore(),
							items : bbarItems,
							displayInfo : true
						});
				me.getSelectionModel().on('selectionchange', me.onSelectChange, this);
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

			}
		});