package com.brutalfighters.server.util;

/**
 * 计时器
 *
 */
public class Counter {
	private int DEFAULT;
	private int counter;
	
	/**
	 * 构造函数
	 * @param counter
	 * @param DEFAULT
	 */
	public Counter(int counter, int DEFAULT) {
		this.setDEFAULT(DEFAULT);
		this.setCounter(counter);
	}
	public Counter(int counter) {
		this(counter, counter);
	}

	
	public int getCounter() {
		return counter;
	}
	public void setCounter(int counter) {
		this.counter = counter;
	}
	public void resetCounter(int DEFAULT) {
		setDEFAULT(DEFAULT);
		setCounter(counter);
	}
	public void resetCounter() {
		resetCounter(getDEFAULT());
	}
	public boolean isFinished() {
		return getCounter() <= 0;
	}
	public void subCounter(int sub) {
		setCounter(getCounter() - sub);
	}

	
	public int getDEFAULT() {
		return DEFAULT;
	}
	public void setDEFAULT(int dEFAULT) {
		DEFAULT = dEFAULT;
	}
}
