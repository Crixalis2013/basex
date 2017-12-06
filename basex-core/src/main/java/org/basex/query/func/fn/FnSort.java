package org.basex.query.func.fn;

import static org.basex.query.QueryError.*;

import java.util.*;

import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.func.*;
import org.basex.query.iter.*;
import org.basex.query.util.collation.*;
import org.basex.query.util.list.*;
import org.basex.query.value.*;
import org.basex.query.value.item.*;
import org.basex.query.value.seq.*;
import org.basex.query.value.type.*;

/**
 * Function implementation.
 *
 * @author BaseX Team 2005-17, BSD License
 * @author Christian Gruen
 */
public final class FnSort extends StandardFunc {
  @Override
  public Value value(final QueryContext qc) throws QueryException {
    final Value value = exprs[0].value(qc), val = value(value);
    return val != null ? val : iter(value, qc).value(qc);
  }

  @Override
  public Iter iter(final QueryContext qc) throws QueryException {
    final Value value = exprs[0].value(qc), v = value(value);
    return v != null ? v.iter() : iter(value, qc);
  }

  /**
   * Sort the input data and returns an iterator.
   * @param value value
   * @param qc query context
   * @return item order
   * @throws QueryException query exception
   */
  private Iter iter(final Value value, final QueryContext qc) throws QueryException {
    Collation coll = sc.collation;
    if(exprs.length > 1) {
      final byte[] token = toTokenOrNull(exprs[1], qc);
      if(token != null) coll = Collation.get(token, qc, sc, info, WHICHCOLL_X);
    }
    final FItem key = exprs.length > 2 ? checkArity(exprs[2], 1, qc) : null;

    final long size = value.size();
    final ValueList values = new ValueList(size);
    final Iter iter = value.iter();
    for(Item item; (item = qc.next(iter)) != null;) {
      values.add((key == null ? item : key.invokeValue(qc, info, item)).atomValue(qc, info));
    }

    final Integer[] order = sort(values, this, coll, qc);
    return new BasicIter<Item>(size) {
      @Override
      public Item get(final long i) {
        return value.itemAt(order[(int) i]);
      }
    };
  }

  /**
   * Sort the input data and returns integers representing the item order.
   * @param vl value list
   * @param sf calling function
   * @param coll collation
   * @param qc query context
   * @return item order
   * @throws QueryException query exception
   */
  public static Integer[] sort(final ValueList vl, final StandardFunc sf, final Collation coll,
      final QueryContext qc) throws QueryException {

    final int al = vl.size();
    final Integer[] order = new Integer[al];
    for(int o = 0; o < al; o++) order[o] = o;
    try {
      Arrays.sort(order, (i1, i2) -> {
        qc.checkStop();
        try {
          final Value v1 = vl.get(i1), v2 = vl.get(i2);
          final long s1 = v1.size(), s2 = v2.size(), sl = Math.min(s1, s2);
          for(int v = 0; v < sl; v++) {
            Item m = v1.itemAt(v), n = v2.itemAt(v);
            if(m == Dbl.NAN || m == Flt.NAN) m = null;
            if(n == Dbl.NAN || n == Flt.NAN) n = null;
            if(m != null && n != null && !m.comparable(n)) throw diffError(m, n, sf.info);
            final int d = m == null ? n == null ? 0 : -1 : n == null ? 1 :
              m.diff(n, coll, sf.info);
            if(d != 0 && d != Item.UNDEF) return d;
          }
          return (int) (s1 - s2);
        } catch(final QueryException ex) {
          throw new QueryRTException(ex);
        }
      });
    } catch(final QueryRTException ex) {
      throw ex.getCause();
    }
    return order;
  }

  @Override
  protected Expr opt(final CompileContext cc) throws QueryException {
    // optimize sort on sequences
    final Expr expr1 = exprs[0];
    final int el = exprs.length;
    final SeqType st1 = expr1.seqType();
    if(st1.zero()) return expr1;

    if(expr1 instanceof Value) {
      final Value value = value((Value) expr1);
      if(value != null) return value;
    }
    if(el == 3) coerceFunc(2, cc, SeqType.AAT_ZM, st1.type.seqType());

    return adoptType(expr1);
  }

  /**
   * Evaluates value arguments.
   * @param value value
   * @return sorted value or {@code null}
   */
  private Value value(final Value value) {
    if(exprs.length < 2) {
      // range values
      if(value instanceof RangeSeq) {
        final RangeSeq seq = (RangeSeq) value;
        return seq.asc ? seq : seq.reverse(null);
      }
      // sortable single or singleton values
      final SeqType st = value.seqType();
      if(st.type.isSortable() && (st.one() || value instanceof SingletonSeq)) return value;
    }
    // no pre-evaluation possible
    return null;
  }
}
