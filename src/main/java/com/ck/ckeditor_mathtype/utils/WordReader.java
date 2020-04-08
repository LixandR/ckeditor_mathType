package com.ck.ckeditor_mathtype.utils;

import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMath;
import org.openxmlformats.schemas.officeDocument.x2006.math.CTOMathPara;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTP;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.stream.FileImageOutputStream;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class WordReader {
    private static final String MATHBEGIN = "<m:oMath>";
    private static final String MATHEND = "</m:oMath>";
    private static final String XML_TEXT = "<w:t>";
    private static final String IMG = "<w:drawing>";
    private static File stylesheet = new File("src/main/resources/OMML2MML.XSL");
    private static TransformerFactory tFactory = TransformerFactory.newInstance();
    private static StreamSource stylesource = new StreamSource(stylesheet);

    public static void main(String[] args) {
        try {
            //这里是文件路径
            String result = readWord("F://test.docx");
            //这里是解析完成后的word文档中的内容
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    public static  String  readWord(String path) throws IOException {
        XWPFDocument document = new XWPFDocument(new FileInputStream(path));
        try {
            // 获取word中的所有段落与表格
            List<IBodyElement> elements = document.getBodyElements();
            String all = "";
            // 获取到公式的Map集合
            for (IBodyElement element : elements) {
                // 段落
                if (element instanceof XWPFParagraph) {
                    String paragraphText = getParagraphText((XWPFParagraph) element, document);
                    all += paragraphText + "<br/>";
                }
                // 表格
                else if (element instanceof XWPFTable) {
                    String tabelText = getTabelText((XWPFTable) element, document);
                    all += tabelText + "<br/>";
                }
            }
            System.out.println("all" + all);
            return all;
        } finally {
            document.close();
        }
    }
    public static int getCount(String str, String key) {
        int count = 0;
        int index = 0;
        while ((index = str.indexOf(key, index)) != -1) {
            index = index + key.length();

            count++;
        }
        return count;
    }
    /**
     * 获取<w:t>标签的文字
     **/
    public static String parseXml(String xml) {
        xml = xml.replaceAll("<w:t xml:space=\"preserve\">", "<w:t>");
        Pattern p = compile("\\<w:t>(.*?)\\</w:t>");//正则表达式，取=和|之间的字符串，不包括=和|
        Matcher m = p.matcher(xml);
        String str = "";
        while (m.find()) {
            str += m.group(1);
        }
        return str;
    }
    /**
     * 获取MathML
     *
     * @param ctomath
     * @return
     * @throws Exception
     */
    static String getMathML(CTOMath ctomath) throws Exception {
        Transformer transformer = tFactory.newTransformer(stylesource);
        Node node = ctomath.getDomNode();
        DOMSource source = new DOMSource(node);
        StringWriter stringwriter = new StringWriter();
        StreamResult result = new StreamResult(stringwriter);
        transformer.setOutputProperty("omit-xml-declaration", "yes");
        transformer.transform(source, result);
        String mathML = stringwriter.toString();
        stringwriter.close();
        mathML = mathML.replaceAll("xmlns:m=\"http://schemas.openxmlformats.org/officeDocument/2006/math\"", "");
        mathML = mathML.replaceAll("xmlns:mml", "xmlns");
        mathML = mathML.replaceAll("mml:", "");
        return mathML;
    }
    /**
     * 获取段落内容
     *
     * @param xwpfParagraph
     */
    private static String getParagraphText(XWPFParagraph xwpfParagraph, XWPFDocument xwpfDocument) {
        try {
            //公式的索引
            int j=0;
            Map<Integer, String> result = new HashMap<>();
            List<String> mathMLList = new ArrayList<String>();
            CTP ctp1 = xwpfParagraph.getCTP();
            for (CTOMath ctomath : xwpfParagraph.getCTP().getOMathList()) {
                mathMLList.add(getMathML(ctomath));
            }
            for (CTOMathPara ctomathpara : xwpfParagraph.getCTP().getOMathParaList()) {
                for (CTOMath ctomath : ctomathpara.getOMathList()) {
                    mathMLList.add(getMathML(ctomath));
                }
            }
            for (int i = 0; i < mathMLList.size(); i++) {
                // 替换特殊符号(由于页面上无法直接展示特殊符号,所以需要进行替换,将特殊符号替换为html可以认识的标签(https://www.cnblogs.com/xinlvtian/p/8646683.html))
                String s = mathMLList.get(i)
                        .replaceAll("±", "&#x00B1;")
                        .replaceAll("∑", "&sum;");
                s = "<math xmlns=\"http://www.w3.org/1998/Math/MathML\">" + s + "</math>";
                result.put(i, s);
            }
            // 获取段落中所有内容
            List<XWPFRun> runs = xwpfParagraph.getRuns();
//            if (runs.size() == 0) {
//                return "";
//            }
            StringBuffer runText = new StringBuffer();
            //公式文本 ---------------------------------------------------------------------------------
            CTP ctp = ctp1;
            String xmlText = ctp.xmlText();
            int countImg = getCount(xmlText,IMG);
            int count = getCount(xmlText, MATHBEGIN);
            //这一段的全部文本
            if (count <= 0 && countImg <=0) {
                //都是文字
                runText.append(xwpfParagraph.getParagraphText());
            } else if (count>0 && countImg<=0){
                String str1 = xmlText;
                for (int k = 0; k < count; k++) {
                    int index = str1.indexOf(MATHBEGIN);
                    String str2 = str1.substring(0, index);
                    String s1 = parseXml(str2);
                    runText.append(s1);
                    String s = result.get(j++);
                    runText.append(s);
                    str1 = str1.substring(str1.indexOf(MATHEND)+10);

                    if (k == count - 1) {
                        //处理加入字符串
                        String s2 = parseXml(str1);
                        runText.append(s2);
                    }
                }
            }else if (count<=0 && countImg>0) {
                //图片
                List<XWPFRun> runs1 = xwpfParagraph.getRuns();
                for (XWPFRun run : runs1) {
                    Node node = run.getCTR().getDomNode();
                    // drawing 一个绘画的图片
                    Node drawingNode = getChildNode(node, "w:drawing");
                    if (drawingNode == null) {
                        continue;
                    }
                    // 绘画图片的宽和高
                    Node extentNode = getChildNode(drawingNode, "wp:extent");
                    NamedNodeMap extentAttrs = extentNode.getAttributes();
                    double cx = Double.parseDouble(extentAttrs.getNamedItem("cx").getNodeValue());
                    int width = MyUnits.emuToPx(cx);
                    double cy = Double.parseDouble(extentAttrs.getNamedItem("cy").getNodeValue());
                    int higth = MyUnits.emuToPx(cy);
                    // 绘画图片具体引用
                    Node blipNode = getChildNode(drawingNode, "a:blip");
                    NamedNodeMap blipAttrs = blipNode.getAttributes();
                    String rid = blipAttrs.getNamedItem("r:embed").getNodeValue();
                    // 获取图片信息
                    PackagePart part = xwpfDocument.getPartById(rid);
                    System.out.println(part.getContentType());
                    System.out.println(part.getPartName().getName());
                    System.out.println(part.getInputStream());
                    System.out.println("------ run ------");
                    byte[] filebt = readStream(part.getInputStream());
                    String path = "D://" + part.getPartName().getName();
                    String s1 = "<img style="+"width:"+width+"px;height:"+higth+"px"+" src="+path+">";
                    runText.append(s1);
                    byte2image(filebt, path);
                }
            }
            return runText.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private static Node getChildNode(Node node, String nodeName) {
        if (!node.hasChildNodes()) {
            return null;
        }
        NodeList childNodes = node.getChildNodes();
        for (int i = 0; i < childNodes.getLength(); i++) {
            Node childNode = childNodes.item(i);
            String name = childNode.getNodeName();
            if (nodeName.equals(name)) {
                return childNode;
            }
            childNode = getChildNode(childNode, nodeName);
            if (childNode != null) {
                return childNode;
            }
        }
        return null;
    }

    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    public static void byte2image(byte[] data, String path) {
        if (data.length < 3 || path.equals("")) {
            return;
        }
        FileImageOutputStream imageOutput = null;
        try {
            imageOutput = new FileImageOutputStream(new File(path));
            imageOutput.write(data, 0, data.length);
//            System.out.println("Make Picture success,Please find image in " + path);
        } catch (Exception ex) {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        } finally {
            try {
                imageOutput.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取表格内容
     *
     * @param table
     */
    private static String getTabelText(XWPFTable table, XWPFDocument document) {
        List<XWPFTableRow> rows = table.getRows();

        String str3 = "<table>";
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            String str2 = "<tr>";
            for (XWPFTableCell cell : cells) {
                // 简单获取内容（简单方式是不能获取字体对齐方式的）
                // System.out.println(cell.getText());
                // 一个单元格可以理解为一个word文档，单元格里也可以加段落与表格
                List<XWPFParagraph> paragraphs = cell.getParagraphs();
                String str1 = "<td>";
                for (XWPFParagraph paragraph : paragraphs) {
                    String paragraphText = getParagraphText(paragraph, document);
                    str1 += paragraphText;
                }
                str2 += str1 + "</td>";
            }
            str3 += str2 + "</tr>";
        }
        return str3 + "</table>";
    }
}