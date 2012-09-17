var svgDocument, svgRoot, active, moving = false, fromBox, toBox;
var mouseCoord = {
	x : 0.,
	y : 0.
};
// 用户坐标：相对于原始SVG,坐标位置
var userCoord = {
	x : 0.,
	y : 0.
};
var cRadius = 4.; // 半径
function init(evt) {
	svgDocument = evt.target.ownerDocument;
	svgRoot = svgDocument.rootElement;
	fromBox = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
	fromBox.setAttribute('width', 8);
	fromBox.setAttribute('height', 8);
	fromBox.setAttribute('type', 'fromBox');
	toBox = document.createElementNS('http://www.w3.org/2000/svg', 'rect');
	toBox.setAttribute('width', 8);
	toBox.setAttribute('height', 8);
	toBox.setAttribute('type', 'toBox');
	svgRoot.appendChild(fromBox);
	svgRoot.appendChild(toBox);
}
function mousedown(evt) {
	var el = evt.target;
	var href;
	var g, node;
	active = null;
	moving = false;
	if (el.getAttribute("type") == 'def' || el.getAttribute("type") == 'db') {
		getCoords(evt);
		href = el.getAttribute('xlink:href');
		console.log(svgDocument.getElementById(href.substr(1)));
		/*
		 * g = document.createElementNS('http://www.w3.org/2000/svg', 'g');
		 * g.setAttribute('transform', 'translate(' + mouseCoord.x + ',' +
		 * mouseCoord.y + ')');
		 * 
		 * node = document.createElementNS('http://www.w3.org/2000/svg',
		 * 'circle'); node.setAttribute("cx", 10); node.setAttribute("cy", 10);
		 * node.setAttribute("r", 10); // node.setAttribute('transform',
		 * 'translate(' + mouseCoord.x + ',' + // mouseCoord.y + ')');
		 * node.setAttribute("style", "fill:red"); g.appendChild(node);
		 * svgRoot.appendChild(g);
		 */
		fromBox.setAttribute('moving', 0);
		toBox.setAttribute('moving', 0);
		fromBox.setAttribute('x', 0);
		fromBox.setAttribute('y', 0);
		toBox.setAttribute('x', 0);
		toBox.setAttribute('y', 0);
		if (el.getAttribute("type") == 'def') {

			g = document.createElementNS('http://www.w3.org/2000/svg', 'g');
			g.setAttribute('transform', 'translate(' + mouseCoord.x + ',' + mouseCoord.y + ')');
			node = document.createElementNS('http://www.w3.org/2000/svg', 'use');
			node.setAttributeNS('http://www.w3.org/1999/xlink', 'xlink:href', href);
			node.setAttribute('type', 'db');
			g.appendChild(node);
			svgRoot.appendChild(g);
			active = g;
		} else {
			// el.setAttribute('transform', 'translate(' + mouseCoord.x + ',' +
			// mouseCoord.y + ')');
			active = el.parentNode;
			if (href == '#transition') {
				// console.log(active.childNodes);[0,5][40,5]
				var trans = active.getAttribute('transform');
				trans = trans.replace('translate(', '');
				trans = trans.replace(')', '');
				console.log(trans.split(','));
				var x = trans.split(',')[0];
				var y = trans.split(',')[1];
				fromBox.setAttribute('x', x - 3);
				fromBox.setAttribute('y', y - 3 + 5);
				fromBox.setAttribute('moving', 1);
				toBox.setAttribute('x', x - 3 + 40);
				toBox.setAttribute('y', y - 3 + 5);
				toBox.setAttribute('moving', 1);
				// console.log(el);
				// console.log(document.getElementById(href));
				// active.getAttribute('y1');
				//				
				// active.getAttribute('x2');
				// active.getAttribute('y2');
			}
		}
		moving = true;
	}
}

function mouseup(evt) {
	active = null;
	moving = false;
	fromBox.setAttribute('moving', 0);
	toBox.setAttribute('moving', 0);
}
function mousemove(evt) {
	if (moving && active) {
		getCoords(evt);
		active.setAttribute('transform', 'translate(' + mouseCoord.x + ',' + mouseCoord.y + ')');
		if (fromBox.getAttribute('moving') == 1) {
			fromBox.setAttribute('x', mouseCoord.x - 3);
			fromBox.setAttribute('y', mouseCoord.y - 3 + 5);
		}
		if (toBox.getAttribute('moving') == 1) {
			toBox.setAttribute('x', mouseCoord.x - 3 + 40);
			toBox.setAttribute('y', mouseCoord.y - 3 + 5);
		}
	}
}

// console.log(window.location.search);
function createTmp() {
	var node = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
	node.setAttribute("cx", 200);
	node.setAttribute("cy", 200);
	node.setAttribute("r", "100");
	node.setAttribute("style", "fill:red");
	svgRoot.appendChild(node);
	// root.replaceChild(node, obj);
}

// function getSVGDocument(evt) {
// var target = evt.target;
// var parent = target.parentNode;
// svgDocument = parent.getOwnerDocument();
// svgRoot = svgDocument.documentElement;
// }

// 获取鼠标事件相关的SVG Document
// function getSVGDocument(evt) {
// var target = evt.target;
// var parent = target.parentNode;
// svgDocument = parent.getOwnerDocument();
// svgRoot = svgDocument.documentElement;
// }

// 计算坐标位置:包括用户坐标、鼠标坐标
function getCoords(evt) {
	var x_trans = 0.; // X偏移
	var y_trans = 0.; // Y偏移
	var x_scale = 1.; // ViewBox导致的X缩放比例
	var y_scale = 1.; // ViewBox导致的Y缩放比例
	var scale = 1.; // 缩放比例
	var trans = null;
	var viewbox = null;

	scale = svgRoot.currentScale; // 获取当前缩放比例
	trans = svgRoot.currentTranslate; // 获取当前偏移
	// 获取SVG的当前viewBox
	viewbox = svgRoot.getAttributeNS(null, "viewBox"); // 获取ViewBox属性

	// 获取用户坐标：原始SVG的坐标位置
	userCoord.x = evt.clientX;
	userCoord.y = evt.clientY;

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
		// Break the viewBox parameters into an array to make life easier
		var params = viewbox.split(/\s+/g);
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
	// 根据用户坐标、偏移量、缩放等计算鼠标坐标
	mouseCoord.x = userCoord.x * x_scale + x_trans;
	mouseCoord.y = userCoord.y * y_scale + y_trans;
}