package it.golem.alarm;

import it.golem.alarm.exceptions.AlarmException;

import java.util.List;

public interface  Alarm<T, E> {

	public abstract void send(T t);
	public abstract void compare(T t, List<E> list) throws AlarmException;

}
