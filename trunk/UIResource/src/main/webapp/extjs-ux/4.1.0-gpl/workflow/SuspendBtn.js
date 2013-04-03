// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 案件挂起
Ext.define('com.ad.workflow.SuspendBtn', {
			extend : 'Ext.Button',
			config : {
				_document : null,
				_window:null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			tooltip : '执行挂起操作',
			text : '挂起',
			iconCls : 'hung-business',
			disabled : true,
			itemId : 'com_ad_workflow_SuspendBtn',
			handler : function() {
				var me = this;
				var ok = 1;
				me._window.query('tabpanel')[0].items.each( function(item){
					if(Ext.type(item.beforeSuspend)=='function'){
						ok =item.beforeSuspend();
					}
				} );
				if (!ok)return;
			}
		});