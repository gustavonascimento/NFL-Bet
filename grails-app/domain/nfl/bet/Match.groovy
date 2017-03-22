package nfl.bet

class Match {

    Date date
    String stadium
    String winner
    int betLimitDate

    static belongsTo = [round: Round]
    static hasMany = [teams: Team, bets: Bet]

    static constraints = {
    }
}
