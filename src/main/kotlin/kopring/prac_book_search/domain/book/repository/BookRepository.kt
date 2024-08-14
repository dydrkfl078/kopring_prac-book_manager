package kopring.prac_book_search.domain.book.repository

import kopring.prac_book_search.domain.book.domain.Book

interface BookRepository {
    fun createBook(book: Book): Book
    fun getBookById(id: Int): Book
    fun getAllBooks(offset: Int): List<Book>
    fun updateBook(id: Int, book: Book): Book
    fun deleteBook(id: Int)
}