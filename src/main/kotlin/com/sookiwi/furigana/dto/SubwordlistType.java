
package com.sookiwi.furigana.dto;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>subwordlist_type complex type에 대한 Java 클래스입니다.
 *
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 *
 * <pre>
 * &lt;complexType name="subwordlist_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SubWord" type="{urn:yahoo:jp:jlp:FuriganaService}subword_type"
 *         maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subwordlist_type", namespace = "urn:yahoo:jp:jlp:FuriganaService", propOrder = {
        "subWord"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
        comments = "JAXB RI v2.2.8-b130911.1802")
public class SubwordlistType {

    @XmlElement(name = "SubWord", namespace = "urn:yahoo:jp:jlp:FuriganaService")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    protected List<SubwordType> subWord;

    /**
     * Gets the value of the subWord property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subWord property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubWord().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SubwordType }
     *
     *
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    public List<SubwordType> getSubWord() {
        if (subWord == null) {
            subWord = new ArrayList<SubwordType>();
        }
        return this.subWord;
    }

}
