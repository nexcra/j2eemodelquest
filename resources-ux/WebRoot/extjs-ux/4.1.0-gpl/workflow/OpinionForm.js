// author:YMQ
// version: 1.0.0
// create:2012-6-15
// 工作流申请的form
Ext.define('com.ad.workflow.OpinionForm', {
			extend : 'Ext.form.Panel',
			bodyStyle : 'padding:5px 5px 0',
			autoScroll : true,
			layout : 'fit',
			border : 0,
			// fieldDefaults : {
			// labelAlign : 'right',
			// height : 25,
			// labelWidth : 90,
			// anchor : '100%',
			// msgTarget : 'side'
			// },
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

				me.items = [{
							xtype : 'textarea',
							name : 'msg',
							itemId : 'msg',
							value : me._document.msg
						}];
				me.callParent();
			},
			buttons : [{
						text : '保存',
						handler : function() {
							var form = this.up('form');
							com.ad.ajax({
										params : {
											$actionid : 202,
											sid : form._document.stepid,
											msg : form.down('#msg').getValue()
										},
										callback : function(input) {
										}
									});
						}
					}]
		});