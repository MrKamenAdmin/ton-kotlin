package org.ton.asm.arithmetic.basic

import org.ton.asm.Instruction
import org.ton.cell.CellBuilder
import org.ton.cell.CellSlice
import org.ton.tlb.TlbConstructor
import org.ton.tlb.providers.TlbConstructorProvider

object SUB : Instruction, TlbConstructorProvider<SUB> by SUBTlbConstructor {
    override fun toString(): String = "SUB"
}

private object SUBTlbConstructor : TlbConstructor<SUB>(
    schema = "asm_sub#a1 = SUB;",
    type = SUB::class
) {
    override fun storeTlb(cellBuilder: CellBuilder, value: SUB) {
    }

    override fun loadTlb(cellSlice: CellSlice): SUB = SUB
}