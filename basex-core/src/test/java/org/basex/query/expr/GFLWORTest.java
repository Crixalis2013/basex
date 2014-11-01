package org.basex.query.expr;

import static org.basex.query.util.Err.*;
import static org.junit.Assert.*;

import org.basex.query.*;
import org.basex.util.*;
import org.junit.*;

/**
 * Test cases for FLWOR expressions.
 *
 * @author BaseX Team 2005-14, BSD License
 * @author Leo Woerteler
 */
public final class GFLWORTest extends AdvancedQueryTest {
  /** Tests shadowing of outer variables. */
  @Test
  public void shadowTest() {
    query("for $a in for $a in <a>1</a> return $a/text() return <x>{ $a }</x>",
        "<x>1</x>");
    error("for $a at $b in 'c'[$b > 1] return $a", VARUNDEF_X);
  }

  /** Tests shadowing between grouping variables. */
  @Test
  public void groupShadowTest() {
    query("let $i := 1 group by $i, $i return $i", "1");
  }

  /**
   * Runs an updating query and matches the result of the second query
   * against the expected output.
   * @param query query
   * @param expected expected output
   */
  private static void query(final String query, final String expected) {
    try(final QueryProcessor proc = new QueryProcessor(query, context)) {
      final String result = proc.value().serialize().toString();
      // quotes are replaced by apostrophes to simplify comparison
      assertEquals(expected.replaceAll("\"", "'"), result.replaceAll("\"", "'"));
    } catch(final Exception ex) {
      throw (Error) new AssertionError(Util.message(ex)).initCause(ex);
    }
  }
}
