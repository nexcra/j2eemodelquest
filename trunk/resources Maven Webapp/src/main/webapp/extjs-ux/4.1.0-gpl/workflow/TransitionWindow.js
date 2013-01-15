// author:YMQ
// version: 1.0.0
// create:2012-6-21
// 案件审批窗口
Ext.define('com.ad.workflow.TransitionWindow', {
			extend : 'Ext.window.Window',
			height : 300,
			width : 400,
			layout : 'fit',
			title : '选择去向',
			shadow : false,
			modal : true,
			border : 0,
			config : {
				_document : null, // document数据
				_aftersubmitFN : null
				// 确定提交后的callback
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				me.items = Ext.create('com.ad.mq.DefaultGridPanel', {
							_dataid : 202,
							border : 1,
							_extraParams : {
								nid : me._document.nid
							}
						});
				me.callParent();
			},
			buttons : [{
						text : '确定',
						handler : function() {
							var window = this.up('window');
							var grid = this.up('window').down('panel').down('panel');
							var transition = grid.getView().getSelectionModel().getSelection()[0];
							if (!transition)
								return;
							Ext.Msg.confirm('提醒', '确定要提交当前案件至下一节点 ？', function(opt) {
										if (opt === 'yes') {
											var did = window._document.id;
											var tid = transition.get('id');
											var sid = window._document.stepid;
											com.ad.ajax({
														params : {
															$actionid : 201,
															did : did,
															tid : tid,
															sid :sid
														},
														callback : function(input) {
															if (Ext.isEmpty(input.message)) {
																window.close();
																window._aftersubmitFN(input);
															}
															// if
															// (Ext.isEmpty(input.message))
															// {
															// Ext.Msg.alert('警告',
															// input.message);
															// } else {
															// // if (typeof
															// window._aftersubmitFN
															// == 'function') {
															// window._aftersubmitFN(input);
															// // }
															// }
														}
													});
										}
									});
						}
					}, {
						text : '关闭',
						handler : function() {
							this.up('window').close();
						}
					}]
		});