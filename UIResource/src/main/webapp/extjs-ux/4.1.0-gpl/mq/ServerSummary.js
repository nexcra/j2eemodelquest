Ext.define('com.ad.mq.ServerSummary', {
			alias : 'feature.serversummary',
			extend : 'Ext.grid.feature.Summary',
			getSummary : function(store, type, field, group) {
				var reader = store.proxy.reader;
				if (this.remoteRoot && reader.rawData) {
					// reset reader root and rebuild extractors to extract
					// summaries data
					root = reader.root;
					reader.root = this.remoteRoot;
					reader.buildExtractors(true);
					summaryRow = reader.getRoot(reader.rawData);
					// restore initial reader configuration
					reader.root = root;
					reader.buildExtractors(true);

					if (typeof summaryRow === 'undefined' || summaryRow.length === 0) {
						return '';
					}

					// console.log(summaryRow[0]);
					// console.log(summaryRow[0][field]);
					if (typeof summaryRow[0][field.toUpperCase()] != 'undefined') {
						return summaryRow[0][field.toUpperCase()];
					}

					return '';
				}
				return this.callParent(arguments);
			}
		});