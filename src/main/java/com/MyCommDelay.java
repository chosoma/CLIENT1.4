package com;

public class MyCommDelay {

	private String name;
	private int wait, space, delay, timeout;

	public MyCommDelay(String name, int wait, int space, int delay, int timeout) {
		this.name = name;
		this.wait = wait;
		this.space = space;
		this.delay = delay;
		this.timeout = timeout;
	}

	public String getName() {
		return name;
	}

	public int getWait() {
		return wait;
	}

	public int getSpace() {
		return space;
	}

	public int getDelay() {
		return delay;
	}

	public int getTimeout() {
		return timeout;
	}

}
