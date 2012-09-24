Ext.define('test.TestPanel', {
			extend : 'Ext.panel.Panel',
			layout : 'fit',
			title : '1111',
			config : {
				cfg : null,
				_dataid : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				Ext.create('Ext.data.Store', {
							storeId : 'simpsonsStore',
							fields : ['name', 'email', 'phone'],
							data : {
								'items' : [{
											'name' : 'Lisa',
											"email" : "lisa@simpsons.com",
											"phone" : "555-111-1224"
										}, {
											'name' : 'Bart',
											"email" : "bart@simpsons.com",
											"phone" : "555-222-1234"
										}, {
											'name' : 'Homer',
											"email" : "home@simpsons.com",
											"phone" : "555-222-1244"
										}, {
											'name' : 'Marge',
											"email" : "marge@simpsons.com",
											"phone" : "555-222-1254"
										}]
							},
							proxy : {
								type : 'memory',
								reader : {
									type : 'json',
									root : 'items'
								}
							}
						});

				var grid = Ext.create('Ext.grid.Panel', {
							title : 'Simpsons',
							store : Ext.data.StoreManager.lookup('simpsonsStore'),
							columns : [{
										header : 'Name',
										dataIndex : 'name'
									}, {
										header : 'Email',
										dataIndex : 'email',
										flex : 1
									}, {
										header : 'Phone',
										dataIndex : 'phone'
									}],
							listeners : {

								selectionchange : function(model, records, eOpts) {
									console.log(records[0].get('name'));
								}

							}
						});
				me.items = grid;
				me.callParent();
			}
		});