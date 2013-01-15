Ext.define('com.ad.report.ReportDesignContentPanel', {
			extend : 'Ext.panel.Panel',
			layout : 'border',
			border : 0,
			reportCfg : null,
			reportData : null,
			reportTableId : null,
			// reportXFieldComboId : null,
			initComponent : function() {
				var me = this;
				me.autoScroll = true;
				me.reportXFieldComboId = Ext.id();
				// Ext.define('xfieldComboModel', {
				// extend : 'Ext.data.Model',
				// idProperty : 'value',
				// fields : [{
				// name : 'value',
				// type : 'string'
				// }, {
				// name : 'name',
				// type : 'string'
				// }]
				// });

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
						// ,
						// dockedItems : [{
						// xtype : 'toolbar',
						// items : Ext.create('Ext.form.ComboBox', {
						// fieldLabel : 'X',
						// labelWidth : 10,
						// itemId : me.reportXFieldComboId,
						// // queryMode : 'local',
						// store : Ext.create('Ext.data.Store', {
						// fields : [{
						// name : 'value',
						// type : 'string'
						// }, {
						// name : 'name',
						// type : 'string'
						// }],
						// proxy : {
						// type : 'memory',
						// reader : {
						// type : 'json'
						// }
						// },
						// autoLoad : true
						// }),
						// editable : false,
						// displayField : 'name',
						// valueField : 'value',
						// listeners : {
						// change : function(field, newValue, oldValue, eOpts) {
						// if (me.reportCfg.xfield !== newValue) {
						// me.reportCfg.xfield = newValue;
						// me.createChart(field.up().up(),
						// Ext.get(me.reportTableId), newValue);
						// }
						// // me.paintChart(field.up().up());
						//
						// }
						// }
						// })
						// }]
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
				me.reportCfg = rpcfg;
				var xfield = me.reportCfg.xfield;

				// var reportXFieldComboStore =
				// me.getComponent('reportChart').getComponent(me.reportXFieldComboId).getStore();
				// reportXFieldComboStore.removeAll();
				var combodata = me.createXFieldComboData(me.reportCfg.cols, me.reportCfg.rows);
				// reportXFieldComboStore.add(combodata);
				// console.log(me.reportXFieldCombo.syncFx());
				// for (var i = 0, len = combodata.length; i < len; i++) {
				// console.log(combodata[i]);
				// reportXFieldComboStore.add(combodata[i]);
				// }
				// reportXFieldComboStore.load();
				// me.reportXFieldCombo.getStore().each(function(record) {
				// console.log(record);
				//
				// });

				//				
				// = reportXFieldComboStore
				// reportXFieldComboStore.load();
				if (Ext.isEmpty(xfield)) {
					me.reportCfg.xfield = combodata[0]['value'];
				}
				// console.log(xfield);
				// me.reportXFieldCombo.setValue(xfield);
				// console.log(me.reprotCfg);
				com.ad.ajax({
							params : {
								$actionid : -6009,
								reportcfg : Ext.JSON.encode(me.reportCfg)
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

				var tablehtml = '<table id=\'' + me.reportTableId + '\' border=\'1\' class=\'_report_table\' ><tbody>';
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
								tablehtml += '<td class=\'_report_col' + (('col_' + cfg.cols[j]['clmnname'] === me.reportCfg.xfield) ? ' _xfield' : '') + '\'' + colspan + ' name=\'col_'
										+ cfg.cols[j]['clmnname'] + '\'>' + precolname + '</td>';
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
								tablehtml += '<td class=\'_report_col' + (('col_' + cfg.cols[j]['clmnname'] === me.reportCfg.xfield) ? ' _xfield' : '') + '\'' + colspan + ' name=\'col_'
										+ cfg.cols[j]['clmnname'] + '\'>' + precolname + '</td>';
							}
						}
						tablehtml += '</tr>';
					}
					if (gcunt > 1) {
						tablehtml += '<tr>';
						for (var i = 0, li = cols.length; i < li; i++) {
							for (var j = 0, lj = cfg.groups.length; j < lj; j++) {
								tablehtml += '<td class=\'_report_group\'  name=\'group_' + cfg.groups[j]['clmnname'] + '\'>' + cfg.groups[j]['labelname'] + '</td>';
							}
						}
						tablehtml += '</tr>';
					}
				} else {

					if (gcunt > 1) {
						// tablehtml += '<tr>';
						for (var i = 0, li = cols.length; i < li; i++) {
							for (var j = 0, lj = cfg.groups.length; j < lj; j++) {
								tablehtml += '<td class=\'_report_group\'  name=\'group_' + cfg.groups[j]['clmnname'] + '\'>' + cfg.groups[j]['labelname'] + '</td>';
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
						var datarowname = '';
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
								datarowname += '%row_' + cfg.rows[j]['clmnname'] + ':' + rowname + '%,';
								tempval += '_' + rowname;
								if (newline) {
									prerowArr[j] = rowname;
									prerowCunt[j] = 0;
									prerowAsia[j] = '__' + j + '_' + rowname;
									temp += '<td class=\'_report_row' + (('row_' + cfg.rows[j]['clmnname'] === me.reportCfg.xfield) ? ' _xfield' : '') + '\' rowspan=\'' + prerowAsia[j]
											+ '\' name=\'row_' + cfg.rows[j]['clmnname'] + '\'>' + rowname + '</td>';
								}

								if (prerowArr[j] !== rowname) {
									temp = temp.replace(prerowAsia[j], prerowCunt[j]);
									prerowArr[j] = rowname;
									prerowCunt[j] = 1;
									prerowAsia[j] = '__' + j + '_' + rowname;
									temp += '<td class=\'_report_row' + (('row_' + cfg.rows[j]['clmnname'] === me.reportCfg.xfield) ? ' _xfield' : '') + '\' rowspan=\'' + prerowAsia[j]
											+ '\' name=\'row_' + cfg.rows[j]['clmnname'] + '\'>' + rowname + '</td>';

								} else {
									prerowCunt[j]++;
								}

							}
						} else {
							temp += '<td></td>';
						}

						if (gcunt > 0) {
							for (var m = 0, lm = cols.length; m < lm; m++) {
								var colname = '';
								for (var o = 0, lo = cfg.groups.length; o < lo; o++) {
									valtmplate = '';
									for (var n = 0, ln = cfg.cols.length; n < ln; n++) {
										valtmplate += '_' + me.getValueByString(cols[m][n]);
										colname += '%col_' + cfg.cols[n]['clmnname'] + ':' + me.getValueByString(cols[m][n]) + '%,';
									}
									valtmplate += tempval + '_' + cfg.groups[o].clmnname;
									temp += '<td class=\'_report_data\' name=\'' + cfg.groups[o]['clmnname'] + '\' rowname=\'' + datarowname + '\' colname=\'' + colname + '\'>%{' + valtmplate
											+ '}%</td>';
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
				// console.log(me.getComponent('reportContent').body);
				var chartPanel = me.getComponent('reportChart');
				me.getComponent('reportContent').body.on('dblclick', function(evt, el, o) {
							var clickdom = new Ext.dom.Element(el);

							if (!(clickdom.hasCls('_report_col') || clickdom.hasCls('_report_row'))) {
								return;
							}
							if (clickdom.hasCls('_xfield')) {
								return;
							}
							var newXFiled = clickdom.getAttribute('name');
							var datatable = Ext.get(me.reportTableId);
							datatable.select('td[class*=_xfield]').each(function(item) {
										item.removeCls('_xfield');
									});
							datatable.select('td[name=' + newXFiled + ']').each(function(item) {
										item.toggleCls('_xfield');
									});
							me.reportCfg.xfield = newXFiled;
							if (!chartPanel.isHidden())
								me.createChart(chartPanel, datatable, newXFiled);

						});

				if (!chartPanel.isHidden())
					me.paintChart(chartPanel);
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

				var dataTable = Ext.get(me.reportTableId);
				var xfield = me.reportCfg.xfield;
				if (Ext.isEmpty(dataTable)) {
					return;
				}

				me.createChart(panel, dataTable, xfield);

			},
			createChart : function(panel, table, xFieldName) {
				var me = this;
				var groupscfg = me.reportCfg.groups;
				var chartFields = [{
							name : xFieldName,
							type : 'string'
						}];
				var numericFields = [], categoryFields = [];
				var chartData = [];
				var transfered = {};
				table.select('td[name=' + xFieldName + ']').each(function(item) {
							var data = [];
							var selector;
							var sumobj = {};
							if (!Ext.isEmpty(transfered[item.dom.textContent])) {
								return true;
							}
							data.push(item.dom.textContent);
							transfered[item.dom.textContent] = true;

							if (xFieldName.substr(0, 4) === 'row_') {
								selector = 'td[rowname*=\'%' + item.getAttribute('name') + ':' + item.dom.textContent + '%\']';
							} else {
								selector = 'td[colname*=\'%' + item.getAttribute('name') + ':' + item.dom.textContent + '%\']';
							}
//							console.log(selector);
							for (var i = 0, len = groupscfg.length; i < len; i++) {
								sumobj[groupscfg[i]['clmnname']] = 0;
							}
							table.select(selector).each(function(dataItem) {
										sumobj[dataItem.getAttribute('name')] += Ext.Number.from(dataItem.dom.textContent, 0);
									});
							for (var i = 0, len = groupscfg.length; i < len; i++) {
								data.push(sumobj[groupscfg[i]['clmnname']]);
							}
							chartData.push(data);
						});
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
							data : chartData
						});
				var chart = Ext.create('Ext.chart.Chart', {
							xtype : 'chart',
							title : 'test',
							style : 'background:#fff',
							animate : 0,
							shadow : 0,
							store : chartstore,
							legend : {
								position : 'right'
							},
							axes : [{
										type : 'Numeric',
										position : 'left',
										fields : numericFields,
										title : false,
										grid : true,
										label : {
							// renderer : Ext.util.Format.numberRenderer('0,0')
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
										position : 'bottom',
										fields : xFieldName,
										title : false,
										label : {
											rotate : {
												degrees : 270
											}
										}
									}],
							series : [{
										type : 'column',
										axis : 'left',
										gutter : 80,
										xField : xFieldName,
										yField : numericFields,
										stacked : true,
										tips : {
											trackMouse : true,
											width : 200,
											height : 100,
											renderer : function(storeItem, item) {
												// this.setTitle(String(item.value[1]
												// / 1000000) + 'M');
												var title = storeItem.get(xFieldName) + ': ';
												if (numericFields.length > 1) {
													var total = 0;
													for (var i = 0, len = numericFields.length; i < len; i++) {
														title += ' <br/>&nbsp;&nbsp&nbsp&nbsp' + numericFields[i] + ':' + storeItem.get(numericFields[i]);
														total += storeItem.get(numericFields[i]);
													}
													title += ' <br/>&nbsp;&nbsp&nbsp&nbsp合计：' + total;
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
				// --
				// return [];
			},
			createXFieldComboData : function(colsCfg, rowCfg) {
				var data = [];

				if (!Ext.isEmpty(colsCfg)) {
					for (var i = 0, len = colsCfg.length; i < len; i++) {
						data.push({
									value : 'col_' + colsCfg[i]['clmnname'],
									name : '列:' + colsCfg[i]['labelname']
								});
					}
				}
				if (!Ext.isEmpty(rowCfg)) {
					for (var i = 0, len = rowCfg.length; i < len; i++) {
						data.push({
									value : 'row_' + rowCfg[i]['clmnname'],
									name : '行:' + rowCfg[i]['labelname']
								});
					}
				}
				return data;

			}
		});