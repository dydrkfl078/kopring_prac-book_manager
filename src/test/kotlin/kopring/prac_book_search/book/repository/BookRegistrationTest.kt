package kopring.prac_book_search.book.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import kopring.prac_book_search.domain.book.domain.Book
import kopring.prac_book_search.domain.book.repository.BookRepository
import kopring.prac_book_search.domain.book.repository.BookRepositoryImpl
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import javax.sql.DataSource

private val logger = KotlinLogging.logger {  }

@SpringBootTest
@Transactional
class BookRegistrationTest {

    private lateinit var bookRepository: BookRepository

    @Autowired
    private lateinit var dataSource : DataSource

    companion object {
        val book1 = Book("책 제목", "작자미상", LocalDate.now(), 18000, null, 1)
        val book2 = Book("책 제목", "작자미상", LocalDate.now(), 18000, null, 2)
        val book3 = Book("책 제목", "작자미상", LocalDate.now(), 18000, null, 3)
    }

    @BeforeEach
    fun init() {
        bookRepository = BookRepositoryImpl(dataSource)
    }

    @AfterEach
    fun afterEach(){
        bookRepository.deleteBook(1)
    }

    @Test
    @DisplayName("DB에 책 등록하기")
    fun createBook() {

        bookRepository.createBook(book1) shouldBe book1
    }

    @Test
    @DisplayName("id로 등록된 책 찾기")
    fun findBooksById() {
        val id = 1
        bookRepository.createBook(book1)

        bookRepository.getBookById(id).title shouldBe "책 제목"
    }

    @Test
    fun exFindBookById() {
        val id = 94930
        bookRepository.createBook(book1)

        shouldThrow<NoSuchElementException> { bookRepository.getBookById(id) }
    }

    @Test
    fun findAllBooks() {
        bookRepository.createBook(book1)
        bookRepository.createBook(book2)
        bookRepository.createBook(book3)

        bookRepository.getAllBooks(0).size shouldBe 3

        bookRepository.deleteBook(2)
        bookRepository.deleteBook(3)
    }

    @Test
    fun updateBook() {
        bookRepository.createBook(book1)

        val id = 1
        val newBook = Book("수정된 책 제목", "작자미상", LocalDate.now(), 16000, null, id)
        bookRepository.updateBook(id, newBook)

        bookRepository.getBookById(id) shouldBe newBook
    }

    @Test
    fun delete() {
        bookRepository.createBook(book1)

        val id = 1

        bookRepository.deleteBook(id)

        bookRepository.getAllBooks(0) shouldBe emptyList()
    }
}