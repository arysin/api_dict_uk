package dict_uk


import grails.rest.*
import grails.converters.*

//import io.swagger.annotations.*
import com.wordnik.swagger.annotations.*


@Api(value = "Dictionary search services", 
    description = "Dictionary services for Ukrainian language",
    produces = 'application/json',
    consumes = 'application/json'
)
class SearchController {
    static responseFormats = ['json']
    static allowedMethods = [save: "POST"]
    static defaultAction = "save"
    static TEXT_LIMIT = 80

    def searchService

    @ApiOperation(value = "Searches the work in the dictionary", 
                httpMethod = "GET",
				responseContainer = "List",
                response = Article.class)
    @ApiResponses([
        @ApiResponse(code = 400, message = "Invalid parameter provided"),
        @ApiResponse(code = 404, message = "Word not found"),
    ])
    @ApiImplicitParams([
        @ApiImplicitParam(name = 'word', paramType = 'query', required = true, dataType='string', value='Word to search'),
    ])
    def index() {

        if( ! validateRequest(request) )
            return

        try {
            List<Article> articles = searchService.findWord(params.word, params)
            
            if( ! articles ) {
                render(status: 404)
                return
            }
            
            render articles as JSON
        }
        catch(Exception e) {
            e.printStackTrace()
            render(status: 500, text: "Internal error: " + e.getMessage())
            return
        }
    }

    @ApiOperation(value = "Searches the word neighbors in the dictionary", 
                httpMethod = "GET",
				responseContainer = "List",
                response = String.class)
    @ApiResponses([
        @ApiResponse(code = 400, message = "Invalid parameter provided"),
    ])
    @ApiImplicitParams([
        @ApiImplicitParam(name = 'word', paramType = 'query', required = true, dataType='string', value='Word to search'),
    ])
	def findNeighbors() {

		if( ! validateRequest(request) )
			return

		try {
			List<String> neighbors = searchService.findNeighbors(params.word, params)

			render neighbors as JSON
		}
		catch(Exception e) {
			e.printStackTrace()
			render(status: 500, text: "Internal error: " + e.getMessage())
			return
		}
	}

	
    def validateRequest(request) {
        if( ! params.word ) {
            render(status: 400, text: "\"word\" field not specified in the request")
            return false
        }

//        if( ! request.JSON?.flags ) {
//            render(status: 400, text: "\"flags\" field not specified in the request")
//            return false
//        }

        if( params.word.size() > TEXT_LIMIT ) {
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
