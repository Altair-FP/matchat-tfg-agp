package andres.personal.matchat.models

class RolModel {

    var accion: String? = null
    var uid: String? = null

    constructor() {}
    constructor(accion: String?, uid: String?) {
        this.accion = accion
        this.uid = uid
    }
}