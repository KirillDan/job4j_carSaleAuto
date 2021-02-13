package ru.job4j.function;

@FunctionalInterface
public interface TriConsumer <T, U, R, S> {
	public void accept(T t, U u, R r, S s);
}
