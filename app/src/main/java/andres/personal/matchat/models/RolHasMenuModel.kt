package andres.personal.matchat.models

class RolHasMenuModel {

    var menuid: String? = null
    var rolid: String? = null
    var uid: String? = null

    constructor() {}
    constructor(menuid: String?, rolid: String?, uid: String?) {
        this.menuid = menuid
        this.rolid = rolid
        this.uid = uid
    }


}