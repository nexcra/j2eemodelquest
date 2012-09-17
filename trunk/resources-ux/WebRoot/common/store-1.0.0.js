// 写一份可重用的store

function getBaseStoreCfg() {// 得到JsonStore的基本配置
	return {
		autoDestroy : true,
		remoteSort : true,
		autoSync : false,
		autoLoad : false,
		buffered : false,
		proxy : {
			type : 'ajax',
			url : 'mq',
			limitParam : '$limit',
			startParam : '$start',
			sortParam : '$sort',
			pageParam : '$page',
			groupParam : '$group',
			filterParam : '$filter',
			reader : {
				type : 'json',
				root : 'data'
			}
		}
	};
}

function getStoreByName(name, cfg, record) {// 根据名称得到Store
	var store;
	switch (name) {
		case 'combox-edu' :// 学历
			var storecfg = {
				fields : ['val', 'name'],
				data : [{
							"val" : "0",
							"name" : "中专"
						}, {
							"val" : "1",
							"name" : "大专"
						}, {
							"val" : "2",
							"name" : "双专科"
						}, {
							"val" : "3",
							"name" : "本科"
						}, {
							"val" : "4",
							"name" : "双学位"
						}, {
							"val" : "5",
							"name" : "硕士研究生"
						}, {
							"val" : "6",
							"name" : "博士研究生"
						}]
			};
			Ext.apply(storecfg, cfg || {});
			store = Ext.create('Ext.data.Store', storecfg);
			break;
		case 'combox-taskstate' : // 状态
			var storecfg = {
				fields : ['val', 'name'],
				data : [{
							"val" : "1",
							"name" : "已完成"
						}, {
							"val" : "2",
							"name" : "未完成"
						}]
			};
			Ext.apply(storecfg, cfg || {});
			store = Ext.create('Ext.data.Store', storecfg);
			break;
		case 'combox-sex' : // 性别
			var storecfg = {
				fields : ['val', 'name'],
				data : [{
							"val" : "1",
							"name" : "男"
						}, {
							"val" : "0",
							"name" : "女"
						}]
			};
			Ext.apply(storecfg, cfg || {});
			store = Ext.create('Ext.data.Store', storecfg);
			break;
		case 'combox-bool' : // 是与否
			var storecfg = {
				fields : ['val', 'name'],
				data : [{
							"val" : "1",
							"name" : "是"
						}, {
							"val" : "0",
							"name" : "否"
						}]
			};
			Ext.apply(storecfg, cfg || {});
			store = Ext.create('Ext.data.Store', storecfg);
			break;
		case 'auto-store-combo' ://combo通用store
			var base = getBaseStoreCfg();
			Ext.apply(base, {
						listeners : {
							load : function(store, records, successful, operation, eOpts) {
								if (record && cfg.fields && cfg.aliasFields) {
									var newRecord = {};
									newRecord[cfg.fields[0]] = record.get(cfg.aliasFields[0]);
									newRecord[cfg.fields[1]] = record.get(cfg.aliasFields[1]);
									store.add(newRecord);
								}
							}
						}
					});
			
			Ext.applyIf(base, cfg);
			Ext.applyIf(base.proxy, cfg.proxy || {});
			if (base.proxy.extraParams) {
				Ext.applyIf(base.proxy.extraParams, cfg.proxy.extraParams || {});
			}else{
				base.proxy.extraParams = cfg.proxy.extraParams || {};
			}
			store = Ext.create('Ext.data.JsonStore', base);
			break;
		case 'auto-store' :// 通用store
			var base = getBaseStoreCfg();
			Ext.applyIf(base, cfg);
			Ext.applyIf(base.proxy, cfg.proxy || {});
			if (base.proxy.extraParams) {
				Ext.applyIf(base.proxy.extraParams, cfg.proxy.extraParams || {});
			}else{
				base.proxy.extraParams = cfg.proxy.extraParams || {};
			}
			store = Ext.create('Ext.data.JsonStore', base);
			break;
	}
	return store;
}
