package andres.personal.matchat.models
//esta es la clase  que tendran los mensajes
//constructor con parametros y vacio, y getters y setters
class Message {
    var message: String? = null
    var senderId: String? = null
    var chatid: String? = null

    constructor() {}
    constructor(message: String?, senderId: String?, chatRoom: String?) {
        this.message = message
        this.senderId = senderId
        this.chatid = chatRoom
    }
}