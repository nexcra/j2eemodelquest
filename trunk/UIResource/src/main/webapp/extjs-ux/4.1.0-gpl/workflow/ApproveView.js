// author:YMQ
// version: 1.0.0
// create:2012-6-19
// 审批视图

Ext.define('com.ad.workflow.ApproveView', {
			extend : 'Ext.tab.Panel',
			border : 0,
			bodyBorder : 0,
			config : {
				_document : null
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				var itemCfg = { // tabPanel的参数
					title : null,
					tabTip : null,
					iconCls : null,
					closable : false,
					layout : 'fit',
					border : 0,
					items : null
				};

				com.ad.ajax({
							params : {
								$actionid : 1004,
								$dataid : 201,
								nid : me._document.nid
							},
							callback : function(input) {
								var datas = input.data;
								var cfg = {};
								for (var i = 0, len = datas.length; i < len; i++) {
									itemCfg.title = datas[i].title;
									itemCfg.tabTip = datas[i].tabTip;
									itemCfg.iconCls = datas[i].iconCls;
									Ext.apply(cfg, Ext.JSON.decode(datas[i].cfg || '{}'));
									if (cfg._extraParams) {
										Ext.apply(cfg._extraParams, {
													did : me._document.id
												});
									} else {
										cfg._extraParams = {
											did : me._document.id
										};
									}
									Ext.apply(cfg, {
												_document : me._document,
												_view : datas[i]
											});
									itemCfg.items = Ext.create(datas[i].clazz, cfg);
									me.add(itemCfg).show();

								}
								me.setActiveTab(0);
							}
						});
				me.callParent();
			}
		});