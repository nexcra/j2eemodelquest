package com.ad.workflow.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

/**
 * 工作流扭转
 * @author YMQ
 *
 */
@Entity(name="WORKFLOW$TRANSITION")
public class WorkFlowTransition implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ")
	@SequenceGenerator(name = "SEQ", sequenceName = "SEQ_WORKFLOW")
	protected Integer id;
	protected String name;
	protected Integer fromnode;
	protected Integer tonode;
	protected String description;
	protected String execute;
	protected Integer transtype;//1,pull ,2,push
	
	public Integer getTranstype() {
		return transtype;
	}
	public void setTranstype(Integer transtype) {
		this.transtype = transtype;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getFromnode() {
		return fromnode;
	}
	public void setFromnode(Integer fromnode) {
		this.fromnode = fromnode;
	}
	public Integer getTonode() {
		return tonode;
	}
	public void setTonode(Integer tonode) {
		this.tonode = tonode;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getExecute() {
		return execute;
	}
	public void setExecute(String execute) {
		this.execute = execute;
	}
	
}
