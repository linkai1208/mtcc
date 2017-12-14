package com.sten.framework.event;

import org.springframework.context.ApplicationEvent;

/**
 * Created by linkai on 16/7/15.
 */
public class CustomEvent extends ApplicationEvent {

	public CustomEvent(Object source) {
		super(source);
	}

	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return this.value;
	}

	public String toString() {
		return "My Custom Event";
	}
}
