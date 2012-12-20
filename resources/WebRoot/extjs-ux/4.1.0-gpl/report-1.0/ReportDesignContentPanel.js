Ext.define('com.ad.report.ReportDesignContentPanel', {
			extend : 'Ext.panel.Panel',
			layout : 'border',
			border : 0,
			reportCfg : null,
			reportData : null,
			reportTableId : null,
			initComponent : function() {
				var me = this;
				me.autoScroll = true;

				me.items = [{
							xtype : 'panel',
							title : '图表',
							region : 'north',
							itemId : 'reportChart',
							layout : 'fit',
							collapsible : true,
							animCollapse : true,
							autoScroll : true,
							border : 0,
							style : 'border-bottom: 1px solid #8db2e3;',
							split : 1,
							hidden : 1,
							collapsed : 1,
							height : '50%',
							listeners : {
								expand : function(panel, eOpts) {

									me.paintChart(panel);
								}
							}
						}, {
							xtype : 'panel',
							title : '数据',
							itemId : 'reportContent',
							autoScroll : true,
							border : 0,
							layout : 'fit',
							region : 'center',
							style : 'border-top: 1px solid #8db2e3;',
							bodyPadding : '5 5 5 5'

						}], me.callParent();
			},
			repaint : function(rpcfg) {
				var me = this;
				if (rpcfg.cols.length == 0 && rpcfg.rows.length == 0) {
					return;
				}
				me.reprotCfg = rpcfg;
				// console.log(me.reprotCfg);
				com.ad.ajax({
							params : {
								$actionid : -6009,
								reportcfg : Ext.JSON.encode(me.reprotCfg)
							},
							callback : function(dd) {
								if (dd.message) {
									Ext.Msg.alert(dd.message);
								} else {
									me.paintTable(rpcfg, dd.data);
								}
							}
						});
			},
			paintTable : function(cfg, datas) {
				var me = this;
				var rows = datas.rows, cols = datas.cols, data = datas.datas;
				var gcunt = cfg.groups.length, colspan = 0;
				me.reportTableId = Ext.id();
				me.reportCfg = cfg;
				me.reportData = datas;

				var tablehtml = '<table id=\'' + me.reportTableId + '\' border=\'1\' style=\'text-align:center;\'><tbody>';
				tablehtml += '<tr><td colspan=\'' + cfg.rows.length + '\' rowspan=\'' + (cfg.cols.length + (gcunt > 1 ? 1 : 0)) + '\'>&nbsp;</td>';

				if (cfg.cols.length > 0) {

					for (var j = 0, lj = cfg.cols.length; j < lj; j++) {

						var precolname, curcolname;
						var samecunt = 1;
						if (j > 0) {
							tablehtml += '<tr>';
						}
						for (var i = 0, li = cols.length; i < li; i++) {
							curcolname = me.getValueByString(cols[i][j]);
							if (i === 0) {
								precolname = curcolname;
								samecunt = 0;
							}
							if (precolname !== curcolname) {
								if (gcunt > 1) {
									colspan = ' colspan=\'' + (samecunt < 1 ? gcunt : samecunt * gcunt) + '\'';
								} else {
									colspan = (samecunt > 1 ? ' colspan=\'' + samecunt + '\'' : '')
								}
								tablehtml += '<td class=\'_report_col\' ' + colspan + '>' + precolname + '</td>';
								precolname = curcolname;
								samecunt = 1;
							} else {
								samecunt++;
							}
							if (i === (li - 1)) {

								if (gcunt > 1) {
									colspan = ' colspan=\'' + (samecunt < 1 ? gcunt : samecunt * gcunt) + '\'';
								} else {
									colspan = (samecunt > 1 ? ' colspan=\'' + samecunt + '\'' : '')
								}
								tablehtml += '<td class=\'_report_col\'' + colspan + '>' + precolname + '</td>';
							}
						}
						tablehtml += '</tr>';
					}
					if (gcunt > 1) {
						tablehtml += '<tr>';
						for (var i = 0, li = cols.length; i < li; i++) {
							for (var j = 0, lj = cfg.groups.length; j < lj; j++) {
								tablehtml += '<td class=\'_report_group\'>' + cfg.groups[j].labelname + '</td>';
							}
						}
						tablehtml += '</tr>';
					}
				} else {

					if (gcunt > 1) {
						// tablehtml += '<tr>';
						for (var i = 0, li = cols.length; i < li; i++) {
							for (var j = 0, lj = cfg.groups.length; j < lj; j++) {
								tablehtml += '<td class=\'_report_group\'>' + cfg.groups[j].labelname + '</td>';
							}
						}
					}
					// tablehtml += '</tr>';
					else {
						tablehtml += '<td>&nbsp;</td></tr>';
					}
					tablehtml += '</tr>';
				}

				if (rows.length > 0) {
					var rowname;
					var temp = tempval = valtmplate = '';
					var newline = true;
					var prerowArr = new Array();
					var prerowAsia = new Array();
					var prerowCunt = new Array();
					for (var k = 0, lk = cfg.rows.length; k < lk; k++) {
						prerowArr[k] = "";
						prerowAsia[k] = "";
						prerowCunt[k] = 1;
					}

					for (var i = 0, li = rows.length; i < li; i++) {

						if (i > 0) {
							rowname = me.getValueByString(rows[i][0]);
							if (prerowArr[0] !== rowname) {
								for (var j = 0, lj = prerowCunt.length; j < lj; j++) {
									// temp.replace('__' + (j- 1) + '_' +
									// prerowArr[j], prerowCunt[j]);
									temp = temp.replace(prerowAsia[j], prerowCunt[j]);
								}
								tablehtml += temp;
								temp = '';
								newline = true;
							}
						}
						tempval = '';
						temp += '<tr>';
						if (cfg.rows.length > 0) {
							for (var j = 0, lj = cfg.rows.length; j < lj; j++) {
								rowname = me.getValueByString(rows[i][j]);
								tempval += '_' + rowname;
								if (newline) {
									prerowArr[j] = rowname;
									prerowCunt[j] = 0;
									prerowAsia[j] = '__' + j + '_' + rowname;
									temp += '<td class=\'_report_row\' rowspan=\'' + prerowAsia[j] + '\'>' + rowname + '</td>';
								}

								if (prerowArr[j] !== rowname) {
									temp = temp.replace(prerowAsia[j], prerowCunt[j]);
									prerowArr[j] = rowname;
									prerowCunt[j] = 1;
									prerowAsia[j] = '__' + j + '_' + rowname;
									temp += '<td class=\'_report_row\' rowspan=\'' + prerowAsia[j] + '\'>' + rowname + '</td>';

								} else {
									prerowCunt[j]++;
								}

							}
						} else {
							temp += '<td></td>';
						}

						if (gcunt > 0) {
							for (var m = 0, lm = cols.length; m < lm; m++) {
								for (var o = 0, lo = cfg.groups.length; o < lo; o++) {
									valtmplate = '';
									for (var n = 0, ln = cfg.cols.length; n < ln; n++) {
										valtmplate += '_' + me.getValueByString(cols[m][n])
									}
									valtmplate += tempval + '_' + cfg.groups[o].clmnname;
									temp += '<td class=\'_report_data\'>%{' + valtmplate + '}%</td>';
								}
							}
						} else {
							for (var m = 0, lm = cols.length; m < lm; m++) {
								temp += '<td class=\'_report_data\'>&nbsp;</td>';
							}
						}

						temp += '</tr>';
						newline = false;
						if (i === (li - 1)) {
							for (var j = 0, lj = prerowCunt.length; j < lj; j++) {
								// temp.replace('__' + (j - 1) + '_' +
								// prerowArr[j], prerowCunt[j]);
								temp = temp.replace(prerowAsia[j], prerowCunt[j]);
							}
							tablehtml += temp;
							temp = '';
						}
					}
				}

				tablehtml += '</tbody></table>';
				if (data.length > 0) {
					for (var i = 0, len = data.length; i < len; i++) {
						for (var o = 0, lo = cfg.groups.length; o < lo; o++) {
							valtmplate = "%{";
							for (var j = 0, lj = cfg.rows.length + cfg.cols.length; j < lj; j++) {
								valtmplate += '_' + data[i][j];
							}
							valtmplate += '_' + cfg.groups[o].clmnname + '}%';
							tablehtml = tablehtml.replace(valtmplate, data[i][cfg.rows.length + cfg.cols.length + o]);
						}

					}
				}
				// console.log(tablehtml);
				tablehtml = tablehtml.replace(/\%\{(.*?)\}\%/g, '0');
				// console.log(data);

				me.getComponent('reportContent').body.update(tablehtml);
				if (!me.getComponent('reportChart').isHidden())
					me.paintChart(me.getComponent('reportChart'));
				// me.getComponent('reportContent').doLayout();
			},
			getValueByString : function(val) {
				if (Ext.type(val) === 'object') {
					return Ext.JSON.encode(val);
				} else {
					return val;
				}
			},
			paintChart : function(panel) {
				var me = this;
				var chartFields = [];
				var names = [];
				var numericFields = [], categoryFields = [];
				var dataTable = Ext.get(me.reportTableId);
				if (Ext.isEmpty(dataTable)) {
					return;
				}
				console.log(dataTable);
				for (var i = 0, len = me.reportCfg.cols.length; i < len; i++) {
					chartFields.push({
								name : 'col_' + me.reportCfg.cols[i]['labelname'],
								type : 'string'
							});
					categoryFields.push('col_' + me.reportCfg.cols[i]['labelname']);
				}

				for (var i = 0, len = me.reportCfg.rows.length; i < len; i++) {
					chartFields.push({
								name : 'row_' + me.reportCfg.rows[i]['labelname'],
								type : 'string'
							});
					categoryFields.push('row_' + me.reportCfg.rows[i]['labelname']);
				}
				for (var i = 0, len = me.reportCfg.groups.length; i < len; i++) {
					chartFields.push({
								name : me.reportCfg.groups[i]['labelname'],
								type : 'double'
							});
					numericFields.push(me.reportCfg.groups[i]['labelname']);
				}
				var chartstore = Ext.create('Ext.data.ArrayStore', {
							autoDestroy : true,
							fields : chartFields,
							data : me.reportData.datas
						});

				var chart = Ext.create('Ext.chart.Chart', {
							xtype : 'chart',
							style : 'background:#fff',
							animate : 0,
							shadow : 0,
							store : chartstore,
							legend : {
								position : 'right'
							},
							axes : [{
										type : 'Numeric',
										position : 'bottom',
										fields : numericFields,
										title : false,
										grid : true,
										label : {
											renderer : Ext.util.Format.numberRenderer('0,0')
											// renderer : function(v) {
											// return
											// String(v).replace(/(.)00000$/,
											// '.$1M');
											// renderer:
											// Ext.util.Format.numberRenderer('0,0')
											// }
										}
									}, {
										type : 'Category',
										position : 'left',
										fields : categoryFields,
										title : false
									}],
							series : [{
										type : 'bar',
										axis : 'bottom',
										gutter : 80,
										xField : categoryFields[0],
										yField : numericFields,
										stacked : true,
										tips : {
											trackMouse : true,
											width : 200,
											height : 100,
											renderer : function(storeItem, item) {
												// this.setTitle(String(item.value[1]
												// / 1000000) + 'M');
												var title = storeItem.get(categoryFields[0]) + ': ';
												if (numericFields.length > 1) {
													var total = 0;
													for (var i = 0, len = numericFields.length; i < len; i++) {
														title += ' <br/>' + numericFields[i] + ':' + storeItem.get(numericFields[i]);
														total += storeItem.get(numericFields[i]);
													}
													title += ' <br/>合计：' + total;
												} else {
													title += storeItem.get(numericFields[0]);
												}

												this.setTitle(title);
											}
										}
									}]
						});
				panel.removeAll();
				panel.add(chart);
				// console.log(chart);
				/**
				 * 
				 * bug: 现生成图标的xField是写死的，应可选择，并根据其选择项对数据进行合计处理
				 */
			}
		});