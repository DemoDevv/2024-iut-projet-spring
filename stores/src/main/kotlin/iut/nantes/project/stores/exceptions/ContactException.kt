package iut.nantes.project.stores.exceptions

import java.lang.RuntimeException


// Exception levée lorsqu'un contact n'est pas trouvé (HTTP 404)
class ContactNotFoundException(): RuntimeException()

// Exception levée lorsqu'un l'email du contact est invalide
class EmailNotValidExeception(): RuntimeException()

//Exception levée lorsque le numéro de telephone est invalide
class PhoneNumberException(): RuntimeException()

//Exception levée lorsque la rue n'a pas assez ou trop de caractères
class StreetNotValidException(): RuntimeException()

//Exception levée lorque la ville n'est pas assez grande ou trop grande

class CityNotValidException(): RuntimeException()

//Exception levée lorsque la ville n'a pas un code postal valide
class PostalCodeNotValidException(): RuntimeException()

