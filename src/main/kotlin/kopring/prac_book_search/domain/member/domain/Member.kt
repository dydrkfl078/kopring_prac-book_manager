package kopring.prac_book_search.domain.member.domain

data class Member(
    val id : String,
    val password : String,
    val nickname : String,
    val email : String,
    val uid : String?,
)
