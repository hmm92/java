/*
 * This file is generated by jOOQ.
 */
package sparkJava.generated.tables;


import java.util.Arrays;
import java.util.List;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Name;
import org.jooq.Record;
import org.jooq.Row4;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.TableOptions;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.TableImpl;

import sparkJava.generated.Keys;
import sparkJava.generated.Public;
import sparkJava.generated.tables.records.ArticleRecord;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Article extends TableImpl<ArticleRecord> {

    private static final long serialVersionUID = 879125966;

    /**
     * The reference instance of <code>public.article</code>
     */
    public static final Article ARTICLE = new Article();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ArticleRecord> getRecordType() {
        return ArticleRecord.class;
    }

    /**
     * The column <code>public.article.id</code>.
     */
    public final TableField<ArticleRecord, Integer> ID = createField(DSL.name("id"), org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.article.title</code>.
     */
    public final TableField<ArticleRecord, String> TITLE = createField(DSL.name("title"), org.jooq.impl.SQLDataType.VARCHAR(255).nullable(false), this, "");

    /**
     * The column <code>public.article.description</code>.
     */
    public final TableField<ArticleRecord, String> DESCRIPTION = createField(DSL.name("description"), org.jooq.impl.SQLDataType.VARCHAR(255), this, "");

    /**
     * The column <code>public.article.author_id</code>.
     */
    public final TableField<ArticleRecord, Integer> AUTHOR_ID = createField(DSL.name("author_id"), org.jooq.impl.SQLDataType.INTEGER, this, "");

    /**
     * Create a <code>public.article</code> table reference
     */
    public Article() {
        this(DSL.name("article"), null);
    }

    /**
     * Create an aliased <code>public.article</code> table reference
     */
    public Article(String alias) {
        this(DSL.name(alias), ARTICLE);
    }

    /**
     * Create an aliased <code>public.article</code> table reference
     */
    public Article(Name alias) {
        this(alias, ARTICLE);
    }

    private Article(Name alias, Table<ArticleRecord> aliased) {
        this(alias, aliased, null);
    }

    private Article(Name alias, Table<ArticleRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    public <O extends Record> Article(Table<O> child, ForeignKey<O, ArticleRecord> key) {
        super(child, key, ARTICLE);
    }

    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    @Override
    public UniqueKey<ArticleRecord> getPrimaryKey() {
        return Keys.ARTICLE_PKEY;
    }

    @Override
    public List<UniqueKey<ArticleRecord>> getKeys() {
        return Arrays.<UniqueKey<ArticleRecord>>asList(Keys.ARTICLE_PKEY);
    }

    @Override
    public List<ForeignKey<ArticleRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ArticleRecord, ?>>asList(Keys.ARTICLE__FK_AUTHOR_ID);
    }

    public Author author() {
        return new Author(this, Keys.ARTICLE__FK_AUTHOR_ID);
    }

    @Override
    public Article as(String alias) {
        return new Article(DSL.name(alias), this);
    }

    @Override
    public Article as(Name alias) {
        return new Article(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public Article rename(String name) {
        return new Article(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public Article rename(Name name) {
        return new Article(name, null);
    }

    // -------------------------------------------------------------------------
    // Row4 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row4<Integer, String, String, Integer> fieldsRow() {
        return (Row4) super.fieldsRow();
    }
}
