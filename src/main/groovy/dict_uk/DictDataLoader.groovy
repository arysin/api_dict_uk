package dict_uk

import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.OpenOption;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


@Singleton(strict=false)
class DictDataLoader {
    final Map<String, List<Integer>> indexes = new LinkedHashMap(265000)
    final def file = DictDataLoader.class.getResource("dict_flex.txt").getFile()

    DictDataLoader() {
        println "Loading words indexes..."

        def last = null

        long tm1 = System.currentTimeMillis()

        DictDataLoader.class.getResource("dict_flex.idx").eachLine {

            def (word, idx, sz) = it.split(" ")
            indexes[word] = [Integer.parseInt(idx), Integer.parseInt(sz), indexes.size()]

            if( indexes.size() % 10000 == 0 )
            	println "\t" + indexes.size()
        }

        def tm2 = System.currentTimeMillis()
        println "Words loaded: " + indexes.size()
        println "== timing: " + (tm2-tm1) + " ms"
    }


    List<Article> findWord(word) {

        if( ! (word in indexes) )
			return []

        def (idx, sz) = indexes[word]

		def str = readArtice(idx, sz)
		
        def lines = str.split(/\n/)

        lines.collect { line ->
			line.split(/\|/).collect {
				def (form, tag) = it.split(" ")
				new Article(form, tag)
			}
        }

    }

	List<String> findNeighbors(word) {
        if( word in indexes ) {
			def idx = indexes[word][2]
			def from = Math.max(idx - 7, 0)
			def to = Math.min(idx + 7, indexes.size()-1)
			
			def range = new ArrayList(indexes.keySet())[from..to]
        }
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
}
