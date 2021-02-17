package kz.asaheyt.inc.autopartsstore.http.util

import kz.asaheyt.inc.autopartsstore.http.model.{AutoPart, Summary}

trait Codec {

  implicit val autoPartEncodeDecode: EncoderDecoder[AutoPart] = DerivedEncoderDecoder[AutoPart]

  implicit val summaryEncodeDecode: EncoderDecoder[Summary] = DerivedEncoderDecoder[Summary]

}
