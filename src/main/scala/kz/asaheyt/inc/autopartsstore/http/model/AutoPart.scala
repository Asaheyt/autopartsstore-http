package kz.asaheyt.inc.autopartsstore.http.model

case class AutoPart(autoPartId: Long,
                     name: String,
                     price: Double,
                     quantity: Int,
                     customerId: Long,
                     totalPrice: Double)