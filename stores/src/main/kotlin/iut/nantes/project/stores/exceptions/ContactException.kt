package iut.nantes.project.stores.exceptions

import java.lang.RuntimeException


// Exception levée lorsqu'un contact n'est pas trouvé (HTTP 404)
class ContactNotFoundException() : RuntimeException()

// Exception levée lorsqu'un l'email du contact est invalide
class EmailNotValidExeception() : RuntimeException()

