package dict_uk

import org.junit.Test

class DictDataLoaderTest extends GroovyTestCase {
	final DictDataLoader instance = DictDataLoader.instance

	
	@Test
	void test1() {
		assertTrue(240000 < instance.indexes.size())

		assert [] == instance.findWord('xxx')

		List<Article> articles = instance.findWord('ячмінь')

		assert articles.size() == 2
		assert articles[0][0].form == "ячмінь"
		assertTrue articles[0][0].tags.endsWith(":xp1")

		assertTrue articles[1][0].tags.endsWith(":xp2")
	}

	@Test
	void test2() {
		assert instance.findNeighbors("ячмінь").size() == 15
	}
}