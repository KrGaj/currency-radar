package com.example.currencyradar.data.remote.client.resources

import com.example.currencyradar.domain.models.TableType
import io.ktor.resources.Resource
import kotlinx.datetime.LocalDate

@Resource("/rates")
class Rates {
    @Resource("/{table}")
    class Table(
        val parent: Rates = Rates(),
        val table: TableType,
    ) {
        @Resource("/{code}")
        class Code(
            val parent: Table,
            val code: String,
        ) {
            @Resource("/{startDate}/{endDate}")
            class DateRange(
                val parent: Code,
                val startDate: LocalDate,
                val endDate: LocalDate,
            )
        }
    }
}
