/**
 * 工作流程定义
 */
Ext.define('com.ad.workflow.WorkFlowSVG', {
			extend : 'Ext.panel.Panel',
			layout : 'fit',
			border : 1,
			title : 'SVG',
			height : 600,
			config : {
				_did : null,
				_active : null,
				_moving : false,
				_offset : null
			},
			mouseCoord : {
				x : 0.,
				y : 0.
			},
			userCoord : {
				x : 0.,
				y : 0.
			},
			constructor : function(cfg) {
				this.callParent(arguments);
				this.initConfig(cfg);
				return this;
			},
			initComponent : function() {
				var me = this;
				me.callParent();
			},
			tbar : [{
						text : 'Create',
						handler : function() {
							var cmp = this.up('panel');
							var svg = $(cmp.body).svg('get');
							// // console.log(svg);
							// svg.circle(334, 289, 50, {
							// fill : 'red',
							// stroke : 'red',
							// 'stroke-width' : 3,
							// id : 'testNew'
							// });
							// console.log($('#testNew'));
							// $('#testNew').click(function(opt) {
							// console.log(opt)
							// });

							svg.use(null, 334, 289, 1, 1, '#start', {
										type : 'patten',
										id : 'test'
									});
						}
					}],
			listeners : {
				afterrender : function(cmp, eOpts) {
					$(cmp.body).svg({
								onLoad : function(svg) {
									var svgEl = $('svg', cmp.body.dom)
									svgEl.css('width', '100%').css('height', '100%');
									svgEl.mousemove(function(evt) {
												cmp.doMouseMove(svg, evt, this);
											});
									svgEl.mouseup(function(evt) {
												cmp.doMouseUp(svg, evt, this);
											});
									var defs = svg.defs(null);
									var start = svg.group({
												id : 'start'
											});

									svg.circle(defs, 50, 50, 12, {
												fill : 'black',
												id : 'start'
											});
									svg.circle(defs, 50, 50, 12, {
												stroke : 'black',
												'stroke-width' : 4,
												fill : 'white',
												id : 'end'
											});
									svg.rect(defs, 0, 0, 40, 20, {
												id : 'node',
												style : "fill:white;stroke:black;stroke-width:2;"
											});
									svg.rect(defs, 0, 0, 40, 20, 10, 10, {
												id : 'tasknode',
												style : "fill:white;stroke:black;stroke-width:2;"
											});
									svg.ellipse(defs, 0, 0, 20, 10, {
												style : "fill:white;stroke:black;stroke-width:2;",
												id : 'tasknodepool'
											});
									svg.polygon(defs, [[50, 0], [67, 14], [59, 30], [40, 30], [32, 14]], {
												id : 'decision',
												style : "fill:white;stroke:black;stroke-width:2;"
											});
									var fork = svg.group({
												id : 'fork'
											});
									svg.line(fork, 50, 0, 50, 10, {
												stroke : "black",
												'stroke-width' : 2
											});
									svg.line(fork, 30, 10, 70, 10, {
												stroke : "black",
												'stroke-width' : 2
											});
									svg.line(fork, 40, 10, 40, 20, {
												stroke : "black",
												'stroke-width' : 2
											});
									svg.line(fork, 60, 10, 60, 20, {
												stroke : "black",
												'stroke-width' : 2
											});

									var join = svg.group({
												id : 'join'
											});
									svg.line(join, 40, 0, 40, 10, {
												stroke : "black",
												'stroke-width' : 2
											});
									svg.line(join, 60, 0, 60, 10, {
												stroke : "black",
												'stroke-width' : 2
											});
									svg.line(join, 30, 10, 70, 10, {
												stroke : "black",
												'stroke-width' : 2
											});
									svg.line(join, 50, 10, 50, 20, {
												stroke : "black",
												'stroke-width' : 2
											});
									svg.line(null, 0, 0, 40, 0, {
												stroke : "black",
												'stroke-width' : 2,
												id : 'transition'
											});
									svg.rect(null, 0, 0, 100, 450, {
												fill : 'lightgray'
											});
									svg.use(null, 0, 0, 1, 1, '#start', {
												def : '#start'
											});
									svg.use(null, 0, 40, 1, 1, '#end', {
												type : '#end'
											});
									svg.use(null, 30, 130, 1, 1, '#node', {
												type : 'patten'
											});
									svg.use(null, 30, 180, 1, 1, '#tasknode', {
												type : 'patten'
											});
									svg.use(null, 50, 240, 1, 1, '#tasknodepool', {
												type : 'patten'
											});
									svg.use(null, 0, 280, 1, 1, '#decision', {
												type : 'patten'
											});
									svg.use(null, 0, 330, 1, 1, '#fork', {
												type : 'patten'
											});
									svg.use(null, 0, 380, 1, 1, '#join', {
												type : 'patten'
											});
									svg.use(null, 30, 430, 1, 1, '#transition', {
												type : 'patten'
											});

									$('use', cmp.body.dom).mousedown(function(evt) {
												cmp.doMouseDown(svg, evt, this);
											});

								}
							});

				}
			},
			doMouseDown : function(svg, evt, el) {
				// var activEl = Ext.clone(el);
				// this._active = svg.group(null, {
				// id : Ext.id(),
				// transform : 'translate(100,100)'
				// });
				// $(activEl).attr('type', 'db');
				// $(activEl).attr('id', this._active_id);
				// svg.add(this._active, activEl);
				var body = $('svg', this.body.dom);
				this._offset = body.offset();

				console.log(body.offset());
				console.log(svg);
				this._active = svg.use(null, (evt.clientX - this._offset.left), (evt.clientY - this._offset.top), 1, 1, '#start', {
							id : Ext.id()
						});
				// $(this._active).attr('transform','translate(' +evt.clientX
				// +',' + evt.clientY+ ')');
				// $(this._active).attr('id',Ext.id());

				// console.log($(el).attr('href').attr('id'));
				// console.log($(el).get('href'));
				// this._active = svg.use(null, evt.clientX, evt.clientY, 1, 1,
				// '#start', {
				// id:Ext.id()
				// });
				this._moving = true;
			},
			doMouseMove : function(svg, evt, el) {
				if (this._moving && this._active) {
					// $(this._active).attr('transform', 'translate(' +
					// (evt.clientX -
					// this._offset.left)+ ',' + (evt.clientY -
					// this._offset.top) + ')');
					$(this._active).attr('x', evt.clientX);
					$(this._active).attr('y', (evt.clientY));
				}

			},
			doMouseUp : function(svg, evt, el) {
				this._moving = false;
				this._active = null;
			},
			// 获取鼠标事件相关的SVG Document
			getSVGDocument : function(evt) {
				var target = evt.target;
				var parent = target.parentNode;
				svgDocument = parent.getOwnerDocument();
				svgRoot = svgDocument.documentElement;
			},
			getCoords : function(evt) {
				var svgDocument = null;
				var svgRoot = null;

				var x_trans = 0.; // X偏移
				var y_trans = 0.; // Y偏移
				var x_scale = 1.; // ViewBox导致的X缩放比例
				var y_scale = 1.; // ViewBox导致的Y缩放比例
				var scale = 1.; // 缩放比例
				var trans = null;
				var viewbox = null;

				getSVGDocument(evt);

				scale = svgRoot.currentScale; // 获取当前缩放比例
				trans = svgRoot.currentTranslate; // 获取当前偏移
				// 获取SVG的当前viewBox
				viewbox = svgRoot.getAttributeNS(null, "viewBox"); // 获取ViewBox属性

				// 获取用户坐标：原始SVG的坐标位置
				userCoord.x = evt.getClientX();
				userCoord.y = evt.getClientY();

				// 计算偏移、缩放等
				x_trans = (0.0 - trans.x) / scale;
				y_trans = (0.0 - trans.y) / scale;
				// Now that we have moved the rectangle's corner to the
				// upper-left position, let's scale the rectangle to fit
				// the current view. X and Y scales are maintained seperately
				// to handle possible anamorphic scaling from the viewBox
				zoom = scale;
				x_scale = 1.0 / scale;
				y_scale = 1.0 / scale;
				if (viewbox) {
					// We have a viewBox so, update our translation and scale
					// to take the viewBox into account
					// Break the viewBox parameters into an array to make life
					// easier
					var params = viewbox.split(/ s+/);
					// Determine the scaling from the viewBox
					// Note that these calculations assume that the outermost
					// SVG element has height and width attributes set to 100%.
					var h_scale = 1.0 / parseFloat(params[2]) * window.innerWidth;
					var v_scale = 1.0 / parseFloat(params[3]) * window.innerHeight;
					// Update our previously calculated transform
					x_trans = x_trans / h_scale + parseFloat(params[0]);
					y_trans = y_trans / v_scale + parseFloat(params[1]);
					x_scale = x_scale / h_scale;
					y_scale = y_scale / v_scale;
				}
			}
		});