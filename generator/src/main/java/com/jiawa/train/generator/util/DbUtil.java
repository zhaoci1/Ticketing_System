package com.jiawa.train.generator.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbUtil {
    //    数据库连接的信息
    public static String url = "";
    public static String user = "";
    public static String password = "";

    /**
     * 连接数据库,传统连接数据库的方法，基于JDBC
     *
     * @return
     */
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = DbUtil.url;
            String user = DbUtil.user;
            String password = DbUtil.password;
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

    /**
     * 通过表的注释，获取表名
     *
     * @param tableName
     * @return
     * @throws Exception
     */
    public static String getTableComment(String tableName) throws Exception {
        Connection conn = getConnection();
//        执行sql语句的对象
        Statement stmt = conn.createStatement();
//执行sql语句，获取该表的元数据
        ResultSet rs = stmt.executeQuery("select table_comment " +
                "from information_schema.tables " +
                "WHERE table_name = '" + tableName + "'");
        String tableNameCH = "";
//        通过该对象获取表的注释
        if (rs != null) {
            while (rs.next()) {
                tableNameCH = rs.getString("table_comment");
                break;
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        System.out.println("表名：" + tableNameCH);
        return tableNameCH;
    }

    public static List<Field> getColumnByTableName(String tableName) throws Exception {
        List<Field> filedList = new ArrayList<>();
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
//        获取表的结构
        ResultSet rs = stmt.executeQuery("show full columns from `" + tableName + "`");
        if (rs != null) {
            while (rs.next()) {
//                字段的属性名
                String columnName = rs.getString("Field");
//                字段的类型
                String type = rs.getString("Type");
//                字段的备注
                String comment = rs.getString("Comment");
//                字段是否能为空
                String nullAble = rs.getString("Null");
                Field field = new Field();
                field.setName(columnName);
                field.setNameHump(lineToHump(columnName));
                field.setNameBigHump(lineToBigHump(columnName));
//                原数据库的类型
                field.setType(type);
//                获取java类型
                field.setJavaType(DbUtil.sqlTypeToJavaType(rs.getString("Type")));
//                获取注释
                field.setComment(comment);
//                如果有下划线，则取下划线左边
                if (comment.contains("|")) {
                    field.setNameCn(comment.substring(0, comment.indexOf("|")));
                } else {
//                    如果没有下划线，就取整个注释
                    field.setNameCn(comment);
                }
//                是否可为空
                field.setNullAble("YES".equals(nullAble));
//                设置长度，如果为string类型，还可以拿到最长是多少
                if (type.toUpperCase().contains("varchar".toUpperCase())) {
                    String lengthStr = type.substring(type.indexOf("(") + 1, type.length() - 1);
                    field.setLength(Integer.valueOf(lengthStr));
                } else {
                    field.setLength(0);
                }
                if (comment.contains("枚举")) {
                    field.setEnums(true);
                    int start = comment.indexOf("[");
                    int end = comment.indexOf("]");
//                    对枚举字段进行截取
                    String enumsName = comment.substring(start + 1, end);
//                    转为下划线连接，变成全大写，并且把_ENUM替换掉
                    String enumsConst = StrUtil.toUnderlineCase(enumsName).toUpperCase().replace("_ENUM", "");
                    field.setEnumsConst(enumsConst);
                } else {
                    field.setEnums(false);
                }
//                以上所有操作都是为了活动一个属性字段
                filedList.add(field);
            }
        }
        rs.close();
        stmt.close();
        conn.close();
        System.out.println("列信息：" + JSONUtil.toJsonPrettyStr(filedList));
        return filedList;
    }

    /**
     * 下划线转小驼峰：member_id 转成 memberId
     */
    public static String lineToHump(String str) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 下划线转大驼峰：member_id 转成 MemberId
     */
    public static String lineToBigHump(String str) {
        String s = lineToHump(str);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    /**
     * 数据库类型转为Java类型
     */
    public static String sqlTypeToJavaType(String sqlType) {
        if (sqlType.toUpperCase().contains("varchar".toUpperCase())
                || sqlType.toUpperCase().contains("char".toUpperCase())
                || sqlType.toUpperCase().contains("text".toUpperCase())) {
            return "String";
        } else if (sqlType.toUpperCase().contains("datetime".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("time".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("date".toUpperCase())) {
            return "Date";
        } else if (sqlType.toUpperCase().contains("bigint".toUpperCase())) {
            return "Long";
        } else if (sqlType.toUpperCase().contains("int".toUpperCase())) {
            return "Integer";
        } else if (sqlType.toUpperCase().contains("long".toUpperCase())) {
            return "Long";
        } else if (sqlType.toUpperCase().contains("decimal".toUpperCase())) {
            return "BigDecimal";
        } else if (sqlType.toUpperCase().contains("boolean".toUpperCase())) {
            return "Boolean";
        } else {
            return "String";
        }
    }
}
