/*
 * This file is generated by jOOQ.
 */
package sparkJava.generated;


import java.util.Arrays;
import java.util.List;

import org.jooq.Catalog;
import org.jooq.Table;
import org.jooq.impl.SchemaImpl;

import sparkJava.generated.tables.Article;
import sparkJava.generated.tables.Author;
import sparkJava.generated.tables.Refresh;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Public extends SchemaImpl {

    private static final long serialVersionUID = 1397704118;

    /**
     * The reference instance of <code>public</code>
     */
    public static final Public PUBLIC = new Public();

    /**
     * The table <code>public.article</code>.
     */
    public final Article ARTICLE = Article.ARTICLE;

    /**
     * The table <code>public.author</code>.
     */
    public final Author AUTHOR = Author.AUTHOR;

    /**
     * The table <code>public.refresh</code>.
     */
    public final Refresh REFRESH = Refresh.REFRESH;

    /**
     * No further instances allowed
     */
    private Public() {
        super("public", null);
    }


    @Override
    public Catalog getCatalog() {
        return DefaultCatalog.DEFAULT_CATALOG;
    }

    @Override
    public final List<Table<?>> getTables() {
        return Arrays.<Table<?>>asList(
            Article.ARTICLE,
            Author.AUTHOR,
            Refresh.REFRESH);
    }
}
