package nfl.bet

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class ChampionshipController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Championship.list(params), model:[championshipCount: Championship.count()]
    }

    def show(Championship championship) {
        respond championship
    }

    def create() {
        respond new Championship(params)
    }

    @Transactional
    def save(Championship championship) {
        if (championship == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (championship.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond championship.errors, view:'create'
            return
        }

        championship.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'championship.label', default: 'Championship'), championship.id])
                redirect championship
            }
            '*' { respond championship, [status: CREATED] }
        }
    }

    def edit(Championship championship) {
        respond championship
    }

    @Transactional
    def update(Championship championship) {
        if (championship == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (championship.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond championship.errors, view:'edit'
            return
        }

        championship.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'championship.label', default: 'Championship'), championship.id])
                redirect championship
            }
            '*'{ respond championship, [status: OK] }
        }
    }

    @Transactional
    def delete(Championship championship) {

        if (championship == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        championship.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'championship.label', default: 'Championship'), championship.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'championship.label', default: 'Championship'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
