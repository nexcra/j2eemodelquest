Ext.Loader.setConfig({
			enabled : true,
			paths : {
				'App' : 'app',
				'Ext.ux' : '/resources/extjs/4.1.0-gpl/examples/ux',
				'com.ad' : '/resources/extjs-ux/4.1.0-gpl'
			}
		});
Ext.onReady(function() {

			Ext.tip.QuickTipManager.init();
			Ext.create('com.ad.workflow.WorkFlowSVG', {
						renderTo : Ext.getBody()
					});

		});