package nfl.bet

import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class BetController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Bet.list(params), model:[betCount: Bet.count()]
    }

    def show(Bet bet) {
        respond bet
    }

    def create() {
        respond new Bet(params)
    }

    @Transactional
    def save(Bet bet) {
        if (bet == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (bet.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond bet.errors, view:'create'
            return
        }

        bet.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'bet.label', default: 'Bet'), bet.id])
                redirect bet
            }
            '*' { respond bet, [status: CREATED] }
        }
    }

    def edit(Bet bet) {
        respond bet
    }

    @Transactional
    def update(Bet bet) {
        if (bet == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        if (bet.hasErrors()) {
            transactionStatus.setRollbackOnly()
            respond bet.errors, view:'edit'
            return
        }

        bet.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'bet.label', default: 'Bet'), bet.id])
                redirect bet
            }
            '*'{ respond bet, [status: OK] }
        }
    }

    @Transactional
    def delete(Bet bet) {

        if (bet == null) {
            transactionStatus.setRollbackOnly()
            notFound()
            return
        }

        bet.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'bet.label', default: 'Bet'), bet.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'bet.label', default: 'Bet'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
