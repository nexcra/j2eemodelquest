// author:YMQ
// version: 1.0.0
// dataid:dataid//数据源编号

// _dataid : null, 数据源编号
// _extraParams : null, 请求到服务器的参数
// _gridpanel : null, 异步动态构建的gridpanel
// _gridcfg : null, 指定给异步构建gridpanel的参数
// _autoLoad : true //是否加载数据

Ext.define('com.ad.mq.DefaultEditGridPanel', {
	extend : 'Ext.panel.Panel',
	layout : 'fit',
	border : 0,
	config : {
		_dataid : null,
		_extraParams : null,
		_gridpanel : null,
		_gridcfg : null,
		_autoLoad : true,
		_auth : 0,
		_localcfgToken : null,
		_localcfgName : null
	},
	constructor : function(cfg) {
		this.callParent(arguments);
		this.initConfig(cfg);
		return this;
	},
	initComponent : function() {
		var me = this, localStorage = com.ad.getLocalStorage(), token = -1, tokenName = Ext.Loader.getConfig('appName') + '$' + (me._localcfgToken || ('data_' + me._dataid + '_token')), localCfg = null, localCfgName = Ext.Loader
				.getConfig('appName')
				+ '$' + (me._localcfgName || 'data_' + me._dataid);
		if (localStorage) {
			token = localStorage.getItem(tokenName) || -1;
			localCfg = localStorage.getItem(localCfgName);
			if (localCfg)
				localCfg = Ext.JSON.decode(localCfg);
		}
		com.ad.ajax({
					url : 'mq',
					params : {
						$actionid : 1100,
						$dataid : me._dataid,
						token : token
					},
					callback : function(dd) {
						if (localStorage) {
							if (dd.token > token) {
								localStorage.setItem(localCfgName, Ext.JSON.encode(dd));
								localStorage.setItem(tokenName, dd.token);
								localCfg = dd;
							}
						} else {
							localCfg = dd;
						}
						if (!localCfg || !localCfg.data) {
							me.html = '<div style="color:red;font-size:14pt;">获取配置数据失败！</div>';
						} else {
							var cfg = {
								data : localCfg.data,
								cfg : Ext.JSON.decode(localCfg.cfg || {}),
								dataid : me._dataid,
								auth : (me._auth || dd.auth || 0)
							};
							var grid = Ext.create('com.ad.mq.DefaultEditGrid', Ext.apply({
												input : cfg,
												_localcfgToken : tokenName,
												_localcfgName : localCfgName
											}, me._gridcfg));
							me._gridpanel = grid;
							me.add(grid).show();
							// me.doComponentLayout();
							var store = grid.getStore();
							Ext.applyIf(store.getProxy().extraParams, me._extraParams);
							if (me._autoLoad && store) {
								store.load();
							}
						}

					}
				});

		me.callParent();
	},
	getGrid : function() {
		return this._gridpanel;
	}
});