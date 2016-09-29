package dict_uk


class SearchService {
    static transactional = false

    def articles = DictDataLoader.articles

    def search(word, params) {
        long tm1 = System.currentTimeMillis()

        def search = word
/*
        def found = DictDataLoader.articles.findAll {
            it[0].form.equalsIgnoreCase(search)
        }
*/
        def found = []
        def firstIdx = -1
        
        articles.eachWithIndex { article, i ->
            if( article[0].form.equalsIgnoreCase(search) ) {
                found << article
                if( firstIdx == -1 ) {
                    firstIdx = i
                }
            }
        }

        def tm2 = System.currentTimeMillis()
        println "== timing: " + (tm2-tm1) + " ms"

        if( params.withNeighborArticles ) {
            int from = Math.max(firstIdx - 5, 0)
            int to = Math.min(firstIdx + 5, articles.size - 1)
        
        
            def neighbors = articles[from..to].collect { it[0] }
            return new SearchResult(found, neighbors)
        }
        else {
            return new SearchResult(found, null)
        }
    }
}
