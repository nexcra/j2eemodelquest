Ext.onReady(function() {
			var w = '100%', h = '100%', paper = Raphael("canvas_container", w, h);
			var transform = [], transGoal = null;
			init(paper);
			function init(p) {
				var ptndesk = p.rect(0, 0, 100, 450);
				ptndesk.attr('fill', 'lightgray');
				createStart(p, 10).translate(50, 20);
				createEnd(p, 10).translate(50, 50);
				createNode(p, 40, 20).translate(30, 70);
				createTaskNode(p, 40, 20).translate(30, 100);
				createTaskPoolNode(p, 20, 10).translate(50, 140);
				// createDecision(p, 30, 30).translate(35, 170).rotate(45);
				createDecision(p).translate(30, 170);
				createFork(p).translate(30, 220);
				createJoin(p).translate(30, 250);
				var trans = createTrans(p).translate(30, 300);
				trans.data('dept')[0].translate(30, 300);
				trans.data('dept')[1].translate(30, 300);
				createText(p, 'NOTE').translate(50, 330);

			}

			// create node node
			function createStart(p, radius, scope) {
				var node = p.circle(0, 0, radius);
				node.attr({
							id : Ext.id(),
							fill : 'black',
							title : '开始节点',
							cursor : 'move',
							'text-anchor' : 'start'
						});
				node.data('type', 'start');
				node.data('scope', scope ? scope : 'pattern');
				// if (node.data('scope') === 'db')
				node.mouseout(moveout);
				node.mouseover(movemove);
				node.drag(move, start, up);
				return node;
			}
			// create end node
			function createEnd(p, radius, scope) {
				var node = p.circle(0, 0, radius);
				node.attr({
							id : Ext.id(),
							fill : 'white',
							title : '结束节点',
							stroke : 'black',
							'stroke-width' : 4,
							cursor : 'move'
						});
				node.data('type', 'end');
				node.data('scope', scope ? scope : 'pattern');
				node.mouseout(moveout);
				node.mouseover(movemove);
				node.drag(move, start, up);
				return node;
			}

			// create auto node
			function createNode(p, width, height, scope) {
				var node = p.rect(0, 0, width, height);
				node.attr({
							id : Ext.id(),
							fill : 'white',
							title : '自动节点',
							stroke : 'black',
							'stroke-width' : 2,
							cursor : 'move'
						});
				node.data('type', 'node');
				node.data('scope', scope ? scope : 'pattern');
				node.mouseout(moveout);
				node.mouseover(movemove);
				node.drag(move, start, up);
				return node;
			}

			// create task node
			function createTaskNode(p, width, height, scope) {
				var node = p.rect(0, 0, width, height, 10);

				node.attr({
							id : Ext.id(),
							fill : 'white',
							title : '一般任务节点',
							stroke : 'black',
							'stroke-width' : 2,
							cursor : 'move'
						});
				node.data('type', 'tasknode');
				node.data('scope', scope ? scope : 'pattern');
				node.mouseout(moveout);
				node.mouseover(movemove);
				node.drag(move, start, up);
				return node;
			}

			// create TaskPool node
			function createTaskPoolNode(p, rx, ry, scope) {
				var node = p.ellipse(0, 0, rx, ry);// 20,10
				node.attr({
							id : Ext.id(),
							fill : 'white',
							title : '多处理者任务节点',
							stroke : 'black',
							'stroke-width' : 2,
							cursor : 'move'
						});

				node.data('type', 'taskpoolnode');
				node.data('scope', scope ? scope : 'pattern');
				node.mouseout(moveout);
				node.mouseover(movemove);
				node.drag(move, start, up);
				return node;
			}

			// create decision node -- old
			// function createDecision(p, width, height, scope) {
			// var node = p.rect(0, 0, width, height); // 30
			//
			// node.attr({
			// fill : 'white',
			// title : '分支节点',
			// stroke : 'black',
			// 'stroke-width' : 2,
			// cursor : 'move'
			// });
			//
			// node.data('type', 'decision');
			// node.data('scope', scope ? scope : 'pattern');
			// node.mouseout(moveout);
			// node.mouseover(movemove);
			// node.drag(move, start, up);
			// // node.rotate(45);
			// return node;
			// }

			// create decision node
			function createDecision(p, scope) {
				var node = p.path('M 20,0 L 40,20 L 20,40 L 0,20 Z'); // 30

				node.attr({
							id : Ext.id(),
							title : '分支节点',
							stroke : 'black',
							'stroke-width' : 2,
							fill : 'white',
							cursor : 'move'
						});

				node.data('type', 'decision');
				node.data('scope', scope ? scope : 'pattern');
				node.mouseout(moveout);
				node.mouseover(movemove);
				node.drag(move, start, up);
				// node.rotate(45);
				return node;
			}

			// create fork node
			function createFork(p, scope) {
				var node = p.path('M 20,0 V 10 M 0,10 H 40 M10,10 V 20 M 30,10 V 20'); // 30

				node.attr({
							id : Ext.id(),
							title : 'FORK节点',
							stroke : 'black',
							'stroke-width' : 2,
							cursor : 'move'
						});

				node.data('type', 'fork');
				node.data('scope', scope ? scope : 'pattern');
				node.mouseout(moveout);
				node.mouseover(movemove);
				node.drag(move, start, up);
				// node.rotate(45);
				return node;
			}

			// create fork node
			function createJoin(p, scope) {
				var node = p.path('M 10,0 V 10 M 30,0 V 10 M 0,10 H 40 M 20,10 V 20'); // 30

				node.attr({
							id : Ext.id(),
							title : 'FORK节点',
							stroke : 'black',
							'stroke-width' : 2,
							cursor : 'move'
						});
				node.data('type', 'join');
				node.data('scope', scope ? scope : 'pattern');
				node.mouseout(moveout);
				node.mouseover(movemove);
				node.drag(move, start, up);
				// node.rotate(45);
				return node;
			}

			function createTrans(p, scope) {
				var node = p.path('M 0,0 L 40,0');
				node.attr({
							id : Ext.id(),
							title : 'Transition',
							stroke : 'black',
							'stroke-width' : 2,
							cursor : 'move'
						});

				var startPoint = p.circle(0, 0, 2);
				startPoint.attr({
							id : Ext.id(),
							title : '拖动[开始]',
							cursor : 'move',
							fill : 'white'
						});
				startPoint.data('parent', node);
				startPoint.data('type', 'startPoint');
				startPoint.drag(move, start, up);
				var endPoint = p.circle(40, 0, 2);
				endPoint.attr({
							id : Ext.id(),
							title : '拖动[到达]',
							cursor : 'move',
							fill : 'red'
						});
				endPoint.data('parent', node);
				endPoint.data('type', 'endPoint');
				endPoint.drag(move, start, up);

				node.data('type', 'trans');
				node.data('scope', scope ? scope : 'pattern');
				node.data('dept', [startPoint, endPoint]);
				node.mouseout(moveout);
				node.mouseover(movemove);
				node.drag(move, start, up);
				// node.rotate(45);
				// console.log(node);
				return node;

			}

			function createText(p, val, scope) {
				var node = p.text(0, 0, val);

				node.attr({
							id : Ext.id(),
							title : '说明',
							// stroke : 'black',
							// 'stroke-width' : 1,
							cursor : 'move',
							stroke : 'blue'
						});

				node.data('type', 'text');
				node.data('scope', scope ? scope : 'pattern');
				// node.mouseout(moveout);
				// node.mouseover(movemove);
				node.drag(move, start, up);
				// node.rotate(45);
				return node;

			}

			function start(x, y, evt) {
				// createStart(paper, 10).translate(50, 20);
				// this.data('scope', 'db');
				if (this.data('scope') === 'pattern') {
					switch (this.data('type')) {
						case 'start' :
							createStart(this.paper, 10).translate(50, 20);
							break;
						case 'end' :
							createEnd(this.paper, 10).translate(50, 50);
							break;
						case 'node' :
							createNode(this.paper, 40, 20).translate(30, 70);
							break;
						case 'tasknode' :
							createTaskNode(this.paper, 40, 20).translate(30, 100);
							break;
						case 'taskpoolnode' :
							createTaskPoolNode(this.paper, 20, 10).translate(50, 140);
							break;
						case 'decision' :
							// createDecision(this.paper, 30, 30).translate(35,
							// 170).rotate(45);
							createDecision(this.paper).translate(30, 170);
							break;
						case 'fork' :
							createFork(this.paper).translate(30, 220);
							break;
						case 'join' :
							createJoin(this.paper).translate(30, 250);
							break;
						case 'trans' :
							var trans = createTrans(this.paper).translate(30, 300);
							trans.data('dept')[0].translate(30, 300);
							trans.data('dept')[1].translate(30, 300);
							break;
						case 'text' :
							createText(this.paper, 'NOTE').translate(50, 330);
							break;
					}
					this.data('scope', 'db');
				}

				transform = this.transform();

				// if (this.data('dept')) {
				// console.log(this.attr('path'));
				// var clone = transform.slice(0);
				// clone[0][1] = this.attr('path')[0][1];
				// clone[0][2] = this.attr('path')[0][2];
				// // this.data('dept')[0].attr('x' ,);
				// this.data('dept')[0].transform(clone);
				//					
				// clone[0][1] = this.attr('path')[1][1];
				// clone[0][2] = this.attr('path')[1][2];
				// // this.data('dept')[0].attr('x' ,);
				// this.data('dept')[1].transform(clone);
				// }
				this.attr({
							opacity : .5
						});
			}

			function movemove() {
				transGoal = this;
				this.attr({
							stroke : 'red',
							opacity : 0.2
						});
			}

			function moveout() {
				transGoal = null;
				this.attr({
							stroke : 'black',
							opacity : 1
						});
			}
			function move(dx, dy, x, y, evt) {

				var clone = deepCopy(transform);
				clone[0][1] = x - (this.attr('x') || this.attr('cx') || 0);
				clone[0][2] = y - (this.attr('y') || this.attr('cy') || 0);
				if (this.data('parent')) {
					var parent = this.data('parent');
					var op = (this.data('type') === 'startPoint') ? parent.data('dept')[1] : parent.data('dept')[0];
					if (this.matrix.e > op.matrix.e)
						clone[0][1] -= 2;
					else
						clone[0][1] += 2;

					if (this.matrix.f > op.matrix.f)
						clone[0][2] -= 2;
					else
						clone[0][2] += 2;
				}
				this.transform(clone);
				if (this.data('dept')) {

					var startClone = deepCopy(clone);
					var path = this.attr('path');
					var paths = [[0,0,0],[0,0,0]];
					if (typeof path ==='string'){
						path = path.split(' ');
						var arr = path[1].split(',');
						paths[0][1] = arr[0];
						paths[0][2] = arr[1];
						arr = path[3].split(',');
						paths[1][1] = arr[0];
						paths[1][2] = arr[1];
					}else {
						paths = path;
					}
					if (path) {
						startClone[0][1] += paths[0][1] - this.data('dept')[0].attr('cx') || 0;
						startClone[0][2] +=paths[0][2] - this.data('dept')[0].attr('cy') || 0;
					}
					this.data('dept')[0].transform(startClone);

					var endClone = deepCopy(clone);
					if (paths) {
						endClone[0][1] += paths[1][1] - this.data('dept')[1].attr('cx') || 0;
						endClone[0][2] += paths[1][2] - this.data('dept')[1].attr('cy') || 0;
					}
					this.data('dept')[1].transform(endClone);
				}
				if (this.data('parent')) {
					var path = this.data('parent');
					var pathPath = path.attr('path');
					if (this.data('type') === 'startPoint') {
						pathPath[0] = ['M', clone[0][1] - path.matrix.e, clone[0][2] - path.matrix.f];
					} else {
						pathPath[1] = ['L', clone[0][1] - path.matrix.e + ((this.attr('x') || this.attr('cx') || 0)), clone[0][2] - path.matrix.f + (this.attr('y') || this.attr('cy') || 0)];
					}
					path.attr('path', pathPath);
				}

				if (this.data('trans')) {
					var trans = this.data('trans');
					for (var i = 0, len = trans.length; i < len; i++) {
						var goal = trans[i];
						var goalClone = deepCopy(clone);
						var offsetX = 0, offsetY = 0;
						switch (this.data('type')) {
							case 'node' :
							case 'tasknode' :
								if (goal.data('type') === 'startPoint') {
									offsetX = 20;
									offsetY = 10;
								} else {
									offsetX = -20;
									offsetY = 10;
								}
								break;
							case 'decision' :
								if (goal.data('type') === 'startPoint') {
									offsetX = 20;
									offsetY = 20;
								} else {
									offsetX = -20;
									offsetY = 20;
								}
								break;
							case 'taskpoolnode' :
								if (goal.data('type') === 'startPoint') {
									offsetX = 0;
									offsetY = 0;
								} else {
									offsetX = -40;
									offsetY = 0;
								}
								break;
							case 'start' :
							case 'end' :
								if (goal.data('type') === 'startPoint') {
									offsetX = 0;
									offsetY = 0;
								} else {
									offsetX = -40;
									offsetY = 0;
								}
								break;
							case 'fork' :
							case 'join' :
								if (goal.data('type') === 'startPoint') {
									offsetX = 20;
									offsetY = 10;
								} else {
									offsetX = -20;
									offsetY = 10;
								}
								break;
						}

						var path = goal.data('parent');
						var pathPath = path.attr('path');

						// var clonePoint = deepCopy(clone);
						// clonePoint[0][1] -= goal.attr('cx') + offset;
						// clonePoint[0][2] -= goal.attr('cy') + offset;

						// clonePoint[0][1] -= path.matrix.e + offset;
						// clonePoint[0][2] -= path.matrix.f + offset;
						// goal.transform(clonePoint);
						//						
						// var clonePoint = deepCopy(clone);
						// clone[0][1] -= path.matrix.e + offsetX;
						// clone[0][2] -= path.matrix.f + offsetY;
						// console.log('offsetX:' + offsetX + ',offsetY:' +
						// offsetY);
						if (goal.data('type') === 'startPoint') {
							pathPath[0] = ['M', clone[0][1] - path.matrix.e + offsetX, clone[0][2] - path.matrix.f + offsetY];
							// goalClone[0][1] -= path.matrix.e + offset;
							// goalClone[0][2] -= path.matrix.f + offset;
						} else {
							pathPath[1] = ['L', clone[0][1] - path.matrix.e + offsetX + ((goal.attr('x') || goal.attr('cx') || 0)),
									clone[0][2] - path.matrix.f + offsetY + (goal.attr('y') || goal.attr('cy') || 0)];
							// goalClone[0][1] -= path.matrix.e + offset +
							// ((goal.attr('x') || goal.attr('cx') || 0));
							// goalClone[0][2] -= path.matrix.f + offset +
							// (goal.attr('y') || goal.attr('cy') || 0);
						}

						var clonePoint = deepCopy(clone);
						clonePoint[0][1] += offsetX;
						clonePoint[0][2] += offsetY;
						goal.transform(clonePoint);
						path.attr('path', pathPath);
					}
				}

			}
			function up() {
				// restoring state
				transform = [];
				if ((this.data('type') === 'startPoint' || this.data('type') === 'endPoint')) {

					if (this.data('trans') && this.data('trans').data('trans')) {
						Ext.Array.remove(this.data('trans').data('trans'), this);
						// remove( Array array, Object item )
						// for (var i =0 ,len
						// =this.data('trans').data('trans').length;i <len ;
						// i++){
						//							
						// }
					}

					if (transGoal) {
						var trans = transGoal.data('trans');
						var parentTrans = this.data('parent');
						var tag = false;
						if (trans) {
							for (var i = 0, len = trans.length; i < len; i++) {
								if (trans[i].data('parent').id === parentTrans.id) {
									tag = true;
									break;
								}
							}
							if (!tag)
								trans.push(this);
							// if (!Ext.Array.contains(trans, this))
							// trans.push(this);
						} else
							trans = [this];
						transGoal.data('trans', trans);

						this.data('trans', transGoal);
					}
				}
				this.attr({
							opacity : 1
						});

			};

			function deepCopy(obj) {
				// 如果obj是数组
				if (Object.prototype.toString.call(obj) === '[object Array]') {
					var out = [], i = 0, len = obj.length;
					for (; i < len; i++) {
						// arguments.callee == deepCopy
						// 当然可以用deepCopy代替arguments.callee
						out[i] = arguments.callee(obj[i]);
					}
					return out;
				}
				// 如果是对象
				if (typeof obj === 'object') {
					var out = {}, i;
					for (i in obj) {
						out[i] = arguments.callee(obj[i]);
					}
					return out;
				}
				return obj;
			}
		});