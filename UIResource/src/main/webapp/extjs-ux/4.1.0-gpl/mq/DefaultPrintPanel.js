/**
 * 自动生成表格并打印
 */
Ext.define('com.ad.mq.DefaultPrintPanel', {
			extend : 'Ext.panel.Panel',
			layout : 'fit',
			border : 0,
			autoScroll : true,
			config : {
				_store : null, // 数据源
				_tabletitle : null, // 表的标题
				_headerHTML : null, // 头部说明修饰
				_totalTD : null,
				_TH : null, // 表格字段头
				_footerHTML : null, // 尾部说明修饰
				_footerTD : null, // 尾部追加
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
				var titleDIV = '<div class="printTableTitle printTable_Title">' + me._tabletitle + '</div>';
				var headerDIV = me._headerHTML ? ('<div>' + me._headerHTML + '</div>') : null;
				var footerDIV = me._footerHTML ? ('<div>' + me._footerHTML + '</div>') : null;
				var tableBody = '<div style="text-align:center;"><table class="printTable print_Table"><tr>';

				if (!!me._TH) {
					tableBody += me._TH;
				} else {
					if (me._showIdxNum) {
						tableBody += '<td style="width:100px;text-align:center;"> 序号</td>';
					}
					for (var i = 0, len = me._fields.length; i < len; i++) {
						tableBody += '<td style="' + me._fields[i]['style'] + '" >' + me._fields[i]['name'] + '</td>';
					}
				}

				tableBody += '</tr>';
				var cunt = 0;
				me._store.each(function(record) {
							tableBody += '<tr>';
							if (me._showIdxNum) {
								tableBody += '<td style="text-align:center;">' + (++cunt) + '</td>';
							}
							for (var i = 0, len = me._fields.length; i < len; i++) {
								var val = record.get(me._fields[i]['dataIndex']);
								if (Ext.typeOf(val)==='date'){
									val = Ext.Date.format(val ,'Y-m-d');
								}
								if (Ext.typeOf(val)==='number'){
									val = Ext.util.Format.number(val,'0,000.00')
								}
								tableBody += '<td style="' + me._fields[i]['style'] + '" >' + (Ext.isEmpty(val) ? '' : val) + '</td>';
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

					if (me._totalTD) {
						for (var j = 0, lj = me._sumfields.length; j < lj; j++) {
							me._totalTD = me._totalTD.replace("{" + me._sumfields[j]['dataIndex'] + '}', Ext.util.Format.number(me._sumfields[j]['value'],'0,000.00'));
						}
						tableBody += me._totalTD;
					}
				}
				if (me._footerTD){
					tableBody +=me._footerTD;
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