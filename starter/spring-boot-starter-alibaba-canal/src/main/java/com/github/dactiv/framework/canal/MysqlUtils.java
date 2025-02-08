package com.github.dactiv.framework.canal;

import com.github.dactiv.framework.canal.domain.meta.TableColumnInfoMetadata;
import com.github.dactiv.framework.commons.Casts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;

public class MysqlUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MysqlUtils.class);

    public static List<TableColumnInfoMetadata> getTableColumns(String tableName, String databaseName, Connection connection) {

        List<TableColumnInfoMetadata> columns = new LinkedList<>();


        ResultSet resultSet = null;
        PreparedStatement statement = null;

        try {

            statement = connection.prepareStatement("SELECT COLUMN_NAME, COLUMN_COMMENT FROM information_schema.COLUMNS WHERE TABLE_NAME = ? AND TABLE_SCHEMA = ?");
            statement.setString(1, tableName);
            statement.setString(2, databaseName);

            resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String columnName = resultSet.getString(TableColumnInfoMetadata.MYSQL_COLUMN_NAME);
                String columnComment = resultSet.getString(TableColumnInfoMetadata.MYSQL_COLUMN_COMMENT);
                String camelName = Casts.castSnakeCaseToCamelCase(columnName);

                TableColumnInfoMetadata tableColumnInfoMetadata = new TableColumnInfoMetadata();

                tableColumnInfoMetadata.setComment(columnComment);
                tableColumnInfoMetadata.setId(camelName);
                tableColumnInfoMetadata.setName(columnName);

                columns.add(tableColumnInfoMetadata);
            }
        } catch (Exception e) {
            LOGGER.warn("读取 {} 表的列内容出现错误", tableName, e);
        } finally {
            JdbcUtils.closeResultSet(resultSet);
            JdbcUtils.closeStatement(statement);
            JdbcUtils.closeConnection(connection);
        }

        return columns;
    }
}
