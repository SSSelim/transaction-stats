package com.selimssevgi.trxstats.domain.specification;

/**
 * Specification interface.
 * Instead of directly implementing this interface, extend {@link AbstractSpecification}.
 */
public interface Specification<T> {

  /**
   * Check if {@code t} is satisfied by the specification.
   *
   * @param t object to test.
   * @return {@code true} if {@code t} satisfies the specification.
   */
  boolean isSatisfiedBy(T t);

  /**
   * Method to be used when negation is required.
   * Makes it more readable, and explicit.
   *
   * @param t object to test.
   * @return {@code true} if {@code t} does not satisfy the specification.
   */
  boolean isNotSatisfiedBy(T t);
}
