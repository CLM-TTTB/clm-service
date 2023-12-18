package com.clm.api.interfaces;

import com.clm.api.exceptions.business.NotFoundException;
import java.security.Principal;
import java.util.Map;

/** CRUDService */
public interface CRUDService<T, ID> {

  T get(ID id) throws NotFoundException;

  T create(T t, Principal connectedUser);

  T update(T t, Principal connectedUser);

  T patch(
      Map<String, Object> identifyFields,
      Map<String, Object> updateFields,
      String[] ignoreFields,
      Principal connectedUser);

  void delete(ID id, Principal connectedUser);
}
