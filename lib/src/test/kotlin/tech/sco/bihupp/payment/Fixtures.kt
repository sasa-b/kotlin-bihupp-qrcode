package tech.sco.bihupp.payment

internal object Fixtures {
    fun sender(name: Name = Name.of("Marko", "Marković")) = Sender(
        name = name,
        address = Address(
            addressLine1 = AddressLine1.of("Ulica Meše Selimovića", "12"),
            addressLine2 = AddressLine2.of("78000", "Banja Luka"),
        ),
        account = Account("1234567890123456"),
    )

    fun recipient(name: Name = Name.of("Pero", "Perić")) = Recipient(
        name = name,
        address = Address(
            addressLine1 = AddressLine1.of("Titova", "1"),
            addressLine2 = AddressLine2.of("71000", "Sarajevo"),
        ),
        account = RecipientAccount.of(Account("9876543210987654")),
    )
}
