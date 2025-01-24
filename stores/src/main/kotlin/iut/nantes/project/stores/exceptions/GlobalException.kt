package iut.nantes.project.stores.exceptions

import java.lang.RuntimeException

// Exception levée si l'ID est invalide (HTTP 400)
class InvalidIdFormatException(): RuntimeException()

// Exception levée lors d'un conflit (HTTP 409)
class ConflictException(): RuntimeException()