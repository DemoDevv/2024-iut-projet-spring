package iut.nantes.project.stores.controllers.entities

import iut.nantes.project.stores.controllers.dto.Address
import iut.nantes.project.stores.controllers.dto.ContactDto
import jakarta.persistence.*

@Entity
@Table(name = "contact_table")
class ContactEntity(
    @Id @GeneratedValue @Column(name = "contact_id")
    var id: Long?,
    var email: String,
    var phone: String,
    @Embedded
    var address: Address,
) {
    constructor() : this(0L, "", "", Address())

    fun toDto(): ContactDto {
        return ContactDto(this.id!!, this.email, this.phone, this.address)
    }
}