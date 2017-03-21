package nfl.bet

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class RoundController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Round.list(params), model:[roundCount: Round.count()]
    }

    def show(Round round) {
        respond round
    }

    def create() {
        respond new Round(params)
    }

    @Transactional
    def save(Round round) {
        if (round == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (round.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond round.errors, view:'create'
            return
        }

        round.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'round.label', default: 'Round'), round.id])
                redirect round
            }
            '*' { respond round, [status: CREATED] }
        }
    }

    def edit(Round round) {
        respond round
    }

    @Transactional
    def update(Round round) {
        if (round == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (round.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond round.errors, view:'edit'
            return
        }

        round.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'round.label', default: 'Round'), round.id])
                redirect round
            }
            '*'{ respond round, [status: OK] }
        }
    }

    @Transactional
    def delete(Round round) {

        if (round == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        round.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'round.label', default: 'Round'), round.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'round.label', default: 'Round'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
