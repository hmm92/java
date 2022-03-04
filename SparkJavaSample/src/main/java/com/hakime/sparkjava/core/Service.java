package com.hakime.sparkjava.core;

import com.google.gson.Gson;
import io.github.cdimascio.dotenv.Dotenv;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import spark.Request;
import spark.Response;

import javax.ws.rs.NotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import static jooq.generated.tables.Author.AUTHOR;
import static jooq.generated.tables.Book.BOOK;
import static jooq.generated.tables.BookRequest.BOOK_REQUEST;
import static spark.Spark.exception;
import static spark.Spark.*;

public class Service {

    Dotenv dotenv = Dotenv.load();
    String userName = dotenv.get("DATASOURCE_USERNAME");
    String password = dotenv.get("DATASOURCE_PASSWORD");
    String url = dotenv.get("DATASOURCE_JDBC_URL");
    Connection conn = null;
    DSLContext context = null;

    public void init() {

        exception(NotFoundException.class, (e, request, response) -> {
            response.status(404);
            response.body("id not found");
        });

        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        context = DSL.using(conn, SQLDialect.POSTGRES);

        post("/author", (request, response) -> authorCreate(request, response));

        get("/author", (request, response) -> authorAll(response));

        get("/author/:page/:per-page", (request, response) -> authorPage(request, response));

        put("/author/:id", (request, response) -> authorUpdate(request, response));

        delete("/author/:id", (request, response) -> authorDelete(request, response));

        get("/author/book", (request, response) -> authorBook(response));

        post("/book-request", (request, response) -> bookRequest(request, response));

        post("/admin/book-arrived", (request, response) -> bookArrived(request));


    }

    private String authorCreate(Request request, Response response) {
        Author author = new Gson().fromJson(request.body(), Author.class);
        System.out.println(author.getFirstName());
        try {
            context.insertInto(AUTHOR)
                    .set(AUTHOR.FIRST_NAME, author.getFirstName())
                    .execute();
            return new Gson()
                    .toJson(new StandardResponse(StatusResponse.SUCCESS));

        } catch (IllegalArgumentException e) {
            response.status(400);
            return null;

        }
    }


    private String authorAll(Response response) {

        List<Author> todoList = context.select(AUTHOR.ID, AUTHOR.FIRST_NAME)
                .from(AUTHOR)
                .fetchInto(Author.class);

        response.type("application/json");
        return new Gson().toJson(
                new StandardResponse(StatusResponse.SUCCESS, new Gson()
                        .toJsonTree(todoList)));

    }


    private String authorPage(Request request, Response response) {
        int pageNumber = Integer.parseInt(request.params("page")); // use per-page
        int RowsOfPage = Integer.parseInt(request.params("per-page"));

        List<Author> todoList = context.select(AUTHOR.ID, AUTHOR.FIRST_NAME)
                .from(AUTHOR)
                .orderBy(AUTHOR.ID).limit(RowsOfPage).offset((pageNumber - 1) * RowsOfPage)
                .fetchInto(Author.class);

        response.type("application/json");
        return new Gson().toJson(
                new StandardResponse(StatusResponse.SUCCESS, new Gson()
                        .toJsonTree(todoList)));

    }

    private String authorUpdate(Request request, Response response) {
        int id = Integer.parseInt(request.params("id"));
        Author author = new Gson().fromJson(request.body(), Author.class);
        response.type("application/json");

        Author todoItem = context
                .select(AUTHOR.ID, AUTHOR.FIRST_NAME)
                .from(AUTHOR)
                .where(AUTHOR.ID.equal(id))
                .fetchOneInto(Author.class);
        if (todoItem == null) {
            return new Gson().toJson(
                    new StandardResponse(StatusResponse.ERROR, new Gson()
                            .toJson("Author not found or error in edit")));
        }
        context.update(AUTHOR)
                .set(AUTHOR.FIRST_NAME, author.getFirstName())
                .where(AUTHOR.ID.eq(id))
                .execute();

        return new Gson().toJson(
                new StandardResponse(StatusResponse.SUCCESS));
    }

    private String authorDelete(Request request, Response response) {
        response.type("application/json");
        int id = Integer.parseInt(request.params("id"));
        context.delete(AUTHOR)
                .where(AUTHOR.ID.eq(id))
                .execute();
        return new Gson().toJson(
                new StandardResponse(StatusResponse.SUCCESS, "Author deleted"));
    }

    //join
    private String authorBook(Response response) {

        List<AuthorBook> todoList = context.select(AUTHOR.FIRST_NAME, AUTHOR.ID, BOOK.TITLE)
                .select(BOOK.fields())
                .from(BOOK)
                .join(AUTHOR).on(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                .fetchInto(AuthorBook.class);

        response.type("application/json");
        return new Gson().toJson(
                new StandardResponse(StatusResponse.SUCCESS, new Gson()
                        .toJsonTree(todoList)));
    }

    private String bookRequest(Request request, Response response) {

        BookRequest book = new Gson().fromJson(request.body(), BookRequest.class);
        AuthorBook item = context.select(AUTHOR.FIRST_NAME, AUTHOR.ID, BOOK.TITLE)
                .select(BOOK.ID, BOOK.TITLE, BOOK.AUTHOR_ID)
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

        response.type("application/json");

        return new Gson().toJson(
                new StandardResponse(StatusResponse.SUCCESS));

    }


    private String bookArrived(Request request) {
        BookRequest book = new Gson().fromJson(request.body(), BookRequest.class);
        List<BookRequest> todoList = context.select(BOOK_REQUEST.EMAIL, BOOK_REQUEST.FIRST_NAME, BOOK_REQUEST.TITLE)
                .from(BOOK_REQUEST)
                .where(BOOK_REQUEST.FIRST_NAME.eq(book.getFirstName()).and(BOOK_REQUEST.TITLE.eq(book.getTitle())))
                .fetchInto(BookRequest.class);

        for (BookRequest item : todoList) {
//            System.out.println(item.getEmail());
            //send email
        }
        return new Gson().toJson(
                new StandardResponse(StatusResponse.SUCCESS));
    }

}
