package iut.nantes.project.stores.controllers.entities

import iut.nantes.project.stores.controllers.dto.Address
import iut.nantes.project.stores.controllers.dto.ContactDto
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "contact_table")
class ContactEntity(
    @Id @GeneratedValue @Column(name = "contact_id")
    val id: Long?,
    var email: String,
    var phone: String,
    var address: Address,
) {
    constructor(): this(0L, "", "",  Address())

    fun toDto(): ContactDto {
        return ContactDto(this.id!!, this.email, this.phone, this.address)
    }
}