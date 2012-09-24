// 写一些可重用的Vtype
Ext.onReady(function() {
			Ext.apply(Ext.form.field.VTypes, {
						passwordcompare : function(val, field) { // val指这里的文本框值，field指这个文本框组件，大家要明白这个意思
							if (field.confirmTo) { // confirmTo是我们自定义的配置参数，一般用来保存另外的组件的id值
								var pwd = Ext.getCmp(field.confirmTo); // 取得confirmTo的那个id的值
								return (val == pwd.getValue());
							}
							return true;
						},
						passwordcompareText : '两次输入的密码不一致！',
						// 身份证验证
						idcard : function(pId, field) {
							var arrVerifyCode = [1, 0, "x", 9, 8, 7, 6, 5, 4, 3, 2];
							var Wi = [7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2];
							var Checker = [1, 9, 8, 7, 6, 5, 4, 3, 2, 1, 1];
							if (pId.length != 15 && pId.length != 18) {
								return false;
							}
							var Ai = pId.length == 18 ? pId.substring(0, 17) : pId.slice(0, 6) + "19" + pId.slice(6, 16);
							if (!/^\d+$/.test(Ai)) {
								return false;
							}
							var yyyy = Ai.slice(6, 10), mm = Ai.slice(10, 12) - 1, dd = Ai.slice(12, 14);
							var d = new Date(yyyy, mm, dd), now = new Date();
							var year = d.getFullYear(), mon = d.getMonth(), day = d.getDate();
							if (year != yyyy || mon != mm || day != dd || d > now || year < 1940) {
								return false;
							}
							for (var i = 0, ret = 0; i < 17; i++)
								ret += Ai.charAt(i) * Wi[i];
							Ai += arrVerifyCode[ret %= 11];
							return pId.length == 18 && pId != Ai ? false : true;
						},
						'idcardText' : '身份证号码输入错误！',

						passwordnew : function(val, field) {

							try {
								if (val.length >= 6)// 文本框值的长度
									return true;
								return false;
							} catch (e) {
								return false;
							}

						},
						passwordnewText : '密码不能少于六位',
						telenumber : function(val, field) {
							try {
								if (/(^0?[0-9]{8,12}$)/.test(val))// java正则表达式

									return true;
								return false;
							} catch (e) {
								return false;
							}
						},
						telenumberText : '请输入正确的用户号码,号码位数范围8~12位',// 错误提示

						yearnumber : function(val, field) {
							try {
								if (/(^0?[0-9]{4}$)/.test(val))// java正则表达式

									return true;
								return false;
							} catch (e) {
								return false;
							}
						},
						yearnumberText : '请输入正确的四位年数',// 错误提示

						howage : function(val, field) {

							try {
								if ((/(^\d+$)/.test(val)) && (parseInt(val) >= 1 && parseInt(val) <= 120))
									return true;
								return false;
							} catch (err) {
								return false;
							}

						},
						howageText : '请输入正确的年龄格式,范围在1~120!',

						attribute : function(val, field) {
							
							try {
						//	console.log(field);
								if ((/[0-9]+/.test(val)))
									return true;
								return false;
							} catch (err) {
								return false;
							}

						},
						attributeText : '请输入正确的数据格式!'
					});

		});