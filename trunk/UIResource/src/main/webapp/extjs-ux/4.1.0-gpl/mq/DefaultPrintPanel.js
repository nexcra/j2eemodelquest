/**
 * 自动生成表格并打印
 */
Ext.define('com.ad.mq.DefaultPrintPanel', {
			extend : 'Ext.panel.Panel',
			layout : 'fit',
			border : 0,
			config : {
				_store : null, // 数据源
				_tabletitle : null, // 表的标题
				_headerHTML : null, // 头部说明修饰
				_footerHTML : null, // 尾部说明修饰
				_fields : [], // 字段列表
				_sumfields : [],
				_showIdxNum : false,
				_autoPrint : true
				// 合计的字段项
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			printTableAreaId : null,

			initComponent : function() {
				var me = this;
				me.printTableAreaId = Ext.id();
				me.tbar = [{
							xtype : 'button',
							text : '打印',
							handler : function() {
								me.doPrint();
							}
						}];
				var titleDIV = '<div class="printTableTitle">' + me._tabletitle + '</div>';
				var headerDIV = me._headerHTML ? ('<div>' + me._headerHTML + '</div>') : null;
				var footerDIV = me._footerHTML ? ('<div>' + me._footerHTML + '</div>') : null;
				var tableBody = '<div style="text-align:center;"><table class="printTable"><tr>';
				if (me._showIdxNum) {
					tableBody += '<td style="width:100px;text-align:center;"> 序号</td>';
				}
				for (var i = 0, len = me._fields.length; i < len; i++) {
					tableBody += '<td style="' + me._fields[i]['style'] + '" >' + me._fields[i]['name'] + '</td>';
				}
				tableBody += '</tr>';
				var cunt = 0;
				me._store.each(function(record) {
							tableBody += '<tr>';
							if (me._showIdxNum) {
								tableBody += '<td style="text-align:center;">' + (++cunt) + '</td>';
							}
							for (var i = 0, len = me._fields.length; i < len; i++) {
								tableBody += '<td style="' + me._fields[i]['style'] + '" >' + record.get(me._fields[i]['dataIndex']) + '</td>';
							}
							for (var i = 0, len = me._sumfields.length; i < len; i++) {
								me._sumfields[i]['value'] += record.get(me._sumfields[i]['dataIndex']);
							}
							tableBody += '</tr>';
						}, me);

				if (me._sumfields.length > 0) {
					var colspanNum = 0;
					if (me._showIdxNum) {
						colspanNum = 1;
					}
					tableBody += '<tr>';
					var hasTrue = false;
					for (var i = 0, len = me._fields.length; i < len; i++) {
						for (var j = 0, lj = me._sumfields.length; j < lj; j++) {
							if (me._fields[i]['dataIndex'] == me._sumfields[j]['dataIndex']) {
								tableBody += '<td colspan="' + colspanNum + '" style="text-align:center;">合计</td><td>' + me._sumfields[j]['value'] + '</td>';
								i++;
								hasTrue = true;
								break;
							}
							colspanNum++;
						}

						if (i < len && hasTrue) {
							for (var j = 0, lj = me._sumfields.length; j < lj; j++) {
								tableBody += '<td>';
								if (me._fields[i]['dataIndex'] == me._sumfields[j]['dataIndex']) {
									tableBody += me._sumfields[j]['value'];
								} else {
									tableBody += '-';
								}
								tableBody += '</td>';
							}
						}

					}
					tableBody += '</tr>';
				}

				tableBody += '</table></div>';
				me.html = '<div id="' + me.printTableAreaId + '">' + titleDIV + (headerDIV ? headerDIV : '') + tableBody + (footerDIV ? footerDIV : '') + '</div>';
				if (me._autoPrint) {
					me.listeners = {
						afterrender : function(_panel, eOpts) {
							me.doPrint();
						}
					}
				}

				me.callParent();
			},
			getPrintAreaId : function() {
				return this.printTableAreaId;
			},
			doPrint : function() {
				$('#' + this.printTableAreaId).printArea();
			}

		});