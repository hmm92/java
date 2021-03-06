/*
 * This file is generated by jOOQ.
 */
package sparkJava.generated;


import org.jooq.Identity;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import sparkJava.generated.tables.Author;
import sparkJava.generated.tables.Book;
import sparkJava.generated.tables.BookRequest;
import sparkJava.generated.tables.FlywaySchemaHistory;
import sparkJava.generated.tables.records.AuthorRecord;
import sparkJava.generated.tables.records.BookRecord;
import sparkJava.generated.tables.records.BookRequestRecord;
import sparkJava.generated.tables.records.FlywaySchemaHistoryRecord;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------

    public static final Identity<AuthorRecord, Integer> IDENTITY_AUTHOR = Identities0.IDENTITY_AUTHOR;
    public static final Identity<BookRecord, Integer> IDENTITY_BOOK = Identities0.IDENTITY_BOOK;
    public static final Identity<BookRequestRecord, Integer> IDENTITY_BOOK_REQUEST = Identities0.IDENTITY_BOOK_REQUEST;

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = UniqueKeys0.FLYWAY_SCHEMA_HISTORY_PK;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class Identities0 {
        public static Identity<AuthorRecord, Integer> IDENTITY_AUTHOR = Internal.createIdentity(Author.AUTHOR, Author.AUTHOR.ID);
        public static Identity<BookRecord, Integer> IDENTITY_BOOK = Internal.createIdentity(Book.BOOK, Book.BOOK.ID);
        public static Identity<BookRequestRecord, Integer> IDENTITY_BOOK_REQUEST = Internal.createIdentity(BookRequest.BOOK_REQUEST, BookRequest.BOOK_REQUEST.ID);
    }

    private static class UniqueKeys0 {
        public static final UniqueKey<FlywaySchemaHistoryRecord> FLYWAY_SCHEMA_HISTORY_PK = Internal.createUniqueKey(FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY, "flyway_schema_history_pk", new TableField[] { FlywaySchemaHistory.FLYWAY_SCHEMA_HISTORY.INSTALLED_RANK }, true);
    }
}
