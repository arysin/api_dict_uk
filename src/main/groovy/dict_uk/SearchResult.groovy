package dict_uk

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class SearchResult {
    final List<Lemma> articles
    final List<Lemma> neighbors
}
