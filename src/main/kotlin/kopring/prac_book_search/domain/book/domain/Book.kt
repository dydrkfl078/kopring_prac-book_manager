package kopring.prac_book_search.domain.book.domain

import org.springframework.web.multipart.MultipartFile
import java.time.LocalDate

data class Book(
    val title : String,
    val author : String,
    val publishedDate : LocalDate,
    val price : Int,
    val bookCover : MultipartFile? = null,
    val id : Int? = null,
)
