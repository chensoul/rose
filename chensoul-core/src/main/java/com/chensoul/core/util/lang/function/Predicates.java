package com.chensoul.core.util.lang.function;

import java.util.function.Predicate;

/**
 * TODO
 *
 * @author <a href="mailto:ichensoul@gmail.com">chensoul</a>
 * @since 0.0.1
 */
public abstract class Predicates {
	public static Predicate[] EMPTY_PREDICATE_ARRAY = new Predicate[0];

	/**
	 * <p>emptyArray.</p>
	 *
	 * @param <T> a T class
	 * @return an array of {@link Predicate} objects
	 */
	public static <T> Predicate<T>[] emptyArray() {
		return (Predicate<T>[]) EMPTY_PREDICATE_ARRAY;
	}

	/**
	 * {@link Predicate} always return <code>true</code>
	 *
	 * @param <T> the type to test
	 * @return <code>true</code>
	 */
	public static <T> Predicate<T> alwaysTrue() {
		return e -> true;
	}

	/**
	 * {@link Predicate} always return <code>false</code>
	 *
	 * @param <T> the type to test
	 * @return <code>false</code>
	 */
	public static <T> Predicate<T> alwaysFalse() {
		return e -> false;
	}

	/**
	 * a composed predicate that represents a short-circuiting logical AND of {@link Predicate predicates}
	 *
	 * @param predicates {@link Predicate predicates}
	 * @param <T>        the type to test
	 * @return non-null
	 */
	public static <T> Predicate<? super T> and(Predicate<? super T>... predicates) {
		int length = predicates == null ? 0 : predicates.length;
		if (length == 0) {
			return alwaysTrue();
		} else if (length == 1) {
			return predicates[0];
		} else {
			Predicate<T> andPredicate = alwaysTrue();
			for (Predicate<? super T> p : predicates) {
				andPredicate = andPredicate.and(p);
			}
			return andPredicate;
		}
	}

	public static <T> Predicate<? super T> not(Predicate<? super T>... predicates) {
		if (predicates == null || predicates.length == 0) {
			// 如果没有传入任何Predicate，返回一个总是返回true的Predicate
			return e -> true;
		} else if (predicates.length == 1) {
			// 如果只有一个Predicate，直接对其取反
			return predicates[0].negate();
		} else {
			// 如果有多个Predicate，对它们全部取反
			Predicate<T> notPredicate = alwaysTrue();
			for (Predicate<? super T> p : predicates) {
				notPredicate = notPredicate.and(p.negate());
			}
			return notPredicate;
		}
	}

	/**
	 * a composed predicate that represents a short-circuiting logical OR of {@link Predicate predicates}
	 *
	 * @param predicates {@link Predicate predicates}
	 * @param <T>        the detected type
	 * @return non-null
	 */
	public static <T> Predicate<? super T> or(Predicate<? super T>... predicates) {
		int length = predicates == null ? 0 : predicates.length;
		if (length == 0) {
			return alwaysTrue();
		} else if (length == 1) {
			return predicates[0];
		} else {
			Predicate<T> orPredicate = alwaysFalse();
			for (Predicate<? super T> p : predicates) {
				orPredicate = orPredicate.or(p);
			}
			return orPredicate;
		}
	}

}
