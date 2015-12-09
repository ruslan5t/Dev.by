package by.dev.android.domain

import java.util.*

data class News(var id: Long? = null, var title: String? = null, var pubDate: Date? = null,
                var description: String? = null, var fullText: String? = null, var link: String? = null,
                var guid: String? = null, var enclosures: MutableList<Enclosure> = ArrayList<Enclosure>())