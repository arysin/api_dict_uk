package dict_uk


class DictDataLoader {
    static final ArrayList<List> articles = new ArrayList(260000)

    static {
        println "Loading words list..."

        def last = null

        long tm1 = System.currentTimeMillis()
        DictDataLoader.class.getResource("dict_flex.txt").eachLine {

            def items = it.split("\\|")

            articles << items.collect {
                def (form, tag) = it.split(" ")
                new Lemma(form, tag)
            }
            
            if( articles.size() % 10000 == 0 )
                println "\t${articles.size}"
        }

        def tm2 = System.currentTimeMillis()
        println "Words loaded: ${articles.size}"
        println "== timing: " + (tm2-tm1) + " ms"
    }


}
