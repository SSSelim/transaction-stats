package com.selimssevgi.trxstats.domain.shared;

/**
 * Implements general purpose methods for specifications.
 */
public abstract class AbstractSpecification<T> implements Specification<T> {

  @Override
  public boolean isNotSatisfiedBy(T t) {
    return !isSatisfiedBy(t);
  }

}
