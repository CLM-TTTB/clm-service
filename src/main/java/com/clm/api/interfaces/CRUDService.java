package com.clm.api.interfaces;

import java.security.Principal;

/** CRUDService */
public interface CRUDService<T, ID> {

  T get(ID id);

  T create(T t, Principal connectedUser);

  T update(T t, Principal connectedUser);

  void delete(ID id, Principal connectedUser);
}
