package dict_uk

import org.junit.Test

class DictDataLoaderTest extends GroovyTestCase {
	final DictDataLoader instance = DictDataLoader.instance


	@Test
	void testFindWord() {
		assertTrue(240000 < instance.indexes.size())

		assert [] == instance.findWord('xxx')

		List<Article> articles = instance.findWord('ячмінь')

		assert articles.size() == 2
		assert articles[0][0].form == "ячмінь"
		assertTrue articles[0][0].tags.endsWith(":xp1")

		assertTrue articles[1][0].tags.endsWith(":xp2")
		
		assert instance.findWord('автор').size() == 1
		
	}

	@Test
	void testFindNeighbors() {
		def neighbors = instance.findNeighbors("ячмінь")
		assert neighbors.size() >= 14

		for(List<Article> neigh: neighbors) {
			assert neigh.size() == 1
		}
	}
	
	@Test
	void testNonExistingArticle() {
		def neighbors = instance.findNeighbors("ячмі")
		assert neighbors.size() >= 14
		assert "ячмінь" in neighbors.collect{ it[0].form } 
	}
}
