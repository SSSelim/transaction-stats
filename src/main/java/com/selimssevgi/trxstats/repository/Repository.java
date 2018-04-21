package com.selimssevgi.trxstats.repository;

import com.selimssevgi.trxstats.domain.shared.Specification;

import java.util.List;

public interface Repository<T> {

  /**
   * Saves given object to underlying data structure.
   *
   * @param t object to be saved
   * @throws NullPointerException if given argument is null
   */
  void save(T t);

  /**
   * Finds object satisfying given specification.
   *
   * @param specification decides which objects to be included
   * @return list of object, or empty list if none found
   */
  List<T> findAllBySpecification(Specification<T> specification);

}
