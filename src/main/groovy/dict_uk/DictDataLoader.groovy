package dict_uk

import groovy.transform.Canonical;

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


@Singleton(strict=false)
class DictDataLoader {
	final Comparator<String> comparator = new UkDictComparator()
    final Map<String, IndexRecord> indexes = new LinkedHashMap(265000)
	final List<String> indexKeys
    final def file = DictDataLoader.class.getResource("dict_flex.txt").getFile()

    DictDataLoader() {
        println "Loading words indexes..."

        def last = null

        long tm1 = System.currentTimeMillis()

        DictDataLoader.class.getResource("dict_flex.idx").eachLine {

            def (word, idx, sz) = it.split(" ")
            indexes[word] = new IndexRecord(Integer.parseInt(idx), Integer.parseInt(sz), indexes.size())

            if( indexes.size() % 10000 == 0 )
            	println "\t" + indexes.size()
        }

		indexKeys = new ArrayList(indexes.keySet())
		
        def tm2 = System.currentTimeMillis()
        println "Words loaded: " + indexes.size()
        println "== timing: " + (tm2-tm1) + " ms"
    }


    List<Article> findWord(word) {

        if( ! (word in indexes) )
			return []

        def indexRecord = indexes[word]

		def str = readArtice(indexRecord.pos, indexRecord.length)
		
		parseArticles(str)
    }
	
	List<Article> parseArticles(str) {
		def lines = str.split(/\n/)

		lines.collect { line ->
			line.split(/\|/).collect {
				def (form, tag) = it.split(" ")
				new Article(form, tag)
			}
		}
	}

	List<String> findNeighbors(word) {
		def idx = (word in indexes) ? indexes[word].idx : findNearestIdx(word)

		def from = Math.max(idx - 7, 0)
		def to = Math.min(idx + 7, indexes.size()-1)

		List<IndexRecord> values = new ArrayList(indexes.values())

		def readFrom = values[from].pos
		def readLength = 0
		for(int i=from; i<=to; i++) {
			readLength += values[i].length + 1
		}

		def str = readArtice(readFrom, readLength)
		parseArticles(str).collect() { it[0..0] }
	}

	private findNearestIdx(word) {
		- Collections.binarySearch(indexKeys, word, comparator)
	}
		
	private readArtice(def pos, int sz) {
		ByteBuffer buffer = ByteBuffer.allocate(sz)

		FileChannel fc = FileChannel.open(Paths.get(file)) //, new OpenOption[]{StandardOpenOption.READ})
		try {
			fc.position(pos)

			fc.read(buffer);

			buffer.flip()
	
			byte[] dst = new byte[sz]
			buffer.get(dst)
	
			return new String(dst, 'UTF-8')
	
		} catch (IOException x) {
			throw x
			//System.out.println("I/O Exception: " + x);
		}
		finally {
			fc.close()
		}
	}
	
	@Canonical
	private static class IndexRecord {
		final int pos;
		final int length;
		final int idx;
	}
}
