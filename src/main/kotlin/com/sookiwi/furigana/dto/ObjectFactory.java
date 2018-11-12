
package com.sookiwi.furigana.dto;

import javax.xml.bind.annotation.XmlRegistry;

/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.sookiwi.furigana package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 *
 */
@XmlRegistry
public class ObjectFactory {

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for
     * package: com.sookiwi.furigana
     *
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ResultSet }
     *
     */
    public ResultSet createResultSet() {
        return new ResultSet();
    }

    /**
     * Create an instance of {@link ResultType }
     *
     */
    public ResultType createResultType() {
        return new ResultType();
    }

    /**
     * Create an instance of {@link WordType }
     *
     */
    public WordType createWordType() {
        return new WordType();
    }

    /**
     * Create an instance of {@link SubwordType }
     *
     */
    public SubwordType createSubwordType() {
        return new SubwordType();
    }

    /**
     * Create an instance of {@link WordListType }
     *
     */
    public WordListType createWordListType() {
        return new WordListType();
    }

    /**
     * Create an instance of {@link SubwordlistType }
     *
     */
    public SubwordlistType createSubwordlistType() {
        return new SubwordlistType();
    }

}
