	int NODE_START = 0; // 开始节点
	int NODE_NODE = 1; // 一般节点【不停留，自动向下】
	int NODE_TASKNODE = 2; // 人工节点【只对应一个审批人WorkFlowNode.usrid】
	int NODE_TASKNODEPOOL = 3; // 人工节点【对应多个审批人，来自于WorkFlowNodePool ,推与拉二种方式】
	int NODE_DECISION = 4; // 条件选择扭转节点
	int NODE_FORK = 5; // fork
	int NODE_JOIN = 6;// join
	int NODE_END = 9; // 结束节点