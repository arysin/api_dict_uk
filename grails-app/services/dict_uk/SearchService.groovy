package dict_uk

import java.util.List;


class SearchService {
	static transactional = false

	def dictData = DictDataLoader.instance

	List<Article> findWord(word, params) {
		long tm1 = System.currentTimeMillis()

		List<Article> articles = dictData.findWord(word)

		def tm2 = System.currentTimeMillis()
		println "== timing: " + (tm2-tm1) + " ms"

		return articles
	}

	List<String> findNeighbors(word, params) {
		return dictData.findNeighbors(word)
	}
}
