<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <!-- Attribute used for table border -->
    <xsl:attribute-set name="tableBorder">
        <xsl:attribute name="border">solid 0.1mm black</xsl:attribute>
    </xsl:attribute-set>
    <xsl:template match="orders">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="simpleA4"
                                       page-height="29.7cm" page-width="25.0cm" margin="1cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="simpleA4">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="32pt" text-align="center" font-family="Helvetica" color="blue" font-weight="bold" space-after="5mm">
                       Order Id: <xsl:value-of select="orderId"/>
                    </fo:block>
                    <fo:block font-size="10pt">
                        <fo:table table-layout="fixed" width="100%" border-collapse="separate">
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-header font-weight="bold">
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" font-weight="bold">Barcode</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" font-weight="bold">Product Name</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" font-weight="bold">Mrp</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" font-weight="bold">Quantity</fo:block>
                                </fo:table-cell>
                                <fo:table-cell border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                                    <fo:block font-size="15pt" font-weight="bold">Total Price</fo:block>
                                </fo:table-cell>
                            </fo:table-header>
                            <fo:table-body>
                                <xsl:apply-templates select="order"/> <!-- branch tag is taken and pasted in below used template 46 line-->
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block font-size="16pt" font-family="Helvetica" color="black" font-weight="bold" space-after="5mm">
                        Total Amount : Rs. <xsl:value-of select="totalAmount"/> /-
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
    <xsl:template match="order">
        <fo:table-row>
            <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block font-size="15pt">
                    <xsl:value-of select="barcode"/>
                </fo:block>
            </fo:table-cell>

            <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block font-size="15pt">
                    <xsl:value-of select="productName"/>
                </fo:block>
            </fo:table-cell>
            <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block font-size="15pt">
                    <xsl:value-of select="mrp"/>
                </fo:block>
            </fo:table-cell>
                <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                    <fo:block font-size="15pt">
                        <xsl:value-of select="quantity"/>
                    </fo:block>
                </fo:table-cell>
            <fo:table-cell  border="1pt solid black" xsl:use-attribute-sets="tableBorder">
                <fo:block font-size="15pt">
                    <xsl:value-of select="quantity*mrp"/>
                </fo:block>
            </fo:table-cell>
        </fo:table-row>
    </xsl:template>
</xsl:stylesheet>