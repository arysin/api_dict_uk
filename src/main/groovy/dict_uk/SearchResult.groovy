package dict_uk

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class SearchResult {
    final List<Article> articles
    final List<Article> neighbors
}
