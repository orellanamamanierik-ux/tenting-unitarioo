package backend.etec.service

data class Item(
    val nombre: String,
    val precio: Int,
)

class OrdenDeCompra() {
    val items = mutableListOf<Item>()
    fun agregarItem(unItem: Item) {
        this.items.add(unItem)
    }

    fun calcularTotal(): Int {
        return this.items.sumOf { it.precio }
    }
}
