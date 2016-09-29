package dict_uk

import groovy.transform.Canonical
import groovy.transform.CompileStatic

@CompileStatic
@Canonical
class Lemma {
    final String form
    final String tags
}
