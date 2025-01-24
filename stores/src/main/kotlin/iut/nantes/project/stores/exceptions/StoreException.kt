package iut.nantes.project.stores.exceptions

import java.lang.RuntimeException

// Exception levée lorsqu'un store n'est pas trouvé (HTTP 404)
class StoreNotFoundException(): RuntimeException()