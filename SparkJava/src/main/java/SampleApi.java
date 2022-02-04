import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import org.jooq.*;
import org.jooq.codegen.GenerationTool;
import org.jooq.impl.DSL;

import javax.ws.rs.NotFoundException;

import static spark.Spark.*;
import static sparkJava.generated.tables.Author.AUTHOR;
import static sparkJava.generated.tables.Book.BOOK;
import static sparkJava.generated.tables.BookRequest.BOOK_REQUEST;


public class SampleApi {
    private static Map<String, String> usernamePasswords = new HashMap<String, String>();

    public static void main(String[] args) throws Exception {

//        GenerationTool.generate(Files.readString(Path.of("refresh.xml")));
        String userName = "postgres";
        String password = "password";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        Connection conn = null;
        Gson gson = new Gson();
        exception(NotFoundException.class, (e, request, response) -> {
            response.status(404);
            response.body("id not found");
        });

        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DSLContext context = DSL.using(conn, SQLDialect.POSTGRES);




        //CREATE
        post("/author", (request, response) -> {
//            String firstName = request.queryParams("first-name");
            Author author = new Gson().fromJson(request.body(), Author.class);
            context.insertInto(AUTHOR)
                    .set(AUTHOR.FIRST_NAME, author.getFirstName())
                    .execute();
            return "record inserted";
        });


        // READ
        get("/author/:page/:per-page", (request, response) -> {
            int pageNumber = Integer.parseInt(request.params("page")); // use per-page
            int RowsOfPage = Integer.parseInt(request.params("per-page"));

            List<Author> todoList = context.select(AUTHOR.ID, AUTHOR.FIRST_NAME)
                    .from(AUTHOR)
                    .orderBy(AUTHOR.ID).limit(RowsOfPage).offset((pageNumber - 1) * RowsOfPage)
                    .fetchInto(Author.class);
            return todoList;
        }, gson::toJson);


        // READ ALL
        get("/author", (request, response) -> {

            List<Author> todoList = context.select(AUTHOR.ID, AUTHOR.FIRST_NAME)
                    .from(AUTHOR)
                    .fetchInto(Author.class);

            return todoList;
        }, gson::toJson);


        //UPDATE
        put("/author/:id", (request, response) -> {
//            String firstName = request.queryParams("first-name"); //path param
            int id = Integer.parseInt(request.params("id"));
            Author author = new Gson().fromJson(request.body(), Author.class);

            Author todoItem = context
                    .select(AUTHOR.ID,AUTHOR.FIRST_NAME)
                    .from(AUTHOR)
                    .where(AUTHOR.ID.equal(id))
                    .fetchOneInto(Author.class);
            System.out.println(todoItem);
            if (todoItem == null) {
                throw new NotFoundException("");
            }
            context.update(AUTHOR)
                    .set(AUTHOR.FIRST_NAME, author.getFirstName())
                    .where(AUTHOR.ID.eq(id))
                    .execute();
            return "item updated";
        });

        //DELETE
        delete("/author/:id", (request, response) -> {
            int id = Integer.parseInt(request.params("id"));
            context.delete(AUTHOR)
                    .where(AUTHOR.ID.eq(id))
                    .execute();

            return "item deleted";
        });

        //join
        get("/author-book", (request, response) -> {

            List<AuthorBook> todoList = context.select(AUTHOR.FIRST_NAME,AUTHOR.ID,BOOK.TITLE)
                    .select(BOOK.fields())
                    .from(BOOK)
                    .join(AUTHOR).on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                    .fetchInto(AuthorBook.class);

            return todoList;
        }, gson::toJson);


        post("/book-request", (request, response) -> {
//            String firstName = request.queryParams("first-name");
//            String title = request.queryParams("title");
//            String email = request.queryParams("email");
            BookRequest book = new Gson().fromJson(request.body(), BookRequest.class);

            AuthorBook item = context.select(AUTHOR.FIRST_NAME,AUTHOR.ID,BOOK.TITLE)
                    .select(BOOK.ID,BOOK.TITLE,BOOK.AUTHOR_ID)
                    .from(BOOK)
                    .join(AUTHOR).on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                    .where(AUTHOR.FIRST_NAME.eq(book.getFirstName()).and(BOOK.TITLE.eq(book.getTitle())))
                    .fetchOneInto(AuthorBook.class);

            if (item != null) {
                halt(403, "I don't think so!!!");
                return null;
            }

            context.insertInto(BOOK_REQUEST)
                    .set(BOOK_REQUEST.TITLE, book.getTitle())
                    .set(BOOK_REQUEST.FIRST_NAME, book.getFirstName())
                    .set(BOOK_REQUEST.EMAIL, book.getEmail())
                    .execute();
            return "record inserted";
        });




        usernamePasswords.put("admin", "admin");

        before("/admin/book-arrived",(request, response) -> {
            String user = request.queryParams("user");
            String pass = request.queryParams("pass");

            String dbPassword = usernamePasswords.get(user);
            if (!(pass!= null && pass.equals(dbPassword))) {
                halt(401, "You are not welcome here!!!");
            }
        });


        post("/admin/book-arrived", (request, response) -> {
//            String firstName = request.queryParams("first-name");
//            String title = request.queryParams("title");
            BookRequest book = new Gson().fromJson(request.body(), BookRequest.class);

            List<BookRequest> todoList = context.select(BOOK_REQUEST.EMAIL,BOOK_REQUEST.FIRST_NAME,BOOK_REQUEST.TITLE)
                    .from(BOOK_REQUEST)
                    .where(BOOK_REQUEST.FIRST_NAME.eq(book.getFirstName()).and(BOOK_REQUEST.TITLE.eq(book.getTitle())))
                    .fetchInto(BookRequest.class);

            for(BookRequest item : todoList) {
                System.out.println(item.getEmail());
                //send email
            }
            return todoList;
        }, gson::toJson);





    }
}
//add book table join author
//select join tables
//migration
//flyway
//restful api
//graphql
