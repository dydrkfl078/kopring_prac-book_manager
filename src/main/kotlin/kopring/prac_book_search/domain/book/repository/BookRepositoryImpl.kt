package kopring.prac_book_search.domain.book.repository

import io.github.oshai.kotlinlogging.KotlinLogging
import kopring.prac_book_search.domain.book.domain.Book
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator
import org.springframework.stereotype.Repository
import java.sql.Date
import java.sql.SQLException
import javax.sql.DataSource
import kotlin.NoSuchElementException

private val logger = KotlinLogging.logger {  }

@Repository
class BookRepositoryImpl (
    private val dataSource: DataSource,

) : BookRepository {

    private val exTranslator = SQLErrorCodeSQLExceptionTranslator(dataSource)

    override fun createBook(book: Book): Book {
        val sql = "INSERT INTO books (title, author, published_date, price, id) VALUES (?, ?, ?, ?, ?)"

        try {
            dataSource.connection.use { con ->
                logger.info { "Connection metadata : ${con.metaData}" }
                con.prepareStatement(sql).use { pstmt ->
                    pstmt.apply {
                        setString(1, book.title)
                        setString(2, book.author)
                        setDate(3, Date.valueOf(book.publishedDate))
                        setInt(4, book.price)
                        setInt(5, book.id!!)
                    }
                    pstmt.executeUpdate()
                }
            }

        } catch (e: DataAccessException) {
            logger.info { "createBook Exception : $e" }
        }
        return book
    }

    override fun getBookById(id: Int): Book {
        val sql = "SELECT * FROM books WHERE id = ?"

        try {
            dataSource.connection.use { con ->
                con.prepareStatement(sql).use { pstmt ->
                    pstmt.apply {
                        setInt(1, id)
                    }
                    pstmt.executeQuery().use { rs ->
                        if (rs.next()) {
                            val result = Book(
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getDate("published_date").toLocalDate(),
                                rs.getInt("price"),
                                null,
                                rs.getInt("id")
                            )

                            return result
                        } else {
                            throw NoSuchElementException("not found id : $id")
                        }
                    }
                }
            }
        } catch (e: SQLException) {
            logger.info { "getBookById Exception : $e" }
            throw exTranslator.translate("findById",sql,e) ?: e
        }
    }

    override fun getAllBooks(offset: Int): List<Book> {
        val sql = "SELECT * FROM books ORDER BY id DESC LIMIT 10 OFFSET 0"

        try {
            dataSource.connection.use { con ->
                con.prepareStatement(sql).use { pstmt ->
                    pstmt.executeQuery().use { rs ->
                        val bookList = mutableListOf<Book>()
                        while (rs.next()) {
                            val result = Book(
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getDate("published_date").toLocalDate(),
                                rs.getInt("price"),
                                null,
                                rs.getInt("id")
                            )
                            bookList.add(result)
                        }

                        return bookList
                    }
                }
            }
        } catch (e: SQLException) {
            logger.info { "findAllBooks Exception : $e" }
            throw exTranslator.translate("findAllBooks", sql, e) ?: e
        }
    }

    override fun updateBook(id: Int, book: Book): Book {
        val sql = "UPDATE books SET title = ?, author = ?, published_date = ?, price = ? WHERE id = ?"

        try {
            dataSource.connection.use { con ->
                con.prepareStatement(sql).use { pstmt ->
                    pstmt.apply {
                        setString(1, book.title)
                        setString(2, book.author)
                        setDate(3, Date.valueOf(book.publishedDate))
                        setInt(4, book.price)
                        setInt(5, id)
                    }
                    pstmt.execute()

                    return book
                }
            }

        } catch (e: SQLException) {
            logger.info { "updateBook Exception : $e" }
            throw exTranslator.translate("update", sql, e) ?: e
        }
    }

    override fun deleteBook(id: Int) {
        val sql = "DELETE FROM books WHERE id = ?"

        try {
            dataSource.connection.use { con ->
                con.prepareStatement(sql).use { pstmt ->
                    pstmt.apply {
                        setInt(1, id)
                        executeUpdate()
                    }
                }
            }

        } catch (e: SQLException) {
            logger.info { "deleteBook Exception : $e" }
            throw exTranslator.translate("delete", sql, e) ?: e
        }
    }
}