Ext.define('com.ad.report.ReportPlanForm', {
			extend : 'Ext.form.Panel',
			layout : 'anchor',
			border : 0,
			bodyPadding : '5 5 5 5',
			defaults : {
				anchor : '100%'
			},
			items : [{
						labelAlign : 'right',
						xtype : 'textfield',
						fieldLabel : '名称',
						labelWidth : 50,
						name : 'name',
						allowBlank : false
					}, {
						labelAlign : 'right',
						labelWidth : 50,
						xtype : 'fieldcontainer',
						fieldLabel : '公共',
						defaultType : 'radiofield',
						layout : 'hbox',
						items : [{
									boxLabel : '是',
									name : 'ispublic',
									inputValue : 1,
									checked : 1
								}, {
									boxLabel : '否',
									name : 'ispublic',
									inputValue : 0
								}]
					}]
		});