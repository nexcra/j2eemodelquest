Ext.define('com.ad.report.UserAuthTreePanel', {
			extend : 'Ext.tree.Panel',
			rootVisible : false,
			multiSelect : false,
			border : 0,
			rootVisible : false,
			root : {
				loaded : true,
				expanded : true
			},
			columns : [{
						xtype : 'treecolumn',
						text : '名称',
						sortable : true,
						flex : 0,
						dataIndex : 'text',
						width : 200
					}, {
						text : '说明',
						sortable : true,
						flex : 1,
						dataIndex : 'rmk'
					}],
			config : {
				_dataid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				me.store = Ext.create('Ext.data.TreeStore', {
							nodeParam : 'parentid',
							autoLoad : false,
							fields : ['id', 'text', 'parentid', 'rmk'],
							proxy : {
								type : 'ajax',
								url : 'mq',
								extraParams : {
									$actionid : 1004,
									$dataid : me._dataid
								},
								reader : {
									type : 'json',
									root : 'data'
								}
							},
							folderSort : true

						});
				me.callParent();
			},
			listeners : {
				checkchange : function(node, checked, eOpts) {
					var me = this;
					var params = Ext.clone(me.getStore().getProxy().extraParams);
					var nodes = node.childNodes;
					for (var i = 0, len = nodes.length; i < len; i++) {
						nodes[i].set('checked', checked);
					}

					Ext.apply(params, {
								$actionid: -6000,
								$dataid : 0,
								checked : checked,
								parentid : node.get('parentid'),
								nodeid : node.get('id').substr(1)
							});
					
					com.ad.ajax({
								params : params,
								callback : function(dd) {
									if (dd && dd.message==='ok'){
										
									}else{
										Ext.Msg.alert('数据没有保存成功！');
									}
								}
							});
				}
			}
		});
