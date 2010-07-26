package org.basex.query.expr;

import static org.basex.query.QueryTokens.*;
import static org.basex.query.QueryText.*;
import java.io.IOException;
import org.basex.data.Serializer;
import org.basex.query.QueryContext;
import org.basex.query.QueryException;
import org.basex.query.item.Item;
import org.basex.query.item.SeqType;
import org.basex.query.iter.Iter;
import org.basex.query.util.Err;
import org.basex.util.Token;

/**
 * Treat as expression.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-10, ISC License
 * @author Christian Gruen
 */
public final class Treat extends Single {
  /** Instance. */
  final SeqType seq;

  /**
   * Constructor.
   * @param e expression
   * @param s sequence type
   */
  public Treat(final Expr e, final SeqType s) {
    super(e);
    seq = s;
  }

  @Override
  public Expr comp(final QueryContext ctx) throws QueryException {
    super.comp(ctx);
    if(!checkUp(expr, ctx).item()) return this;
    ctx.compInfo(OPTPRE, this);
    return atomic(ctx);
  }

  @Override
  public Iter iter(final QueryContext ctx) throws QueryException {
    final Iter iter = ctx.iter(expr);
    final Item it = iter.next();
    if(it == null) {
      if(seq.mayBeZero()) Err.empty(this);
      return Iter.EMPTY;
    }
    if(seq.zeroOrOne()) {
      if(iter.next() != null) Err.or(NOTREATS, info(), seq);
      if(!it.type.instance(seq.type)) Err.or(NOTREAT, info(), seq, it.type);
      return it.iter();
    }

    return new Iter() {
      Item i = it;

      @Override
      public Item next() throws QueryException {
        if(i == null) return null;
        if(!i.type.instance(seq.type)) Err.or(NOTREAT, info(), seq, i.type);
        final Item ii = i;
        i = iter.next();
        return ii;
      }
    };
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.openElement(this, TYPE, Token.token(seq.toString()));
    expr.plan(ser);
    ser.closeElement();
  }

  @Override
  public String toString() {
    return expr + " " + TREAT + " " + AS + " " + seq;
  }
}
