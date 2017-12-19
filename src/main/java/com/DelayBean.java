package com;

class DelayBean {
	private int wait, space, reply, timeout;

	DelayBean(String wait, String space, String delay, String timeout) {
		this.wait = Integer.valueOf(wait.replaceAll(" ", ""));
		this.space = Integer.valueOf(space.replaceAll(" ", ""));
		this.reply = Integer.valueOf(delay.replaceAll(" ", ""));
		this.timeout = Integer.valueOf(timeout.replaceAll(" ", ""));
	}

	int getWait() {
		return wait;
	}

	int getSpace() {
		return space;
	}

	int getReply() {
		return reply;
	}

	int getTimeout() {
		return timeout;
	}

}
