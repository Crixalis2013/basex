package org.basex.query.func.array;

import static org.basex.query.QueryError.*;

import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.value.array.Array;
import org.basex.query.value.item.*;
import org.basex.query.value.type.*;
import org.basex.util.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public final class ArrayTail extends ArrayFn {
  @Override
  public Item item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Array array = toArray(exprs[0], qc);
    if(array.isEmptyArray()) throw ARRAYEMPTY.get(info);
    return array.tail();
  }

  @Override
  protected Expr opt(final CompileContext cc) {
    final Type type = exprs[0].seqType().type;
    if(type instanceof ArrayType) exprType.assign(type);
    return this;
  }
}
