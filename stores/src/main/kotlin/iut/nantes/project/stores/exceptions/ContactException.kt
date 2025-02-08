package iut.nantes.project.stores.exceptions

import java.lang.RuntimeException


// caught when contact not found (HTTP 404)
class ContactNotFoundException() : RuntimeException()

// caught when the email of  contact is invalid
class EmailNotValidExeception() : RuntimeException()

