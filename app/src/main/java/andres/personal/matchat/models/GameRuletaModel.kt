package andres.personal.matchat.models

class GameRuletaModel : GameModel {
    var turno: String? = null
    var empezada: Boolean? = null

    constructor() : super() {}
    constructor(
        gameModeid: String?,
        fecCreacion: String?,
        users: String?,
        turno: String?
    ) : super(gameModeid, fecCreacion, users) {
        this.turno = turno
    }
}