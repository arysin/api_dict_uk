package dict_uk


class SearchService {
    static transactional = false

    def dictData = DictDataLoader.instance

    def search(word, params) {
        long tm1 = System.currentTimeMillis()

        def search = word
        def found = []
        def firstIdx = -1
        
//        articles.eachWithIndex { article, i ->
//            if( article[0].form.equalsIgnoreCase(search) ) {
//                found << article
//                if( firstIdx == -1 ) {
//                    firstIdx = i
//                }
//            }
//        }

		found = dictData.findWord(word)
		
        def tm2 = System.currentTimeMillis()
        println "== timing: " + (tm2-tm1) + " ms"

        if( params.withNeighborArticles ) {
            return new SearchResult(found, dictData.findNeighbors(word))
        }
        else {
            return new SearchResult(found, null)
        }
    }
}
