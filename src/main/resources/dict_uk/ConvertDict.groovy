#!/bin/env groovy

def out = new File("dict_flex.txt")

def last = null
def hasFlex = false

new File(args[0]).eachLine {

            if( it.startsWith(" ") ) {
//                if( ! hasFlex ) {
//                    out << "|"
//                }

                out << "|" << it.trim()
                hasFlex = true
            }
            else {
                if( last )
                    out << "\n"
                out << it
                last = it
                hasFlex = false
            }

}