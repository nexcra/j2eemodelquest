Ext.define('com.ad.report.ReportSrcWhereForm', {
			extend : 'Ext.form.Panel',
			layout : 'anchor',
			border : 0,
			bodyPadding : '5 5 5 5',
			defaults : {
				anchor : '100%'
			},
			config : {
				_expreDatas : null,
				_labelname : null,
				_valType : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				me.items = [{
							xtype : 'displayfield',
							labelAlign : 'right',
							labelWidth : 50,
							fieldLabel : '名称',
							value : me._labelname
						}, {
							xtype : 'combo',
							labelAlign : 'right',
							fieldLabel : '条件',
							labelWidth : 50,
							name : 'expre',
							allowBlank : 0,
							store : Ext.create('Ext.data.Store', {
										fields : ['value'],
										data : me._expreDatas
									}),
							queryMode : 'local',
							editable : 0,
							displayField : 'value',
							valueField : 'value',
							listeners : {
								change : function(field, newValue, oldValue, eOpts) {
										if (newValue==='非空' || newValue==='为空' ){
											me.getComponent('valueItem').allowBlank =1;
										}
								}
							}
						}, {
							xtype : me._valType,
							labelAlign : 'right',
							labelWidth : 50,
							fieldLabel : '内容',
							allowBlank : 0,
							itemId :'valueItem',
							name : 'value',
							value : null,
							width : 300
						}];
				me.callParent();
			}

		});