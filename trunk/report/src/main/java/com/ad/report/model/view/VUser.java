package com.ad.report.model.view;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Transient;

public class VUser implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	private String text;
	private String folder;
	private Boolean expanded;
	@Transient
	private Boolean leaf;
	private Boolean checked;

	private String parentid;

	@Transient
	private List<VUser> children;

	public String getParentid() {
		return parentid;
	}

	public void setParentid(String parentid) {
		this.parentid = parentid;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFolder() {
		return folder;
	}

	public void setFolder(String folder) {
		this.folder = folder;
	}

	public Boolean getExpanded() {
		return expanded;
	}

	public void setExpanded(Boolean expanded) {
		this.expanded = expanded;
	}

	public Boolean getLeaf() {
		return leaf;
	}

	public void setLeaf(Boolean leaf) {
		this.leaf = leaf;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public List<VUser> getChildren() {
		return children;
	}

	public void setChildren(List<VUser> children) {
		this.children = children;
	}

}
