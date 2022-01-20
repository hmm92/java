import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import com.google.gson.Gson;
import org.jooq.*;
import org.jooq.codegen.GenerationTool;
import org.jooq.impl.DSL;

import javax.ws.rs.NotFoundException;

import static spark.Spark.*;
import static sparkJava.generated.tables.Author.AUTHOR;


public class SampleApi {

    public static void main(String[] args) throws Exception {

        GenerationTool.generate(Files.readString(Path.of("refresh.xml")));
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
        post("/author/:first-name", (request, response) -> {
            String firstName = request.params(":first-name");

            context.insertInto(AUTHOR)
                    .set(AUTHOR.FIRST_NAME, firstName)
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
        put("/author/:id/:first-name", (request, response) -> {
            String firstName = request.params("first-name"); //path param
            int id = Integer.parseInt(request.params("id"));

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
                    .set(AUTHOR.FIRST_NAME, firstName)
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


    }
}
//add book table join author
//select join tables