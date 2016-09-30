#!/bin/env groovy

def out = new File("dict_flex.txt")
out.text = ""

def outIndex = new File("dict_flex.idx")
outIndex.text = ""

def lastLemma = null
def sz = 0

def idxMap = [:]
def lastIdx = null

new File(args[0]).eachLine {

	if( ! it.startsWith(" ") ) {
		def lemma = it.split(" ")[0]

		if( lastLemma != lemma ) {
			if( lastIdx ) {
				lastIdx[1] = out.size() - lastIdx[0]
			}

			if( lastLemma ) {
				out << "\n"
			}

			lastIdx = [out.size(), 0]
			idxMap[ lemma ] = lastIdx

			lastLemma = lemma
		}
		else {
//			lastIdx[1] += it.size() + 1
			out << "\n"
		}

		out << it
	}
	else {
		def str =  "|" + it.trim()
//		lastIdx[1] += str.size()

		out << str
	}

}

outIndex.text = idxMap.collect { k,v -> k + " " + v.join(" ") }.join("\n")

