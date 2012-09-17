// author:YMQ
// version: 1.0.0

// cfg = {
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

// type: 0->查询 ;1->新增; 2->删除 ; 4->编辑, 8->编辑[只读]
//
//

Ext.define('App.sys.MQGrid', {
			extend : 'Ext.grid.Panel',
			autoScroll : true,
			config : {
				cfg : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				var _cfg = me.cfg.cfg;
				me.cfg.selectionHook = [];
				// me.selectionHook = [];
				var dataid = me.cfg.dataid || 0;
				me.features = [Ext.create('Ext.ux.grid.FiltersFeature', {
							encode : true
						})];
				// this.id = "_grid_" + dataid;
				// this.itemId = "_grid_" + dataid;

				var fields = me.getFieldsCfg(me.cfg.data);
				this.columns = me.getColumnsCfg(me.cfg.data);
				var selectActionId = _cfg.grid.select_actionid || 1000;
				var params = {
					$actionid : selectActionId
				};
				Ext.apply(params, _cfg.params || {});
				Ext.apply(params, {
							$dataid : dataid
						});
				var storeId = '_store_' + dataid;
				var storeCfg = Ext.apply(getBaseStoreCfg(), {
							fields : fields,
							dataid : dataid,
							proxy : {
								extraParams : cfg.params
							}
						});
				Ext.apply(storeCfg , cfg.store || {});
				Ext.apply(storeCfg, {
							storeId : storeId,
							filterOnLoad : false,
							pruneModifiedRecords : true
						});
				Ext.apply(storeCfg, _cfg.store || {});

				var store = Ext.create('Ext.data.JsonStore', storeCfg);
				// store.clearFilter(true);
				// store.load({
				// params : {}
				// });
				me.store = store;

				var type = me.cfg.auth || _cfg.grid.auth || 0;
				// console.log(type);
				var tbarItems = [{
							iconCls : Ext.baseCSSPrefix + 'tbar-loading',
							tooltip : '刷新[P:' + dataid + ']',
							scope : this,
							handler : me.doRefresh
						}];

				if ((type & 1) === 1) {
					tbarItems.push({
								iconCls : 'icon-add',
								tooltip : '添加',
								scope : this,
								itemId : '_grid_add',
								handler : me.onAddClick
							});
				}

				if ((type & 2) === 2) {
					tbarItems.push({
								iconCls : 'icon-delete',
								tooltip : '删除',
								disabled : true,
								itemId : 'delete',
								scope : this,
								handler : me.onDeleteClick
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
							if (tbarItemClazz[i].selectionHook && tbarItemClazz[i].itemId) {
								me.cfg.selectionHook.push(tbarItemClazz[i].itemId);
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
					this.tbar = {
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
							if (rbarItemClazz[i].selectionHook && rbarItemClazz[i].itemId) {
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
							if (lbarItemClazz[i].selectionHook && lbarItemClazz[i].itemId) {
								me.cfg.selectionHook.push(lbarItemClazz[i].itemId);
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
				var bbarItems = [];
				if (bbarItemClazz.length > 0) {
					var newObject;
					for (var i = 0, len = bbarItemClazz.length; i < len; i++) {

						if (bbarItemClazz[i].constructor === Object) {
							newObject = Ext.create(bbarItemClazz[i].name, {
										_grid : me
									});
							if (bbarItemClazz[i].selectionHook && bbarItemClazz[i].itemId) {
								me.cfg.selectionHook.push(bbarItemClazz[i].itemId);
							}
						} else {
							newObject = Ext.create(bbarItemClazz[i], {
										_grid : me
									});
						}
						bbarItems.push(newObject);
					}
				}
				this.bbar = Ext.create('Ext.toolbar.Paging', {
							dock : 'bottom',
							store : store,
							items : bbarItems
						});

				Ext.apply(this, _cfg.grid || {});
				this.callParent();
				this.getSelectionModel().on('selectionchange', this.onSelectChange, this);
				if (((type & 4) === 4) || ((type & 8) === 8)) {
					this.listeners = {
						beforeitemdblclick : function(view, record, item, index, e, eOpts) {
							this.createWindow(view, record, item, index, e, eOpts);
						}
					};
				}
			},
			border : false,
			stateful : true,
			stateId : 'DataObjectGrid',
			loadMask : true,
			viewConfig : {
				stripeRows : true,
				loadMask : false
			},

			onSelectChange : function(selModel, selections) {
				if (this.down('#delete'))
					this.down('#delete').setDisabled(selections.length === 0);
				var btn = null;
				for (var i = 0, len = this.cfg.selectionHook.length; i < len; i++) {
					btn = this.down('#' + this.cfg.selectionHook[i]);
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
				var formCfg = {
					cfg : {
						grid : {
							oView : view,
							oRecord : record,
							oItem : item,
							oIndex : index,
							oOpts : eOpts
						},
						cfg : me.cfg.cfg,
						data : me.cfg.data,
						dataid : me.cfg.dataid,
						auth : me.cfg.auth
					}
				};

				var form = Ext.create('App.sys.DataObjectForm', formCfg);
				if (record)
					form.loadRecord(record);
				var win = {
					title : '窗体',
					height : 200,
					width : 400,
					layout : 'fit',
					shadow : false,
					modal : true
				};

				Ext.apply(win, me.cfg.cfg.formWindow || {});
				Ext.apply(win, {
							items : form
						});

				Ext.create('Ext.window.Window', win).show();
			},
			onDeleteClick : function() {
				var me = this;
				var selection = me.getView().getSelectionModel().getSelection()[0];
				var deleteActionId = me.cfg.cfg.grid.delete_actionid || 1002;
				if (selection) {
					if (!me.idProperty)
						return;
					var params = {
						$actionid : deleteActionId,
						$dataid : me.store.dataid
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
								text : value.fieldname
							};
							if (value.columnattrs) {
								Ext.apply(column, Ext.JSON.decode(value.columnattrs) || {});
								if (column.renderer && column.renderer.name) {
									column.renderer = me.getRenderer(column.renderer.name, column.renderer);
								}
							}
							Ext.apply(column, {
										hidden : !value.gridshow
									});
							columns.push(column);
						});
				return columns;
			}
		});