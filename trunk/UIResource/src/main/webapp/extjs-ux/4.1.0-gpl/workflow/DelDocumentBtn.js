// author:YMQ
// version: 1.0.0
// create:2013-4-3
// 案件删除
Ext.define('com.ad.workflow.DelDocumentBtn', {
			extend : 'Ext.Button',
			config : {
				_grid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			tooltip : '删除选中案件',
			text : '删除',
			iconCls : 'icon-delete',
			disabled : true,
			itemId : 'com_ad_workflow_DelDocumentBtn',
			handler : function() {
				var me = this;
				var selection = me._grid.getSelectionModel().getSelection()[0];
				if (!selection)
					return;
				if (selection.get('nodetype')!='0'){
					Ext.Msg.alert('提醒','案件只有在开始节点才允许删除！');
					return ;
				}
				Ext.Msg.confirm('提醒', '确定要删除选中案件 ？', function(opt) {
							if (opt === 'yes') {
								com.ad.ajax({
											params : {
												$actionid : 204,
												did : selection.get('id')
											},
											callback : function(input) {
												if (!Ext.isEmpty(input.message)) {
													window.alert(input.message);
												} else {
													me._grid.getStore().load();
												}
											}
										});
							}
						});
			}
		});
