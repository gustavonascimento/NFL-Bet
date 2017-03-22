package nfl.bet

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class MatchController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Match.list(params), model:[matchCount: Match.count()]
    }

    def show(Match match) {
        respond match
    }

    def create() {
        respond new Match(params)
    }

    @Transactional
    def save(Match match) {
        if (match == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (match.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond match.errors, view:'create'
            return
        }

        match.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'match.label', default: 'Match'), match.id])
                redirect match
            }
            '*' { respond match, [status: CREATED] }
        }
    }

    def edit(Match match) {
        respond match
    }

    @Transactional
    def update(Match match) {
        if (match == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (match.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond match.errors, view:'edit'
            return
        }

        match.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'match.label', default: 'Match'), match.id])
                redirect match
            }
            '*'{ respond match, [status: OK] }
        }
    }

    @Transactional
    def delete(Match match) {

        if (match == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        match.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'match.label', default: 'Match'), match.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'match.label', default: 'Match'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
