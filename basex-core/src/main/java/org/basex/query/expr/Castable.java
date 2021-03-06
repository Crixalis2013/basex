package org.basex.query.expr;

import static org.basex.query.QueryText.*;

import org.basex.query.*;
import org.basex.query.value.*;
import org.basex.query.value.item.*;
import org.basex.query.value.type.*;
import org.basex.query.var.*;
import org.basex.util.*;
import org.basex.util.hash.*;

/**
 * Castable expression.
 *
 * @author BaseX Team 2005-20, BSD License
 * @author Christian Gruen
 */
public final class Castable extends Single {
  /** Static context. */
  private final StaticContext sc;
  /** Sequence type to check for. */
  private final SeqType seqType;

  /**
   * Constructor.
   * @param sc static context
   * @param info input info
   * @param expr expression
   * @param seqType sequence type to check
   */
  public Castable(final StaticContext sc, final InputInfo info, final Expr expr,
      final SeqType seqType) {
    super(info, expr, SeqType.BLN_O);
    this.sc = sc;
    this.seqType = seqType;
  }

  @Override
  public Expr compile(final CompileContext cc) throws QueryException {
    return super.compile(cc).optimize(cc);
  }

  @Override
  public Expr optimize(final CompileContext cc) {
    return expr.seqType().instanceOf(seqType) ? cc.replaceWith(this, Bln.TRUE) : this;
  }

  @Override
  public Bln item(final QueryContext qc, final InputInfo ii) throws QueryException {
    final Value value = expr.value(qc);
    final long size = value.size();
    return Bln.get(seqType.occ.check(size) &&
        (size == 0 || seqType.cast((Item) value, false, qc, sc, info) != null));
  }

  @Override
  public Expr copy(final CompileContext cc, final IntObjMap<Var> vm) {
    return new Castable(sc, info, expr.copy(cc, vm), seqType);
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj instanceof Castable && seqType.eq(((Castable) obj).seqType) &&
        super.equals(obj);
  }

  @Override
  public void plan(final QueryPlan plan) {
    plan.add(plan.create(this, AS, seqType), expr);
  }

  @Override
  public void plan(final QueryString qs) {
    qs.token(expr).token(CASTABLE).token(AS).token(seqType);
  }
}
