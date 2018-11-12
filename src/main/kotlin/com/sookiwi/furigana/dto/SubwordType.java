
package com.sookiwi.furigana.dto;

import javax.annotation.Generated;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>subword_type complex type에 대한 Java 클래스입니다.
 *
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 *
 * <pre>
 * &lt;complexType name="subword_type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Surface" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Furigana" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Roman" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "subword_type", namespace = "urn:yahoo:jp:jlp:FuriganaService", propOrder = {
        "surface",
        "furigana",
        "roman"
})
@Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
        comments = "JAXB RI v2.2.8-b130911.1802")
public class SubwordType {

    @XmlElement(name = "Surface", namespace = "urn:yahoo:jp:jlp:FuriganaService", required = true)
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    protected String surface;
    @XmlElement(name = "Furigana", namespace = "urn:yahoo:jp:jlp:FuriganaService")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    protected String furigana;
    @XmlElement(name = "Roman", namespace = "urn:yahoo:jp:jlp:FuriganaService")
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    protected String roman;

    /**
     * surface 속성의 값을 가져옵니다.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    public String getSurface() {
        return surface;
    }

    /**
     * surface 속성의 값을 설정합니다.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    public void setSurface(String value) {
        this.surface = value;
    }

    /**
     * furigana 속성의 값을 가져옵니다.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    public String getFurigana() {
        return furigana;
    }

    /**
     * furigana 속성의 값을 설정합니다.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    public void setFurigana(String value) {
        this.furigana = value;
    }

    /**
     * roman 속성의 값을 가져옵니다.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    public String getRoman() {
        return roman;
    }

    /**
     * roman 속성의 값을 설정합니다.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    @Generated(value = "com.sun.tools.internal.xjc.Driver", date = "2018-11-12T02:14:57+09:00",
            comments = "JAXB RI v2.2.8-b130911.1802")
    public void setRoman(String value) {
        this.roman = value;
    }

}
