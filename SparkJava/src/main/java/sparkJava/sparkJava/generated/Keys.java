/*
 * This file is generated by jOOQ.
 */
package sparkJava.generated;


import org.jooq.ForeignKey;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.Internal;

import sparkJava.generated.tables.Article;
import sparkJava.generated.tables.Author;
import sparkJava.generated.tables.Refresh;
import sparkJava.generated.tables.records.ArticleRecord;
import sparkJava.generated.tables.records.AuthorRecord;
import sparkJava.generated.tables.records.RefreshRecord;


/**
 * A class modelling foreign key relationships and constraints of tables of 
 * the <code>public</code> schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ArticleRecord> ARTICLE_PKEY = UniqueKeys0.ARTICLE_PKEY;
    public static final UniqueKey<AuthorRecord> AUTHOR_PKEY = UniqueKeys0.AUTHOR_PKEY;
    public static final UniqueKey<RefreshRecord> REFRESH_PKEY = UniqueKeys0.REFRESH_PKEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ArticleRecord, AuthorRecord> ARTICLE__FK_AUTHOR_ID = ForeignKeys0.ARTICLE__FK_AUTHOR_ID;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 {
        public static final UniqueKey<ArticleRecord> ARTICLE_PKEY = Internal.createUniqueKey(Article.ARTICLE, "article_pkey", new TableField[] { Article.ARTICLE.ID }, true);
        public static final UniqueKey<AuthorRecord> AUTHOR_PKEY = Internal.createUniqueKey(Author.AUTHOR, "author_pkey", new TableField[] { Author.AUTHOR.ID }, true);
        public static final UniqueKey<RefreshRecord> REFRESH_PKEY = Internal.createUniqueKey(Refresh.REFRESH, "refresh_pkey", new TableField[] { Refresh.REFRESH.ID }, true);
    }

    private static class ForeignKeys0 {
        public static final ForeignKey<ArticleRecord, AuthorRecord> ARTICLE__FK_AUTHOR_ID = Internal.createForeignKey(Keys.AUTHOR_PKEY, Article.ARTICLE, "fk_author_id", new TableField[] { Article.ARTICLE.AUTHOR_ID }, true);
    }
}
