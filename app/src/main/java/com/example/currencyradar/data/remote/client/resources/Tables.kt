package com.example.currencyradar.data.remote.client.resources

import com.example.currencyradar.domain.models.TableType
import io.ktor.resources.Resource

@Resource("/tables")
class Tables {
    @Resource("{table}")
    class Table(
        val parent: Tables = Tables(),
        val table: TableType,
    )
}
