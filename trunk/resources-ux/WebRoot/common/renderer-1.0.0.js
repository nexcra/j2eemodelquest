//写一些可重用的render

function getRenderer(name, cfg) {
	var render;
	switch (name) {
		case 'date' :
			render = Ext.util.Format.dateRenderer(cfg.format);
			break;
		case 'bool' :
			render = function(value) {
				if (value) {
					return '是';
				}
				return '否';
			}
			break;
		case 'bool-sex' :
			render = function(value) {
				if (value) {
					return '男';
				}
				return '女';
			}
			break;
		case 'bool-time' :
			render = function(value) {
				if (value) {
					return '根据任务日期,确定项目终止日期';
				}
				return '根据本表起止日期,安排任务日期';
			}
			break;
		case 'birthday-time' :
			render = function(value) {
				if (Math.ceil(value) >= 0 && Math.ceil(value) <= 3) {
					return '<img src="images/birthday.png" width="16" height="16"></img>(' + Math.ceil(value) + ')';
				} else {
					return Math.ceil(value);
				}
			}
			break;
		case 'tomonth-time' :
			render = function(value) {
				if (0 < Math.ceil(value) && Math.ceil(value) <= 30) {
					return '<img src="images/tomonth.png" width="16" height="16"></img>(' + Math.ceil(value) + ')';
				} else {
					return Math.ceil(value);
				}
			}
			break;
		case 'taskstate' :
			render = function(value) {
				// if (value == 0) {
				// return '挂起';
				// } else
				//								
				if (value == 1) {
					return '已完成';
				} else if (value == 2) {
					return '未完成';

				}
			}
			break;

		case 'bool-prjstate' :
			render = function(value) {

				// if (value == 0) {
				// return '论证阶段';
				// } else if (value == 1) {
				// return '商务接触';
				// } else if (value == 2) {
				// return '需求采集';
				// } else if (value == 3) {
				// return '已签合同';
				// } else if (value == 4) {
				// return '详细设计';
				// } else if (value == 5) {
				// return '编码/测试';
				// } else if (value == 6) {
				// return '试运行';
				// } else if (value == 7) {
				// return '交付运行';
				// } else if (value == 8) {
				// return '终结';
				// }
			}
			break;
		case 'state' :
			render = function(value) {
				if (value == 1) {
					return '正常';
				} else if (value == 3) {
					return '禁用';
				} else if (value == 4) {
					return '离职';

				}

			}
			break;
	}
	return render;
}

