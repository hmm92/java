import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import org.jooq.*;
import org.jooq.codegen.GenerationTool;
import org.jooq.impl.DSL;

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


        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DSLContext dsl = DSL.using(conn, SQLDialect.POSTGRES);

        //CREATE
        post("/author", (request, response) -> {
            String first_name = request.queryParams("first_name");
            int id = Integer.parseInt(request.queryParams("id"));
            dsl.insertInto(AUTHOR,
                            AUTHOR.ID, AUTHOR.FIRST_NAME)
                    .values(id, first_name)
                    .execute();
            return "record inserted";

        });


        // READ
        get("/author", (request, response) -> {

            List<Author> todoList = dsl.select(AUTHOR.ID, AUTHOR.FIRST_NAME)
                    .from(AUTHOR)
                    .fetchInto(Author.class);

            int page = Integer.parseInt(request.queryParams("page"));
            int perPage = Integer.parseInt(request.queryParams("per-page"));


            int size = todoList.size();
            int startIndex = page * perPage - perPage;
            int endIndex = startIndex + perPage;
            System.out.println("StartIndex = " + startIndex);
            System.out.println("endIndex = " + endIndex);


            List newList = new ArrayList();
            if (startIndex <= size) {
                if (endIndex < size) {

                    newList.addAll(todoList.subList(startIndex, endIndex));
                } else {
                    newList.addAll(todoList.subList(startIndex, size));

                }


                return newList;

            }
            return "no item";

        }, gson::toJson);

       // READ ALL
        get("/authorall", (request, response) -> {

            List<Author> todoList = dsl.select(AUTHOR.ID, AUTHOR.FIRST_NAME)
                    .from(AUTHOR)
                    .fetchInto(Author.class);

            return todoList;

        }, gson::toJson);



        //UPDATE
        put("/author", (request, response) -> {
            String first_name = request.queryParams("first_name");
            int id = Integer.parseInt(request.queryParams("id"));

            List<Author> todoList  = dsl
                    .selectFrom(AUTHOR)
                    .where(AUTHOR.ID.equal(id))
                    .fetchInto(Author.class);
            //if found
            if (!todoList.isEmpty()) {

            dsl.update(AUTHOR)
                    .set(AUTHOR.FIRST_NAME,first_name)
                    .where(AUTHOR.ID.eq(id))
                    .execute();
                return "Example updated";

            }
            else {
                return "Example dosent exist";
            }

        });

        //DELETE
        delete("/author", (request, response) -> {
            int id = Integer.parseInt(request.queryParams("id"));

            dsl.delete(AUTHOR)
                    .where(AUTHOR.ID.eq(id))
                    .execute();

             return "delete";
        });



    }
}