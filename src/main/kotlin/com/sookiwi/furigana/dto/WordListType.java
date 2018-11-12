
package com.sookiwi.furigana.dto;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>word_list_type complex type에 대한 Java 클래스입니다.
 *
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 *
 * <pre>
 * &lt;complexType name="word_list_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Word" type="{urn:yahoo:jp:jlp:FuriganaService}word_type" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "word_list_type", namespace = "urn:yahoo:jp:jlp:FuriganaService", propOrder = {
        "word"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
        comments = "JAXB RI v2.2.8-b130911.1802")
public class WordListType {

    @XmlElement(name = "Word", namespace = "urn:yahoo:jp:jlp:FuriganaService", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    protected List<WordType> word;

    /**
     * Gets the value of the word property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the word property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getWord().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link WordType }
     *
     *
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    public List<WordType> getWord() {
        if (word == null) {
            word = new ArrayList<WordType>();
        }
        return this.word;
    }

}
