package nfl.bet

class Round {

		Date startDate
		Date finalDate
		Date betLimitDate

		static belongsTo = [championship: Championship]

    static constraints = {
    }
}
