package dict_uk


import grails.rest.*
import grails.converters.*

//import io.swagger.annotations.*
import com.wordnik.swagger.annotations.*


class InflectController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST"]
    static defaultAction = "save"
    static TEXT_LIMIT = 80

    def inflectService

    def save() {
        if( ! validateRequest(request) )
            return

        try {
            def response = inflectService(request.word, request.flags)
            render response as JSON
        }
        catch(Exception e) {
            e.printStackTrace()
            render(status: 500, text: "Internal error: " + e.getMessage())
            return
        }
    }

    def validateRequest(request) {
        if( ! request.JSON?.word ) {
            render(status: 400, text: "\"word\" field not specified in the request")
            return false
        }

        if( ! request.JSON?.flags ) {
            render(status: 400, text: "\"flags\" field not specified in the request")
            return false
        }

        if( request.JSON.word.size() > TEXT_LIMIT || request.JSON.flags.size() > TEXT_LIMIT ) {
            render(status: 400, text: String.format("word/flag length cannot exceed %d characters", TEXT_LIMIT))
            return false
        }

        return true
    }

    def updateNotes(response) {
        if( testLatCyrMix(request.JSON.text) ) {
            response.notes = "Text contains mix of Cyrillic and Lating which may produce suboptimal results"
        }
    }

    def testLatCyrMix(text) {
        return text =~ /[а-яіїєґА-ЯІЇЄҐ]['’ʼ-]?[a-zA-Z]|[a-zA-Z]['’ʼ-]?[а-яіїєґА-ЯІЇЄҐ]/
    }


}
