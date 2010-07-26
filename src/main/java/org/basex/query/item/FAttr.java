package org.basex.query.item;

import static org.basex.query.QueryTokens.*;
import java.io.IOException;
import org.basex.core.Main;
import org.basex.data.Serializer;
import static org.basex.util.Token.*;
import org.w3c.dom.Attr;

/**
 * Attribute node fragment.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-10, ISC License
 * @author Christian Gruen
 */
public final class FAttr extends FNode {
  /** Attribute name. */
  private final QNm name;

  /**
   * Constructor.
   * @param n name
   * @param v value
   * @param p parent
   */
  public FAttr(final QNm n, final byte[] v, final Nod p) {
    super(Type.ATT);
    name = n;
    val = v;
    par = p;
  }

  /**
   * Constructor for DOM nodes (partial).
   * Provided by Erdal Karaca.
   * @param attr DOM node
   * @param p parent reference
   */
  FAttr(final Attr attr, final Nod p) {
    this(new QNm(token(attr.getName())), token(attr.getValue()), p);
  }

  @Override
  public QNm qname() {
    return name;
  }

  @Override
  public byte[] nname() {
    return name.atom();
  }

  @Override
  public FAttr copy() {
    return new FAttr(name, val, par);
  }

  @Override
  public void serialize(final Serializer ser) throws IOException {
    ser.attribute(name.atom(), val);
  }

  @Override
  public void plan(final Serializer ser) throws IOException {
    ser.emptyElement(this, NAM, name.atom(), VAL, val);
  }

  @Override
  public String toString() {
    return Main.info("%(%=\"%\")", name(), name.atom(), val);
  }
}
