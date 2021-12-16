package sparkJava;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jooq.DSLContext;

import org.jooq.SQLDialect;

import org.jooq.impl.DSL;

import static spark.Spark.post;
import static sparkJava.generated.tables.Refresh.REFRESH;
import static sparkJava.generated.tables.Article.ARTICLE;
import static sparkJava.generated.tables.Author.AUTHOR;
import sparkJava.generated.tables.records.AuthorRecord;


public class SampleApi {

    public static void main(String[] args) throws Exception {
        // GenerationTool.generate(Files.readString(Path.of("refresh.xml")));

        String userName = "postgres";
        String password = "password";
        String url = "jdbc:postgresql://localhost:5432/postgres";
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(url, userName, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DSLContext dsl = DSL.using(conn, SQLDialect.POSTGRES);

        //CREATE
        post("/author", (request, response) -> {

            String first_name = request.queryParams("first_name");
            String last_name = request.queryParams("last_name");
            int age = Integer.parseInt(request.queryParams("age"));

            AuthorRecord authorRecord = dsl.newRecord(AUTHOR);
            authorRecord.setFirstName(first_name);
            authorRecord.setLastName(last_name);
            authorRecord.setAge(age);
            authorRecord.setId(8);
            authorRecord.store();

            return authorRecord;

        });

        // READ
        get("/author", (request, response) -> {

            List todoList = dsl.select(AUTHOR.ID, AUTHOR.FIRST_NAME, AUTHOR.LAST_NAME, AUTHOR.AGE)
                    .from(AUTHOR)
                    .fetchInto(AuthorRecord.class);

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

        });

        get("/ping", (request, response) -> {

            dsl.update(REFRESH)
                    .set(REFRESH.COUNTER, REFRESH.COUNTER.plus(1))
                    .where(REFRESH.ID.eq(1))
                    .execute();
            return "pong";
        });
    }
}