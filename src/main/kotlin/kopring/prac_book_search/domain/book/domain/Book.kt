package kopring.prac_book_search.domain.book.domain

import java.time.LocalDate

data class Book(
    val title : String,
    val author : String,
    val publishedDate : LocalDate,
    val price : Int,
    val thumbnail : String? = null,
    val id : Int? = null,
)
