package andres.personal.matchat.models

class MatchMove {

    var desc: String? = null
    var gameid: String? = null
    var playerId: String? = null
    var points: Int? = null
    var turno: Int? = null

    constructor() {}
    constructor(desc: String?, gameid: String?, playerId: String?, points: Int?, turno: Int?) {
        this.desc = desc
        this.gameid = gameid
        this.playerId = playerId
        this.points = points
        this.turno = turno
    }
}