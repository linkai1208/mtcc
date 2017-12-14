/**
 * 
 */
package com.sten.framework.common;

import java.io.Serializable;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

/**
 * @author linkai
 * @date 2015年10月13日 上午10:38:52
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY, getterVisibility = JsonAutoDetect.Visibility.NONE)
// 使用默认大小写属性转换json
@Entity
public class Page implements Serializable {

	public Page() {
		super();
	}

	private int currIndex;

	private int count;

	private int rows;

	public int getPrevIndex() {
		return currIndex - 1;
	}

	public int getCurrIndex() {
		return currIndex;
	}

	public void setCurrIndex(int currIndex) {
		this.currIndex = currIndex;
	}

	public int getNextIndex() {
		return currIndex + 1;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		if (this.currIndex > count) {
			this.currIndex = 1;
		}
		this.count = count;
	}

	public int getRows() {
		return this.rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	@Override
	public String toString() {
		return "Page [currIndex=" + currIndex + ", prevIndex="
				+ this.getPrevIndex() + ", nextIndex=" + this.getNextIndex()
				+ ", count=" + count + "]";
	}

}
